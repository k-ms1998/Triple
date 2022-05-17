package Triple.assignment2.Controller;

import Triple.assignment2.Controller.body.CityBody;
import Triple.assignment2.Controller.body.ResBody;
import Triple.assignment2.Service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
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

    /**
     * 단건조회
     *
     * @param request
     */
    @GetMapping("/city")
    public ResBody city(@RequestBody CityBody request) {

        return cityService.fetchCity(request);
    }


    /**
     * 도시리스트 조회
     * @param request
     * @return
     */
    @GetMapping("/cities")
    public ResBody citiesList(@RequestBody CityBody request) {

        return cityService.fetchCitiesList(request);
    }
}
