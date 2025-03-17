package org.example.test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

import javax.swing.plaf.nimbus.State;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class UpdateUserAdminController implements Initializable{

    @FXML
    private TextField CNPFldUA;

    @FXML
    private TextField IBANFldUA;

    @FXML
    private TextField adresaFldUA;

    @FXML
    private Button backButtonA;

    @FXML
    private TextField emailFldUA;

    @FXML
    private ChoiceBox<String> functieFldUA;

    @FXML
    private Label messageLabelUA;

    @FXML
    private TextField nrTelFldUA;

    @FXML
    private TextField numeFldUA;

    @FXML
    private TextField parolaFldUA;

    @FXML
    private TextField prenumeFldUA;

    @FXML
    private Button updateButtonA;

    private String initialEmail;

    private String initialCNP;

    private String initialIBAN;

    private String[] functii = {"Inspector Resurse Umane", "Expert Financiar Contabil", "Receptioner", "Asistent Medical", "Medic"};


    @FXML
    void backButton(ActionEvent event) {
        Stage stage = (Stage) backButtonA.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void updateButton(ActionEvent e){
        if(numeFldUA.getText().isBlank() || prenumeFldUA.getText().isBlank() || emailFldUA.getText().isBlank() || nrTelFldUA.getText().isBlank() || CNPFldUA.getText().isBlank() || IBANFldUA.getText().isBlank() || adresaFldUA.getText().isBlank() || functieFldUA.getValue() == null){
            messageLabelUA.setText("Please complete all the fields");
        }
        else{
            validateData();
        }
    }

    public void validateData(){
        Connection connection;
        DatabaseConnection connectionNow = new DatabaseConnection();
        connection = connectionNow.getConnection();
        String verifyCNP = "SELECT * FROM Utilizatori WHERE CNP = '" + CNPFldUA.getText() + "'";
        String verifyEmail = "SELECT * FROM Utilizatori WHERE Email = '" + emailFldUA.getText() + "'";
        String verifyIBAN = "SELECT * FROM Utilizatori WHERE Email = '" + IBANFldUA.getText() + "'";
        Statement statement = null;
        ResultSet resultSet = null;
        boolean exist = false;
        try{
            statement = connection.createStatement();
            resultSet = statement.executeQuery(verifyCNP);
            while (resultSet.next()){
                if(!initialCNP.equals(resultSet.getString("CNP"))){
                    exist = true;
                }
            }
            statement = connection.createStatement();
            resultSet = statement.executeQuery(verifyEmail);
            while (resultSet.next()){
                if(!initialEmail.equals(resultSet.getString("Email"))){
                    exist = true;
                }
            }
            statement = connection.createStatement();
            resultSet = statement.executeQuery(verifyIBAN);
            while (resultSet.next()){
                if(!initialIBAN.equals(resultSet.getString("ContIBAN"))){
                    exist = true;
                }
            }
        }catch (Exception ex){
        }
        finally {
            closeOperation(connection);
            closeOperation(statement);
            closeOperation(resultSet);
        }
        if(exist){
            messageLabelUA.setText("User already exists");
        }
        else{
            updateUser();
        }
    }

    public void updateUser(){
        Connection connection;
        DatabaseConnection connectionNow = new DatabaseConnection();
        connection = connectionNow.getConnection();
        Statement statement = null;
        ResultSet resultSet = null;
        String getUserID = "SELECT * FROM Utilizatori WHERE Email = '" + UserToUpdate.getEmail() + "'";
        int userID = 0;
        try{
            statement = connection.createStatement();
            resultSet = statement.executeQuery(getUserID);
            while(resultSet.next()){
                userID = resultSet.getInt("ID_Utilizator");
            }

            String updateNume = "UPDATE utilizatori SET Nume = '" + numeFldUA.getText() + "' WHERE ID_Utilizator = " + userID;
            String updatePrenume = "UPDATE utilizatori SET Prenume = '" + prenumeFldUA.getText() + "' WHERE ID_Utilizator = " + userID;
            String updateParola = "UPDATE utilizatori SET Parola = '" + parolaFldUA.getText() + "' WHERE ID_Utilizator = " + userID;
            String updateCNP = "UPDATE utilizatori SET CNP = '" + CNPFldUA.getText() + "' WHERE ID_Utilizator = " + userID;
            String updateIBAN = "UPDATE utilizatori SET ContIBAN = '" + IBANFldUA.getText() + "' WHERE ID_Utilizator = " + userID;
            String updateAdresa = "UPDATE utilizatori SET Adresa = '" + adresaFldUA.getText() + "' WHERE ID_Utilizator = " + userID;
            String updateFunctie = "UPDATE utilizatori SET Functie = '" + String.valueOf(functieFldUA.getValue()) + "' WHERE ID_Utilizator = " + userID;
            String updateEmail = "UPDATE utilizatori SET Email = '" + emailFldUA.getText() + "' WHERE ID_Utilizator = " + userID;

            statement = connection.createStatement();
            statement.executeUpdate(updateNume);
            statement.executeUpdate(updatePrenume);
            statement.executeUpdate(updateParola);
            statement.executeUpdate(updateCNP);
            statement.executeUpdate(updateIBAN);
            statement.executeUpdate(updateAdresa);
            statement.executeUpdate(updateFunctie);
            statement.executeUpdate(updateEmail);

        }catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            closeOperation(connection);
            closeOperation(statement);
            closeOperation(resultSet);
        }
        Stage stage = (Stage) updateButtonA.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String selectUser = "SELECT * FROM Utilizatori WHERE Email = '" + UserToUpdate.getEmail() + "'";
        Connection connection = null;
        DatabaseConnection connectionNow = new DatabaseConnection();
        connection = connectionNow.getConnection();
        Statement statement = null;
        ResultSet resultSet = null;
        try{
            statement = connection.createStatement();
            resultSet = statement.executeQuery(selectUser);
            while(resultSet.next()){
                numeFldUA.setText(resultSet.getString("Nume"));
                prenumeFldUA.setText(resultSet.getString("Prenume"));
                emailFldUA.setText(resultSet.getString("Email"));
                initialEmail = resultSet.getString("Email");
                parolaFldUA.setText(resultSet.getString("Parola"));
                CNPFldUA.setText(resultSet.getString("CNP"));
                initialCNP = resultSet.getString("CNP");
                IBANFldUA.setText(resultSet.getString("ContIBAN"));
                initialIBAN = resultSet.getString("ContIBAN");
                adresaFldUA.setText(resultSet.getString("Adresa"));
                nrTelFldUA.setText(resultSet.getString("NumarTelefon"));
                functieFldUA.setValue(resultSet.getString("Functie"));
                functieFldUA.getItems().addAll(functii);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            closeOperation(connection);
            closeOperation(statement);
            closeOperation(resultSet);
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
}
