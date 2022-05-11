package triple.assignment1.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import triple.assignment1.Entity.AttachedPhoto;

@Repository
@Transactional
public interface AttachedPhotoRepository extends JpaRepository<AttachedPhoto, String> {

    void deleteByReviewId(String reviewId);

}
