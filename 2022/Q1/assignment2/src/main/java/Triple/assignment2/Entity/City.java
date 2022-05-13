package Triple.assignment2.Entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
public class City {

    @Id
    @GeneratedValue
    @Column(name = "cityId")
    private Long id;

    @Column(unique = true)
    private String name;

    @CreatedDate
    @Column(updatable = false)
    private LocalDate createdDate;

    private LocalDate viewedDate;

    protected City() {
    }

    public City(Long id) {
        this.id = id;
    }

    public City(String name) {
        this.name = name;
    }

    public City(String name, LocalDate viewedDate) {
        this.name = name;
        this.viewedDate = viewedDate;
    }
}
