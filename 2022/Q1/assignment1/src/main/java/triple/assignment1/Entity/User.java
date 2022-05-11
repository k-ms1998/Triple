package triple.assignment1.Entity;

import lombok.Getter;
import org.springframework.data.domain.Persistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Table(name = "users")
public class User implements Persistable<String> {

    @Id
    @Column(name = "userId")
    private String id;

    @Override
    public boolean isNew() {
        return id == null;
    }

    protected User() {
    }

    public User(String id) {
        this.id = id;
    }
}
