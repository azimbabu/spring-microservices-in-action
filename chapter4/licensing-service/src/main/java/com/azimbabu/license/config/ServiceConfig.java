package com.azimbabu.license.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@Data
@RefreshScope
public class ServiceConfig {

  @Value("${example.property}")
  private String exampleProperty;
}
