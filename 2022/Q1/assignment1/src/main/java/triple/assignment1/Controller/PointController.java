package triple.assignment1.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import triple.assignment1.Entity.Action;
import triple.assignment1.Service.PointService;

@Controller
@ResponseBody
@RequiredArgsConstructor
public class PointController {

    private final PointService pointService;

    @PostMapping("/addPoints")
    public String addPoints(@RequestBody EventBody request) {
        System.out.println("request = " + request);
        if (request.getAction() == Action.ADD) {
            return pointService.addPoints(request);
        } else if (request.getAction() == Action.MOD) {
            return pointService.updatePoints(request);
        } else {
            return pointService.removePoints(request);
        }
    }

    @GetMapping("/getPoints")
    public String getPoints(@RequestBody EventBody request) {
        pointService.getPoints(request);

        return "";
    }

}
