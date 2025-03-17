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
import java.util.ResourceBundle;

public class AddUserSuperAdminController implements Initializable {
    @FXML
    private TextField CNPFldASA;

    @FXML
    private TextField IBANFldASA;

    @FXML
    private Button addButtonSA;

    @FXML
    private TextField adresaFldASA;

    @FXML
    private DatePicker dateFldASA;

    @FXML
    private TextField emailFldASA;

    @FXML
    private ChoiceBox<String> functieFldASA;

    @FXML
    private TextField nrTelFldASA;

    @FXML
    private TextField numeFldASA;

    @FXML
    private TextField parolaFldASA;

    @FXML
    private TextField prenumeFldASA;

    @FXML
    private Button inapoiButtonSA;

    @FXML
    private Label messageLabelASA;
    private String[] functii = {"Inspector Resurse Umane", "Expert Financiar Contabil", "Receptioner", "Asistent Medical", "Medic", "Admin"};

    @FXML
    public void inapoiButton(ActionEvent e) {
        Stage stage = (Stage) inapoiButtonSA.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void addButton(ActionEvent e){
        if(numeFldASA.getText().isBlank() || prenumeFldASA.getText().isBlank() || emailFldASA.getText().isBlank() || nrTelFldASA.getText().isBlank() || CNPFldASA.getText().isBlank() || IBANFldASA.getText().isBlank() || adresaFldASA.getText().isBlank() || functieFldASA.getValue() == null || dateFldASA.getValue() == null){
            messageLabelASA.setText("Please complete all the fields");
        }
        else{
            validateData();
        }
    }

    public void validateData(){
        Connection connection;
        DatabaseConnection connectionNow = new DatabaseConnection();
        connection = connectionNow.getConnection();
        String verifyCNP = "SELECT * FROM Utilizatori WHERE CNP = '" + CNPFldASA.getText() + "'";
        String verifyEmail = "SELECT * FROM Utilizatori WHERE Email = '" + emailFldASA.getText() + "'";
        String verifyIBAN = "SELECT * FROM Utilizatori WHERE Email = '" + IBANFldASA.getText() + "'";
        Statement statement = null;
        ResultSet resultSet = null;
        boolean exist = false;
        try{
            statement = connection.createStatement();
            resultSet = statement.executeQuery(verifyCNP);
            while (resultSet.next()){
                exist = true;
            }
            statement = connection.createStatement();
            resultSet = statement.executeQuery(verifyEmail);
            while (resultSet.next()){
                exist = true;
            }
            statement = connection.createStatement();
            resultSet = statement.executeQuery(verifyIBAN);
            while (resultSet.next()){
                exist = true;
            }
        }catch (Exception ex){
        }
        finally {
            closeOperation(connection);
            closeOperation(statement);
            closeOperation(resultSet);
        }
        if(exist){
            messageLabelASA.setText("Utilizatorul exista deja");
        }
        else{
            addUser();
        }
    }

    public void addUser(){
        Connection connection;
        DatabaseConnection connectionNow = new DatabaseConnection();
        connection = connectionNow.getConnection();
        int superAdminPolyclinicID = 0;
        String superAdminPolyclinic = "SELECT ID_Policlinici FROM Utilizatori WHERE Email = '" + UserData.getEmail() + "'";
        String userToInsert = "INSERT INTO Utilizatori (ID_Policlinici, CNP, nume, prenume, adresa, NumarTelefon, Email, Parola, ContIBAN, DataAngajarii, Functie) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = null;
        Statement statement = null;
        ResultSet resultSetStatement = null;
        try{
            statement = connection.createStatement();
            resultSetStatement = statement.executeQuery(superAdminPolyclinic);
            while(resultSetStatement.next()){
                superAdminPolyclinicID = resultSetStatement.getInt("ID_Policlinici");
            }
            preparedStatement = connection.prepareStatement(userToInsert);
            preparedStatement.setInt(1, superAdminPolyclinicID);
            preparedStatement.setString(2, CNPFldASA.getText());
            preparedStatement.setString(3, numeFldASA.getText());
            preparedStatement.setString(4, prenumeFldASA.getText());
            preparedStatement.setString(5, adresaFldASA.getText());
            preparedStatement.setString(6, nrTelFldASA.getText());
            preparedStatement.setString(7, emailFldASA.getText());
            preparedStatement.setString(8, parolaFldASA.getText());
            preparedStatement.setString(9, IBANFldASA.getText());
            preparedStatement.setString(10, String.valueOf(dateFldASA.getValue()));
            preparedStatement.setString(11, String.valueOf(functieFldASA.getValue()));
            preparedStatement.execute();

        }catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            closeOperation(connection);
            closeOperation(preparedStatement);
            closeOperation(statement);
            closeOperation(resultSetStatement);
        }
        Stage stage = (Stage) addButtonSA.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        functieFldASA.getItems().addAll(functii);
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
