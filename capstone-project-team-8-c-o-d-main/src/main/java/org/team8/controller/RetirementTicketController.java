package org.team8.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.team8.client.Session;
import org.team8.client.TicketApi;
import org.team8.client.TicketDto;

import java.io.IOException;
import java.time.LocalDate;

/**
 * Controller for the Retirement Ticket Pane.
 * Allows IT Technicians to submit retirement requests for assets.
 */
public class RetirementTicketController {

    @FXML private AnchorPane rootPane;
    @FXML private TextField assetIdField;
    @FXML private ComboBox<String> assetTypeComboBox;
    @FXML private TextArea reasonArea;
    @FXML private CheckBox outOfWarrantyCheck;
    @FXML private CheckBox beyondRepairCheck;
    @FXML private CheckBox lostStolenCheck;
    @FXML private DatePicker requestDatePicker;
    @FXML private Label statusLabel;

    private final TicketApi api = new TicketApi();

    @FXML
    private void initialize() {
        assetTypeComboBox.getItems().addAll("Laptop", "Desktop", "Monitor", "Peripheral");
        requestDatePicker.setValue(LocalDate.now());
    }

    @FXML
    private void handleSubmitTicket() {
        try {
            String assetId = assetIdField.getText();
            String assetType = assetTypeComboBox.getValue();
            String reason = reasonArea.getText();
            LocalDate requestDate = requestDatePicker.getValue();

            if (assetId == null || assetId.isBlank()
                    || assetType == null || reason == null || reason.isBlank()
                    || requestDate == null) {
                statusLabel.setText("⚠ Please fill out all required fields.");
                statusLabel.setTextFill(Color.RED);
                return;
            }

            boolean outOfWarranty = outOfWarrantyCheck.isSelected();
            boolean beyondRepair = beyondRepairCheck.isSelected();
            boolean lostStolen = lostStolenCheck.isSelected();

            StringBuilder conditionDetails = new StringBuilder();
            if (outOfWarranty) conditionDetails.append("Out of Warranty; ");
            if (beyondRepair) conditionDetails.append("Beyond Repair; ");
            if (lostStolen) conditionDetails.append("Lost/Stolen; ");

            // Build DTO
            TicketDto dto = new TicketDto();
            dto.ticketCode = "RET-" + System.currentTimeMillis();
            dto.assetName = assetType + " (" + assetId + ")";
            dto.status = "retired"; // mark as retired substate
            dto.employeeId = Session.role() != null ? Session.role() : "IT_Tech";
            dto.issueDescription = reason + (conditionDetails.length() > 0
                    ? " [" + conditionDetails.toString().trim() + "]" : "");
            dto.dateSubmitted = requestDate.atStartOfDay().atOffset(java.time.ZoneOffset.UTC).toString();


            // Call API
            api.createRetirement(dto);

            statusLabel.setText("✅ Ticket marked as retired successfully!");
            statusLabel.setTextFill(Color.GREEN);
            clearForm();

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("❌ Error submitting ticket: " + e.getMessage());
            statusLabel.setTextFill(Color.RED);
        }
    }

    @FXML
    private void handleClearForm() {
        clearForm();
        statusLabel.setText("");
    }

    private void clearForm() {
        assetIdField.clear();
        assetTypeComboBox.getSelectionModel().clearSelection();
        reasonArea.clear();
        outOfWarrantyCheck.setSelected(false);
        beyondRepairCheck.setSelected(false);
        lostStolenCheck.setSelected(false);
        requestDatePicker.setValue(LocalDate.now());
    }

    @FXML
    private void handleBackToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/team8/capstoneprojectteam8cod2/DashboardPrototype2.fxml"));
            Parent dashboardRoot = loader.load();
            Stage stage = (Stage) rootPane.getScene().getWindow();
            Scene scene = rootPane.getScene();
            scene.setRoot(dashboardRoot);
        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("❌ Unable to return to dashboard.");
            statusLabel.setTextFill(Color.RED);
        }
    }
}
