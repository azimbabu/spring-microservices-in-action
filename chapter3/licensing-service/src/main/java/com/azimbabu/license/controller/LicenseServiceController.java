package com.azimbabu.license.controller;

import com.azimbabu.license.model.License;
import com.azimbabu.license.service.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "v1/organizations/{organizationId}/licenses")
public class LicenseServiceController {

  private LicenseService licenseService;

  @Autowired
  public LicenseServiceController(LicenseService licenseService) {
    this.licenseService = licenseService;
  }

  @GetMapping
  public List<License> getLicenses(@PathVariable("organizationId") String organizationId) {
    return licenseService.getLicensesByOrganizationId(organizationId);
  }

  @GetMapping("/{licenseId}")
  public License getLicense(
      @PathVariable("organizationId") String organizationId,
      @PathVariable("licenseId") String licenseId) {
    return licenseService.getLicense(organizationId, licenseId);
  }

  @PutMapping("/{licenseId}")
  public ResponseEntity<Void> updateLicense(
      @PathVariable("organizationId") String organizationId,
      @PathVariable("licenseId") String licenseId,
      @RequestBody License license) {
    Optional<License> licenseOptional =
        licenseService.updateLicense(organizationId, licenseId, license);
    if (!licenseOptional.isPresent()) {
      return ResponseEntity.notFound().build();
    } else {
      return ResponseEntity.noContent().build();
    }
  }

  @PostMapping
  public ResponseEntity<License> createLicense(
      @PathVariable("organizationId") String organizationId,
      @RequestBody License license,
      UriComponentsBuilder uriComponentsBuilder) {
    License savedLicense = licenseService.createLicense(organizationId, license);
    URI uri = uriComponentsBuilder.path("{id}").buildAndExpand(savedLicense.getLicenseId()).toUri();
    return ResponseEntity.created(uri).build();
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<Void> deleteLicense(
      @PathVariable("organizationId") String organizationId,
      @PathVariable("licenseId") String licenseId) {
    boolean deleted = licenseService.deleteLicense(organizationId, licenseId);
    return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
  }
}
