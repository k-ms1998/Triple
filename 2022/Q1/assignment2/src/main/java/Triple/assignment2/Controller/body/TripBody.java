package Triple.assignment2.Controller.body;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TripBody {

    private Long userId;
    private Long cityId;
    private String startDate;
    private String endDate;

    public TripBody() {

    }

    public TripBody(Long userId, Long cityId, String startDate, String endDate) {
        this.userId = userId;
        this.cityId = cityId;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
