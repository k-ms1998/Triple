package Triple.assignment2;

import Triple.assignment2.Entity.City;
import Triple.assignment2.Entity.Trip;
import Triple.assignment2.Entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;

@Profile("local")
@Component
@RequiredArgsConstructor
public class InitData {

    private final InitUser initUser;
    private final InitCity initCity;
    private final InitTrip initTrip;

    @PostConstruct
    public void init() {
        initUser.init();
        initCity.init();
        initTrip.init();
    }

    @Component
    static class InitUser {

        @PersistenceContext
        private EntityManager em;

        @Transactional
        public void init() {

            em.persist(new User());
            em.persist(new User());
            em.persist(new User());

        }
    }

    @Component
    static class InitCity {

        @PersistenceContext
        private EntityManager em;

        @Transactional
        public void init() {

            em.persist(new City("Seoul"));
            em.persist(new City("Ulsan"));
            em.persist(new City("Busan"));

            City incheon = new City("Incheon");
            em.persist(incheon);
            incheon.updateCreatedDate(LocalDate.of(2022,05,18));

            City jeju = new City("Jeju");
            em.persist(jeju);
            jeju.updateCreatedDate(LocalDate.of(2022, 05, 17));

            City yeosu = new City("Yeosu");
            yeosu.updateViewedDate(LocalDate.of(2022,5,16));
            em.persist(yeosu);

            City sejong = new City("Sejong");
            sejong.updateViewedDate(LocalDate.of(2022,4,16));
            em.persist(sejong);

            em.flush();
            em.clear();
        }
    }

    @Component
    static class InitTrip {

        @PersistenceContext
        private EntityManager em;

        @Transactional
        public void init() {
            City seoul = findCity("Seoul");
            City ulsan = findCity("Ulsan");
            City busan = findCity("Busan");
            City incheon = findCity("Incheon");

            em.persist(new Trip(em.find(City.class, seoul.getId()), em.find(User.class, 2L), LocalDate.of(2022,04,01), LocalDate.of(2022,05,01)));
            em.persist(new Trip(em.find(City.class, busan.getId()), em.find(User.class, 3L), LocalDate.of(2022,06,01), LocalDate.of(2022,06,03)));


            em.persist(new Trip(em.find(City.class, seoul.getId()), em.find(User.class, 1L), LocalDate.now(), LocalDate.of(2022, 06, 01)));
            em.persist(new Trip(em.find(City.class, seoul.getId()), em.find(User.class, 2L), LocalDate.of(2022,05,01), LocalDate.of(2022,06,01)));
            em.persist(new Trip(em.find(City.class, ulsan.getId()), em.find(User.class, 3L), LocalDate.of(2022, 04, 01), LocalDate.of(2022, 06, 01)));


        }

        public City findCity(String cityName) {
            return em.createQuery("select c from City c where c.name = :cityName", City.class)
                    .setParameter("cityName", cityName)
                    .getSingleResult();

        }
    }

}