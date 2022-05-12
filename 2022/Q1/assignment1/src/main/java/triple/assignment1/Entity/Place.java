package triple.assignment1.Entity;

import lombok.Getter;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;

@Entity
@Getter
public class Place implements Persistable<String> {

    @Id
    @Column(name = "placeId")
    private String id;

    protected Place() {

    }

    public Place(String id) {
        this.id = id;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }
}
