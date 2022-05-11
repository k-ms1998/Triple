package triple.assignment1.Entity;

import lombok.Getter;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Review implements Persistable<String> {

    @Id
    @Column(name = "reviewId")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "placeId")
    private Place place;

    private String content;

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

    public void updateContent(String content) {
        this.content = content;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }
}


