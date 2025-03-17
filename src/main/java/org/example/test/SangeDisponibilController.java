package org.example.test;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class SangeDisponibilController implements Initializable{
    @FXML
    private Button backButtonSD;

    @FXML
    private Label grupa0Fld;

    @FXML
    private Label grupaABFld;

    @FXML
    private Label grupaAFld;

    @FXML
    private Label grupaBFld;

    @FXML
    public void backButton(ActionEvent e) {
        Stage stage = (Stage) backButtonSD.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Connection connection = null;
        DatabaseConnection connectionNow = new DatabaseConnection();
        connection = connectionNow.getConnection();
        Statement statement = null;
        ResultSet resultSet = null;
        int polyclinicID = 0;
        try {
            String getPolyclinicID = "SELECT ID_Policlinici FROM Utilizatori WHERE Email = '" + UserData.getEmail() + "'";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(getPolyclinicID);
            while(resultSet.next()){
                polyclinicID = resultSet.getInt("ID_Policlinici");
            }
            String getSange = "SELECT * FROM banca WHERE ID_Policlinica = " + polyclinicID;
            resultSet = statement.executeQuery(getSange);
            while (resultSet.next()){
                if(resultSet.getInt("unit_0") == 0)
                    grupa0Fld.setText("-");
                else{
                    grupa0Fld.setText(resultSet.getString("unit_0"));
                }
                if(resultSet.getInt("unit_A") == 0)
                    grupaAFld.setText("-");
                else{
                    grupaAFld.setText(resultSet.getString("unit_A"));
                }
                if(resultSet.getInt("unit_B") == 0)
                    grupaBFld.setText("-");
                else{
                    grupaBFld.setText(resultSet.getString("unit_B"));
                }
                if(resultSet.getInt("unit_AB") == 0)
                    grupaABFld.setText("-");
                else{
                    grupaABFld.setText(resultSet.getString("unit_AB"));
                }
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
