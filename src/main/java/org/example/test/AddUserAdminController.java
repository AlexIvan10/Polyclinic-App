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

public class AddUserAdminController implements Initializable {
    @FXML
    private TextField CNPFldAA;

    @FXML
    private TextField IBANFldAA;

    @FXML
    private Button addButtonA;

    @FXML
    private TextField adresaFldAA;

    @FXML
    private DatePicker dateFldAA;

    @FXML
    private TextField emailFldAA;

    @FXML
    private ChoiceBox<String> functieFldAA;

    @FXML
    private TextField nrTelFldAA;

    @FXML
    private TextField numeFldAA;

    @FXML
    private TextField parolaFldAA;

    @FXML
    private TextField prenumeFldAA;

    @FXML
    private Button inapoiButtonA;

    @FXML
    private Label messageLabelAA;

    private String[] functii = {"Inspector Resurse Umane", "Expert Financiar Contabil", "Receptioner", "Asistent Medical", "Medic"};

    @FXML
    public void inapoiButton(ActionEvent e) {
        Stage stage = (Stage) inapoiButtonA.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void addButton(ActionEvent e){
        if(numeFldAA.getText().isBlank() || prenumeFldAA.getText().isBlank() || emailFldAA.getText().isBlank() || nrTelFldAA.getText().isBlank() || CNPFldAA.getText().isBlank() || IBANFldAA.getText().isBlank() || adresaFldAA.getText().isBlank() || functieFldAA.getValue() == null || dateFldAA.getValue() == null){
            messageLabelAA.setText("Please complete all the fields");
        }
        else{
            validateData();
        }
    }

    public void validateData(){
        Connection connection;
        DatabaseConnection connectionNow = new DatabaseConnection();
        connection = connectionNow.getConnection();
        String verifyCNP = "SELECT * FROM Utilizatori WHERE CNP = '" + CNPFldAA.getText() + "'";
        String verifyEmail = "SELECT * FROM Utilizatori WHERE Email = '" + emailFldAA.getText() + "'";
        String verifyIBAN = "SELECT * FROM Utilizatori WHERE Email = '" + IBANFldAA.getText() + "'";
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
            messageLabelAA.setText("Utilizatorul exista deja");
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
            preparedStatement.setString(2, CNPFldAA.getText());
            preparedStatement.setString(3, numeFldAA.getText());
            preparedStatement.setString(4, prenumeFldAA.getText());
            preparedStatement.setString(5, adresaFldAA.getText());
            preparedStatement.setString(6, nrTelFldAA.getText());
            preparedStatement.setString(7, emailFldAA.getText());
            preparedStatement.setString(8, parolaFldAA.getText());
            preparedStatement.setString(9, IBANFldAA.getText());
            preparedStatement.setString(10, String.valueOf(dateFldAA.getValue()));
            preparedStatement.setString(11, String.valueOf(functieFldAA.getValue()));
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
        Stage stage = (Stage) addButtonA.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        functieFldAA.getItems().addAll(functii);
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
