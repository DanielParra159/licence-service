package com.danielparra.licenseservice.contollers;

import com.danielparra.licenseservice.model.License;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/organizations/{organizationId}/licenses")
public class LicenseServiceController {

    @RequestMapping(value = "/{licenceId}", method = RequestMethod.GET)
    public License getLicenses(
            @PathVariable("organizationId") String organizationId,
            @PathVariable("licenceId") String licenceId) {
        return new License()
                .withId(licenceId)
                .withProductName("TestProductName")
                .withLicenseType("TestLicenceType")
                .withOrganizationId("TestOrg");
    }
}
