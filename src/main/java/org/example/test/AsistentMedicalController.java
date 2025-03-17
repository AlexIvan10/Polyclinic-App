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

public class AsistentMedicalController implements Initializable {
    @FXML
    private Label gradAsistentFld;

    @FXML
    private Label numeAsistentFld;

    @FXML
    private Label oreAsistenFld;

    @FXML
    private Label prenumeAsistentFld;

    @FXML
    private Label salariuAsistentFld;

    @FXML
    private Label tipAsistentFld;

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
                FXMLLoader fxmlLoader = new FXMLLoader(AsistentMedicalController.class.getResource("DoneazaSange.fxml"));
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
        HelloController.logOutButtonAction(e, AsistentMedicalController.class);
    }

    @FXML
    protected void disponibilitateSangeButtonAction(ActionEvent e){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(MedicController.class.getResource("SangeDisponibil.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 470);
            Stage stageGAM = new Stage();
            stageGAM.setScene(scene);
            stageGAM.show();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @FXML
    protected void gestiuneaActivitatilorAction(ActionEvent e){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(MedicController.class.getResource("GestiuneaActivitatilorAsistent.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 770, 500);
            Stage stageGAA = new Stage();
            stageGAA.setScene(scene);
            stageGAA.show();
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
        String asistentInfo = "SELECT Nume, Prenume, TipAsistent, GradAsistent, NumarOre, Salariu" +
                              " FROM utilizatori, angajat, asistentmedical" +
                              " WHERE utilizatori.ID_Utilizator = angajat.ID_Utilizator AND angajat.ID_Angajat = asistentmedical.ID_Angajat AND Email = '" + UserData.getEmail() + "'";
        try{
            statement = connection.createStatement();
            resultSet = statement.executeQuery(asistentInfo);
            while (resultSet.next()){
                numeAsistentFld.setText(resultSet.getString("Nume"));
                prenumeAsistentFld.setText(resultSet.getString("Prenume"));
                tipAsistentFld.setText(resultSet.getString("TipAsistent"));
                gradAsistentFld.setText(resultSet.getString("GradAsistent"));
                oreAsistenFld.setText(resultSet.getString("NumarOre") + " ore");
                salariuAsistentFld.setText(resultSet.getString("Salariu") + " RON");
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
