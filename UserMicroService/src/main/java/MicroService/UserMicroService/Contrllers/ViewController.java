package MicroService.UserMicroService.Contrllers;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/setup")
    public String userSetupPage() {
        return "userSetup";
    }
}