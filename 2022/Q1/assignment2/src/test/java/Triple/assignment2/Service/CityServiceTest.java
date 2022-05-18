package Triple.assignment2.Service;

import Triple.assignment2.Controller.body.CityBody;
import Triple.assignment2.Controller.body.ResBody;
import Triple.assignment2.Controller.dto.CityDTO;
import Triple.assignment2.Entity.City;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CityServiceTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private CityService cityService;

    static List<City> fetchCitiesList = new ArrayList<>();

    @Test
    @Order(10)
    void save() {
        String cityName = "Pyeong-Taek";
        CityBody body = new CityBody(cityName);

        ResBody resBody = cityService.save(body);
        CityDTO savedCity = (CityDTO) resBody.getData().get(0);

        City findCity = em.createQuery("select c from City c where c.name = :cityName", City.class)
                .setParameter("cityName", cityName)
                .getSingleResult();

        Assertions.assertThat(resBody.getStatus()).isEqualTo(200);
        Assertions.assertThat(findCity.getName()).isEqualTo(savedCity.getCityName());
    }

    @Test
    @Order(1)
    void saveDuplicateCity() {
        String cityName = "Seoul";
        CityBody body = new CityBody(cityName);

        ResBody resBody = cityService.save(body);

        Assertions.assertThat(resBody.getStatus()).isEqualTo(404);
    }

    @Test
    @Order(2)
    void fetchCitySuccess() {
        String cityName = "Seoul";
        CityBody body = new CityBody(cityName);

        ResBody resBody = cityService.fetchCity(body);

        Assertions.assertThat(resBody.getStatus()).isEqualTo(200);
    }

    @Test
    @Order(3)
    void fetchCityFailure() {
        String cityName = "Non-existent city";
        CityBody body = new CityBody(cityName);

        ResBody resBody = cityService.fetchCity(body);

        Assertions.assertThat(resBody.getStatus()).isEqualTo(404);
    }

    @Test
    @Order(4)
    void fetchCities() {
        ResBody resBody = cityService.fetchCitiesList(new CityBody());
        List<CityDTO> data = resBody.getData();
        data.stream()
                .forEach(d -> System.out.println("d = " + d));

    }

    @Test
    @Order(5)
    void fetchOngoing() {
        List<City> cities = cityService.fetchCitiesOngoing(LocalDate.now());
        fetchCitiesList.addAll(cities);

        Assertions.assertThat(cities.size()).isEqualTo(3);
    }

    @Test
    @Order(6)
    void fetchExpected() {
        List<City> cities = cityService.fetchCitiesExpected(LocalDate.now(), 10);
        fetchCitiesList.addAll(cities);

        Assertions.assertThat(cities.size()).isEqualTo(1);
    }

    @Test
    @Order(7)
    void fetchByCreatedDate() {
        List<City> cities = cityService.fetchCitiesExpectedByCreatedDate(LocalDate.now(), 10, fetchCitiesList);
        fetchCitiesList.addAll(cities);

        Assertions.assertThat(cities.size()).isEqualTo(3);
    }

    @Test
    @Order(8)
    void fetchByViewedDate() {
        System.out.println("fetchCitiesList = " + fetchCitiesList);
        List<City> cities = cityService.fetchCitiesExpectedByViewedDate(LocalDate.now(), 10, fetchCitiesList);
        fetchCitiesList.addAll(cities);

        Assertions.assertThat(cities.size()).isEqualTo(0);
    }

    @Test
    @Order(9)
    void fetchRest() {
        System.out.println("fetchCitiesList = " + fetchCitiesList);
        List<City> cities = cityService.fetchRemainingCities(10, fetchCitiesList);
        fetchCitiesList.addAll(cities);

        Assertions.assertThat(cities.size()).isEqualTo(1);
    }

}
