package org.team8.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.team8.client.TicketDto;

public class TicketDetailController {

    @FXML private Label lblCode;
    @FXML private Label lblAsset;
    @FXML private Label lblStatus;
    @FXML private Label lblEmployee;
    @FXML private Label lblDate;
    @FXML private TextArea txtDescription;

    private TicketDto selected;

    // Called by DashboardController
    public void setTicket(TicketDto ticket) {
        this.selected = ticket;

        lblCode.setText(ticket.ticketCode);
        lblAsset.setText(ticket.assetName);
        lblStatus.setText(ticket.status);
        lblEmployee.setText(ticket.employeeId);
        lblDate.setText(ticket.dateSubmitted);
        txtDescription.setText(ticket.issueDescription);
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) lblCode.getScene().getWindow();
        stage.close();
    }
}
