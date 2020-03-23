package com.azimbabu.license.service;

import com.azimbabu.license.client.OrganizationDiscoveryClient;
import com.azimbabu.license.client.OrganizationFeignClient;
import com.azimbabu.license.client.OrganizationRestTemplateClient;
import com.azimbabu.license.config.ServiceConfig;
import com.azimbabu.license.model.License;
import com.azimbabu.license.model.Organization;
import com.azimbabu.license.repository.LicenseRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@Service
public class LicenseService {

  private final LicenseRepository licenseRepository;

  private final ServiceConfig serviceConfig;

  private final OrganizationDiscoveryClient organizationDiscoveryClient;

  private final OrganizationRestTemplateClient organizationRestTemplateClient;

  private final OrganizationFeignClient organizationFeignClient;

  @Autowired
  public LicenseService(LicenseRepository licenseRepository, ServiceConfig serviceConfig, OrganizationDiscoveryClient organizationDiscoveryClient, OrganizationRestTemplateClient organizationRestTemplateClient, OrganizationFeignClient organizationFeignClient) {
    this.licenseRepository = licenseRepository;
    this.serviceConfig = serviceConfig;
    this.organizationDiscoveryClient = organizationDiscoveryClient;
    this.organizationRestTemplateClient = organizationRestTemplateClient;
    this.organizationFeignClient = organizationFeignClient;
  }

  public License getLicense(String organizationId, String licenseId) {
    License license =
        licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
    license.setComment(serviceConfig.getExampleProperty());
    return license;
  }

  public List<License> getLicensesByOrganizationId(String organizationId) {
    return licenseRepository.findByOrganizationId(organizationId);
  }

  public License createLicense(String organizationId, License license) {
    license.setLicenseId(UUID.randomUUID().toString());
    license.setOrganizationId(organizationId);
    return licenseRepository.save(license);
  }

  public Optional<License> updateLicense(String organizationId, String licenseId, License license) {
    License existingLicense = getLicense(organizationId, licenseId);
    if (existingLicense == null) {
      return Optional.empty();
    } else {
      license.setLicenseId(licenseId);
      license.setOrganizationId(organizationId);
      return Optional.of(licenseRepository.save(license));
    }
  }

  public boolean deleteLicense(String organizationId, String licenseId) {
    License existingLicense = getLicense(organizationId, licenseId);
    if (existingLicense == null) {
      return false;
    } else {
      licenseRepository.delete(existingLicense);
      return true;
    }
  }

  public License getLicenseWithClient(String organizationId, String licenseId, String clientType) {
    License license =
            licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);

    Organization organization = retrieveOrgInfo(organizationId, clientType);

    license.setComment(serviceConfig.getExampleProperty());
    return license.toBuilder()
            .organizationName(organization.getName())
            .contactName(organization.getContactName())
            .contactEmail(organization.getContactEmail())
            .contactPhone(organization.getContactPhone())
            .build();
  }

  private Organization retrieveOrgInfo(String organizationId, String clientType) {
    switch (clientType) {
      case "discovery":
        log.info("Using discovery client");
        return organizationDiscoveryClient.getOrganization(organizationId);
      case "rest":
        log.info("Using rest client");
        return organizationRestTemplateClient.getOrganization(organizationId);
      case "feign":
        log.info("Using feign client");
        return organizationFeignClient.getOrganization(organizationId);
      default:
        log.info("Using rest client as default");
        return organizationRestTemplateClient.getOrganization(organizationId);
    }
  }
}
