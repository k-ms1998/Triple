package Triple.assignment2.Controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TripDTO {

    private String cityname;
    private Long user;
    private LocalDate startDate;
    private LocalDate endDate;

    public TripDTO() {
    }

    public TripDTO(String cityname, Long user, LocalDate startDate, LocalDate endDate) {
        this.cityname = cityname;
        this.user = user;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
