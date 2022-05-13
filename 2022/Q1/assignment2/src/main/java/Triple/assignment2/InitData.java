package Triple.assignment2;

import Triple.assignment2.Entity.City;
import Triple.assignment2.Entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Profile("local")
@Component
@RequiredArgsConstructor
public class InitData {

    private final InitUser initUser;
    private final InitCity initCity;

    @PostConstruct
    public void init() {
        initUser.init();
        initCity.init();
    }

    @Component
    static class InitUser {

        @PersistenceContext
        private EntityManager em;

        @Transactional
        public void init() {
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
        }
    }




}
