package triple.assignment1.Repository.Query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import triple.assignment1.Entity.PointLogs;
import triple.assignment1.Entity.QPointLogs;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class QueryPointLogsRepositoryTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private JPAQueryFactory queryFactory;

    @Autowired
    private QueryPointLogsRepository qPointLogsRepository;

    private QPointLogs pointLogs = QPointLogs.pointLogs;

    @Test
    void getPointsByReview() {
        int pointsByReview01 = qPointLogsRepository.getPointsByReview("user_01", "review_01");
        int pointsByReview02 = qPointLogsRepository.getPointsByReview("user_01", "review_02");


        Assertions.assertThat(pointsByReview01).isEqualTo(3);
        Assertions.assertThat(pointsByReview02).isEqualTo(0);
    }
}