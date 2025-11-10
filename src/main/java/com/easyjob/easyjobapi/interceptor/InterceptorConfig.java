package com.easyjob.easyjobapi.interceptor;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {
    private final UserInterceptor userInterceptor;
    private final UserDetailsInterceptor userDetailsInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //Load UserDAO
        registry.addInterceptor(userInterceptor)
                .addPathPatterns("/api/**");

        registry.addInterceptor(userDetailsInterceptor)
                .addPathPatterns("/api/**")
                .addPathPatterns("/internal/**");
    }
}