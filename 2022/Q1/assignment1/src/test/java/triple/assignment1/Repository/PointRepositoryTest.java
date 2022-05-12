package triple.assignment1.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import triple.assignment1.Controller.EventBody;
import triple.assignment1.Entity.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static triple.assignment1.Entity.QAttachedPhoto.attachedPhoto;
import static triple.assignment1.Entity.QPlace.place;
import static triple.assignment1.Entity.QPointLogs.pointLogs;
import static triple.assignment1.Entity.QReview.review;
import static triple.assignment1.Entity.QUser.user;

@SpringBootTest
@Transactional
class PointRepositoryTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private JPAQueryFactory queryFactory;

    @Autowired
    private PointRepository pointRepository;

    static EventBody body = new EventBody();

    void createEventBody() {
        List<String> photoIds = new ArrayList<>();
        photoIds.add("test_photo_id_01");
        photoIds.add("test_photo_id_02");

        body.setType("REVIEW");
        body.setAction(Action.ADD);
        body.setReviewId("test_review_id_01");
        body.setContent("Good!");
        body.setAttachedPhotoIds(photoIds);
        body.setUserId("user_id_01");
        body.setPlaceId("place_id_01");

    }

    void createUpdateBody(EventBody updateBody, String reviewId, String userid, String placeId, List<String> photoIds) {

        updateBody.setType("REVIEW");
        updateBody.setAction(Action.ADD);
        updateBody.setReviewId(reviewId);
        updateBody.setContent("Good!");
        updateBody.setAttachedPhotoIds(photoIds);
        updateBody.setUserId(userid);
        updateBody.setPlaceId(placeId);
    }

    @Test
    void saveNew() {
        createEventBody();

        List<PointType> pointTypes = new ArrayList<>();
        pointTypes.add(PointType.REVIEW);
        pointTypes.add(PointType.PHOTO);

        pointRepository.save(body, pointTypes);

        List<Review> findReview = queryFactory
                .selectFrom(review)
                .where(review.id.eq("test_review_id_01"))
                .fetch();

        List<AttachedPhoto> findAttachedPhotos = queryFactory
                .selectFrom(attachedPhoto)
                .where(attachedPhoto.review.id.eq("test_review_id_01"))
                .fetch();

        Assertions.assertThat(findReview.size()).isEqualTo(1);
        Assertions.assertThat(findAttachedPhotos.size()).isEqualTo(2);
    }

    /**
     * 사진이 첨부되어 있지 않은 경우에서 사진을 첨부 했을때
     */
    @Test
    void updateReviewAddPhotos() {
        //before
        Integer beforePoint = queryFactory
                .select(pointLogs.point.sum())
                .from(pointLogs)
                .where(pointLogs.user.id.eq("user_02"))
                .fetchOne();


        EventBody updateBody = new EventBody();
        List<String> photoIds = new ArrayList<>();
        photoIds.add("test_photo_id_01");
        photoIds.add("test_photo_id_02");

        createUpdateBody(updateBody,  "review_03", "user_02" , "place_02", photoIds);

        //update review
        pointRepository.updateReview(updateBody);

        //after
        Integer afterPoint = queryFactory
                .select(pointLogs.point.sum())
                .from(pointLogs)
                .where(pointLogs.user.id.eq("user_02"))
                .fetchOne();

        List<AttachedPhoto> findAttachedPhotos = queryFactory
                .selectFrom(attachedPhoto)
                .where(attachedPhoto.review.id.eq("review_03"))
                .fetch();

        Assertions.assertThat(beforePoint).isEqualTo(2);
        Assertions.assertThat(afterPoint).isEqualTo(3);
        Assertions.assertThat(findAttachedPhotos.size()).isEqualTo(2);
    }

    /**
     * 이미 리뷰에 사진이 첨부되어 있을때 추가로 사진을 더 첨부했을 경우
     */
    @Test
    void updateReviewAddMorePhotos() {
        EventBody updateBodyA = new EventBody();
        List<String> photoIdsA = new ArrayList<>();
        photoIdsA.add("test_photo_id_01");
        createUpdateBody(updateBodyA, "review_01", "user_01", "place_02", photoIdsA);

        EventBody updateBodyB = new EventBody();
        List<String> photoIdsB = new ArrayList<>();
        photoIdsB.add("test_photo_id_01");
        photoIdsB.add("test_photo_id_02");
        createUpdateBody(updateBodyB, "review_01", "user_01", "place_02", photoIdsB);

        //before
        pointRepository.updateReview(updateBodyA);
        Integer beforePoint = queryFactory
                .select(pointLogs.point.sum())
                .from(pointLogs)
                .where(pointLogs.user.id.eq("user_01"))
                .fetchOne();

        List<AttachedPhoto> beforeFindAttachedPhotos = queryFactory
                .selectFrom(attachedPhoto)
                .where(attachedPhoto.review.id.eq("review_01"))
                .fetch();


        //after
        pointRepository.updateReview(updateBodyB);

        Integer afterPoint = queryFactory
                .select(pointLogs.point.sum())
                .from(pointLogs)
                .where(pointLogs.user.id.eq("user_01"))
                .fetchOne();

        List<AttachedPhoto> afterFindAttachedPhotos = queryFactory
                .selectFrom(attachedPhoto)
                .where(attachedPhoto.review.id.eq("review_01"))
                .fetch();

        Assertions.assertThat(beforePoint).isEqualTo(afterPoint);
        Assertions.assertThat(afterFindAttachedPhotos.size()).isEqualTo(beforeFindAttachedPhotos.size()+1);
    }

    /**
     * 첨부된 사진을 삭제할때
     */
    @Test
    void updateReviewRemovePhotos() {
        Integer beforePoint = queryFactory
                .select(pointLogs.point.sum())
                .from(pointLogs)
                .where(pointLogs.user.id.eq("user_01"))
                .fetchOne();

        EventBody updateBody = new EventBody();
        createUpdateBody(updateBody, "review_01", "user_01", "place_02", new ArrayList<>());
        pointRepository.updateReview(updateBody);

        Integer afterPoint = queryFactory
                .select(pointLogs.point.sum())
                .from(pointLogs)
                .where(pointLogs.user.id.eq("user_01"))
                .fetchOne();

        Assertions.assertThat(afterPoint).isEqualTo(beforePoint-1);
    }

    @Test
    void removeReview() {
        EventBody updateBody = new EventBody();
        createUpdateBody(updateBody, "review_01", "user_01", "place_02", new ArrayList<>());
        pointRepository.updateReview(updateBody);

        pointRepository.removeReview(updateBody);

        Long attachedPhotosCount = queryFactory
                .select(attachedPhoto.count())
                .from(attachedPhoto)
                .where(attachedPhoto.review.id.eq("review_01"))
                .fetchOne();

        Long reviewsCount = queryFactory
                .select(review.count())
                .from(review)
                .where(review.id.eq("review_01"))
                .fetchOne();

        Integer pointsAfterDeletion = queryFactory
                .select(pointLogs.point.sum())
                .from(pointLogs)
                .where(pointLogs.review.eq("review_01"))
                .fetchOne();

        Assertions.assertThat(attachedPhotosCount).isEqualTo(0L);
        Assertions.assertThat(reviewsCount).isEqualTo(0L);
        Assertions.assertThat(pointsAfterDeletion).isEqualTo(0);
    }

}