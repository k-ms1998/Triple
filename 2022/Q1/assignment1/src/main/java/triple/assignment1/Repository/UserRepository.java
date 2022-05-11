package triple.assignment1.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import triple.assignment1.Entity.User;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, String> {
}
