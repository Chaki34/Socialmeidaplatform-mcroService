package MicroService.UserMicroService.Configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {


        System.out.println("PROFILE IMAGE CONFIG LOADED");


        registry.addResourceHandler("/profile-images/**")
                .addResourceLocations(
                        "file:///F:/Microservices Using Springboot/Social meida platform webapp/src/main/resources/profile_images/"
                );


    }

}