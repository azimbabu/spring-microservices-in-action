package com.azimbabu.license.client;

import com.azimbabu.license.model.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OrganizationRestTemplateClient {

  private final RestTemplate restTemplate;

  @Autowired
  public OrganizationRestTemplateClient(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public Organization getOrganization(String organizationId) {
    String serviceUrl = "http://organization-service/v1/organizations/{organizationId}";
    ResponseEntity<Organization> responseEntity =
        restTemplate.exchange(serviceUrl, HttpMethod.GET, null, Organization.class, organizationId);
    return responseEntity.getBody();
  }
}
