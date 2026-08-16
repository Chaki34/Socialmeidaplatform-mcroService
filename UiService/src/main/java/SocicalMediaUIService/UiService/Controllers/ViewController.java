package SocicalMediaUIService.UiService.Controllers;



import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/setup")
    public String userSetupPage() {
        return "userSetup";
    }

    @GetMapping("/feed")
    public String userFeedPage() {
        return "userFeed";
    }


    @GetMapping("/")
    public String LandingPage(Model model) {
        model.addAttribute("heroTitle", "Your life's work, powered by our life's work");
        return "landing";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        // This returns the login.html Thymeleaf template
        return "login";
    }
}