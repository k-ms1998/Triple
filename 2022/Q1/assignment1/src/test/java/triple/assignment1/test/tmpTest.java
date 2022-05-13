package triple.assignment1.test;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import triple.assignment1.Entity.*;
import triple.assignment1.Repository.PlaceRepository;
import triple.assignment1.Repository.ReviewRepository;
import triple.assignment1.Repository.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static triple.assignment1.Entity.QPlace.place;
import static triple.assignment1.Entity.QReview.*;
import static triple.assignment1.Entity.QUser.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
public class tmpTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private JPAQueryFactory queryFactory;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private ReviewRepository reviewRepository;


    void init() {
        User user = new User("user_01");
        Place place = new Place("place_01");
        Review reviewA = new Review("review_01", place, "Content #1", user.getId());
        Review reviewB = new Review("review_02", place, "Content #2", "user_02");

        userRepository.save(user);
        placeRepository.save(place);
        reviewRepository.save(reviewA);
        reviewRepository.save(reviewB);
    }

    @Test
    void joinTest() {
        init();

        em.flush();
        em.clear();

        List<Review> reviewLeftJoinPlace = queryFactory
                .select(review)
                .from(review)
                .leftJoin(review.place, place).fetchJoin()
                .fetch();
        System.out.println("reviewLeftJoinPlace = " + reviewLeftJoinPlace);

        em.flush();
        em.clear();

        List<Tuple> reviewLeftJoinUser = queryFactory
                .select(review, user)
                .from(review)
                .leftJoin(user)
                .on(review.userId.eq(user.id))
                .where(review.userId.eq("user_01"))
                .fetch();
        System.out.println("reviewLeftJoinUser = " + reviewLeftJoinUser);

        Assertions.assertThat(reviewLeftJoinPlace.size()).isEqualTo(2);
        Assertions.assertThat(reviewLeftJoinUser.size()).isEqualTo(1);
    }
}
