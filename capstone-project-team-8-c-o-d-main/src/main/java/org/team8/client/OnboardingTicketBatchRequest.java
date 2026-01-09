package org.team8.client;

import java.util.List;

public class OnboardingTicketBatchRequest {

    public String fullName;
    public String employeeId;
    public String email;
    public String phone;
    public String address;
    public List<String> equipments;
    public String assetName;
    public String issueDescription;

    public OnboardingTicketBatchRequest() {
    }

    public OnboardingTicketBatchRequest(
            String fullName,
            String employeeId,
            String email,
            String phone,
            String address,
            List<String> equipments,
            String assetName,
            String issueDescription
    ) {
        this.fullName = fullName;
        this.employeeId = employeeId;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.equipments = equipments;
        this.assetName = assetName;
        this.issueDescription = issueDescription;
    }
}
