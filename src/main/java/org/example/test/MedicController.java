package org.example.test;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class MedicController implements Initializable {
    @FXML
    private Label codParafaMedicFld;

    @FXML
    private Label gradMedicFld;

    @FXML
    private Label numeMedicFld;

    @FXML
    private Label oreLucruMedicFld;

    @FXML
    private Label postDidacticMedicFld;

    @FXML
    private Label prenumeMedicFld;

    @FXML
    private Label procentMedicFld;

    @FXML
    private Label salariuMedicFld;

    @FXML
    private Label specialitateMedicFld;

    @FXML
    private Label titluStiintificMedicFld;

    @FXML
    private Label competenteFld;

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
                FXMLLoader fxmlLoader = new FXMLLoader(MedicController.class.getResource("DoneazaSange.fxml"));
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
    protected void gestiuneDonatori(ActionEvent e){
        errorMessage.setText("");
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(MedicController.class.getResource("GestioneazaDonatori.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 860, 510);
            Stage stageGAM = new Stage();
            stageGAM.setScene(scene);
            stageGAM.show();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @FXML
    protected void logOutButtonAction(ActionEvent e){
        HelloController.logOutButtonAction(e, MedicController.class);
    }

    @FXML
    protected void gestiuneaResurselorButtonAction(ActionEvent e){
        errorMessage.setText("");
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(MedicController.class.getResource("GestiuneaActivitatilorMedic.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1100, 500);
            Stage stageGAM = new Stage();
            stageGAM.setScene(scene);
            stageGAM.show();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @FXML
    protected void disponibilitateSangeButtonAction(ActionEvent e){
        errorMessage.setText("");
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Connection connection = null;
        DatabaseConnection connectionNow = new DatabaseConnection();
        connection = connectionNow.getConnection();
        Statement statement = null;
        Statement statement2 = null;
        ResultSet resultSet = null;
        ResultSet resultSet2 = null;
        String medicInfo = "SELECT medic.ID_Medic, Nume, Prenume, SpecialitateMedic, GradMedic, TitluStiintific, PostDidactic, NumarOre, Salariu, ProcentServiciiMedicale, CodParafa " +
                           "FROM utilizatori, angajat, medic " +
                           "WHERE utilizatori.ID_Utilizator = angajat.ID_Utilizator AND angajat.ID_Angajat = medic.ID_Angajat AND Email = '" + UserData.getEmail() + "'";
        try{
            statement = connection.createStatement();
            resultSet = statement.executeQuery(medicInfo);
            int medicID = 0;
            while(resultSet.next()){
                String competentaAcreditata = "";
                medicID = resultSet.getInt("ID_Medic");
                String competente = "SELECT CompetenteAcreditare FROM acreditarispeciale WHERE ID_Medic = " + medicID;
                statement2 = connection.createStatement();
                resultSet2 = statement2.executeQuery(competente);
                while (resultSet2.next()){
                    competentaAcreditata = resultSet2.getString("CompetenteAcreditare");
                }
                if(competentaAcreditata.isBlank()){
                    competenteFld.setText("-");
                }
                else{
                    competenteFld.setText(competentaAcreditata);
                }
                numeMedicFld.setText(resultSet.getString("Nume"));
                prenumeMedicFld.setText(resultSet.getString("Prenume"));
                specialitateMedicFld.setText(resultSet.getString("SpecialitateMedic"));
                gradMedicFld.setText(resultSet.getString("GradMedic"));
                titluStiintificMedicFld.setText(resultSet.getString("TitluStiintific"));
                postDidacticMedicFld.setText(resultSet.getString("PostDidactic"));
                oreLucruMedicFld.setText(resultSet.getString("NumarOre") + " ore");
                salariuMedicFld.setText(resultSet.getString("Salariu") + " RON");
                procentMedicFld.setText(resultSet.getString("ProcentServiciiMedicale") + "%");
                codParafaMedicFld.setText(resultSet.getString("CodParafa"));
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
