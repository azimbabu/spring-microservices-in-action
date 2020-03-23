package com.azimbabu.license.repository;

import com.azimbabu.license.model.License;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LicenseRepository extends CrudRepository<License, String> {

  List<License> findByOrOrganizationId(String organizationId);

  License findByOrOrganizationIdAndLicenseId(String organizationId, String licenseId);
}
