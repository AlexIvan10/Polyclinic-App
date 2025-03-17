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

public class RapoarteMedicaleController implements Initializable{

    @FXML
    private Button inapoiButton;

    @FXML
    private TextField raportFld;



    @FXML
    public void inapoiButton(ActionEvent e) {
        Stage stage = (Stage) inapoiButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void adaugaButton(ActionEvent e){
        String verificaRaport = "SELECT * FROM rapoartemedicale WHERE ID_Pacient = " + UserRaport.getID();
        Connection connection;
        DatabaseConnection databaseConnection = new DatabaseConnection();
        connection = databaseConnection.getConnection();
        Statement statement;
        ResultSet resultSet;
        CallableStatement callableStatement;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(verificaRaport);
            if(resultSet.next()){
                String modificaRaport = "UPDATE rapoartemedicale SET DetaliiRaport = '" + raportFld.getText() + "' WHERE ID_Pacient = " + UserRaport.getID();
                statement.executeUpdate(modificaRaport);
            }else{
                String gasireProgramare = "SELECT * FROM programarepacienti";
                String creareRaport = "{CALL InsertRaportMedical(?, ?, ?, CURDATE())}";
                resultSet = statement.executeQuery(gasireProgramare);
                int programareID = 0;
                while(resultSet.next()){
                    if(resultSet.getInt("ID_Pacient") == UserRaport.getID()){
                        programareID = resultSet.getInt("ID_Programare");
                    }
                }
                callableStatement = connection.prepareCall(creareRaport);
                callableStatement.setInt(1, UserRaport.getID());
                callableStatement.setInt(2, programareID);
                callableStatement.setString(3, raportFld.getText());
                callableStatement.execute();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

        Stage stage = (Stage) inapoiButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String raport = "";
        String gasesteRaport = "SELECT DetaliiRaport FROM rapoartemedicale WHERE ID_Pacient = " + UserRaport.getID();
        Connection connection;
        DatabaseConnection databaseConnection = new DatabaseConnection();
        connection = databaseConnection.getConnection();
        Statement statement;
        ResultSet resultSet;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(gasesteRaport);
            while (resultSet.next()){
                raport = resultSet.getString("DetaliiRaport");
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        raportFld.setText(raport);
    }
}
