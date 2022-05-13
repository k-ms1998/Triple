package Triple.assignment2.Controller.body;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResBody {
    private Integer status;
    private String message;

    public ResBody(Integer status, String message) {
        this.status = status;
        this.message = message;
    }
}
