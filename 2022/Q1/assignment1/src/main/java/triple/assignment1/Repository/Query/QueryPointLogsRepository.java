package triple.assignment1.Repository.Query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import triple.assignment1.Entity.QPointLogs;

import javax.persistence.EntityManager;

@Repository
@Transactional
@RequiredArgsConstructor
public class QueryPointLogsRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    private QPointLogs pointLogs = QPointLogs.pointLogs;

    /**
     * POINT_LOGS 테이블을 참조해서 유저마다 현재 적립된 포인트 조회
     * @param reviewId
     * @return
     */
    public int getPointsByReview(String userId, String reviewId) {
        return queryFactory
                .select(pointLogs.point.sum())
                .from(pointLogs)
                .where(pointLogs.review.eq(reviewId), pointLogs.user.id.eq(userId))
                .fetchOne();
    }

}
