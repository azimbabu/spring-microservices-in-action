package com.azimbabu.license.service;

import com.azimbabu.license.config.ServiceConfig;
import com.azimbabu.license.model.License;
import com.azimbabu.license.repository.LicenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LicenseService {

  private LicenseRepository licenseRepository;

  private ServiceConfig serviceConfig;

  @Autowired
  public LicenseService(LicenseRepository licenseRepository, ServiceConfig serviceConfig) {
    this.licenseRepository = licenseRepository;
    this.serviceConfig = serviceConfig;
  }

  public License getLicense(String organizationId, String licenseId) {
    License license =
        licenseRepository.findByOrOrganizationIdAndLicenseId(organizationId, licenseId);
    license.setComment(serviceConfig.getExampleProperty());
    return license;
  }

  public List<License> getLicensesByOrganizationId(String organizationId) {
    return licenseRepository.findByOrOrganizationId(organizationId);
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
}
