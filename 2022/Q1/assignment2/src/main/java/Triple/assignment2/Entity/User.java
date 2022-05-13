package Triple.assignment2.Entity;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "Users")
@ToString
public class User {

    @Id
    @GeneratedValue
    @Column(name = "userId")
    private Long id;

    protected User() {
    }

    public User(Long id) {
        this.id = id;
    }
}
