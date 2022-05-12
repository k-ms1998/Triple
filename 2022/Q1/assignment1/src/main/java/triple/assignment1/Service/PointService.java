package triple.assignment1.Service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import triple.assignment1.Controller.EventBody;
import triple.assignment1.Entity.*;
import triple.assignment1.Repository.PointRepository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PointService {
    private final PointRepository pointRepository;

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    private QUser user = QUser.user;
    private QReview review = QReview.review;
    private QPlace place = QPlace.place;
    private QAttachedPhoto attachedPhoto = QAttachedPhoto.attachedPhoto;
    private QPointLogs pointLogs = QPointLogs.pointLogs;

    @Transactional
    public String addPoints(EventBody body) {
        int curPoint = 0;
        List<PointType> pointTypes = new ArrayList<>();

        calculateNewPoint(body, pointTypes);
        pointRepository.save(body, pointTypes);

        return String.valueOf(curPoint);
    }

    @Transactional
    public String updatePoints(EventBody body) {
        pointRepository.updateReview(body);
        return String.valueOf(0);
    }

    @Transactional
    public String removePoints(EventBody body) {

        return String.valueOf(pointRepository.removeReview(body));
    }

    private int calculateNewPoint(EventBody body, List<PointType> pointTypes) {
        int tmpPoint = 0;

        if (!body.getContent().isEmpty()) {
            tmpPoint += 1;
            pointTypes.add(PointType.REVIEW);
        }
        if (body.getAttachedPhotoIds().size() > 0) {
            tmpPoint += 1;
            pointTypes.add(PointType.PHOTO);
        }
        if (fetchReviewCount(body) == 0L) {
            tmpPoint += 1;
            pointTypes.add(PointType.FIRST);
        }

        return tmpPoint;
    }

    public String getPoints(EventBody body) {

        return String.valueOf(queryFactory
                .select(pointLogs.point.sum())
                .from(pointLogs)
                .where(pointLogs.user.id.eq(body.getUserId()))
                .fetchOne());
    }

    public Long fetchReviewCount(EventBody body) {
        return queryFactory
                .select(review.count())
                .from(review)
                .where(review.place.id.eq(body.getPlaceId()))
                .fetchOne();
    }
}
