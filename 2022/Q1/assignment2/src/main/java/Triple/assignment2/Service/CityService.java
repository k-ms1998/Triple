package Triple.assignment2.Service;

import Triple.assignment2.Controller.body.CityBody;
import Triple.assignment2.Controller.body.ResBody;
import Triple.assignment2.Controller.dto.CityDTO;
import Triple.assignment2.Entity.City;
import Triple.assignment2.Repository.CityRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static Triple.assignment2.Entity.QCity.city;
import static Triple.assignment2.Entity.QTrip.trip;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CityService {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    private final CityRepository cityRepository;

    @Transactional
    public ResBody save(CityBody body) {

        if (checkDuplicateCity(body)) {
            return new ResBody(404, "City already exists", null);
        }

        City savedCity = cityRepository.save(new City(body.getName()));

        List<CityDTO> resCity = cityToDTO(List.of(savedCity));

        return new ResBody(200, "City Saved", resCity);
    }

    public ResBody fetchCity(CityBody body) {
        Long cityId = body.getId();
        String cityName = body.getName();

        City findCity = queryFactory
                .selectFrom(city)
                .where(city.name.eq(cityName))
                .fetchFirst();

        if (findCity == null) {
            return new ResBody(404, "No match found", null);
        }

        findCity.updateViewedDate(LocalDate.now());
        refreshContext();

        List<CityDTO> resCity = cityToDTO(List.of(findCity));

        return new ResBody(200, "City searched by city name", resCity);
    }

    /**
     * 도시리스트 조회
     * 1. 여행중인 도시들을 여행 시작일이 빠른 순서대로 (여행 중 == (여행시작일 <= 오늘 <= 여행종료일))
     * 2. 아직 시작하지 않은 여행 중 여행 시작일이 가까운 순서대로 (오늘 < 여행시작일)
     * 3. 최근 1일 이내에 등록된 도시들을 최근에 등록 된 순서대로 => && 여행 일정이 없거나 이미 여행 일정이 끝났을때
     * 4. 최근 1주일 이내에 단건조회 된 도시들을 최근에 조회한 순서대로 => && 여행 일정이 없거나 이미 여행 일정이 끝났을때
     * @param body
     * @return
     */
   public ResBody fetchCitiesList(CityBody body) {
       LocalDate today = LocalDate.now();
       List<City> cities = new ArrayList<>();

       List<City> citiesOngoing = fetchCitiesOngoing(today);
       cities.addAll(citiesOngoing);
       int limitResults = citiesOngoing.size() > 10 ? 10 : 10 - citiesOngoing.size();


       List<City> citiesExpected = fetchCitiesExpected(today, limitResults);
       cities.addAll(citiesExpected);
       limitResults = citiesExpected.size() >= limitResults ? 0 : limitResults - citiesExpected.size();

       if (limitResults > 0) {
           List<City> citiesByCreatedDate = fetchCitiesExpectedByCreatedDate(today, limitResults, cities);
           cities.addAll(citiesByCreatedDate);
           limitResults = citiesByCreatedDate.size() >= limitResults ? 0 : limitResults - citiesByCreatedDate.size();
       }
       if (limitResults > 0) {
           List<City> citiesByViewedDate = fetchCitiesExpectedByViewedDate(today, limitResults, cities);
           cities.addAll(citiesByViewedDate);
           limitResults = citiesByViewedDate.size() >= limitResults ? 0 : limitResults - citiesByViewedDate.size();
       }
       if (limitResults > 0) {
           List<City> remainingCities = fetchRemainingCities(limitResults, cities);
           cities.addAll(remainingCities);
       }

       List<CityDTO> resCity = cityToDTO(cities);

       return new ResBody(200, "Cities in order of specification", resCity);
   }

    private boolean checkDuplicateCity(CityBody body) {

        City findCity = queryFactory
                .selectFrom(city)
                .where(city.name.eq(body.getName()))
                .fetchFirst();

        if (findCity != null) {
            return true;
        }
        return false;
    }

    private List<City> fetchCitiesOngoing(LocalDate today) {

       return queryFactory
               .select(city)
               .from(trip)
               .leftJoin(trip.city, city)
               .where(trip.startDate.loe(today), trip.endDate.goe(today))
               .orderBy(trip.startDate.asc())
               .fetch();

    }

    private List<City> fetchCitiesExpected(LocalDate today, int limit) {

        return queryFactory
                .select(city)
                .from(trip)
                .leftJoin(trip.city, city)
                .where(trip.startDate.gt(today))
                .orderBy(trip.startDate.asc())
                .limit(limit)
                .fetch();
    }

    private List<City> fetchCitiesExpectedByCreatedDate(LocalDate today, int limit, List<City> cities) {
        List<Long> cityIds = fetchAllCityIds(cities);

        return queryFactory
                .selectFrom(city)
                .where(city.createdDate.goe(today.minusDays(1)), city.id.notIn(cityIds))
                .orderBy(city.createdDate.desc())
                .limit(limit)
                .fetch();

    }

    private List<City> fetchCitiesExpectedByViewedDate(LocalDate today, int limit, List<City> cities) {
        List<Long> cityIds = fetchAllCityIds(cities);

        return queryFactory
                .selectFrom(city)
                .where(city.viewedDate.goe(today.minusDays(7)), city.id.notIn(cityIds))
                .orderBy(city.viewedDate.desc())
                .limit(limit)
                .fetch();
    }

    private List<City> fetchRemainingCities(int limit, List<City> cities) {
        List<Long> cityIds = fetchAllCityIds(cities);

        return queryFactory
                .selectFrom(city)
                .where(city.id.notIn(cityIds))
                .limit(limit)
                .fetch();
    }

    private List<Long> fetchAllCityIds(List<City> cities) {
        return cities.stream()
                .map(c -> c.getId()).collect(Collectors.toList());
    }


    private List<CityDTO> cityToDTO(List<City> cities) {
        return cities.stream()
                .map(c -> {
                    return new CityDTO(c.getName(), c.getViewedDate());
                }).collect(Collectors.toList());
    }

    private void refreshContext() {
        em.flush();
        em.clear();
    }
}
