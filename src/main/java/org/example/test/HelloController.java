package org.example.test;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class HelloController implements Initializable{
    @FXML
    private Label logInMessageLabel;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    private SoundManager soundManager;

    @FXML
    protected void logInButtonOnAction(ActionEvent e) {
        if(!emailField.getText().isBlank() && !passwordField.getText().isBlank()){
            validateLogIn(e);
        }
        else{
            logInMessageLabel.setText("Please enter email and password");
        }
    }

    @FXML
    protected void signUpButtonAction(ActionEvent e) {
        try {
            Parent root;
            FXMLLoader loader;
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            loader = new FXMLLoader(HelloController.class.getResource("CreateAccount.fxml"));
            root = loader.load();
            stage.setScene(new Scene(root, 870, 820));
            stage.show();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void validateLogIn(ActionEvent e){
        Connection connection = null;
        Statement selectStatement = null;
        ResultSet resultSet = null;
        Boolean hasResult = false;
        DatabaseConnection connectNow = new DatabaseConnection();
        connection = connectNow.getConnection();
        CallableStatement callableStatement;

        String enteredPassword = passwordField.getText();
        String verifyLogin = "{call Autentificare(?, ?)}";

        try{
            callableStatement = connection.prepareCall(verifyLogin);
            callableStatement.setString(1, emailField.getText());
            callableStatement.setString(2, passwordField.getText());
            hasResult = callableStatement.execute();

            if(hasResult){
                resultSet = callableStatement.getResultSet();
                while (resultSet.next()){
                    String databasePassword = resultSet.getString("Parola");
                    if(enteredPassword.equals(databasePassword)){
                        String functie = resultSet.getString("Functie");
                        Parent root;
                        FXMLLoader loader;
                        Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();

                        UserData.setEmail(emailField.getText());
                        UserData.setPassword(passwordField.getText());

                        if(functie.equals("Medic")){
                            loader = new FXMLLoader(HelloController.class.getResource("Medic.fxml"));
                            root = loader.load();
                        }
                        else if(functie.equals("Asistent Medical")){
                            loader = new FXMLLoader(HelloController.class.getResource("AsistentMedical.fxml"));
                            root = loader.load();
                        }
                        else if(functie.equals("Receptioner")){
                            loader = new FXMLLoader(HelloController.class.getResource("Receptioner.fxml"));
                            root = loader.load();
                        }
                        else if(functie.equals("Expert Financiar Contabil")){
                            loader = new FXMLLoader(HelloController.class.getResource("ExpertFinanciarContabil.fxml"));
                            root = loader.load();
                        }
                        else if(functie.equals("Inspector Resurse Umane")){
                            loader = new FXMLLoader(HelloController.class.getResource("ResurseUmane.fxml"));
                            root = loader.load();
                        }
                        else if(functie.equals("Admin")){
                            loader = new FXMLLoader(HelloController.class.getResource("Admin.fxml"));
                            root = loader.load();
                        }
                        else if(functie.equals("Super Admin")){
                            loader = new FXMLLoader(HelloController.class.getResource("SuperAdmin.fxml"));
                            root = loader.load();
                        }
                        else {
                            loader = new FXMLLoader(HelloController.class.getResource("Pacient.fxml"));
                            root = loader.load();
                        }
                        if(functie.equals("Medic")){
                            SoundManager.stopSound();
                            stage.setScene(new Scene(root, 1000, 744));
                            stage.show();
                        }
                        else{
                            SoundManager.stopSound();
                            stage.setScene(new Scene(root, 1000, 700));
                            stage.show();
                        }
                    }
                    else{
                        logInMessageLabel.setText("Invalid email or password");
                    }
                }

            }
            else{
                logInMessageLabel.setText("Invalid email or password");
            }
        }catch (Exception sqlex){
            System.err.println("An SQL Exception occured. Details are provided below:");
            sqlex.printStackTrace(System.err);
        }
        finally {
            closeOperation(connection);
            closeOperation(selectStatement);
            closeOperation(resultSet);
        }
    }

    public static void logOutButtonAction(ActionEvent e, Class<?> controllerClass) {
        try {
            Parent root;
            FXMLLoader loader = new FXMLLoader(controllerClass.getResource("hello-view.fxml"));
            root = loader.load();
            HelloController helloController = loader.getController();
            Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
            stage.setTitle("Policlinica");
            stage.setScene(new Scene(root, 1000, 700));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void closeOperation(AutoCloseable operation){
        try {
            if (operation != null) {
                operation.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SoundManager.playElevatorSound();
    }
}