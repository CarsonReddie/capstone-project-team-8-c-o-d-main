package org.team8.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javafx.application.Platform;
import javafx.fxml.FXML; import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.concurrent.CompletableFuture;
import org.team8.client.AuthApi;
import org.team8.client.Session;

import java.io.IOException;
import java.util.concurrent.CompletionException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label errorLabel;

    private final AuthApi auth = new AuthApi();

    // Triggered when "Login" button is clicked
   @FXML
    private void handleLogin(ActionEvent event) {

        Alert a = new Alert(Alert.AlertType.NONE);

        var u = usernameField.getText().trim();
        var p = passwordField.getText();
        if (u.isEmpty() || p.isEmpty()) {
            showAlert("Login Failed", "Invalid username or password.");
            //errorLabel.setText("Enter username & password.");
            return;
        }


        loginButton.setDisable(true);
        //showAlert("Login in Progress", "Signing In...");
        a.setAlertType(Alert.AlertType.CONFIRMATION);
        a.setHeaderText("Login in Progress");
        a.setContentText("Signing In...");
        a.show();
        //errorLabel.setText("Signing in…");

        CompletableFuture
                .supplyAsync(() -> {
                    try {
                        System.out.println("[LOGIN] calling /api/auth/login for user=" + u);
                        var res = auth.login(u, p);  // <-- this is where failures usually happen
                        Session.save(res.token(), res.role(), u);
                        return res;
                    } catch (Exception e) {
                        // print full cause to console, then propagate
                        e.printStackTrace();
                        throw new CompletionException(e);
                    }
                })
                .thenAccept(res -> Platform.runLater(() -> {
                    errorLabel.setText("");
                    goToDashboard();
                }))
                .exceptionally(ex -> {
                    Platform.runLater(() -> {
                        loginButton.setDisable(false);
                        var cause = ex.getCause() != null ? ex.getCause() : ex;
                        String msg = cause.getMessage();
                        if (msg == null || msg.isBlank()) msg = cause.getClass().getSimpleName();
                        errorLabel.setText("Login failed: " + msg);
                    });
                    return null;
                });
    }

    private void goToDashboard() {

        URL url = getClass().getResource("/org/team8/capstoneprojectteam8cod2/DashboardPrototype2.fxml");
        if (url == null) {
            errorLabel.setText("FXML not found on classpath.");
            return;
        }

        try {
            // IMPORTANT: absolute classpath path, file must be in src/main/resources under same package
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/team8/capstoneprojectteam8cod2/DashboardPrototype2.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            // Show the real reason if it fails to load (bad path, wrong fx:controller, etc.)
            errorLabel.setText("Open dashboard failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Simple local authentication
    private boolean authenticate(String username, String password) {
        return username.equals("admin") && password.equals("password123");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
