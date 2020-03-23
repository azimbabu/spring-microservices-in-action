package com.azimbabu.license.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Organization {
  private String id;
  private String name;
  private String contactName;
  private String contactEmail;
  private String contactPhone;
}
