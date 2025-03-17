package org.example.test;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class GestiuneaResurselorUmaneController implements Initializable{
    @FXML
    private Label CNPGestiuneFld;

    @FXML
    private Label IBANGestiuneFld;

    @FXML
    private Label adresaGestiuneFld;

    @FXML
    private Button backButtonG;

    @FXML
    private Label dataAngajariiGestiuneFld;

    @FXML
    private Label emailGestiuneFld;

    @FXML
    private Label errorMessage;

    @FXML
    private ChoiceBox<String> functieCautatFld;

    @FXML
    private Label functieGestiuneFld;

    @FXML
    private Label numarTelefonGestiuneFld;

    @FXML
    private TextField numeCautatFld;

    @FXML
    private Label numeGestiuneFld;

    @FXML
    private TextField prenumeCautatFld;

    @FXML
    private Label prenumeGestiuneFld;

    String[] functii = {"Inspector Resurse Umane", "Expert Financiar Contabil", "Receptioner", "Asistent Medical", "Medic", "Admin", "Super Admin", "Pacient"};

    @FXML
    public void backButton(ActionEvent e) {
        Stage stage = (Stage) backButtonG.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void searchButton(ActionEvent e){
        if(numeCautatFld.getText().isBlank() || prenumeCautatFld.getText().isBlank() || functieCautatFld.getValue().isBlank())
            errorMessage.setText("Completeaza toate campurile pentru cautare");
        else{
            Connection connection = null;
            DatabaseConnection connectionNow = new DatabaseConnection();
            connection = connectionNow.getConnection();
            Statement statement = null;
            ResultSet resultSet = null;
            int polyclinicID = 0;
            String nume = numeCautatFld.getText();
            String prenume = prenumeCautatFld.getText();
            String functie = String.valueOf(functieCautatFld.getValue());
            try {
                String getPolyclinicID = "SELECT ID_Policlinici FROM Utilizatori WHERE Email = '" + UserData.getEmail() + "'";
                statement = connection.createStatement();
                resultSet = statement.executeQuery(getPolyclinicID);
                while(resultSet.next()){
                    polyclinicID = resultSet.getInt("ID_Policlinici");
                }
                String searchUser = "SELECT * " +
                                    "FROM Utilizatori " +
                                    "WHERE Nume = '" + nume +
                                    "' AND Prenume = '" + prenume +
                                    "' AND Functie = '" + functie + "'";
                resultSet = statement.executeQuery(searchUser);
                if(resultSet.next()){
                    errorMessage.setText("");
                    numeGestiuneFld.setText(resultSet.getString("Nume"));
                    prenumeGestiuneFld.setText(resultSet.getString("Prenume"));
                    CNPGestiuneFld.setText(resultSet.getString("CNP"));
                    adresaGestiuneFld.setText(resultSet.getString("Adresa"));
                    emailGestiuneFld.setText(resultSet.getString("Email"));
                    functieGestiuneFld.setText(resultSet.getString("Functie"));
                    dataAngajariiGestiuneFld.setText(resultSet.getString("DataAngajarii"));
                    IBANGestiuneFld.setText(resultSet.getString("ContIBAN"));
                    numarTelefonGestiuneFld.setText(resultSet.getString("NumarTelefon"));
                }
                else{
                    errorMessage.setText("Utilizatorul cautat nu exista");
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        functieCautatFld.getItems().addAll(functii);
    }
}
