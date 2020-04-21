package app.controllers;

import app.Manager;
import app.classes.Employee;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;

public class EmployeeInfo {

    @FXML private AnchorPane employeeInfoPane;
    @FXML private Label fullName;
    @FXML private Label phone;
    @FXML private Label id;
    @FXML private Label title;
    @FXML private Label mail;
    @FXML private Label birthDate;
    @FXML private Label country;
    @FXML private Label team;
    @FXML private Label project;
    @FXML private Label projectLabel;
    @FXML private Label salary;
    @FXML private Label skills;
    @FXML private Label skillsLabel;
    @FXML private Label acceptDate;
    @FXML private Label senior;
    @FXML private Button editBtn;
    @FXML private Button backBtn;
    @FXML private Button fireBtn;
    @FXML private Button saveBtn;
    @FXML private Button discardBtn;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField phoneField;
    @FXML private TextField idField;
    @FXML private ComboBox titleCombo;
    @FXML private TextField mailField;
    @FXML private DatePicker birthDatePicker;
    @FXML private DatePicker acceptDatePicker;
    @FXML private TextField countryField;
    @FXML private ChoiceBox teamSelector;
    @FXML private TextField salaryField;
    @FXML private ComboBox skillsCombo;
    @FXML private CheckBox seniorCheck;
    private boolean admin;

    public void start(Employee employee, final Employee user) {
        Stage stage = (Stage) employeeInfoPane.getScene().getWindow();
        stage.sizeToScene();
        stage.setTitle("Employee Details");
        stage.setResizable(false);

        Scene scene = employeeInfoPane.getScene();
        scene.getStylesheets().add("app/resources/styles/style.css");

        editModeOff();
        initializeLabels(employee);

        backBtn.setOnAction(actionEvent -> {
            Manager.viewDashboard(user);
        });

        fireBtn.setOnAction(actionEvent -> {
            if (employee.getId() == user.getId()) {
                Manager.showAlert(Alert.AlertType.WARNING, "Error", "You cannot fire the currently logged user.");
                return;
            }
            fireEmployee(employee);
            Manager.viewDashboard(user);
        });

        editBtn.setOnAction(actionEvent -> {
            editModeOn(employee);
        });

        seniorCheck.setOnMouseClicked(mouseEvent -> {
            admin = !admin; // toggle
        });

        saveBtn.setOnAction(actionEvent ->  {

            // these are unnecessary, but to make it look clean
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            long phone  = Long.parseLong(phoneField.getText());
            //int id = Integer.parseInt(idField.getText());
            Date acceptDate = Date.valueOf(acceptDatePicker.getValue());

            // make first letter capital
            String title = titleCombo.getValue().toString().substring(0, 1).toUpperCase() + titleCombo.getValue().toString().substring(1);;
            String mail = mailField.getText();
            Date birthDate = Date.valueOf(birthDatePicker.getValue());
            String country = countryField.getText();
            int salary = Integer.parseInt(salaryField.getText());
            String skill = null;

            if (title.equals("Programmer"))
                skill = skillsCombo.getValue().toString();

            // do not change title if the person is in team (screws up the system otherwise)
            System.out.println(title);
            System.out.println(employee.getTitle());
            System.out.println(isEmployeeInTeam(employee));
            if (isEmployeeInTeam(employee) && !title.equals(employee.getTitle()))
            {
                Manager.showAlert(Alert.AlertType.WARNING, "",
                        "You cannot change the employee's title if he/she is currently in team. First, remove him/her from team, and then change the title.");
                return;
            }

            savechanges(employee, firstName, lastName, phone, acceptDate, title, mail, birthDate, country, salary,
                    skill, admin);
            editModeOff();
            initializeLabels(employee);
        });

        discardBtn.setOnAction(actionEvent -> {
            editModeOff();
        });
    }

    /**
     * Set all the labels according to the employee details.
     * @param employee Employee
     */
    private void initializeLabels(Employee employee) {
        fullName.setText(employee.getFullName());
        phone.setText(Long.toString(employee.getPhone()));
        id.setText(Integer.toString(employee.getId()));
        title.setText(employee.getTitle());
        mail.setText(employee.getEmail());
        birthDate.setText(employee.getBirthdate().toString());
        country.setText(employee.getNationality());
        for (String t: getEmployeeTeams(employee))
            team.setText(t);

        /*for (String p: getEmployeeProjects(employee))
            project.setText(p);*/ // TODO: For future
        project.setVisible(false);
        projectLabel.setVisible(false);

        salary.setText(Integer.toString(employee.getSalary()));

        if (employee.getTitle().equals("Programmer")) {
            skills.setText(employee.getSkill());
            skillsLabel.setVisible(true);
        }
        else {
            skillsLabel.setVisible(false);
            skills.setVisible(false);
        }

        acceptDate.setText(employee.getAcceptdate().toString());

        if (employee.isAdmin()) senior.setText("Yes");
        else senior.setText("No");
    }

    /**
     * Hide labels and show fields, for editing. Inverse of editModeOff().
     */
    private void editModeOn(Employee employee) {
        fullName.setVisible(false);
        phone.setVisible(false);
        //id.setVisible(false);
        title.setVisible(false);
        mail.setVisible(false);
        birthDate.setVisible(false);
        country.setVisible(false);
        team.setVisible(false);
        acceptDate.setVisible(false);
        salary.setVisible(false);
        senior.setVisible(false);
        skills.setVisible(false);
        editBtn.setVisible(false);
        backBtn.setVisible(false);
        fireBtn.setVisible(false);

        firstNameField.setVisible(true);
        lastNameField.setVisible(true);
        phoneField.setVisible(true);
        //idField.setVisible(true);
        titleCombo.setVisible(true);
        mailField.setVisible(true);
        birthDatePicker.setVisible(true);
        countryField.setVisible(true);
        teamSelector.setVisible(true);
        acceptDatePicker.setVisible(true);
        salaryField.setVisible(true);
        seniorCheck.setVisible(true);
//        skillsCombo.setVisible(true);
        saveBtn.setVisible(true);
        discardBtn.setVisible(true);


        // split fullName text
        String firstName = "";
        String lastName = "";

        if(fullName.getText().split("\\w+").length>1)
        {
            lastName = fullName.getText().substring(fullName.getText().lastIndexOf(" ") + 1);
            firstName = fullName.getText().substring(0, fullName.getText().lastIndexOf(' '));
        }
        else firstName = fullName.getText();

        firstNameField.setText(firstName);
        lastNameField.setText(lastName);
        phoneField.setText(phone.getText());
        //idField.setText(id.getText());
        titleCombo.getItems().addAll(Manager.DBGetTitles());
        titleCombo.getSelectionModel().select(employee.getTitle());

        mailField.setText(mail.getText());
        birthDatePicker.setValue(employee.getBirthdate());
        countryField.setText(employee.getNationality());

        teamSelector.getItems().addAll(Manager.getTeams());
        teamSelector.getSelectionModel().selectFirst();

        acceptDatePicker.setValue(employee.getAcceptdate());
        salaryField.setText(Integer.toString(employee.getSalary()));

        seniorCheck.setSelected(true);
    }

    /**
     * Hide fields and show labels, for viewing. Inverse of editModeOn().
     */
    private void editModeOff() {
        fullName.setVisible(true);
        phone.setVisible(true);
        id.setVisible(true);
        title.setVisible(true);
        mail.setVisible(true);
        birthDate.setVisible(true);
        country.setVisible(true);
        team.setVisible(true);
        acceptDate.setVisible(true);
        salary.setVisible(true);
        senior.setVisible(true);
        skills.setVisible(true);
        editBtn.setVisible(true);
        backBtn.setVisible(true);
        fireBtn.setVisible(true);

        firstNameField.setVisible(false);
        lastNameField.setVisible(false);
        phoneField.setVisible(false);
        idField.setVisible(false);
        titleCombo.setVisible(false);
        titleCombo.getItems().clear();
        mailField.setVisible(false);
        birthDatePicker.setVisible(false);
        countryField.setVisible(false);
        teamSelector.setVisible(false);
        teamSelector.getItems().clear();
        acceptDatePicker.setVisible(false);
        salaryField.setVisible(false);
        seniorCheck.setVisible(false);
        skillsCombo.setVisible(false);
        skillsCombo.getItems().clear();
        saveBtn.setVisible(false);
        discardBtn.setVisible(false);
    }

    /**
     * Find all the rows in teams where employee id matches.
     * @param employee Employee
     * @return Team list
     */
    private ArrayList<String> getEmployeeTeams(Employee employee) {
        try {
            Connection connection = Manager.getConnection();
            String sql = "select * from teams where ? in (manager, analyst, designer, programmer)"; // TODO: select all columns
            PreparedStatement statement = connection.prepareStatement(sql);
            ArrayList<String> teams = new ArrayList<>();

            statement.setInt(1, employee.getId());

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) { // store the results
                teams.add(resultSet.getString("t_name"));
            }
            return teams;
        } catch (SQLException e) {
            System.out.println("Exception at getEmployeeTeams() : teams not found.");
            return null;
        }
    }

    /**
     * Deletes employee record from DB.
     * @param employee Employee to be deleted
     */
    private void fireEmployee(Employee employee) {
        Connection connection = Manager.getConnection();
        String sql = "delete from company where id = ?";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setInt(1, employee.getId());

            // show confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Remove");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setHeaderText(null);
            alert.setContentText("Confirm to fire this employee?");
            alert.showAndWait();

            if (alert.getResult() == ButtonType.OK) {
                statement.executeUpdate();
                Manager.showAlert(Alert.AlertType.INFORMATION, "",
                        "Employee successfully removed from this company.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Couldn't delete the employee records.");
        }
    }

    private void errorCheck() {

    }

    private void savechanges(Employee employee, String firstName, String lastName, long phone, Date acceptDate,
                             String title, String mail, Date birthDate, String country, int salary, String skills,
                             boolean admin) {
        try {
            Connection con = Manager.getConnection();
            PreparedStatement preparedStatement = null;
            String sql = "UPDATE company " +
                    "SET acceptDate = ?, " +
                    "title = ?, " +
                    "first_name = ?, " +
                    "last_name = ?, " +
                    "email = ?, " +
                    "phone = ?, " +
                    "birthdate = ?, " +
                    "nationality = ?, " +
                    "salary = ?, " +
                    "skills = ?, " +
                    "admin = ? " +
                    "WHERE id = ?";

            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setDate(1, acceptDate);
            preparedStatement.setString(2, title);
            preparedStatement.setString(3, firstName);
            preparedStatement.setString(4, lastName);
            preparedStatement.setString(5, mail);
            preparedStatement.setLong(6, phone);
            preparedStatement.setDate(7, birthDate);
            preparedStatement.setString(8, country);
            preparedStatement.setInt(9, salary);
            if (skills != null) preparedStatement.setString(10, skills);
            else preparedStatement.setNull(10, Types.VARCHAR);
            preparedStatement.setBoolean(11, admin);
            preparedStatement.setInt(12, employee.getId());

            employee.setAcceptdate(acceptDate.toLocalDate());
            employee.setTitle(title);
            employee.setFirstName(firstName);
            employee.setLastName(lastName);
            employee.setEmail(mail);
            employee.setPhone(phone);
            employee.setBirthdate(birthDate.toLocalDate());
            employee.setNationality(country);
            employee.setSalary(salary);
            employee.setSkill(skills);
            employee.setAdmin(admin);

            // show confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Save");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setHeaderText(null);
            alert.setContentText("Confirm to save all changes?");
            alert.showAndWait();

            if (alert.getResult() == ButtonType.OK) {
                preparedStatement.executeUpdate();
                Manager.showAlert(Alert.AlertType.INFORMATION, "", "Employee successfully updated.");
            }

        } catch (Exception e) {
            Manager.showAlert(Alert.AlertType.ERROR, "Error", "Couldn't update employee.");
        }
    }

    /**
     * Checks whether employee consists in any team.
     * @param employee Employee
     * @return True if in team
     */
    private boolean isEmployeeInTeam(Employee employee) {

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "select * from teams where " + employee.getId() + " in (manager, analyst, designer, programmer, tester)";
        Connection connection = Manager.getConnection();

        try {
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Listener for the titleCombo while editing. If programmer, show the language selections.
     * @param event Changes
     */
    @FXML
    private void titleListener(ActionEvent event) {
        try {
            if (titleCombo.getSelectionModel().getSelectedItem().equals("Programmer")) {
                skillsLabel.setVisible(true);
                skillsCombo.setVisible(true);
                skillsCombo.getItems().addAll(Manager.getProgrammingLangs());
                skillsCombo.getSelectionModel().selectFirst();
            }
        } catch (NullPointerException e) {
            System.out.println("NullPointerException on titleListener().");
        }
    }
}
