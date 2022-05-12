package triple.assignment1;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import triple.assignment1.Entity.*;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Profile("test")
@Component
@RequiredArgsConstructor
public class InitTestData {

    private final InitTestUsers initTestUsers;
    private final InitTestPointLogs initTestPointLogs;
    private final InitPlaces initPlaces;
    private final InitReviews initReviews;
    private final InitAttachedPhotos initAttachedPhotos;

    @PostConstruct
    public void init() {
        initTestUsers.init();
        initTestPointLogs.init();
        initPlaces.init();
        initReviews.init();
        initAttachedPhotos.init();
    }

    @Component
    static class InitTestUsers {

        @PersistenceContext
        private EntityManager em;

        @Transactional
        public void init() {
            em.persist(new User("user_01"));
            em.persist(new User("user_02"));

            em.flush();
            em.clear();
        }
    }


    @Component
    static class InitTestPointLogs {

        @PersistenceContext
        private EntityManager em;

        @Transactional
        public void init() {
            List<PointLogs> pointLogsList = new ArrayList<>();

            pointLogsList.add(new PointLogs(new User("user_01"), "review_01", PointType.FIRST, 1));
            pointLogsList.add(new PointLogs(new User("user_01"), "review_01", PointType.REVIEW, 1));
            pointLogsList.add(new PointLogs(new User("user_01"), "review_01", PointType.PHOTO, 1));

            pointLogsList.add(new PointLogs(new User("user_01"), "review_02", PointType.FIRST, 1));
            pointLogsList.add(new PointLogs(new User("user_01"), "review_02", PointType.REVIEW, 1));
            pointLogsList.add(new PointLogs(new User("user_01"), "review_02", PointType.DELETE, -2));

            pointLogsList.add(new PointLogs(new User("user_02"), "review_03", PointType.FIRST, 1));
            pointLogsList.add(new PointLogs(new User("user_02"), "review_03", PointType.REVIEW, 1));

            pointLogsList
                    .forEach(l -> em.persist(l));
        }
    }


    @Component
    static class InitPlaces {

        @PersistenceContext
        private EntityManager em;

        @Transactional
        public void init() {
            Place place01 = new Place("place_01");
            Place place02 = new Place("place_02");

            em.persist(place01);
            em.persist(place02);
        }
    }

    @Component
    static class InitReviews {

        @PersistenceContext
        private EntityManager em;

        @Transactional
        public void init() {
            Review review01 = new Review("review_01", new Place("place_01"), "Good!", "user_01");
            Review review02 = new Review("review_02", new Place("place_02"), "Good!", "user_01");
            Review review03 = new Review("review_03", new Place("place_02"), "Good!", "user_02");

            em.persist(review01);
            em.persist(review02);
            em.persist(review03);
        }
    }

    @Component
    static class InitAttachedPhotos {

        @PersistenceContext
        private EntityManager em;

        @Transactional
        public void init() {
            List<AttachedPhoto> attachedPhotos = new ArrayList<>();

            attachedPhotos.add(new AttachedPhoto("review_01_photo_01", new Review("review_01")));
            attachedPhotos.add(new AttachedPhoto("review_01_photo_02", new Review("review_01")));
            attachedPhotos.add(new AttachedPhoto("review_01_photo_03", new Review("review_01")));

            attachedPhotos.add(new AttachedPhoto("review_02_photo_01", new Review("review_02")));
            attachedPhotos.add(new AttachedPhoto("review_02_photo_02", new Review("review_02")));

            attachedPhotos
                    .forEach(p -> em.persist(p));
        }
    }
}
