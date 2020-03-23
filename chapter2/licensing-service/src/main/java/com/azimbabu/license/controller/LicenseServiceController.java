package com.azimbabu.license.controller;

import com.azimbabu.license.model.License;
import com.azimbabu.license.service.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "v1/organizations/{organizationId}/licenses")
public class LicenseServiceController {

    private LicenseService licenseService;

    @Autowired
    public LicenseServiceController(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    @GetMapping("/{licenseId}")
    public License getLicense(@PathVariable("organizationId") String organizationId,
                              @PathVariable("licenseId") String licenseId) {
        return licenseService.getLicense(organizationId, licenseId);
    }

    @PutMapping("/{licenseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateLicense(@PathVariable("organizationId") String organizationId,
                              @PathVariable("licenseId") String licenseId,
                              @RequestBody License license) {
        licenseService.updateLicense(organizationId, licenseId, license);
    }

    @PostMapping
    public ResponseEntity<License> createLicense(@PathVariable("organizationId") String organizationId,
                                                 @RequestBody License license,
                                                 UriComponentsBuilder uriComponentsBuilder) {
        License savedLicense = licenseService.createLicense(organizationId, license);
        URI uri = uriComponentsBuilder.path("{id}").buildAndExpand(savedLicense.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLicense(@PathVariable("organizationId") String organizationId,
                              @PathVariable("licenseId") String licenseId) {
        licenseService.deleteLicense(organizationId, licenseId);
    }
}
