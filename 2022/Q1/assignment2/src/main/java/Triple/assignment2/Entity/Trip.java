package Triple.assignment2.Entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Trip {

    @Id
    @GeneratedValue
    @Column(name = "tripId")
    private Long id;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "cityId")
    private City city;

    @OneToMany(fetch = FetchType.LAZY)
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
