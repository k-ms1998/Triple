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

    public int getPointsByReview(String reviewId) {
        return queryFactory
                .select(pointLogs.point.sum())
                .from(pointLogs)
                .where(pointLogs.review.eq(reviewId))
                .fetchOne();
    }

}
