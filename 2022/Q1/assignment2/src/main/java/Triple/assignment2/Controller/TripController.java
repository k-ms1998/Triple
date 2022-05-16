package Triple.assignment2.Controller;

import Triple.assignment2.Controller.body.ResBody;
import Triple.assignment2.Controller.body.TripBody;
import Triple.assignment2.Service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;

    @PostMapping("/trip/save")
    public ResBody saveTrip(@RequestBody TripBody request) {

        return tripService.save(request);
    }

    @GetMapping("/trips")
    public ResBody fetchTrips(@RequestBody TripBody request) {

        return tripService.fetchTrips(request);
    }
}
