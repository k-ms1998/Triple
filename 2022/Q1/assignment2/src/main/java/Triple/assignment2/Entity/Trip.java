package Triple.assignment2.Entity;

import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@ToString(of = {"id"})
public class Trip {

    @Id
    @GeneratedValue
    @Column(name = "tripId")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cityId")
    private City city;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    private LocalDate startDate;
    private LocalDate endDate;

    protected Trip() {

    }

    public Trip(City city, User user, LocalDate startDate, LocalDate endDate) {
        this.city = city;
        this.user = user;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
