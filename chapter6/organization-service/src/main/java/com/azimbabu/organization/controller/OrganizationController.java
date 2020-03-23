package com.azimbabu.organization.controller;

import com.azimbabu.organization.model.Organization;
import com.azimbabu.organization.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("v1/organizations")
public class OrganizationController {

  private OrganizationService organizationService;

  @Autowired
  public OrganizationController(OrganizationService organizationService) {
    this.organizationService = organizationService;
  }

  @GetMapping("/{organizationId}")
  public ResponseEntity<Organization> getOrganization(
      @PathVariable("organizationId") String organizationId) {
    Optional<Organization> organizationOptional =
        organizationService.getOrganization(organizationId);
    if (organizationOptional.isPresent()) {
      return ResponseEntity.ok(organizationOptional.get());
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @PutMapping("{organizationId}")
  public ResponseEntity<Void> updateOrganization(
      @PathVariable("organizationId") String organizationId,
      @RequestBody Organization organization) {
    Optional<Organization> organizationOptional =
        organizationService.updateOrganization(organizationId, organization);
    if (organizationOptional.isPresent()) {
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping
  public ResponseEntity<Organization> createOrganization(
      @RequestBody Organization organization, UriComponentsBuilder uriComponentsBuilder) {
    Organization savedOrganization = organizationService.saveOrganization(organization);
    URI uri = uriComponentsBuilder.path("{id}").buildAndExpand(savedOrganization.getId()).toUri();
    return ResponseEntity.created(uri).build();
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteOrganization(@PathVariable("organizationId") String organizationId) {
    organizationService.deleteOrganization(organizationId);
  }
}
