package app.controllers;

import app.Manager;
import app.classes.Employee;
import app.classes.Project;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

public class EmployeeRegister {

    @FXML private GridPane signUpGridPane;
    @FXML private CheckBox seniorCheck;
    @FXML private Button managerBtn;
    @FXML private Button analystBtn;
    @FXML private Button designerBtn;
    @FXML private Button coderBtn;
    @FXML private Button testerBtn;
    @FXML private Label idLabel;
    @FXML private Label dateLabel;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField passwordField;
    @FXML private DatePicker birthDatePicker;
    @FXML private TextField phoneNoField;
    @FXML private TextField nationField;
    @FXML private TextField salaryField;
    @FXML private ChoiceBox accountingBox;
    @FXML private Label langLabel;
    @FXML private ChoiceBox languageBox;
    @FXML private Button signUpFinishBtn;
    @FXML private Button backBtn;

    private boolean newEmail;
    private boolean newPhoneNo;
    private String title;
    private boolean isTypeChosen = false;
    private boolean newAdmin;
    private Employee newEmployee;

    public void start(final Employee loggedUser, final String[] accountingPrograms, final String[] programmingLangs) {

        Stage stage = (Stage) signUpGridPane.getScene().getWindow();
        stage.sizeToScene();
        stage.setTitle("Import Employee");
        stage.setResizable(false);

        Scene scene = signUpGridPane.getScene();
        scene.getStylesheets().add("app/resources/styles/style.css");

        langLabel.setVisible(false);
        languageBox.setVisible(false);

        // define the range for random id
        int max = 999999, min = 100000, range = max - min + 1, randid;

        // loop until the id is available for use
        do {
            randid = (int) (Math.random() * range) + min; // randomly generate id
            // TODO: maybe use hashTable ?
        } while (!idCheck(randid));

        final int id = randid;
        idLabel.setText(Integer.toString(id));

        accountingBox.getItems().addAll(accountingPrograms);
        accountingBox.getSelectionModel().selectFirst(); // sets default value of choicebox
        languageBox.getItems().addAll(programmingLangs);

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        dateLabel.setText(day + "/" + (month + 1) + "/" + year);

        seniorCheck.setOnMouseClicked(mouseEvent -> {
            newAdmin = !newAdmin; // toggle newAdmin
        });

        managerBtn.setOnAction(actionEvent -> {
            title = "Manager";
            managerBtn.setStyle("-fx-border-color: #4FC3F7");
            analystBtn.setStyle("-fx-border-color: transparent; -fx-opacity: 0.5");
            designerBtn.setStyle("-fx-border-color: transparent; -fx-opacity: 0.5");
            coderBtn.setStyle("-fx-border-color: transparent; -fx-opacity: 0.5");
            testerBtn.setStyle("-fx-border-color: transparent; -fx-opacity: 0.5");
            isTypeChosen = true;
            langLabel.setVisible(false);
            languageBox.setVisible(false);
        });

        analystBtn.setOnAction(actionEvent -> {
            title = "Analyst";
            analystBtn.setStyle("-fx-border-color: #4FC3F7");
            managerBtn.setStyle("-fx-border-color: transparent; -fx-opacity: 0.5");
            designerBtn.setStyle("-fx-border-color: transparent; -fx-opacity: 0.5");
            coderBtn.setStyle("-fx-border-color: transparent; -fx-opacity: 0.5");
            testerBtn.setStyle("-fx-border-color: transparent; -fx-opacity: 0.5");
            isTypeChosen = true;
            langLabel.setVisible(false);
            languageBox.setVisible(false);
        });

        designerBtn.setOnAction(actionEvent -> {
            title = "Designer";
            designerBtn.setStyle("-fx-border-color: #4FC3F7");
            managerBtn.setStyle("-fx-border-color: transparent; -fx-opacity: 0.5");
            analystBtn.setStyle("-fx-border-color: transparent; -fx-opacity: 0.5");
            coderBtn.setStyle("-fx-border-color: transparent; -fx-opacity: 0.5");
            testerBtn.setStyle("-fx-border-color: transparent; -fx-opacity: 0.5");
            isTypeChosen = true;
            langLabel.setVisible(false);
            languageBox.setVisible(false);
        });

        coderBtn.setOnAction(actionEvent -> {
            title = "Programmer";
            coderBtn.setStyle("-fx-border-color: #4FC3F7");
            managerBtn.setStyle("-fx-border-color: transparent; -fx-opacity: 0.5");
            designerBtn.setStyle("-fx-border-color: transparent; -fx-opacity: 0.5");
            analystBtn.setStyle("-fx-border-color: transparent; -fx-opacity: 0.5");
            testerBtn.setStyle("-fx-border-color: transparent; -fx-opacity: 0.5");
            isTypeChosen = true;
            langLabel.setVisible(true);
            languageBox.setVisible(true);
            languageBox.getSelectionModel().selectFirst();
        });

        testerBtn.setOnAction(actionEvent -> {
            title = "Tester";
            testerBtn.setStyle("-fx-border-color: #4FC3F7");
            managerBtn.setStyle("-fx-border-color: transparent; -fx-opacity: 0.5");
            designerBtn.setStyle("-fx-border-color: transparent; -fx-opacity: 0.5");
            coderBtn.setStyle("-fx-border-color: transparent; -fx-opacity: 0.5");
            analystBtn.setStyle("-fx-border-color: transparent; -fx-opacity: 0.5");
            isTypeChosen = true;
            langLabel.setVisible(false);
            languageBox.setVisible(false);
        });

        backBtn.setOnAction(actionEvent -> {
            if (loggedUser.isAdmin() && !loggedUser.isLogged()) Manager.viewLoginPage();
            else Manager.viewDashboard(loggedUser);
        });

        // force the fields to be numeric only
        phoneNoField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                phoneNoField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        salaryField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                salaryField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        signUpFinishBtn.setOnAction(actionEvent -> {

            // check whether any field is empty
            if (firstNameField.getText().isEmpty() || emailField.getText().isEmpty() ||
                    passwordField.getText().isEmpty() || phoneNoField.getText().isEmpty() ||
                    nationField.getText().isEmpty() || salaryField.getText().isEmpty())
            {
                Manager.showAlert(Alert.AlertType.WARNING, "Empty Field", "Please, do not leave any empty field.");
                return;
            }

            // check for unique email and phone number
            identitiyCheck(emailField.getText(), Long.parseLong(phoneNoField.getText()));


            // if the email is in use
            if (!isNewEmail())
            {
                Manager.showAlert(Alert.AlertType.INFORMATION, "Used identitiy", "This email is in use. Please change it.");
                emailField.setStyle("-fx-border-color: #F42");
                return;
            }
            else { emailField.setStyle("-fx-border-color: transparent"); }


            // if the phone number is in use
            if (!isNewPhoneNo())
            {
                Manager.showAlert(Alert.AlertType.INFORMATION, "Used identitiy", "This phone number is in use. Please change it.");
                phoneNoField.setStyle("-fx-border-color: #F42");
                return;
            }
            else phoneNoField.setStyle("-fx-border-color: transparent");

            if (!isTypeChosen)
            {
                Manager.showAlert(Alert.AlertType.WARNING, "No title", "Please choose employee title");
                return;
            }

            LocalDate acceptdate = LocalDate.of(year, month + 1, day);
            LocalDate birthdate = birthDatePicker.getValue();

            if (birthdate == null) {
                Manager.showAlert(Alert.AlertType.WARNING, "No birth date", "Birth Date field cannot be empty.");
                return;
            }

            String accounting = accountingBox.getValue().toString();
            String skill = null;
            try {
                skill = languageBox.getValue().toString();
            } catch (NullPointerException e) {
                System.out.println("NullPointerException at EmployeeRegister: No skill selected.");
            }

            // Programmers must have selected language
            if (skill == null && title.equals("Programmer")) {
                Manager.showAlert(Alert.AlertType.WARNING, "Empty skill", "Skill for programmer is not selected.");
                return;
            }

            // add user to db
            signUp(id, acceptdate, title, firstNameField.getText(), lastNameField.getText(), emailField.getText(),
                    passwordField.getText(), Long.parseLong(phoneNoField.getText()), birthdate, nationField.getText(),
                    Integer.parseInt(salaryField.getText()), accounting, skill, newAdmin);

            if (newEmployee.getTitle().equals("Programmer"))
            {
                assignToProject(newEmployee);
            }

            if (loggedUser.isLogged()) Manager.viewDashboard(loggedUser);
            else Manager.viewLoginPage();
        });
    }

    /**
     * Checks the uniqueness of specific fields.
     * @param email Personal email of the employee.
     * @param phoneNo Personal phone number of the employee.
     */
    private void identitiyCheck(String email, long phoneNo) {
        try {
            String sql = "SELECT * FROM company WHERE email = ?";
            Connection connection = Manager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) setNewEmail(false);
            else setNewEmail(true);

            sql = "SELECT * FROM company WHERE phone = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, phoneNo);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) setNewPhoneNo(false);
            else setNewPhoneNo(true);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check whether ID exists in db or not.
     * @param id Randomly generated id.
     * @return True if id is available for use.
     */
    private boolean idCheck(int id) {
        try {
            String sql = "SELECT * FROM company WHERE id = ?";
            Connection connection = Manager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            return !resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            Manager.showAlert(Alert.AlertType.WARNING, "Exception", "ERR_ID_CHECK");
            return false;
        }
    }

    /**
     * Sends all the user information to database.
     * @param id Randomly generated id.
     * @param acceptDate Date when worker is accepted to company.
     * @param title Job; Manager, Analyst, Designer, etc.
     * @param first_name First name
     * @param last_name Last name
     * @param email Personal email
     * @param password Password to log in
     * @param phone Personal phone number
     * @param birthDate Date of birth
     * @param nationality Nationality from birth
     * @param salary Initial salary
     * @param accounting Accounting program
     * @param skill Particular skill(s) of employee
     * @param admin Is admin of the system
     */
    private void signUp(int id, LocalDate acceptDate, String title, String first_name, String last_name,
                                  String email, String password, long phone, LocalDate birthDate, String nationality,
                                  int salary, String accounting, String skill, boolean admin)
    {
        Connection connection = Manager.getConnection();
        PreparedStatement preparedStatement = null;
        String sql = "INSERT INTO company (id, acceptdate, title, first_name, last_name, email, password, phone, " +
                "birthdate, nationality, salary, accounting, skills, admin) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            if (acceptDate != null) preparedStatement.setDate(2, Date.valueOf(acceptDate));
            else preparedStatement.setNull(2, Types.DATE);
            preparedStatement.setString(3, title);
            preparedStatement.setString(4, first_name);
            preparedStatement.setString(5, last_name);
            preparedStatement.setString(6, email);
            preparedStatement.setString(7, password);
            preparedStatement.setLong(8, phone);
            if (birthDate != null) preparedStatement.setDate(9, Date.valueOf(birthDate));
            else preparedStatement.setNull(9, Types.DATE);
            preparedStatement.setString(10, nationality);
            preparedStatement.setInt(11, salary);
            preparedStatement.setString(12, accounting);
            if (skill != null) preparedStatement.setString(13, skill);
            else preparedStatement.setNull(13, Types.VARCHAR);
            preparedStatement.setBoolean(14, admin);
            preparedStatement.executeUpdate();

            newEmployee = new Employee(id, acceptDate, title, first_name, last_name, email, password, phone, birthDate,
                    nationality, salary, accounting, skill, admin);

            Manager.showAlert(Alert.AlertType.INFORMATION, "", "Registration successful.");
        } catch (Exception e) {
            Manager.showAlert(Alert.AlertType.WARNING, "Exception", "Couldn't complete registration. ERR_SIGN_UP");
            e.printStackTrace();
        }
    }

    /**
     * Assigns employee to appropriate project if exists.
     */
    private void assignToProject(Employee employee) {
        try {
            Connection connection = Manager.getConnection();
            String sql = "select * from projects where lang = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, employee.getSkill());
            ResultSet resultSet = statement.executeQuery();

            ArrayList<Project> projects = new ArrayList<>();
            ArrayList<String> teams = new ArrayList<>();

            // get all the projects list with matching language
            while (resultSet.next())
            {
                projects.add(new Project(resultSet.getString("pr_name"), resultSet.getString("lang"),
                        Dashboard.findTeam(resultSet.getString("team")), resultSet.getDate("startDate").toLocalDate(),
                        resultSet.getDate("dueDate").toLocalDate(), resultSet.getDate("creationDate").toLocalDate(),
                        resultSet.getString("description")));
            }

            // now look if there's empty space for programmer in projects' teams
            for (Project p: projects)
            {
                if (p.getTeam().getProgrammer() == 0) // if programmer is null, store team name
                {
                    teams.add(p.getTeam().getName());
                }
            }

            // if appropriate teams were found
            if (teams.size() > 0)
            {
                System.out.println("sure");
                String projectList = "";
                for (String name: teams)
                {
                    System.out.println("here");
                    projectList += name;
                    projectList += " ";
                }
                String message = "Following projects are available for this employee: " + projectList;
                Manager.showAlert(Alert.AlertType.INFORMATION, "Assign to projects", message);
            }
            else
            {
                System.out.println("fuck");
                Manager.showAlert(Alert.AlertType.INFORMATION, "Project", "No suitable project found for this employee.");
            }

        } catch (SQLException e) {
            Manager.showAlert(Alert.AlertType.WARNING, "Exception", "Error while trying to assign to project.");
            e.printStackTrace();
        }
    }

    public boolean isNewEmail() {
        return newEmail;
    }

    public void setNewEmail(boolean newEmail) {
        this.newEmail = newEmail;
    }

    public boolean isNewPhoneNo() {
        return newPhoneNo;
    }

    public void setNewPhoneNo(boolean newPhoneNo) {
        this.newPhoneNo = newPhoneNo;
    }

    public Employee getNewEmployee() {
        return newEmployee;
    }

    public void setNewEmployee(Employee newEmployee) {
        this.newEmployee = newEmployee;
    }
}
