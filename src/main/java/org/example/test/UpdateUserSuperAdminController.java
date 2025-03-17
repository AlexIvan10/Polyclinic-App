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

public class UpdateUserSuperAdminController implements Initializable{

    @FXML
    private TextField CNPFldUSA;

    @FXML
    private TextField IBANFldUSA;

    @FXML
    private TextField adresaFldUSA;

    @FXML
    private Button backButtonSA;

    @FXML
    private TextField emailFldUSA;

    @FXML
    private ChoiceBox<String> functieFldUSA;

    @FXML
    private Label messageLabelUSA;

    @FXML
    private TextField nrTelFldUSA;

    @FXML
    private TextField numeFldUSA;

    @FXML
    private TextField parolaFldUSA;

    @FXML
    private TextField prenumeFldUSA;

    @FXML
    private Button updateButtonSA;

    private String initialEmail;

    private String initialCNP;

    private String initialIBAN;

    private String[] functii = {"Inspector Resurse Umane", "Expert Financiar Contabil", "Receptioner", "Asistent Medical", "Medic", "Admin"};


    @FXML
    void backButton(ActionEvent event) {
        Stage stage = (Stage) backButtonSA.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void updateButton(ActionEvent e){
        if(numeFldUSA.getText().isBlank() || prenumeFldUSA.getText().isBlank() || emailFldUSA.getText().isBlank() || nrTelFldUSA.getText().isBlank() || CNPFldUSA.getText().isBlank() || IBANFldUSA.getText().isBlank() || adresaFldUSA.getText().isBlank() || functieFldUSA.getValue() == null){
            messageLabelUSA.setText("Please complete all the fields");
        }
        else{
            validateData();
        }
    }

    public void validateData(){
        Connection connection;
        DatabaseConnection connectionNow = new DatabaseConnection();
        connection = connectionNow.getConnection();
        String verifyCNP = "SELECT * FROM Utilizatori WHERE CNP = '" + CNPFldUSA.getText() + "'";
        String verifyEmail = "SELECT * FROM Utilizatori WHERE Email = '" + emailFldUSA.getText() + "'";
        String verifyIBAN = "SELECT * FROM Utilizatori WHERE Email = '" + IBANFldUSA.getText() + "'";
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
            messageLabelUSA.setText("User already exists");
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

            String updateNume = "UPDATE utilizatori SET Nume = '" + numeFldUSA.getText() + "' WHERE ID_Utilizator = " + userID;
            String updatePrenume = "UPDATE utilizatori SET Prenume = '" + prenumeFldUSA.getText() + "' WHERE ID_Utilizator = " + userID;
            String updateParola = "UPDATE utilizatori SET Parola = '" + parolaFldUSA.getText() + "' WHERE ID_Utilizator = " + userID;
            String updateCNP = "UPDATE utilizatori SET CNP = '" + CNPFldUSA.getText() + "' WHERE ID_Utilizator = " + userID;
            String updateIBAN = "UPDATE utilizatori SET ContIBAN = '" + IBANFldUSA.getText() + "' WHERE ID_Utilizator = " + userID;
            String updateAdresa = "UPDATE utilizatori SET Adresa = '" + adresaFldUSA.getText() + "' WHERE ID_Utilizator = " + userID;
            String updateFunctie = "UPDATE utilizatori SET Functie = '" + String.valueOf(functieFldUSA.getValue()) + "' WHERE ID_Utilizator = " + userID;
            String updateEmail = "UPDATE utilizatori SET Email = '" + emailFldUSA.getText() + "' WHERE ID_Utilizator = " + userID;

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
        Stage stage = (Stage) updateButtonSA.getScene().getWindow();
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
                numeFldUSA.setText(resultSet.getString("Nume"));
                prenumeFldUSA.setText(resultSet.getString("Prenume"));
                emailFldUSA.setText(resultSet.getString("Email"));
                initialEmail = resultSet.getString("Email");
                parolaFldUSA.setText(resultSet.getString("Parola"));
                CNPFldUSA.setText(resultSet.getString("CNP"));
                initialCNP = resultSet.getString("CNP");
                IBANFldUSA.setText(resultSet.getString("ContIBAN"));
                initialIBAN = resultSet.getString("ContIBAN");
                adresaFldUSA.setText(resultSet.getString("Adresa"));
                nrTelFldUSA.setText(resultSet.getString("NumarTelefon"));
                functieFldUSA.setValue(resultSet.getString("Functie"));
                functieFldUSA.getItems().addAll(functii);
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
