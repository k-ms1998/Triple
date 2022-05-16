package Triple.assignment2.Service;

import Triple.assignment2.Controller.body.ResBody;
import Triple.assignment2.Controller.body.TripBody;
import Triple.assignment2.Entity.City;
import Triple.assignment2.Entity.QTrip;
import Triple.assignment2.Entity.Trip;
import Triple.assignment2.Entity.User;
import Triple.assignment2.Repository.TripRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.jni.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

import static Triple.assignment2.Entity.QTrip.trip;

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
            return new ResBody(404, "End date must be later that the start date");
        }
        if (checkStartDate(localDateStart)) {
            return new ResBody(404, "Start date must be later than today");
        }
        if (checkDuplicateTrip(body)) {
            return new ResBody(404, "User has already a trip planned at this location");
        }

        tripRepository.save(createTrip(body, localDateStart, localDateEnd));

        return new ResBody(200, "Trip Saved");
    }

    public ResBody fetchTrips(TripBody body) {
        Long userId = body.getUserId();

        List<Trip> findTrips = findTripsByUser(userId);


        return new ResBody(200, "Message");
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
        Long city = Long.valueOf(body.getCity());

        Trip findTrip = queryFactory
                .selectFrom(QTrip.trip)
                .where(QTrip.trip.user.id.eq(userId), QTrip.trip.city.id.eq(city))
                .fetchFirst();

        if (findTrip != null) {
            return true;
        }
        return false;
    }

    private Trip createTrip(TripBody body, LocalDate startDate, LocalDate endDate) {
        return new Trip(new City(Long.valueOf(body.getCity())), new User(body.getUserId()), startDate, endDate);
    }

    private List<Trip> findTripsByUser(Long userId) {
        return queryFactory
                .selectFrom(trip)
                .where(trip.user.id.eq(userId))
                .fetch();
    }
}
