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
import java.math.BigDecimal;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ProgramariPacientiController implements Initializable{

    @FXML
    private DatePicker dataEmiteriiFld;

    @FXML
    private DatePicker dataFld;

    @FXML
    private ChoiceBox<String> medicFld;

    @FXML
    private Button inapoiButton;

    @FXML
    private TextField oraFld;

    @FXML
    private ChoiceBox<String> pacientFld;

    @FXML
    private TextField sumaFld;

    private String[] medici = new String[100];

    @FXML
    private ChoiceBox<String> serviciuFld;

    private int nrMedici = 0;

    private int[] mediciID = new int[100];

    private String[] pacienti = new String[100];

    private int nrPacienti = 0;

    private int[] pacientiID = new int[100];

    private int receptionerPolyclinicID = 0;

    private String[] servicii = new String[100];

    private int nrServicii = 0;

    private int[] serviciiID = new int[100];


    @FXML
    public void inapoiButton(ActionEvent e){
        Stage stage = (Stage) inapoiButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void adaugaButton(ActionEvent e){
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        DatabaseConnection connectNow = new DatabaseConnection();
        connection = connectNow.getConnection();
        CallableStatement callableStatement;
        try {
            int numarMedic = 0;
            for (int i = 0; i < nrMedici; i++) {
                if(String.valueOf(medicFld.getValue()).equals(medici[i])){
                    numarMedic = mediciID[i];
                    break;
                }
            }
            int numarPacient = 0;
            for (int i = 0; i < nrPacienti; i++) {
                if(String.valueOf(pacientFld.getValue()).equals(pacienti[i])){
                    numarPacient = pacientiID[i];
                    break;
                }
            }
            int numarServiciu = 0;
            for (int i = 0; i < nrServicii; i++) {
                if(String.valueOf(serviciuFld.getValue()).equals(servicii[i])){
                    numarServiciu = serviciiID[i];
                    break;
                }
            }
            String modificaMedic = "UPDATE pacienti SET ID_Medic = " + numarMedic + " WHERE ID_Pacient = " + numarPacient;
            statement = connection.createStatement();
            statement.executeUpdate(modificaMedic);

            String insertProgramarePacienti = "{CALL InsertProgramari(?, ?, ?, ?)}";

            callableStatement = connection.prepareCall(insertProgramarePacienti);
            callableStatement.setInt(1, numarServiciu);
            callableStatement.setString(2, String.valueOf(dataFld.getValue()));
            callableStatement.setString(3, String.valueOf(oraFld.getText()));
            callableStatement.setInt(4, numarPacient);
            callableStatement.execute();

            String ultimaInserare = "SELECT COUNT(*) AS NumarProgramari FROM programarepacienti";
            resultSet = statement.executeQuery(ultimaInserare);
            int nrProgramari = 0;
            while(resultSet.next()){
                nrProgramari = resultSet.getInt("NumarProgramari");
            }

            String insertRealizate = "{CALL InsertServiciiMedicaleRealizate(?, ?, ?, ?)}";
            callableStatement = connection.prepareCall(insertRealizate);
            callableStatement.setInt(1, numarPacient);
            callableStatement.setInt(2, nrProgramari);
            BigDecimal suma = new BigDecimal(sumaFld.getText());
            callableStatement.setBigDecimal(3, suma);
            callableStatement.setString(4, String.valueOf(dataEmiteriiFld.getValue()));
            callableStatement.execute();

            String ultimulServiciuRealizat = "SELECT COUNT(*) AS Realizat FROM ServiciiMedicaleRealizate";
            resultSet = statement.executeQuery(ultimulServiciuRealizat);
            int nrRelizat = 0;
            while(resultSet.next()){
                nrRelizat = resultSet.getInt("Realizat");
            }

            String insertBonFiscal = "{CALL InsertBonuriFiscale(?, ?, ?, ?, ?)}";
            callableStatement = connection.prepareCall(insertBonFiscal);
            callableStatement.setInt(1, receptionerPolyclinicID);
            callableStatement.setInt(2, nrRelizat);
            callableStatement.setInt(3, numarPacient);
            callableStatement.setBigDecimal(4, suma);
            callableStatement.setString(5, String.valueOf(dataEmiteriiFld.getValue()));
            callableStatement.execute();

        }catch (Exception ex){
            ex.printStackTrace();
        }
        Stage stage = (Stage) inapoiButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        DatabaseConnection connectNow = new DatabaseConnection();
        connection = connectNow.getConnection();
        try {
            String superAdminPolyclinic = "SELECT ID_Policlinici FROM Utilizatori WHERE Email = '" + UserData.getEmail() + "'";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(superAdminPolyclinic);
            while(resultSet.next()){
                receptionerPolyclinicID = resultSet.getInt("ID_Policlinici");
            }
            String selecteazaMedici = "SELECT ID_Policlinici, ID_Medic, Nume, Prenume FROM Utilizatori, Angajat, Medic " +
                                      "WHERE Utilizatori.ID_Utilizator = Angajat.ID_Utilizator AND Angajat.ID_Angajat = Medic.ID_Angajat AND ID_Policlinici = " + receptionerPolyclinicID;
            resultSet = statement.executeQuery(selecteazaMedici);
            while(resultSet.next()){
                medici[nrMedici] = resultSet.getString("Nume") + " " + resultSet.getString("Prenume");
                mediciID[nrMedici] = resultSet.getInt("ID_Medic");
                nrMedici ++;
            }
            for (int i = 0; i < nrMedici; i++) {
                medicFld.getItems().add(medici[i]);
            }
            String selecteazaPacienti = "SELECT ID_Policlinici, ID_Pacient, Nume, Prenume FROM Utilizatori, Pacienti " +
                                        "WHERE Utilizatori.ID_Utilizator = Pacienti.ID_Utilizator AND ID_Policlinici = " + receptionerPolyclinicID;
            resultSet = statement.executeQuery(selecteazaPacienti);
            while(resultSet.next()){
                pacienti[nrPacienti] = resultSet.getString("Nume") + " " + resultSet.getString("Prenume");
                pacientiID[nrPacienti] = resultSet.getInt("ID_Pacient");
                nrPacienti ++;
            }
            for (int i = 0; i < nrPacienti; i++) {
                pacientFld.getItems().add(pacienti[i]);
            }
            String selecteazaServicii = "SELECT * FROM serviciimedicale";

            resultSet = statement.executeQuery(selecteazaServicii);
            while(resultSet.next()){
                servicii[nrServicii] = resultSet.getString("NumeServiciu");
                serviciiID[nrServicii] = resultSet.getInt("ID_Serviciu");
                nrServicii ++;
            }
            for (int i = 0; i < nrServicii; i++) {
                serviciuFld.getItems().add(servicii[i]);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
