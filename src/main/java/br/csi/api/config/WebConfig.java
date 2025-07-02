package br.csi.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SessaoInterceptor())
                .addPathPatterns("/**")  // aplica a todos os caminhos
                .excludePathPatterns("/login", "/css/**", "/static.js/**", "/imagens/**"); // libera os públicos
    }
}
