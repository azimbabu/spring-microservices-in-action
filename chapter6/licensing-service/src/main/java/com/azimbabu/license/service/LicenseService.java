package com.azimbabu.license.service;

import com.azimbabu.license.client.OrganizationDiscoveryClient;
import com.azimbabu.license.client.OrganizationFeignClient;
import com.azimbabu.license.client.OrganizationRestTemplateClient;
import com.azimbabu.license.config.ServiceConfig;
import com.azimbabu.license.model.License;
import com.azimbabu.license.model.Organization;
import com.azimbabu.license.repository.LicenseRepository;
import com.azimbabu.license.utils.UserContextHolder;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class LicenseService {

  private final LicenseRepository licenseRepository;

  private final ServiceConfig serviceConfig;

  private final OrganizationDiscoveryClient organizationDiscoveryClient;

  private final OrganizationRestTemplateClient organizationRestTemplateClient;

  private final OrganizationFeignClient organizationFeignClient;

  @Autowired
  public LicenseService(
      LicenseRepository licenseRepository,
      ServiceConfig serviceConfig,
      OrganizationDiscoveryClient organizationDiscoveryClient,
      OrganizationRestTemplateClient organizationRestTemplateClient,
      OrganizationFeignClient organizationFeignClient) {
    this.licenseRepository = licenseRepository;
    this.serviceConfig = serviceConfig;
    this.organizationDiscoveryClient = organizationDiscoveryClient;
    this.organizationRestTemplateClient = organizationRestTemplateClient;
    this.organizationFeignClient = organizationFeignClient;
  }

  public License getLicense(String organizationId, String licenseId) {
    License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
    license.setComment(serviceConfig.getExampleProperty());
    return license;
  }

  @HystrixCommand(
      fallbackMethod = "buildFallbackLicenses",
      threadPoolKey = "getLicensesByOrganizationIdThreadPool",
      threadPoolProperties = {
        @HystrixProperty(name = "coreSize", value = "30"),
        @HystrixProperty(name = "maxQueueSize", value = "10")
      },
      commandProperties = {
        @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
        @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "75"),
        @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "7000"),
        @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "15000"),
        @HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "5")
      })
  public List<License> getLicensesByOrganizationId(String organizationId) {
    log.debug(
        "LicenseService.getLicensesByOrganizationId Correlation ID: {}",
        UserContextHolder.getUserContext().getCorrelationId());
    randomlyRunLong();
    return licenseRepository.findByOrganizationId(organizationId);
  }

  private void randomlyRunLong() {
    Random random = new Random();
    int randomNum = random.nextInt(3) + 1;
    if (randomNum == 3) {
      sleep();
    }
  }

  private void sleep() {
    try {
      Thread.sleep(11000);
    } catch (InterruptedException e) {
      log.error("Sleep is interrupted, error={}", e.getMessage());
    }
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

  @HystrixCommand(
      commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "12000")
      })
  public License getLicenseWithClient(String organizationId, String licenseId, String clientType) {
    License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);

    Organization organization = retrieveOrgInfo(organizationId, clientType);

    license.setComment(serviceConfig.getExampleProperty());
    return license
        .toBuilder()
        .organizationName(organization.getName())
        .contactName(organization.getContactName())
        .contactEmail(organization.getContactEmail())
        .contactPhone(organization.getContactPhone())
        .build();
  }

  private Organization retrieveOrgInfo(String organizationId, String clientType) {
    randomlyRunLong();
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

  private List<License> buildFallbackLicenses(String organizationId) {
    License license =
        License.builder()
            .licenseId("0000000-00-00000")
            .organizationId(organizationId)
            .productName("Sorry no licensing information currently available")
            .build();
    return Arrays.asList(license);
  }
}
