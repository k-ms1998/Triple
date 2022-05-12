package triple.assignment1.Repository.Query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import triple.assignment1.Entity.AttachedPhoto;
import triple.assignment1.Entity.QAttachedPhoto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class QueryAttachedPhotoRepositoryTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private JPAQueryFactory queryFactory;

    @Autowired
    private QueryAttachedPhotoRepository qAttachedPhotoRepository;

    private QAttachedPhoto attachedPhoto = QAttachedPhoto.attachedPhoto;

    @Test
    void deleteByReviewId() {
        Long deletePhotos01 = qAttachedPhotoRepository.deleteByReviewId("review_01");
        Long deletePhotos02 = qAttachedPhotoRepository.deleteByReviewId("review_02");


        Assertions.assertThat(deletePhotos01).isEqualTo(3);
        Assertions.assertThat(deletePhotos02).isEqualTo(2);
    }
}