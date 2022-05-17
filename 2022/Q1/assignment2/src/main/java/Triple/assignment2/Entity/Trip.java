package Triple.assignment2.Entity;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
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

    @Override
    public String toString() {
        return "Trip{" +
                "id=" + this.getId() +
                ", city=" + this.getCity().getId() +
                ", user=" + this.getUser().getId() +
                ", startDate=" + this.getStartDate()+
                ", endDate=" + this.getEndDate() +
                '}';
    }
}
