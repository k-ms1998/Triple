package Triple.assignment2.Service;

import Triple.assignment2.Controller.body.CityBody;
import Triple.assignment2.Controller.body.ResBody;
import Triple.assignment2.Entity.City;
import Triple.assignment2.Entity.QCity;
import Triple.assignment2.Repository.CityRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static Triple.assignment2.Entity.QCity.city;


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
            return new ResBody(404, "City already exists");
        }

        cityRepository.save(new City(body.getName()));

        return new ResBody(200, "City Saved");
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

}
