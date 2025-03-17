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

public class GestiuneaActivitatilorReceptionerController implements Initializable{
     @FXML
    private Button backButtonR;

    @FXML
    private TableColumn<UsersR, String> dataColR;

    @FXML
    private TableColumn<UsersR, String> doctorColR;

    @FXML
    private TableColumn<UsersR, String> numeColR;

    @FXML
    private TableColumn<UsersR, String> oraColR;

    @FXML
    private TableColumn<UsersR, String> prenumeColR;

    @FXML
    private TableColumn<UsersR, String> sumaColR;

    @FXML
    private TableView<UsersR> tableReceptioner;

    ObservableList<UsersR> list;

    @FXML
    public void backButton(ActionEvent e) {
        Stage stage = (Stage) backButtonR.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void refreshTable(ActionEvent e){
        list.clear();
        updateTable();
    }

    @FXML
    public void adaugaProgramare(ActionEvent e){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(GestiuneaActivitatilorReceptionerController.class.getResource("ProgramariPacienti.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 610, 630);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void updateTable(){
        numeColR.setCellValueFactory(new PropertyValueFactory<UsersR, String>("Nume"));
        prenumeColR.setCellValueFactory(new PropertyValueFactory<UsersR, String>("Prenume"));
        doctorColR.setCellValueFactory(new PropertyValueFactory<UsersR, String>("Doctor"));
        oraColR.setCellValueFactory(new PropertyValueFactory<UsersR, String>("Ora"));
        sumaColR.setCellValueFactory(new PropertyValueFactory<UsersR, String>("Suma"));
        dataColR.setCellValueFactory(new PropertyValueFactory<UsersR, String>("Data"));
        getUsers();
        tableReceptioner.setItems(list);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateTable();
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
        ObservableList<UsersR> tempList = FXCollections.observableArrayList();

        String getPolyclinicID = "SELECT ID_Policlinici FROM Utilizatori WhERE Email = '" + UserData.getEmail() + "'";
        String pacientiInfo = "SELECT ID_Policlinici, Nume, Prenume, OraProgramare, DataProgramare, Pret, ID_Medic " +
                              "FROM Utilizatori, Pacienti, programarePacienti, serviciimedicale " +
                              "WHERE utilizatori.id_utilizator = Pacienti.ID_Utilizator " +
                              "AND Pacienti.ID_pacient = programarePacienti.id_pacient " +
                              "AND programarePacienti.id_serviciu = serviciimedicale.id_serviciu " +
                              "AND ID_policlinici = ?";
        String medicInfo = "SELECT Nume, Prenume FROM Utilizatori, Angajat, Medic " +
                            "WHERE utilizatori.functie = 'Medic' " +
                            "AND utilizatori.id_utilizator = angajat.id_utilizator " +
                            "AND angajat.id_angajat = medic.id_angajat " +
                            "AND Medic.id_medic = ?";

        try{
            int polyclinicID = 0;
            int medicID = 0;
            String numePacient = "";
            String prenumePacient = "";
            String ora = "";
            String suma = "";
            String data = "";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(getPolyclinicID);
            while(resultSet.next()){
                polyclinicID = resultSet.getInt("ID_Policlinici");
            }
            preparedStatement = connection.prepareStatement(pacientiInfo);
            preparedStatement.setInt(1, polyclinicID);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                numePacient = resultSet.getString("Nume");
                prenumePacient = resultSet.getString("Prenume");
                ora = resultSet.getString("OraProgramare");
                suma = resultSet.getString("Pret");
                data = resultSet.getString("DataProgramare");
                medicID = resultSet.getInt("ID_Medic");
                preparedStatement2 = connection.prepareStatement(medicInfo);
                preparedStatement2.setInt(1, medicID);
                resultSet2 = preparedStatement2.executeQuery();
                while(resultSet2.next()){
                    tempList.add(new UsersR(numePacient, prenumePacient, resultSet2.getString("Nume") + " " + resultSet2.getString("Prenume"), ora, suma, data));
                }
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
