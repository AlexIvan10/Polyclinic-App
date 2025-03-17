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
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class AddAsistentSAController implements Initializable{

    @FXML
    private TextField CNPFldMSA;

    @FXML
    private TextField IBANFldMSA;

    @FXML
    private TextField adresaFldMSA;

    @FXML
    private DatePicker dataAngajariiFldMSA;

    @FXML
    private TextField emailFldMSA;

    @FXML
    private ChoiceBox<String> gradFldMSA;

    @FXML
    private Label messageLabelAM;

    @FXML
    private TextField nrTelFldMSA;

    @FXML
    private TextField numeFldMSA;

    @FXML
    private TextField oreFldMSA;

    @FXML
    private TextField parolaFldMSA;

    @FXML
    private TextField prenumeFldMSA;

    @FXML
    private TextField salariuFldMSA;

    @FXML
    private ChoiceBox<String> tipFldMSA;

    private String[] tip = {"Generalist", "Laborator", "Radiologie"};
    private String[] grad = {"Principal", "Secundar"};

    @FXML
    protected void inapoiButtonAction(ActionEvent e){
        SelectUserTypeSuperAdminController.inapoiFunctiiButtonAction(e, AddAsistentSAController.class);
    }

    @FXML
    protected void adaugaButtonAction(ActionEvent e) {
        if(numeFldMSA.getText().isBlank() || prenumeFldMSA.getText().isBlank() ||
                emailFldMSA.getText().isBlank() || parolaFldMSA.getText().isBlank() ||
                CNPFldMSA.getText().isBlank() || IBANFldMSA.getText().isBlank() ||
                adresaFldMSA.getText().isBlank() || nrTelFldMSA.getText().isBlank() ||
                dataAngajariiFldMSA.getValue() == null ||
                salariuFldMSA.getText().isBlank() || oreFldMSA.getText().isBlank() ||
                tipFldMSA.getValue() == null || gradFldMSA.getValue() == null)
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
                callableStatement.setString(1, emailFldMSA.getText());
                callableStatement.registerOutParameter(2, Types.INTEGER);
                callableStatement.execute();
                valoare = callableStatement.getInt(2);
                if (valoare == 1) {
                    messageLabelAM.setText("Email existent");
                    exista = 1;
                }
                callableStatement = connection.prepareCall(CNPExist);
                callableStatement.setString(1, CNPFldMSA.getText());
                callableStatement.registerOutParameter(2, Types.INTEGER);
                callableStatement.execute();
                valoare = callableStatement.getInt(2);
                if (valoare == 1) {
                    messageLabelAM.setText("CNP existent");
                    exista = 1;
                }
                callableStatement = connection.prepareCall(IBANExist);
                callableStatement.setString(1, IBANFldMSA.getText());
                callableStatement.registerOutParameter(2, Types.INTEGER);
                callableStatement.execute();
                valoare = callableStatement.getInt(2);
                if(valoare == 1){
                    messageLabelAM.setText("IBAN existent");
                    exista = 1;
                }
                if(exista == 0) {
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
                    callableStatement.setString(2, CNPFldMSA.getText());
                    callableStatement.setString(3, numeFldMSA.getText());
                    callableStatement.setString(4, prenumeFldMSA.getText());
                    callableStatement.setString(5, adresaFldMSA.getText());
                    callableStatement.setString(6, nrTelFldMSA.getText());
                    callableStatement.setString(7, emailFldMSA.getText());
                    callableStatement.setString(8, parolaFldMSA.getText());
                    callableStatement.setString(9, IBANFldMSA.getText());
                    callableStatement.setString(10, "Asistent Medical");
                    callableStatement.execute();
                    String getUtilizatorID = "SELECT ID_Utilizator FROM Utilizatori WHERE Email = '" + emailFldMSA.getText() + "'";
                    int utilizatorID = 0;
                    statement = connection.createStatement();
                    resultSet = statement.executeQuery(getUtilizatorID);
                    while(resultSet.next()){
                        utilizatorID = resultSet.getInt("ID_Utilizator");
                    }
                    String insertAngajat = "{CALL InsertAngajat(?, ?, ?, ?, ?)}";
                    callableStatement = connection.prepareCall(insertAngajat);
                    callableStatement.setInt(1, utilizatorID);
                    callableStatement.setInt(2, Integer.parseInt(salariuFldMSA.getText()));
                    callableStatement.setInt(3, Integer.parseInt(oreFldMSA.getText()));
                    callableStatement.setString(4, "Asistent Medical");
                    callableStatement.setString(5, String.valueOf(dataAngajariiFldMSA.getValue()));
                    callableStatement.execute();

                    int angajatID = 0;
                    String getAngajatID = "{CALL ObtineIDAngajat(?, ?)}";
                    callableStatement = connection.prepareCall(getAngajatID);
                    callableStatement.setInt(1, utilizatorID);
                    callableStatement.setString(2, emailFldMSA.getText());
                    callableStatement.execute();

                    resultSet = callableStatement.getResultSet();
                    while(resultSet.next()){
                        angajatID = resultSet.getInt("v_ID_Angajat");
                    }
                    String insertAsistent = "{CALL InsertAsistent(?, ?, ?)}";
                    callableStatement = connection.prepareCall(insertAsistent);
                    callableStatement.setString(1, String.valueOf(tipFldMSA.getValue()));
                    callableStatement.setString(2, String.valueOf(gradFldMSA.getValue()));
                    callableStatement.setInt(3, angajatID);
                    callableStatement.execute();
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
            SelectUserTypeSuperAdminController.inapoiFunctiiButtonAction(e, AddAsistentSAController.class);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tipFldMSA.getItems().addAll(tip);
        gradFldMSA.getItems().addAll(grad);
    }
}
