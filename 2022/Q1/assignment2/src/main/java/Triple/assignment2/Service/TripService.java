package Triple.assignment2.Service;

import Triple.assignment2.Controller.body.ResBody;
import Triple.assignment2.Controller.body.TripBody;
import Triple.assignment2.Controller.dto.TripDTO;
import Triple.assignment2.Entity.*;
import Triple.assignment2.Repository.TripRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.jni.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static Triple.assignment2.Entity.QCity.city;
import static Triple.assignment2.Entity.QTrip.*;
import static Triple.assignment2.Entity.QTrip.trip;
import static Triple.assignment2.Entity.QUser.user;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TripService {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    private final TripRepository tripRepository;

    @Transactional
    public ResBody save(TripBody body) {
        String startDate = body.getStartDate(); // YYYY-MM-DD
        String endDate = body.getEndDate();     // YYYY-MM-DD

        LocalDate localDateStart = stringToLocalDate(startDate);
        LocalDate localDateEnd = stringToLocalDate(endDate);

        if (localDateStart.isAfter(localDateEnd)) {
            return new ResBody(404, "End date must be later than the start date", null);
        }
        if (checkStartDate(localDateStart)) {
            return new ResBody(404, "Start date must be later than today", null);
        }
        if (checkDuplicateTrip(body)) {
            return new ResBody(404, "User already has a trip planned at this location", null);
        }

        Trip savedTrip = tripRepository.save(createTrip(body, localDateStart, localDateEnd));
        List<TripDTO> resTrips = tripToDTO(List.of(savedTrip));

        return new ResBody(200, "Trip Saved", resTrips);
    }

    public ResBody<Trip> fetchTrips(TripBody body) {
        Long userId = body.getUserId();

        List<Trip> findTrips = findTripsByUser(userId);
        List<TripDTO> resTrips = tripToDTO(findTrips);

        return new ResBody(200, "Trips By User", resTrips);
    }

    private boolean checkStartDate(LocalDate startDate) {
        LocalDate today = LocalDate.now();

        return today.isAfter(startDate);
    }

    private LocalDate stringToLocalDate(String date) {
        String[] dateArr = date.split("-");
        Integer year = Integer.valueOf(dateArr[0]);
        Integer month = Integer.valueOf(dateArr[1]);
        Integer day = Integer.valueOf(dateArr[2]);

        return LocalDate.of(year, month, day);
    }

    private boolean checkDuplicateTrip(TripBody body) {
        Long userId = body.getUserId();
        Long cityId = body.getCityId();

        Trip findTrip = queryFactory
                .selectFrom(trip)
                .where(trip.user.id.eq(userId), trip.city.id.eq(cityId))
                .fetchFirst();

        if (findTrip != null) {
            return true;
        }
        return false;
    }

    private Trip createTrip(TripBody body, LocalDate startDate, LocalDate endDate) {
        return new Trip(new City(body.getCityId()), new User(body.getUserId()), startDate, endDate);
    }

    private List<Trip> findTripsByUser(Long userId) {
        return queryFactory
                .selectFrom(trip)
                .innerJoin(trip.user, user).fetchJoin()
                .leftJoin(trip.city, city).fetchJoin()
                .where(trip.user.id.eq(userId))
                .fetch();
    }

    private List<TripDTO> tripToDTO(List<Trip> findTrips) {
        return findTrips.stream()
                .map(t -> {
                    String cityName = t.getCity().getName();
                    Long userId = t.getUser().getId();
                    LocalDate startDate = t.getStartDate();
                    LocalDate endDate = t.getEndDate();

                    return new TripDTO(cityName, userId, startDate, endDate);
                }).collect(Collectors.toList());
    }
}
