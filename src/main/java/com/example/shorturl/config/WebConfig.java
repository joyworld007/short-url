package com.example.shorturl.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Autowired
  @Qualifier(value = "shortUrlInterceptor")
  private HandlerInterceptor interceptor;

  private String[] INTERCEPTOR_WHITE_LIST = {
      "/v1/short-urls/**"
  };

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(interceptor)
        .addPathPatterns("/**")
        .excludePathPatterns(INTERCEPTOR_WHITE_LIST);
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/webjars/**")
        .addResourceLocations("/webjars/")
        .resourceChain(false);
    registry.setOrder(1);
  }

}
