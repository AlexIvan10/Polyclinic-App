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

public class GestioneazaDonatoriController implements Initializable{
    @FXML
    private TableColumn<UsersDonatori, String> RHCol;

    @FXML
    private TableColumn<UsersDonatori, String> dataColectariiCol;

    @FXML
    private TableColumn<UsersDonatori, String> emailCol;

    @FXML
    private TableColumn<UsersDonatori, String> grupaCol;

    @FXML
    private Button inapoiButton;

    @FXML
    private Label errorMessage;

    @FXML
    private TableColumn<UsersDonatori, String> numeCol;

    @FXML
    private TableColumn<UsersDonatori, String> prenumeCol;

    @FXML
    private TableView<UsersDonatori> tableDonatori;

    ObservableList<UsersDonatori> list;

    @FXML
    void inapoiButtonAction(ActionEvent event) {
        Stage stage = (Stage) inapoiButton.getScene().getWindow();
        stage.close();
    }

    public void updateTable(){
        numeCol.setCellValueFactory(new PropertyValueFactory<UsersDonatori, String>("NumeDonator"));
        prenumeCol.setCellValueFactory(new PropertyValueFactory<UsersDonatori, String>("PrenumeDonator"));
        emailCol.setCellValueFactory(new PropertyValueFactory<UsersDonatori, String>("Email"));
        grupaCol.setCellValueFactory(new PropertyValueFactory<UsersDonatori, String>("Grupa"));
        RHCol.setCellValueFactory(new PropertyValueFactory<UsersDonatori, String>("RH"));
        dataColectariiCol.setCellValueFactory(new PropertyValueFactory<UsersDonatori, String>("DataColectarii"));
        getUsers();
        tableDonatori.setItems(list);
    }

    public void getUsers(){
        Connection connection = null;
        Statement selectStatement = null;
        ResultSet resultSet = null;
        ObservableList<UsersDonatori> tempList = FXCollections.observableArrayList();

        DatabaseConnection connectNow = new DatabaseConnection();
        connection = connectNow.getConnection();

        try{
            int polyclinicID = 0;
            String getMedicPolycliniciID = "SELECT ID_Policlinici FROM Utilizatori WHERE Email = '" + UserData.getEmail() + "'";
            selectStatement = connection.createStatement();
            resultSet = selectStatement.executeQuery(getMedicPolycliniciID);
            while(resultSet.next()){
                polyclinicID = resultSet.getInt("ID_Policlinici");
            }

            String getAllData  = "SELECT * FROM Utilizatori, DonatoriSange WHERE Utilizatori.ID_Utilizator = DonatoriSange.ID_Utilizator AND ID_Policlinici = " + polyclinicID;
            resultSet = selectStatement.executeQuery(getAllData);

            while(resultSet.next()){
                tempList.add(new UsersDonatori(resultSet.getString("Nume"), resultSet.getString("Prenume"), resultSet.getString("Email"), resultSet.getString("Grupa"), resultSet.getString("RH"), resultSet.getString("DataColectarii")));
            }
        }catch (Exception sqlex){
            System.err.println("An SQL Exception occured. Details are provided below:");
            sqlex.printStackTrace(System.err);
        }

        list = tempList;
    }

    @FXML
    void stergeDonator(ActionEvent e){
        TableView.TableViewSelectionModel<UsersDonatori> selectedID = tableDonatori.getSelectionModel();
            UsersDonatori selectedUser = selectedID.getSelectedItem();
            if(selectedUser != null){
                errorMessage.setText("");
                Connection connection = null;
                Statement statement = null;
                ResultSet resultSet = null;
                DatabaseConnection connectNow = new DatabaseConnection();
                connection = connectNow.getConnection();
                try {
                    int utilizatorID = 0;
                    String getUtilizatorID = "SELECT ID_Utilizator FROM Utilizatori WHERE Email = '" + selectedUser.getEmail() + "'";
                    statement = connection.createStatement();
                    resultSet = statement.executeQuery(getUtilizatorID);
                    while (resultSet.next()){
                        utilizatorID = resultSet.getInt("ID_Utilizator");
                    }
                    String stergeDonator = "DELETE FROM DonatoriSange WHERE ID_Utilizator = " + utilizatorID;
                    statement.execute(stergeDonator);
                    tableDonatori.getItems().remove(selectedID.getSelectedIndex());
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
            else{
                errorMessage.setText("Selecteaza un donator");
            }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateTable();
    }
}
