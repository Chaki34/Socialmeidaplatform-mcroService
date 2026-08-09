package MicroService.UserMicroService.Configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final String PROFILE_IMAGE_DIRECTORY =
            "file:///F:/Microservices Using Springboot/Social meida platform webapp/src/main/resources/profile_images/";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        System.out.println("======================================");
        System.out.println("PROFILE IMAGE CONFIG LOADED");
        System.out.println("PROFILE IMAGE DIRECTORY: " + PROFILE_IMAGE_DIRECTORY);
        System.out.println("======================================");

        registry.addResourceHandler("/profile-images/**")
                .addResourceLocations(PROFILE_IMAGE_DIRECTORY);
    }
}