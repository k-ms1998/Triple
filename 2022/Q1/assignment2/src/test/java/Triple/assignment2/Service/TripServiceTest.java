package Triple.assignment2.Service;

import Triple.assignment2.Controller.body.ResBody;
import Triple.assignment2.Controller.body.TripBody;
import Triple.assignment2.Entity.City;
import Triple.assignment2.Entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@Transactional
class TripServiceTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private TripService tripService;

    User getFirstUser() {
        return em.createQuery("select u from User u", User.class)
                .setMaxResults(1)
                .getSingleResult();
    }

    City findCity(String cityName) {
        return em.createQuery("select c from City c where c.name = :cityName", City.class)
                .setParameter("cityName", cityName)
                .getSingleResult();

    }

    TripBody createBody(String cityName, int startOffset) {
        return new TripBody(getFirstUser().getId(), findCity(cityName).getId(), LocalDate.now().plusDays(startOffset).toString(), LocalDate.now().plusDays(5).toString());
    }

    @Test
    void saveTrip() {
        TripBody body = createBody("Ulsan", 1);
        ResBody resBody = tripService.save(body);

        Assertions.assertThat(resBody.getStatus()).isEqualTo(200);
    }

    @Test
    void saveDuplicateTrip() {
        TripBody body = createBody("Seoul", 1);
        ResBody resBody = tripService.save(body);

        Assertions.assertThat(resBody.getStatus()).isEqualTo(404);
    }

    @Test
    void saveTripStartDateNotAllowed() {
        TripBody body = createBody("Busan", -1);
        ResBody resBody = tripService.save(body);

        Assertions.assertThat(resBody.getStatus()).isEqualTo(404);
    }

    @Test
    void fetchTripsByUser() {
        Long userId = 2L;

        ResBody resBody = tripService.fetchTrips(new TripBody(userId));
        List data = resBody.getData();

        Assertions.assertThat(data.size()).isEqualTo(2);
    }

}