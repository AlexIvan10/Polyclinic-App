package org.example.test;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ResurseUmaneController implements Initializable{
    @FXML
    private Label numeInspectorFld;

    @FXML
    private Label oreInspectorFld;

    @FXML
    private Label prenumeInspectorFld;

    @FXML
    private Label salariuInspectorFld;

    @FXML
    private Label errorMessage;

    @FXML
    protected void doneazaSange(ActionEvent e){
        Connection connection = null;
        DatabaseConnection connectionNow = new DatabaseConnection();
        connection = connectionNow.getConnection();
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            String cautaDonator = "SELECT Utilizatori.ID_Utilizator FROM Utilizatori, DonatoriSange " +
                                      "WHERE Utilizatori.ID_Utilizator = DonatoriSange.ID_Utilizator AND " +
                                      "Email = '" + UserData.getEmail() + "'";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(cautaDonator);
            if(resultSet.next()){
                errorMessage.setText("Ai donat deja");
            }
            else{
                FXMLLoader fxmlLoader = new FXMLLoader(ResurseUmaneController.class.getResource("DoneazaSange.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 600, 430);
                Stage stageDS = new Stage();
                stageDS.setScene(scene);
                stageDS.show();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @FXML
    protected void logOutButtonAction(ActionEvent e){
        HelloController.logOutButtonAction(e, ResurseUmaneController.class);
    }

    @FXML
    protected void gestiuneaResurselorButtonAction(ActionEvent e){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(MedicController.class.getResource("GestiuneaResurselorUmane.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 780, 860);
            Stage stageGAR = new Stage();
            stageGAR.setScene(scene);
            stageGAR.show();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Connection connection = null;
        DatabaseConnection connectionNow = new DatabaseConnection();
        connection = connectionNow.getConnection();
        Statement statement = null;
        ResultSet resultSet = null;
        try{
            String contabilInfo = "SELECT Nume, Prenume, NumarOre, Salariu FROM Utilizatori, Angajat WHERE Utilizatori.ID_Utilizator = Angajat.ID_Utilizator AND Email = '" + UserData.getEmail() + "'";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(contabilInfo);
            while(resultSet.next()){
                numeInspectorFld.setText(resultSet.getString("Nume"));
                prenumeInspectorFld.setText(resultSet.getString("Prenume"));
                oreInspectorFld.setText(resultSet.getString("NumarOre"));
                salariuInspectorFld.setText(resultSet.getString("Salariu"));
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
    public void closeOperation(AutoCloseable operation) {
        try {
            if (operation != null) {
                operation.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
