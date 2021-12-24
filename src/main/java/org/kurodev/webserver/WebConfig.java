package org.kurodev.webserver;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import javax.annotation.PostConstruct;
import java.time.Duration;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    private final String resourcesFolder = "webResources";
    private final SpringTemplateEngine templateEngine;

    public WebConfig(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations(String.format("file:%s/", resourcesFolder))
                .setCacheControl(CacheControl.maxAge(Duration.ofDays(365)));
    }

    @PostConstruct
    public void extension() {
        FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setPrefix(String.format("./%s/", resourcesFolder));
        resolver.setSuffix(".html");
        resolver.setOrder(templateEngine.getTemplateResolvers().size());
        resolver.setCacheable(false);
        templateEngine.addTemplateResolver(resolver);
    }
}
