package Triple.assignment2.Controller.body;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResBody<T>{
    private Integer status;
    private String message;
    private List<T> data;

    public ResBody(Integer status, String message, List<T> data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
