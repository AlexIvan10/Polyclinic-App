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


public class GestiuneaActivitatilorAsistentController implements Initializable{
    @FXML
    private Button backButtonA;

    @FXML
    private TableColumn<UsersA, String> dataAsistentFld;

    @FXML
    private TableColumn<UsersA, String> medicAsistentFld;

    @FXML
    private TableColumn<UsersA, String> oraAsistentFld;

     @FXML
    private TableView<UsersA> tableAsistent;

    ObservableList<UsersA> list;

    @FXML
    public void backButton(ActionEvent e) {
        Stage stage = (Stage) backButtonA.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        medicAsistentFld.setCellValueFactory(new PropertyValueFactory<UsersA, String>("Medic"));
        dataAsistentFld.setCellValueFactory(new PropertyValueFactory<UsersA, String>("DataProgramare"));
        oraAsistentFld.setCellValueFactory(new PropertyValueFactory<UsersA, String>("OraProgramare"));
        getUsers();
        tableAsistent.setItems(list);
    }
    public void getUsers(){
        Connection connection = null;
        DatabaseConnection connectNow = new DatabaseConnection();
        connection = connectNow.getConnection();
        PreparedStatement preparedStatement = null;
        PreparedStatement preparedStatement2 = null;
        Statement statement = null;
        ResultSet resultSet = null;
        ResultSet resultSet2 = null;
        ObservableList<UsersA> tempList = FXCollections.observableArrayList();

        String getPolyclinicID = "SELECT ID_Policlinici FROM Utilizatori WhERE Email = '" + UserData.getEmail() + "'";
        String programareInfo = "SELECT DataProgramare, OraProgramare, ID_Medic " +
                              "FROM programarepacienti, pacienti, utilizatori " +
                              "WHERE programarepacienti.id_pacient = pacienti.id_pacient " +
                              "AND pacienti.id_utilizator = utilizatori.id_utilizator " +
                              "AND ID_Policlinici = ?";
        String medicInfo = "SELECT Nume, Prenume FROM Utilizatori, Angajat, Medic " +
                            "WHERE utilizatori.functie = 'Medic' " +
                            "AND utilizatori.id_utilizator = angajat.id_utilizator " +
                            "AND angajat.id_angajat = medic.id_angajat " +
                            "AND Medic.id_medic = ?";

        try{
            int polyclinicID = 0;
            int medicID;
            String ora = "";
            String data = "";
            String numeMedic = "";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(getPolyclinicID);
            while(resultSet.next()){
                polyclinicID = resultSet.getInt("ID_Policlinici");
            }
            preparedStatement = connection.prepareStatement(programareInfo);
            preparedStatement.setInt(1, polyclinicID);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                data = resultSet.getString("DataProgramare");
                ora = resultSet.getString("OraProgramare");
                medicID = resultSet.getInt("ID_Medic");
                preparedStatement2 = connection.prepareStatement(medicInfo);
                preparedStatement2.setInt(1, medicID);
                resultSet2 = preparedStatement2.executeQuery();
                while(resultSet2.next()){
                    numeMedic = resultSet2.getString("Nume") + " " + resultSet2.getString("Prenume");
                }
                tempList.add(new UsersA(numeMedic, data, ora));
            }
        }catch (Exception sqlex){
            System.err.println("An SQL Exception occured. Details are provided below:");
            sqlex.printStackTrace(System.err);
        }
        finally {
            closeOperation(connection);
            closeOperation(statement);
            closeOperation(preparedStatement);
            closeOperation(preparedStatement2);
            closeOperation(resultSet);
            closeOperation(resultSet2);
        }
        list = tempList;
    }
    public void closeOperation(AutoCloseable operation){
        try {
            if (operation != null) {
                operation.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
