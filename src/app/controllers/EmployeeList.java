package app.controllers;

import app.Manager;
import app.classes.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static app.controllers.Dashboard.*;

public class EmployeeList {

    @FXML private AnchorPane optionEmployeesPane;
    @FXML private TilePane employeesTilePane;
    @FXML private Button backBtn;

    private ObservableList<Employee> employees = FXCollections.observableArrayList();

    public void start(final Employee user) {
        Stage stage = (Stage) optionEmployeesPane.getScene().getWindow();
        stage.sizeToScene();
        stage.setTitle("Employees");
        stage.setResizable(false);

        Scene scene = optionEmployeesPane.getScene();
        scene.getStylesheets().add("app/resources/styles/style.css");

        loadEmployees();

        // detect mouse on senior employee tile
        for (Node node: employeesTilePane.lookupAll(".employee-indv-tile"))
        {
            node.setOnMouseEntered(mouseEvent -> {
                node.setStyle("-fx-opacity: 0.5");
            });

            node.setOnMouseExited(mouseEvent -> {
                node.setStyle("-fx-opacity: 1.0");
            });

            // get employee info
            node.setOnMouseClicked(mouseEvent -> {
                GridPane gridPane = (GridPane) mouseEvent.getSource();
                Label employeeID = (Label) gridPane.lookup("#employeeID");
                Employee employee = findEmployeeByID(Integer.parseInt(employeeID.getText()));
                Manager.viewEmployeeInfo(employee, user);
            });
        }

        backBtn.setOnAction(actionEvent -> Manager.viewDashboard(user));
    }

    /**
     * Loads employees from DB.
     */
    private void loadEmployees() {

        employeesTilePane.getChildren().clear(); // clear the view
        employees.clear();

        try {
            Connection connection = Manager.getConnection();
            ResultSet rs = connection.createStatement().executeQuery("select * from company");

            // store all data in observable list
            while (rs.next()) {
                employees.add(new Employee(rs.getInt("id"), rs.getDate("acceptdate").toLocalDate(), rs.getString("title"),
                        rs.getString("first_name"), rs.getString("last_name"), rs.getString("email"),
                        rs.getString("password"), rs.getLong("phone"), rs.getDate("birthdate").toLocalDate(),
                        rs.getString("nationality"), rs.getInt("salary"), rs.getString("accounting"),
                        rs.getString("skills"), rs.getBoolean("admin")));
            }

            // now add those teams to view
            for (Employee e: employees) {
                AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/app/view/employee_tile.fxml"));
                GridPane gridPane = (GridPane) anchorPane.getChildren().get(0);
                gridPane.getStyleClass().add("employee-indv-tile");

                Label name = (Label) gridPane.getChildren().get(1);
                if (e.isAdmin()) // distinguish the seniors and normal employees
                {
                    name.setText(e.getFullName() + " (S)");
                }
                else
                {
                    name.setText(e.getFullName());
                }

                Label id = (Label) gridPane.getChildren().get(2);
                id.setText(Integer.toString(e.getId()));

                Label title = (Label) gridPane.getChildren().get(3);
                title.setText(e.getTitle());

                // add parent pane to the dashboard
                employeesTilePane.getChildren().add(anchorPane);
            }

        } catch (SQLException | IOException e) {
            Manager.showAlert(Alert.AlertType.ERROR, "", "Couldn't load seniors list.");
        }
    }

}
