package shop.mtcoding.metamall.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import shop.mtcoding.metamall.core.filter.JwtVerifyFilter;


@Configuration
public class FilterRegisterConfig {
    @Bean
    public FilterRegistrationBean<?> jwtVerifyFilterAdd() {
        FilterRegistrationBean<JwtVerifyFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new JwtVerifyFilter());
        registration.addUrlPatterns("/users/*"); //토큰
        registration.addUrlPatterns("/products/*");//토큰
        registration.addUrlPatterns("/orders/*");//토큰
        registration.addUrlPatterns("/admin/*"); //토큰 & 권한 ADMIN
        registration.addUrlPatterns("/seller/*");//토큰 & 권한 ADMIN, SELLER
        registration.setOrder(1);
        return registration;
    }
}
