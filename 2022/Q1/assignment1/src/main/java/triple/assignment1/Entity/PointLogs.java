package triple.assignment1.Entity;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@ToString(of = {"id", "review", "type", "point"})
public class PointLogs {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    private String review;

    @Enumerated(value = EnumType.STRING)
    private PointType type;

    private int point;

    protected PointLogs() {
    }

    public PointLogs(User user, String review, PointType type, int point) {
        this.user = user;
        this.review = review;
        this.type = type;
        this.point = point;
    }
}
