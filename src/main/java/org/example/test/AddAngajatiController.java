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

public class AddAngajatiController implements Initializable {
    @FXML
    private TextField CNPFld;

    @FXML
    private TextField IBANFld;

    @FXML
    private TextField adresaFld;

    @FXML
    private DatePicker dataAngajariiFld;

    @FXML
    private TextField emailFld;

    @FXML
    private ChoiceBox<String> functieFld;

    @FXML
    private Label messageLabel;

    @FXML
    private TextField nrTelFld;

    @FXML
    private TextField numeFld;

    @FXML
    private TextField oreFld;

    @FXML
    private TextField parolaFld;

    @FXML
    private TextField prenumeFld;

    @FXML
    private TextField salariuFld;
    private String[] functiiSuperAdmin = {"Inspector Resurse Umane", "Expert Financiar Contabil", "Receptioner", "Admin"};

    private String[] functiiAdmin = {"Inspector Resurse Umane", "Expert Financiar Contabil", "Receptioner"};

    @FXML
    protected void inapoiButtonAction(ActionEvent e) {
        SelectUserTypeSuperAdminController.inapoiFunctiiButtonAction(e, AddAngajatiController.class);
    }

    @FXML
    protected void adaugaButtonAction(ActionEvent e) {
        if (numeFld.getText().isBlank() || prenumeFld.getText().isBlank() ||
                emailFld.getText().isBlank() || parolaFld.getText().isBlank() ||
                CNPFld.getText().isBlank() || IBANFld.getText().isBlank() ||
                adresaFld.getText().isBlank() || nrTelFld.getText().isBlank() ||
                dataAngajariiFld.getValue() == null ||
                salariuFld.getText().isBlank() || oreFld.getText().isBlank())
            messageLabel.setText("Completeaza toate campurile");
        else {
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
                    messageLabel.setText("Email existent");
                    exista = 1;
                }
                callableStatement = connection.prepareCall(CNPExist);
                callableStatement.setString(1, CNPFld.getText());
                callableStatement.registerOutParameter(2, Types.INTEGER);
                callableStatement.execute();
                valoare = callableStatement.getInt(2);
                if (valoare == 1) {
                    messageLabel.setText("CNP existent");
                    exista = 1;
                }
                callableStatement = connection.prepareCall(IBANExist);
                callableStatement.setString(1, IBANFld.getText());
                callableStatement.registerOutParameter(2, Types.INTEGER);
                callableStatement.execute();
                valoare = callableStatement.getInt(2);
                if (valoare == 1) {
                    messageLabel.setText("IBAN existent");
                    exista = 1;
                }
                if (exista == 0) {
                    messageLabel.setText("");
                    int superAdminPolyclinicID = 0;
                    String superAdminPolyclinic = "SELECT ID_Policlinici FROM Utilizatori WHERE Email = '" + UserData.getEmail() + "'";
                    String insertUtilizator = "{CALL InsertUtilizator(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
                    statement = connection.createStatement();
                    resultSet = statement.executeQuery(superAdminPolyclinic);
                    while (resultSet.next()) {
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
                    callableStatement.setString(10, String.valueOf(functieFld.getValue()));
                    callableStatement.execute();
                    String getUtilizatorID = "SELECT ID_Utilizator FROM Utilizatori WHERE Email = '" + emailFld.getText() + "'";
                    int utilizatorID = 0;
                    statement = connection.createStatement();
                    resultSet = statement.executeQuery(getUtilizatorID);
                    while (resultSet.next()) {
                        utilizatorID = resultSet.getInt("ID_Utilizator");
                    }
                    String insertAngajat = "{CALL InsertAngajat(?, ?, ?, ?, ?)}";
                    callableStatement = connection.prepareCall(insertAngajat);
                    callableStatement.setInt(1, utilizatorID);
                    callableStatement.setInt(2, Integer.parseInt(salariuFld.getText()));
                    callableStatement.setInt(3, Integer.parseInt(oreFld.getText()));
                    callableStatement.setString(4, String.valueOf(functieFld.getValue()));
                    callableStatement.setString(5, String.valueOf(dataAngajariiFld.getValue()));
                    callableStatement.execute();
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
            SelectUserTypeSuperAdminController.inapoiFunctiiButtonAction(e, AddAngajatiController.class);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Connection connection;
        PreparedStatement preparedStatement = null;
        Statement statement = null;
        ResultSet resultSet = null;
        CallableStatement callableStatement = null;
        DatabaseConnection connectNow = new DatabaseConnection();
        connection = connectNow.getConnection();
        String functie = "";
        try {
            String getFunctie = "SELECT Functie FROM Utilizatori WHERE Email = '" + UserData.getEmail() + "'";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(getFunctie);
            while(resultSet.next()){
                functie = resultSet.getString("Functie");
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        if(functie.equals("Super Admin"))
            functieFld.getItems().addAll(functiiSuperAdmin);
        else{
            functieFld.getItems().addAll(functiiAdmin);
        }
    }
}
