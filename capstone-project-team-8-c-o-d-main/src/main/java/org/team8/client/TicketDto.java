package org.team8.client;

public class TicketDto {
    public Long id;
    public String ticketCode, assetName, status, fullName, employeeId, email, phone, address, equipment, issueDescription;
    public String dateSubmitted;

    // --- JavaFX-compatible getters ---
    public Long getId() { return id; }
    public String getTicketCode() { return ticketCode; }
    public String getAssetName() { return assetName; }
    public String getStatus() { return status; }
    public String getFullName() { return fullName; }
    public String getEmployeeId() { return employeeId; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getEquipment() { return equipment; }
    public String getIssueDescription() { return issueDescription; }
    public String getDateSubmitted() { return dateSubmitted; }
}


