package com.easyjob.easyjobapi.config;

import lombok.RequiredArgsConstructor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.method.AuthorizationManagerBeforeMethodInterceptor;
import org.springframework.security.authorization.method.PreAuthorizeAuthorizationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import java.lang.reflect.Method;
import java.util.Map;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class MethodSecurityConfig {

    private final ApplicationContext context;

    @Bean
    @Order(Ordered.LOWEST_PRECEDENCE) // Run this after other security interceptors
    public AuthorizationManagerBeforeMethodInterceptor defaultDenyInterceptor() {
        // Create a pointcut for controller methods without @PreAuthorize annotation
        StaticMethodMatcherPointcut pointcut = new StaticMethodMatcherPointcut() {
            @Override
            public boolean matches(Method method, Class<?> targetClass) {
                return !method.isAnnotationPresent(PreAuthorize.class);
            }

            @Override
            public ClassFilter getClassFilter() {

                return clazz -> {

                    //only for controllers in project
                    if (clazz.getName()
                             .endsWith("Controller")) {

                        String mainClassPackage = findMainClassPackage();

                        return clazz.getName()
                                    .startsWith(mainClassPackage);
                    } else {
                        return false;
                    }

                };
            }
        };
        // Create an authorization manager that always denies access
        AuthorizationManager<MethodInvocation> authorizationManager =
                (authentication, object) -> new AuthorizationDecision(false);

        return new AuthorizationManagerBeforeMethodInterceptor(pointcut, authorizationManager);
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE) // Process @PreAuthorize first
    public AuthorizationManagerBeforeMethodInterceptor preAuthorizeInterceptor() {
        // This handles methods with @PreAuthorize annotation
        return AuthorizationManagerBeforeMethodInterceptor.preAuthorize(
                new PreAuthorizeAuthorizationManager());
    }

    private String findMainClassPackage() {
        Map<String, Object> annotatedBeans = context.getBeansWithAnnotation(SpringBootApplication.class);
        return annotatedBeans.isEmpty() ? "BOOT_CLASS_NOT_INITIALIZED" : annotatedBeans.values()
                                                                                       .toArray()[0].getClass()
                                                                                                    .getPackageName();
    }
}