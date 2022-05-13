package Triple.assignment2.Repository;

import Triple.assignment2.Entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface TripRepository extends JpaRepository<Trip, Long> {
}
