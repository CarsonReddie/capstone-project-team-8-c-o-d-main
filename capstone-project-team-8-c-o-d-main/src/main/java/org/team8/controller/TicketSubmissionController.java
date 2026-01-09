package org.team8.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.team8.client.OnboardingTicketBatchRequest;
import org.team8.client.TicketApi;
import org.team8.client.TicketDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TicketSubmissionController {

    // --- Form fields (make sure fx:id in FXML match these names) ---
    @FXML private TextField fullNameField;
    @FXML private TextField employeeIdField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField addressField;
    @FXML private TextField assetNameField;
    @FXML private TextArea issueDescriptionArea;

    // --- Equipment checkboxes (edit to match your exact FXML) ---
    @FXML private CheckBox monitorCheck;
    @FXML private CheckBox laptopCheck;
    @FXML private CheckBox tabletCheck;
    @FXML private CheckBox mouseCheck;
    @FXML private CheckBox keyboardCheck;
    @FXML private CheckBox dockingStationCheck;
    @FXML private CheckBox webcamCheck;

    @FXML private Button submitButton;

    public TicketDto buildOnboardingDto(
            String fullName,
            String employeeId,
            String email,
            String phone,
            String address,
            String equipment,
            String assetName,
            String description) {

        TicketDto dto = new TicketDto();
        dto.fullName = fullName;
        dto.employeeId = employeeId;
        dto.email = email;
        dto.phone = phone;
        dto.address = address;
        dto.equipment = equipment;
        dto.assetName = assetName;
        dto.issueDescription = description;
        dto.status = "SUBMITTED";
        return dto;
    }

    private final TicketApi ticketApi = new TicketApi();

    // ---------------------------------------------------------------------
    // Handle "Submit Ticket" button (onAction="#handleSubmit")
    // ---------------------------------------------------------------------
    @FXML
    private void handleSubmit(ActionEvent event) {
        try {
            // 1) Collect selected equipment
            List<String> equipments = new ArrayList<>();
            if (monitorCheck != null && monitorCheck.isSelected())        equipments.add("Monitor");
            if (laptopCheck != null && laptopCheck.isSelected())         equipments.add("Laptop");
            if (tabletCheck != null && tabletCheck.isSelected())         equipments.add("Tablet");
            if (mouseCheck != null && mouseCheck.isSelected())           equipments.add("Mouse");
            if (keyboardCheck != null && keyboardCheck.isSelected())     equipments.add("Keyboard");
            if (dockingStationCheck != null && dockingStationCheck.isSelected())
                equipments.add("Docking Station");
            if (webcamCheck != null && webcamCheck.isSelected())         equipments.add("Webcam");

            if (equipments.isEmpty()) {
                showError("Please select at least one piece of equipment.");
                return;
            }

            // 2) Get text fields
            String fullName = textOrEmpty(fullNameField);
            String empId    = textOrEmpty(employeeIdField);
            String email    = textOrEmpty(emailField);
            String phone    = textOrEmpty(phoneField);
            String address  = textOrEmpty(addressField);
            String asset    = textOrEmpty(assetNameField);
            String desc     = issueDescriptionArea != null ? issueDescriptionArea.getText().trim() : "";

            // minimal validation
            if (fullName.isBlank() || empId.isBlank() || email.isBlank()) {
                showError("Full Name, Employee ID, and Email are required.");
                return;
            }

            // 3) Build request payload
            OnboardingTicketBatchRequest payload =
                    new OnboardingTicketBatchRequest(
                            fullName,
                            empId,
                            email,
                            phone,
                            address,
                            equipments,
                            asset,
                            desc
                    );

            // 4) Call API
            List<TicketDto> createdTickets = ticketApi.createOnboardingBatch(payload);

            showInfo("Created " + createdTickets.size() + " ticket(s) successfully.");
            clearForm();

        } catch (Exception ex) {
            ex.printStackTrace();
            showError("Failed to submit equipment request: " + ex.getMessage());
        }
    }

    // ---------------------------------------------------------------------
    // Handle "Back to Dashboard" button (onAction="#handleBack")
    // ---------------------------------------------------------------------
    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/team8/capstoneprojectteam8cod2/DashboardPrototype2.fxml"));
            Parent dashboardView = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(dashboardView);
            stage.setScene(scene);
            stage.setTitle("Dashboard");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Unable to return to dashboard: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------------
    private String textOrEmpty(TextField field) {
        return field != null && field.getText() != null ? field.getText().trim() : "";
    }

    private void clearForm() {
        if (fullNameField != null) fullNameField.clear();
        if (employeeIdField != null) employeeIdField.clear();
        if (emailField != null) emailField.clear();
        if (phoneField != null) phoneField.clear();
        if (addressField != null) addressField.clear();
        if (assetNameField != null) assetNameField.clear();
        if (issueDescriptionArea != null) issueDescriptionArea.clear();

        if (monitorCheck != null) monitorCheck.setSelected(false);
        if (laptopCheck != null) laptopCheck.setSelected(false);
        if (tabletCheck != null) tabletCheck.setSelected(false);
        if (mouseCheck != null) mouseCheck.setSelected(false);
        if (keyboardCheck != null) keyboardCheck.setSelected(false);
        if (dockingStationCheck != null) dockingStationCheck.setSelected(false);
        if (webcamCheck != null) webcamCheck.setSelected(false);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
