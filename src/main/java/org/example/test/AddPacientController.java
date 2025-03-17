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


public class AddPacientController implements Initializable{
    @FXML
    private TextField CNPFld;

    @FXML
    private TextField IBANFld;

    @FXML
    private TextField adresaFld;

    @FXML
    private TextField emailFld;

    @FXML
    private ChoiceBox<String> medicFld;

    @FXML
    private Label messageLabelAM;

    @FXML
    private TextField nrTelFld;

    @FXML
    private TextField numeFld;

    @FXML
    private TextField parolaFld;

    @FXML
    private TextField prenumeFld;

    private String[] medici = new String[100];

    private int nrMedici = 0;

    private int[] mediciID = new int[100];


    @FXML
    protected void inapoiButtonAction(ActionEvent e){
        SelectUserTypeSuperAdminController.inapoiFunctiiButtonAction(e, AddPacientController.class);
    }

    @FXML
    protected void adaugaButtonAction(ActionEvent e) {
        if(numeFld.getText().isBlank() || prenumeFld.getText().isBlank() ||
                emailFld.getText().isBlank() || parolaFld.getText().isBlank() ||
                CNPFld.getText().isBlank() || IBANFld.getText().isBlank() ||
                adresaFld.getText().isBlank() || nrTelFld.getText().isBlank() ||
                medicFld.getValue() == null)
            messageLabelAM.setText("Completeaza toate campurile");
        else{
            String emailExist = "{CALL VerificaExistentaEmail(?, ?)}";
            String CNPExist = "{CALL VerificaExistentaCNP(?, ?)}";
            String IBANExist = "{CALL VerificaExistentaContIBAN(?, ?)}";
            Connection connection = null;
            Statement statement = null;
            ResultSet resultSet = null;
            Boolean hasResult = false;
            DatabaseConnection connectNow = new DatabaseConnection();
            connection = connectNow.getConnection();
            CallableStatement callableStatement;
            int exista = 0;
            try {
                int valoare = 0;
                callableStatement = connection.prepareCall(emailExist);
                callableStatement.setString(1, emailFld.getText());
                callableStatement.registerOutParameter(2, Types.INTEGER);
                callableStatement.execute();
                valoare = callableStatement.getInt(2);
                if (valoare == 1) {
                    messageLabelAM.setText("Email existent");
                    exista = 1;
                }
                callableStatement = connection.prepareCall(CNPExist);
                callableStatement.setString(1, CNPFld.getText());
                callableStatement.registerOutParameter(2, Types.INTEGER);
                callableStatement.execute();
                valoare = callableStatement.getInt(2);
                if (valoare == 1) {
                    messageLabelAM.setText("CNP existent");
                    exista = 1;
                }
                callableStatement = connection.prepareCall(IBANExist);
                callableStatement.setString(1, IBANFld.getText());
                callableStatement.registerOutParameter(2, Types.INTEGER);
                callableStatement.execute();
                valoare = callableStatement.getInt(2);
                if (valoare == 1) {
                    messageLabelAM.setText("IBAN existent");
                    exista = 1;
                }
                if (exista == 0) {
                    messageLabelAM.setText("");
                    int superAdminPolyclinicID = 0;
                    String superAdminPolyclinic = "SELECT ID_Policlinici FROM Utilizatori WHERE Email = '" + UserData.getEmail() + "'";
                    String insertUtilizator = "{CALL InsertUtilizator(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
                    statement = connection.createStatement();
                    resultSet = statement.executeQuery(superAdminPolyclinic);
                    while(resultSet.next()){
                        superAdminPolyclinicID = resultSet.getInt("ID_Policlinici");
                    }
                    callableStatement = connection.prepareCall(insertUtilizator);
                    callableStatement.setInt(1, superAdminPolyclinicID);
                    callableStatement.setString(2, CNPFld.getText());
                    callableStatement.setString(3, numeFld.getText());
                    callableStatement.setString(4, prenumeFld.getText());
                    callableStatement.setString(5, adresaFld.getText());
                    callableStatement.setString(6, nrTelFld.getText());
                    callableStatement.setString(7, emailFld.getText());
                    callableStatement.setString(8, parolaFld.getText());
                    callableStatement.setString(9, IBANFld.getText());
                    callableStatement.setString(10, "Pacient");
                    callableStatement.execute();
                    String getUtilizatorID = "SELECT ID_Utilizator FROM Utilizatori WHERE Email = '" + emailFld.getText() + "'";
                    int utilizatorID = 0;
                    statement = connection.createStatement();
                    resultSet = statement.executeQuery(getUtilizatorID);
                    while(resultSet.next()){
                        utilizatorID = resultSet.getInt("ID_Utilizator");
                    }
                    int medicID = 0;
                    for (int i = 0; i < nrMedici; i++) {
                        if(medici[i].equals(String.valueOf(medicFld.getValue()))){
                            medicID = mediciID[i];
                            break;
                        }
                    }
                    String insertPacient = "{CALL InsertPacient(?, ?)}";
                    callableStatement = connection.prepareCall(insertPacient);
                    callableStatement.setInt(1, utilizatorID);
                    callableStatement.setInt(2, medicID);
                    callableStatement.execute();
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
            SelectUserTypeSuperAdminController.inapoiFunctiiButtonAction(e, AddPacientController.class);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        Boolean hasResult = false;
        DatabaseConnection connectNow = new DatabaseConnection();
        connection = connectNow.getConnection();
        try {
            int superAdminPolyclinicID = 0;
            String superAdminPolyclinic = "SELECT ID_Policlinici FROM Utilizatori WHERE Email = '" + UserData.getEmail() + "'";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(superAdminPolyclinic);
            while(resultSet.next()){
                superAdminPolyclinicID = resultSet.getInt("ID_Policlinici");
            }
            String selecteazaMedici = "SELECT ID_Policlinici, ID_Medic, Nume, Prenume FROM Utilizatori, Angajat, Medic " +
                                      "WHERE Utilizatori.ID_Utilizator = Angajat.ID_Utilizator AND Angajat.ID_Angajat = Medic.ID_Angajat AND ID_Policlinici = " + superAdminPolyclinicID;
            resultSet = statement.executeQuery(selecteazaMedici);
            while(resultSet.next()){
                medici[nrMedici] = resultSet.getString("Nume") + " " + resultSet.getString("Prenume");
                mediciID[nrMedici] = resultSet.getInt("ID_Medic");
                nrMedici ++;
            }
            for (int i = 0; i < nrMedici; i++) {
                medicFld.getItems().add(medici[i]);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
