package org.example.test;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class DoneazaSangeController implements Initializable{

    @FXML
    private Button doneazaButton;

    @FXML
    private ChoiceBox<String> RHFld;

    @FXML
    private ChoiceBox<String> grupaFld;

    @FXML
    private Label errorMessage;

    private String[] RH = {"Pozitiv", "Negativ"};
    private String[] grupa = {"0", "A", "B", "AB"};

    @FXML
    protected void doneazaAction(ActionEvent e){
        if(RHFld.getValue() == null || grupaFld == null)
            errorMessage.setText("Selecteaza toate campurile");
        else{
            String doneaza = "SELECT ID_Utilizator FROM Utilizatori WHERE Email = '" + UserData.getEmail() + "'";
            Connection connection;
            DatabaseConnection connectionNow = new DatabaseConnection();
            connection = connectionNow.getConnection();
            Statement statement;
            PreparedStatement preparedStatement;
            ResultSet resultSet;
            try {
                int utilizatorID = 0;
                statement = connection.createStatement();
                resultSet = statement.executeQuery(doneaza);
                while(resultSet.next()){
                    utilizatorID = resultSet.getInt("ID_Utilizator");
                }
                String adaugaDonator = "INSERT INTO DonatoriSange (ID_Utilizator, DataColectarii, RH, Grupa) VALUES (?, CURDATE(), ?, ?)";
                preparedStatement = connection.prepareStatement(adaugaDonator);
                preparedStatement.setInt(1, utilizatorID);
                preparedStatement.setString(2, String.valueOf(RHFld.getValue()));
                preparedStatement.setString(3, String.valueOf(grupaFld.getValue()));
                preparedStatement.execute();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        Stage stage = (Stage) doneazaButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        RHFld.getItems().addAll(RH);
        grupaFld.getItems().addAll(grupa);
    }
}
