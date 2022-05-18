package Triple.assignment2.Controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class CityDTO {

    private String cityName;
    private LocalDate createdDate;
    private LocalDate viewedDate;

    public CityDTO() {
    }

    public CityDTO(String cityName, LocalDate createdDate) {
        this.cityName = cityName;
        this.createdDate = createdDate;
    }

    public CityDTO(String cityName, LocalDate createdDate, LocalDate viewedDate) {
        this.cityName = cityName;
        this.createdDate = createdDate;
        this.viewedDate = viewedDate;
    }
}
