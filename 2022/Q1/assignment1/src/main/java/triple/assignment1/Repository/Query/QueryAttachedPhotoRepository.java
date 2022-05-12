package triple.assignment1.Repository.Query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import triple.assignment1.Entity.QAttachedPhoto;

import javax.persistence.EntityManager;

@Repository
@Transactional
@RequiredArgsConstructor
public class QueryAttachedPhotoRepository {

    private EntityManager em;
    private final JPAQueryFactory queryFactory;

    private QAttachedPhoto attachedPhoto = QAttachedPhoto.attachedPhoto;

    /**
     * 특정 리뷰에 첨부된 사진들을 삭제
     * @param reviewId
     * @return
     */
    public Long deleteByReviewId(String reviewId) {
        return queryFactory
                .delete(QAttachedPhoto.attachedPhoto)
                .where(QAttachedPhoto.attachedPhoto.review.id.eq(reviewId))
                .execute();
    }
}
