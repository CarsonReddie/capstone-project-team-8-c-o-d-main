package org.team8.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.team8.client.Session;
import org.team8.client.TicketApi;
import org.team8.client.TicketDto;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;

public class DashboardController {

    // ------------------ FX Controls ------------------
    @FXML private TableView<TicketDto> tableTickets;
    @FXML private TableColumn<TicketDto, String> colCode;
    @FXML private TableColumn<TicketDto, String> colAsset;
    @FXML private TableColumn<TicketDto, String> colStatus;
    @FXML private TableColumn<TicketDto, String> colEmployee;
    @FXML private TableColumn<TicketDto, String> colSubmitted;

    @FXML private TextField searchField;
    @FXML private Button approveButton;
    @FXML private Button viewTicketsButton;
    @FXML private Button btnRefresh;

    private final TicketApi api = new TicketApi();

    // --------------------------------------------------
    // INITIALIZE
    // --------------------------------------------------
    @FXML
    private void initialize() {

        // Table column bindings
        colCode.setCellValueFactory(new PropertyValueFactory<>("ticketCode"));
        colAsset.setCellValueFactory(new PropertyValueFactory<>("assetName"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colEmployee.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        colSubmitted.setCellValueFactory(new PropertyValueFactory<>("dateSubmitted"));


        // ----------------- ROLE CHECK -----------------
        String role = Session.role() == null ? "" : Session.role().trim().toUpperCase();
        boolean canApproveRole = role.equals("MANAGER") || role.equals("ADMIN");
        System.out.println("[Dashboard] role=" + role + " canApproveRole=" + canApproveRole);

        approveButton.setVisible(canApproveRole);
        approveButton.setManaged(canApproveRole);
        approveButton.setDisable(true);


        // ----------------- ENABLE/DISABLE APPROVE -----------------
        tableTickets.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, sel) -> {
            boolean enable = canApproveRole
                    && sel != null
                    && "SUBMITTED".equalsIgnoreCase(sel.status);
            approveButton.setDisable(!enable);

            // View button logic also depends on selection
            viewTicketsButton.setDisable(sel == null);
        });


        // ----------------- VIEW TICKET BUTTON STARTS DISABLED -----------------
        viewTicketsButton.setDisable(true);


        // Load tickets
        refreshTickets();
    }

    // --------------------------------------------------
    // REFRESH TICKETS
    // --------------------------------------------------
    @FXML
    private void onRefresh() {
        refreshTickets();
    }

    private void refreshTickets() {
        CompletableFuture
                .supplyAsync(() -> {
                    try {
                        return api.listAll();
                    } catch (Exception e) {
                        throw new CompletionException(e);
                    }
                })
                .thenAccept(list -> Platform.runLater(() -> {
                    tableTickets.getItems().setAll(list);

                    if (!list.isEmpty()) {
                        tableTickets.getSelectionModel().selectFirst();
                    }
                }))
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    return null;
                });
    }

    // --------------------------------------------------
    // SEARCH
    // --------------------------------------------------
    @FXML
    private void handleSearch() {
        String query = searchField.getText().toLowerCase();
        if (query.isBlank()) {
            refreshTickets();
            return;
        }

        List<TicketDto> filtered = tableTickets.getItems().stream()
                .filter(t -> t.getTicketCode().toLowerCase().contains(query)
                        || t.getAssetName().toLowerCase().contains(query)
                        || t.getStatus().toLowerCase().contains(query)
                        || t.getEmployeeId().toLowerCase().contains(query))
                .collect(Collectors.toList());

        tableTickets.setItems(FXCollections.observableArrayList(filtered));
    }

    // --------------------------------------------------
    // SUBMIT TICKET
    // --------------------------------------------------
    @FXML
    private void handleSubmitTicket(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/team8/capstoneprojectteam8cod2/TicketSubmission.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception ex) {
            new Alert(Alert.AlertType.ERROR, "Open form failed: " + ex.getMessage()).showAndWait();
            ex.printStackTrace();
        }
    }

    // --------------------------------------------------
    // RETIREMENT TICKET
    // --------------------------------------------------
    @FXML
    private void handleOpenRetirementTicket(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/team8/capstoneprojectteam8cod2/RetirementTicketPane.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {
            new Alert(Alert.AlertType.ERROR,
                    "Failed to open Retirement Ticket Pane:\n" + ex.getMessage()).showAndWait();
            ex.printStackTrace();
        }
    }

    // --------------------------------------------------
    // VIEW TICKET DETAILS
    // --------------------------------------------------
    @FXML
    private void handleViewTickets(ActionEvent event) {

        TicketDto selected = tableTickets.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a ticket first.").show();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/team8/capstoneprojectteam8cod2/TicketDetail.fxml"));

            Parent root = loader.load();

            TicketDetailController controller = loader.getController();
            controller.setTicket(selected);

            Stage stage = new Stage();
            stage.setTitle("Ticket Details: " + selected.ticketCode);
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Unable to open ticket details.").show();
        }
    }

    // --------------------------------------------------
    // LOGOUT
    // --------------------------------------------------
    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            Session.clear();

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/team8/capstoneprojectteam8cod2/LoginPane.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            new Alert(Alert.AlertType.ERROR,
                    "Failed to log out:\n" + ex.getMessage()).showAndWait();
            ex.printStackTrace();
        }
    }

    // --------------------------------------------------
    // APPROVE TICKET
    // --------------------------------------------------
    @FXML
    private void handleApproveTicket(ActionEvent event) {

        TicketDto selected = tableTickets.getSelectionModel().getSelectedItem();
        Alert a = new Alert(Alert.AlertType.NONE);

        if (selected == null) {
            a.setAlertType(Alert.AlertType.ERROR);
            a.setHeaderText("Ticket Failure");
            a.setContentText("Select a ticket to approve.");
            a.show();
            return;
        }

        if (!"SUBMITTED".equalsIgnoreCase(selected.status)) {
            a.setAlertType(Alert.AlertType.WARNING);
            a.setHeaderText("Cannot approve");
            a.setContentText("Only SUBMITTED tickets can be approved.");
            a.show();
            return;
        }

        approveButton.setDisable(true);

        CompletableFuture
                .supplyAsync(() -> {
                    try {
                        return api.approveTicket(selected.id);
                    } catch (Exception e) {
                        throw new CompletionException(e);
                    }
                })
                .thenAccept(updated -> Platform.runLater(() -> {

                    selected.status = updated.status;
                    tableTickets.refresh();

                    String role = Session.role() == null ? "" : Session.role().toUpperCase();
                    boolean canApproveRole = role.equals("MANAGER") || role.equals("ADMIN");
                    boolean enable = canApproveRole && "SUBMITTED".equalsIgnoreCase(selected.status);
                    approveButton.setDisable(!enable);

                    a.setAlertType(Alert.AlertType.INFORMATION);
                    a.setHeaderText("Ticket Approved");
                    a.setContentText("Ticket " + updated.ticketCode + " has been approved.");
                    a.show();
                }))
                .exceptionally(ex -> {
                    Platform.runLater(() -> {
                        approveButton.setDisable(false);
                        String msg = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();
                        a.setAlertType(Alert.AlertType.ERROR);
                        a.setHeaderText("Approve failed");
                        a.setContentText(msg);
                        a.show();
                    });
                    return null;
                });
    }

    // inside DashboardController.java (but outside any other method)
    public static boolean canRoleApprove(String role) {
        if (role == null) return false;
        return "MANAGER".equalsIgnoreCase(role) || "ADMIN".equalsIgnoreCase(role);
    }

    public static boolean canApproveTicket(String role, TicketDto ticket) {
        if (!canRoleApprove(role) || ticket == null) return false;
        return "SUBMITTED".equalsIgnoreCase(ticket.status);
    }

    public static boolean canRetireTicket(String role, TicketDto ticket) {
        if (role == null || ticket == null) return false;

        boolean allowedRole =
                "MANAGER".equalsIgnoreCase(role) ||
                        "ADMIN".equalsIgnoreCase(role);

        // Only approved tickets can be retired
        return allowedRole && "APPROVED".equalsIgnoreCase(ticket.status);
    }
}
