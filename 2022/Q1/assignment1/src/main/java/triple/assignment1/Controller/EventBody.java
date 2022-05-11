package triple.assignment1.Controller;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import triple.assignment1.Entity.Action;

import java.util.List;

@Getter
@Setter
@ToString
public class EventBody {

    private String type;
    private Action action;
    private String reviewId;
    private String content;
    private List<String> attachedPhotoIds;
    private String userId;
    private String placeId;

}
