package com.azimbabu.license.service;

import com.azimbabu.license.model.License;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LicenseService {

    public License getLicense(String organizationId, String licenseId) {
        return License.builder()
                .id(licenseId)
                .organizationId(organizationId)
                .productName("Test Product Name")
                .licenseType("PerSeat")
                .build();
    }

    public License createLicense(String organizationId, License license) {
        license.setId(UUID.randomUUID().toString());
        return license;
    }

    public License updateLicense(String organizationId, String licenseId, License license) {
        return license;
    }

    public void deleteLicense(String organizationId, String licenseId) {

    }
}
