package triple.assignment1.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import triple.assignment1.Controller.EventBody;
import triple.assignment1.Entity.*;
import triple.assignment1.Repository.Query.QueryAttachedPhotoRepository;
import triple.assignment1.Repository.Query.QueryPointLogsRepository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
public class PointRepository{

    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    private final ReviewRepository reviewRepository;
    private final AttachedPhotoRepository attachedPhotoRepository;
    private final QueryAttachedPhotoRepository qAttachedPhotoRepository;
    private final PointLogsRepository pointLogsRepository;
    private final QueryPointLogsRepository qPointLogsRepository;

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public void save(EventBody body, List<PointType> pointTypes) {
        String userId = body.getUserId();
        String reviewId = body.getReviewId();
        String placeId = body.getPlaceId();
        String content = body.getContent();

        //User 저장
        User user = new User(userId);
        userRepository.save(user);

        //Place 저장
        Place place = new Place(placeId);
        placeRepository.save(place);

        // Review 저장
        Review review = new Review(reviewId, new Place(placeId), content);
        reviewRepository.save(review);

        refreshContext();

        //AttachedPhoto 저장
        body.getAttachedPhotoIds()
                .forEach(i -> {
                    AttachedPhoto attachedPhoto = new AttachedPhoto(i, review);
                    attachedPhotoRepository.save(attachedPhoto);
                });
        
        //PointLogs 저장
        pointTypes
                .forEach(t -> {
                    PointLogs pointLog = new PointLogs(user, reviewId, t, 1);
                    pointLogsRepository.save(pointLog);
                });
    }

    public void updateReview(EventBody body) {
        String userId = body.getUserId();
        String reviewId = body.getReviewId();
        Review findReview = queryFactory
                .selectFrom(QReview.review)
                .where(QReview.review.id.eq(reviewId))
                .fetchFirst();

        //Update Content
        findReview.updateContent(body.getContent());
        refreshContext();

        List<PointLogs> updateLogs = new ArrayList<>();
        //Update Attached Photos
        // 1. Delete all attached photos associated w/ the review
        Long deletePhotoCount = qAttachedPhotoRepository.deleteByReviewId(reviewId);
        refreshContext();

        //2. Add attached photos associated w/ the review
        body.getAttachedPhotoIds()
                .forEach(i -> {
                    AttachedPhoto attachedPhoto = new AttachedPhoto(i, new Review(reviewId));
                    attachedPhotoRepository.save(attachedPhoto);
                });
        refreshContext();

        //Add PointLog
        int pointUpdate = checkUpdatedAttachedPhoto(deletePhotoCount, body.getAttachedPhotoIds().size());
        System.out.println("pointUpdate = " + pointUpdate);
        if (pointUpdate != 0) {
            PointLogs pointLog = new PointLogs(new User(userId), reviewId, PointType.UPDATE, pointUpdate);
            pointLogsRepository.save(pointLog);
        }
    }


    public int removeReview(EventBody body) {
        String reviewId = body.getReviewId();

        //Delete AttachedPhotos
        qAttachedPhotoRepository.deleteByReviewId(reviewId);
        refreshContext();

        //Delete Review
        Review review = new Review(reviewId);
        reviewRepository.delete(review);

        //Save Log
        int pointsByReview = qPointLogsRepository.getPointsByReview(reviewId);
        PointLogs pointLog = new PointLogs(new User(body.getUserId()), reviewId, PointType.DELETE, (-1)*pointsByReview);
        pointLogsRepository.save(pointLog);

        refreshContext();

        return pointsByReview;
    }

    private int checkUpdatedAttachedPhoto(Long before, int after) {
        if (before > 0L) {
            if (after > 0) {
                return 0;
            } else {
                return -1;
            }
        } else {
            if (after > 0) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public void refreshContext() {
        em.flush();
        em.clear();
    }
}
