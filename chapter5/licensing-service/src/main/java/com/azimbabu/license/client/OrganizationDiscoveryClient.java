package com.azimbabu.license.client;

import com.azimbabu.license.model.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class OrganizationDiscoveryClient {

  private final DiscoveryClient discoveryClient;

  @Autowired
  public OrganizationDiscoveryClient(DiscoveryClient discoveryClient) {
    this.discoveryClient = discoveryClient;
  }

  public Organization getOrganization(String organizationId) {
    List<ServiceInstance> serviceInstances = discoveryClient.getInstances("organization-service");
    if (serviceInstances.isEmpty()) {
      return null;
    }

    String serviceUrl =
        String.format(
            "%s/v1/organizations/%s", serviceInstances.get(0).getUri().toString(), organizationId);
    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<Organization> responseEntity =
        restTemplate.exchange(serviceUrl, HttpMethod.GET, null, Organization.class, organizationId);
    return responseEntity.getBody();
  }
}
