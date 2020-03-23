package com.azimbabu.zuulservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.ForwardedHeaderFilter;

@SpringBootApplication
@EnableZuulProxy
public class ZuulServiceApplication {
  public static void main(String[] args) {
      SpringApplication.run(ZuulServiceApplication.class, args);
  }

  // needed to
  @Bean
  public ForwardedHeaderFilter forwardedHeaderFilter() {
      return new ForwardedHeaderFilter();
  }
}
