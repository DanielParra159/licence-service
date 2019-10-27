package com.danielparra.licenseservice.services;

import com.danielparra.licenseservice.config.ServiceConfig;
import com.danielparra.licenseservice.model.License;
import com.danielparra.licenseservice.model.Organization;
import com.danielparra.licenseservice.repository.LicenseRepository;
import com.danielparra.licenseservice.utils.UserContextHolder;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class LicenseService {
    private static final Logger logger = LoggerFactory.getLogger(LicenseService.class);
    @Autowired
    private LicenseRepository licenseRepository;

    @Autowired
    ServiceConfig config;

    @Autowired
    OrganizationDiscoveryClient organizationDiscoveryClient;

    @HystrixCommand(fallbackMethod = "buildFallbackLicense",
            threadPoolKey = "getLicenseThreadPool")
    public License getLicense(String organizationId, String licenseId) {
        waitRandomTime();
        License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);

        Organization org = retrieveOrgInfo(organizationId);

        return license
                .withOrganizationName(org.getName())
                .withContactName( org.getContactName())
                .withContactEmail( org.getContactEmail() )
                .withContactPhone( org.getContactPhone() )
                .withComment(config.getExampleProperty());
    }


    @HystrixCommand(fallbackMethod = "buildFallbackLicenseList",
            threadPoolKey = "getLicensesByOrgThreadPool")
    public List<License> getLicensesByOrg(String organizationId){
        logger.debug("LicenseService.getLicensesByOrg  Correlation id: {}", UserContextHolder.getContext().getCorrelationId());
        waitRandomTime();
        return licenseRepository.findByOrganizationId( organizationId );
    }

    public void saveLicense(License license){
        license.withId( UUID.randomUUID().toString());

        licenseRepository.save(license);

    }

    public void updateLicense(License license){
        licenseRepository.save(license);
    }

    public void deleteLicense(License license){
        licenseRepository.delete(license);
    }

    private Organization retrieveOrgInfo(String organizationId){
        Organization organization = organizationDiscoveryClient.getOrganization(organizationId);

        return organization;
    }

    private License buildFallbackLicense(String organizationId, String licenseId){
        return new License()
                .withId(licenseId)
                .withOrganizationId( organizationId )
                .withProductName("Sorry no licensing information currently available");
    }

    private List<License> buildFallbackLicenseList(String organizationId){
        List<License> fallbackList = new ArrayList<>();
        License license = new License()
                .withId("0000000-00-00000")
                .withOrganizationId( organizationId )
                .withProductName("Sorry no licensing information currently available");

        fallbackList.add(license);
        return fallbackList;
    }

    private void waitRandomTime() {
        Random rand = new Random();
        int randomNumber = rand.nextInt((3 - 1) + 1) + 1;
        if (randomNumber == 3) sleep();
    }

    private void sleep() {
        try {
            Thread.sleep(11000);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
