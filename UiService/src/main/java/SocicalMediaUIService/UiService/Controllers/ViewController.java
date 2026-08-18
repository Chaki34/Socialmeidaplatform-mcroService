package SocicalMediaUIService.UiService.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ViewController {

    @GetMapping("/setup")
    public String userSetupPage(
            @RequestParam(value = "mode", required = false, defaultValue = "signup") String mode,
            Model model
    ) {
        model.addAttribute("mode", mode);
        return "userSetup";
    }

    @GetMapping("/feed")
    public String userFeedPage() {
        return "userFeed";
    }

    @GetMapping("/")
    public String LandingPage(Model model) {
        model.addAttribute(
                "heroTitle",
                "Your life's work, powered by our life's work"
        );
        return "landing";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/about")
    public String aboutPage() {
        return "about";
    }
}