package triple.assignment1.Entity;

import lombok.Getter;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;

@Entity
@Getter
public class AttachedPhoto implements Persistable<String> {

    @Id
    @Column(name = "attachedPhotoId")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "reviewId")
    private Review review;

    @Override
    public boolean isNew() {
        return id == null;
    }

    protected AttachedPhoto() {

    }

    public AttachedPhoto(String id, Review review) {
        this.id = id;
        setAndUpdateReview(review);
    }

    private void setAndUpdateReview(Review review) {
        this.review = review;
        review.getPhotos().add(this);
    }
}
