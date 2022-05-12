package triple.assignment1.Service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestExecution;
import org.springframework.transaction.annotation.Transactional;
import triple.assignment1.Controller.EventBody;
import triple.assignment1.Entity.Action;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional(readOnly = true)
class PointServiceTest {

    @Autowired
    private PointService pointService;

    static EventBody body = new EventBody();

    void createEventBody() {
        List<String> photoIds = new ArrayList<>();
        photoIds.add("test_photo_id_01");
        photoIds.add("test_photo_id_02");

        body.setType("REVIEW");
        body.setAction(Action.ADD);
        body.setReviewId("review_01");
        body.setContent("Good!");
        body.setAttachedPhotoIds(photoIds);
        body.setUserId("user_01");
        body.setPlaceId("place_01");

    }

    @Test
    void fetchReviewCountNew() {
        createEventBody();

        body.setPlaceId("NEW_PLACE");
        Long reviewCount = pointService.fetchReviewCount(body);

        Assertions.assertThat(reviewCount).isEqualTo(0L);
    }

    @Test
    void fetchReviewCountExisting() {
        createEventBody();

        Long reviewCount = pointService.fetchReviewCount(body);

        Assertions.assertThat(reviewCount).isEqualTo(1L);
    }

    @Test
    void getPoints() {
        createEventBody();

        String points = pointService.getPoints(body);

        Assertions.assertThat(points).isEqualTo(String.valueOf(3));

    }
}