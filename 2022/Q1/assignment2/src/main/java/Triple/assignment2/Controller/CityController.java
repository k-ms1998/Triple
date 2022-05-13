package Triple.assignment2.Controller;

import Triple.assignment2.Controller.body.CityBody;
import Triple.assignment2.Controller.body.ResBody;
import Triple.assignment2.Service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CityController {

    private final CityService cityService;

    @PostMapping("/city/save")
    public ResBody saveCity(@RequestBody CityBody body) {
        return cityService.save(body);
    }



}
