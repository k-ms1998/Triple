package triple.assignment1.Entity;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@ToString(of = {"id", "content", "userId"})
public class Review implements Persistable<String> {

    @Id
    @Column(name = "reviewId")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "placeId")
    private Place place;

    private String content;
    private String userId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "review", cascade = CascadeType.REMOVE)
    private List<AttachedPhoto> photos = new ArrayList<>();

    protected  Review() {

    }

    public Review(String id) {
        this.id = id;
    }

    public Review(String id, Place place, String content) {
        this.id = id;
        this.place = place;
        this.content = content;
    }

    public Review(String id, Place place, String content, String userId) {
        this.id = id;
        this.place = place;
        this.content = content;
        this.userId = userId;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }
}


