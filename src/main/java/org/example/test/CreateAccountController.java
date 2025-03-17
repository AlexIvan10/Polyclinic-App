package org.example.test;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.Node;


import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class CreateAccountController implements Initializable{

    @FXML
    private TextField CNPFld;

    @FXML
    private TextField adresaFld;

    @FXML
    private TextField emailFld;

    @FXML
    private Label errorMessage;

    @FXML
    private TextField nrTelFld;

    @FXML
    private TextField numeFld;

    @FXML
    private PasswordField parolaFld;

    @FXML
    private ChoiceBox<String> policlinicaFld;

    @FXML
    private TextField prenumeFld;

    @FXML
    protected void logOutButtonAction(ActionEvent e){
        HelloController.logOutButtonAction(e, AsistentMedicalController.class);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String findPolyclinics = "SELECT NumePoliclinica FROM policlinici";
        Connection connection = null;
        DatabaseConnection connectionNow = new DatabaseConnection();
        connection = connectionNow.getConnection();
        Statement statement = null;
        ResultSet resultSet = null;
        String[] policlinici = new String[3];
        int nrPoliclinici = 0;
        try{
            statement = connection.createStatement();
            resultSet = statement.executeQuery(findPolyclinics);
            while (resultSet.next()){
                policlinici[nrPoliclinici] = resultSet.getString("NumePoliclinica");
                nrPoliclinici ++;
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        policlinicaFld.getItems().addAll(policlinici);
    }

    @FXML
    void createUser(ActionEvent e){
        if(numeFld.getText().isBlank() || prenumeFld.getText().isBlank() || emailFld.getText().isBlank() || parolaFld.getText().isBlank() || adresaFld.getText().isBlank() || nrTelFld.getText().isBlank() || CNPFld.getText().isBlank() || policlinicaFld.getValue() == null)
            errorMessage.setText("Completeaza toate campurile");
        else if(nrTelFld.getText().length() != 10){
            errorMessage.setText("Numarul de telefon este incorect");
        }
        else if(CNPFld.getText().length() != 13){
            errorMessage.setText("CNP este incorect");
        }
        else{
            Connection connection;
            DatabaseConnection connectionNow = new DatabaseConnection();
            connection = connectionNow.getConnection();
            PreparedStatement preparedStatement = null;
            PreparedStatement preparedStatement2 = null;
            Statement statement = null;
            ResultSet resultSet = null;
            String getPolyclinicID = "SELECT ID_Policlinici FROM Policlinici WHERE NumePoliclinica = '" + policlinicaFld.getValue() + "'";
            String getLastID = "SELECT ID_Utilizator FROM Utilizatori";
            String getLastPacientID = "SELECT ID_Pacient FROM Pacienti";
            String userToCreate = "INSERT INTO Utilizatori (ID_Utilizator, ID_Policlinici, CNP, nume, prenume, adresa, NumarTelefon, Email, Parola, Functie) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            String pacientToCreate = "INSERT INTO Pacienti (ID_Pacient, ID_Utilizator, ID_Medic) VALUES (?, ?, ?)";
            String verifyEmail = "SELECT * FROM Utilizatori WHERE Email = '" + emailFld.getText() + "'";
            String verifyCNP = "SELECT * FROM Utilizatori WHERE CNP = '" + CNPFld.getText() + "'";
            String findMedic = "SELECT ID_Medic FROM Medic";
            int polyclinicID = 0;
            int lastID = 0;
            int exist = 0;
            int medicID = 0;
            int lastPacientID = 0;
            try {
                statement = connection.createStatement();
                resultSet = statement.executeQuery(verifyEmail);
                while(resultSet.next()){
                    exist = 1;
                    errorMessage.setText("Emailul exista deja");
                }
                resultSet = statement.executeQuery(verifyCNP);
                while(resultSet.next()){
                    exist = 1;
                    errorMessage.setText("CNPul exista deja");
                }
                if(exist == 0){
                    resultSet = statement.executeQuery(getPolyclinicID);
                    while (resultSet.next()){
                        polyclinicID = resultSet.getInt("ID_Policlinici");
                    }
                    resultSet = statement.executeQuery(getLastID);
                    while (resultSet.next()){
                        if(lastID < resultSet.getInt("ID_Utilizator"))
                            lastID = resultSet.getInt("ID_Utilizator");
                    }
                    resultSet = statement.executeQuery(findMedic);
                    while (resultSet.next()) {
                        if(medicID < resultSet.getInt("ID_Medic"))
                            medicID = resultSet.getInt("ID_Medic");
                    }
                    resultSet = statement.executeQuery(getLastPacientID);
                    while (resultSet.next()) {
                        if(lastPacientID < resultSet.getInt("ID_Pacient"))
                            lastPacientID = resultSet.getInt("ID_Pacient");
                    }
                    lastID ++;
                    lastPacientID ++;
                    preparedStatement = connection.prepareStatement(userToCreate);
                    preparedStatement.setInt(1, lastID);
                    preparedStatement.setInt(2, polyclinicID);
                    preparedStatement.setString(3, CNPFld.getText());
                    preparedStatement.setString(4, numeFld.getText());
                    preparedStatement.setString(5, prenumeFld.getText());
                    preparedStatement.setString(6, adresaFld.getText());
                    preparedStatement.setString(7, nrTelFld.getText());
                    preparedStatement.setString(8, emailFld.getText());
                    preparedStatement.setString(9, parolaFld.getText());
                    preparedStatement.setString(10, "Pacient");
                    preparedStatement.execute();

                    preparedStatement2 = connection.prepareStatement(pacientToCreate);
                    preparedStatement2.setInt(1, lastPacientID);
                    preparedStatement2.setInt(2, lastID);
                    preparedStatement2.setInt(3, medicID);
                    preparedStatement2.execute();

                    UserData.setEmail(emailFld.getText());
                    UserData.setPassword(parolaFld.getText());
                    Parent root;
                    FXMLLoader loader;
                    Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
                    loader = new FXMLLoader(CreateAccountController.class.getResource("Pacient.fxml"));
                    root = loader.load();
                    stage.setScene(new Scene(root, 1000, 700));
                    stage.show();
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
}
