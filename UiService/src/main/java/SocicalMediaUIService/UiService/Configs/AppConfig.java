package SocicalMediaUIService.UiService.Bins;

import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;// In UiService
@Bean
public RestTemplate restTemplate() {
    return new RestTemplate();
}
