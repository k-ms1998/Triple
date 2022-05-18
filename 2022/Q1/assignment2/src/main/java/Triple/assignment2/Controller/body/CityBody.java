package Triple.assignment2.Controller.body;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CityBody {

    private Long id;
    private String name;

    public CityBody() {
    }

    public CityBody(String name) {
        this.name = name;
    }
}
