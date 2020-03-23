package com.azimbabu.organization.service;

import com.azimbabu.organization.model.Organization;
import com.azimbabu.organization.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class OrganizationService {

  private OrganizationRepository organizationRepository;

  @Autowired
  public OrganizationService(OrganizationRepository organizationRepository) {
    this.organizationRepository = organizationRepository;
  }

  public Optional<Organization> getOrganization(String organizationId) {
    return organizationRepository.findById(organizationId);
  }

  public Organization saveOrganization(Organization organization) {
    organization.setId(UUID.randomUUID().toString());
    return organizationRepository.save(organization);
  }

  public Optional<Organization> updateOrganization(
      String organizationId, Organization organization) {
    Optional<Organization> organizationOptional = getOrganization(organizationId);
    if (organizationOptional.isPresent()) {
      organization.setId(organizationId);
      return Optional.of(organizationRepository.save(organization));
    } else {
      return Optional.empty();
    }
  }

  public void deleteOrganization(String organizationId) {
    organizationRepository.deleteById(organizationId);
  }
}
