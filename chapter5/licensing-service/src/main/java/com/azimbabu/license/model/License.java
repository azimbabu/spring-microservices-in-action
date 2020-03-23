package com.azimbabu.license.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "licenses")
public class License {
  @Id
  @Column(name = "license_id", nullable = false)
  private String licenseId;

  @Column(name = "organization_id", nullable = false)
  private String organizationId;

  @Column(name = "product_name", nullable = false)
  private String productName;

  @Column(name = "license_type", nullable = false)
  private String licenseType;

  @Column(name = "license_max", nullable = false)
  private Integer licenseMax;

  @Column(name = "license_allocated", nullable = false)
  private Integer licenseAllocated;

  @Column(name = "comment")
  private String comment;

  @Transient private String organizationName;

  @Transient private String contactName;

  @Transient private String contactEmail;

  @Transient private String contactPhone;
}
