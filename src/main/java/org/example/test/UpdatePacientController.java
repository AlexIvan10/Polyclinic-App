package org.example.test;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.sql.Date;
import java.time.LocalDate;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class UpdatePacientController implements Initializable {
    @FXML
    private TextField CNPFld;

    @FXML
    private TextField IBANFld;


    @FXML
    private TextField adresaFld;

    @FXML
    private TextField emailFld;

    @FXML
    private Button inapoiButton;

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

    private String initialEmail;

    private String initialCNP;

    private String initialIBAN;

    private int utilizatorID = 0;

    private String[] medici = new String[100];

    private int nrMedici = 0;

    private int[] mediciID = new int[100];

    @FXML
    void inapoiButton(ActionEvent e) {
        Stage stage = (Stage) inapoiButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void modificaButton(ActionEvent e) {
        if (numeFld.getText().isBlank() || prenumeFld.getText().isBlank() ||
                emailFld.getText().isBlank() || parolaFld.getText().isBlank() ||
                CNPFld.getText().isBlank() || IBANFld.getText().isBlank() ||
                adresaFld.getText().isBlank() || nrTelFld.getText().isBlank() ||
                medicFld.getValue() == null)
            messageLabelAM.setText("Completeaza toate campurile");
        else {
            String emailExist = "{CALL VerificaExistentaEmail(?, ?)}";
            String CNPExist = "{CALL VerificaExistentaCNP(?, ?)}";
            String IBANExist = "{CALL VerificaExistentaContIBAN(?, ?)}";
            Connection connection = null;
            Statement statement = null;
            PreparedStatement preparedStatement;
            ResultSet resultSet = null;
            DatabaseConnection connectNow = new DatabaseConnection();
            connection = connectNow.getConnection();
            CallableStatement callableStatement;
            int exista = 0;
            try {
                int valoare = 0;
                if (!emailFld.getText().equals(initialEmail)) {
                    callableStatement = connection.prepareCall(emailExist);
                    callableStatement.setString(1, emailFld.getText());
                    callableStatement.registerOutParameter(2, Types.INTEGER);
                    callableStatement.execute();
                    valoare = callableStatement.getInt(2);
                    if (valoare == 1) {
                        messageLabelAM.setText("Email existent");
                        exista = 1;
                    }
                }
                if (!CNPFld.getText().equals(initialCNP)) {
                    callableStatement = connection.prepareCall(CNPExist);
                    callableStatement.setString(1, CNPFld.getText());
                    callableStatement.registerOutParameter(2, Types.INTEGER);
                    callableStatement.execute();
                    valoare = callableStatement.getInt(2);
                    if (valoare == 1) {
                        messageLabelAM.setText("CNP existent");
                        exista = 1;
                    }
                }
                if (!IBANFld.getText().equals(initialIBAN)) {
                    callableStatement = connection.prepareCall(IBANExist);
                    callableStatement.setString(1, IBANFld.getText());
                    callableStatement.registerOutParameter(2, Types.INTEGER);
                    callableStatement.execute();
                    valoare = callableStatement.getInt(2);
                    if (valoare == 1) {
                        messageLabelAM.setText("IBAN existent");
                        exista = 1;
                    }
                }
                if (exista == 0) {
                    String updateUtilizator = "{call UpdateUtilizator(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
                    callableStatement = connection.prepareCall(updateUtilizator);
                    callableStatement.setInt(1, utilizatorID);
                    callableStatement.setString(2, CNPFld.getText());
                    callableStatement.setString(3, numeFld.getText());
                    callableStatement.setString(4, prenumeFld.getText());
                    callableStatement.setString(5, adresaFld.getText());
                    callableStatement.setString(6, nrTelFld.getText());
                    callableStatement.setString(7, emailFld.getText());
                    callableStatement.setString(8, parolaFld.getText());
                    callableStatement.setString(9, IBANFld.getText());
                    callableStatement.execute();

                    String getUtilizatorID = "SELECT ID_Utilizator FROM Utilizatori WHERE Email = '" + initialEmail + "'";
                    int utilizatorID = 0;
                    statement = connection.createStatement();
                    resultSet = statement.executeQuery(getUtilizatorID);
                    while (resultSet.next()) {
                        utilizatorID = resultSet.getInt("ID_Utilizator");
                    }
                    int medicID = 0;
                    for (int i = 0; i < nrMedici; i++) {
                        if (medici[i].equals(String.valueOf(medicFld.getValue()))) {
                            medicID = mediciID[i];
                            break;
                        }
                    }
                    String updatePacienti = "{call UpdatePacient(?, ?)}";
                    callableStatement = connection.prepareCall(updatePacienti);
                    callableStatement.setInt(1, utilizatorID);
                    callableStatement.setInt(2, medicID);
                    callableStatement.execute();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            Stage stage = (Stage) inapoiButton.getScene().getWindow();
            stage.close();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String selectUser = "SELECT * FROM Utilizatori WHERE Email = '" + UserToUpdate.getEmail() + "'";
        Connection connection = null;
        DatabaseConnection connectionNow = new DatabaseConnection();
        connection = connectionNow.getConnection();
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(selectUser);
            while (resultSet.next()) {
                utilizatorID = resultSet.getInt("ID_Utilizator");
                numeFld.setText(resultSet.getString("Nume"));
                prenumeFld.setText(resultSet.getString("Prenume"));
                emailFld.setText(resultSet.getString("Email"));
                initialEmail = resultSet.getString("Email");
                UserToUpdate.setEmail(initialEmail);
                parolaFld.setText(resultSet.getString("Parola"));
                CNPFld.setText(resultSet.getString("CNP"));
                initialCNP = resultSet.getString("CNP");
                IBANFld.setText(resultSet.getString("ContIBAN"));
                initialIBAN = resultSet.getString("ContIBAN");
                adresaFld.setText(resultSet.getString("Adresa"));
                nrTelFld.setText(resultSet.getString("NumarTelefon"));
            }
            int medicID = 0;
            String pacientInfo = "SELECT * FROM Pacienti WHERE ID_Utilizator = " + utilizatorID;
            resultSet = statement.executeQuery(pacientInfo);
            while (resultSet.next()){
                medicID = resultSet.getInt("ID_Medic");
            }
            String medicInfo = "SELECT Nume, Prenume FROM Utilizatori, angajat, Medic WHERE utilizatori.id_utilizator = angajat.ID_Utilizator AND angajat.ID_Angajat = medic.ID_Angajat AND ID_Medic = " + medicID;
            resultSet = statement.executeQuery(medicInfo);
            String numeDoctor = "";
            while (resultSet.next()){
                numeDoctor = resultSet.getString("Nume") + " " + resultSet.getString("Prenume");
                medicFld.setValue(numeDoctor);
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
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
