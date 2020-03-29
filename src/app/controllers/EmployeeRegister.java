package app.controllers;

import app.Manager;
import app.tables.Company;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;
import java.util.Calendar;

public class EmployeeRegister {

    @FXML private GridPane signUpGridPane;
    @FXML private Button seniorBtn;
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
    @FXML private ChoiceBox languageBox;
    @FXML private Button signUpFinishBtn;
    @FXML private Button backBtn;

    private boolean newEmail;
    private boolean newPhoneNo;
    private String title;
    private boolean isTypeChosen = false;

    private String[] accountingPrograms = {"Program 1", "Program 2"};
    private String[] programmingLangs = {"C", "Java", "Python", "Go", "JavaScript", ".NET"};

    public void start(final Manager manager, final boolean admin, final Company loggedUser) {

        Stage stage = (Stage) signUpGridPane.getScene().getWindow();
        stage.sizeToScene();
        stage.setTitle("Import Employee");
        stage.setResizable(false);

        Scene scene = signUpGridPane.getScene();
        scene.getStylesheets().add("app/resources/styles/style.css");

        if (admin) // if we want to create new admin
        {
            seniorBtn.setVisible(true);
            managerBtn.setVisible(false);
            analystBtn.setVisible(false);
            designerBtn.setVisible(false);
            coderBtn.setVisible(false);
            testerBtn.setVisible(false);
        }

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
        languageBox.getSelectionModel().selectFirst(); // sets default value of choicebox

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        dateLabel.setText(day + "/" + (month + 1) + "/" + year);

        // Define title of employee

        seniorBtn.setOnAction(actionEvent -> {
            title = "Senior";
            seniorBtn.setStyle("-fx-border-color: #4FC3F7");
            isTypeChosen = true;
        });

        managerBtn.setOnAction(actionEvent -> {
            title = "Manager";
            managerBtn.setStyle("-fx-border-color: #4FC3F7");
            analystBtn.setStyle("-fx-border-color: transparent; -fx-opacity: 0.5");
            designerBtn.setStyle("-fx-border-color: transparent; -fx-opacity: 0.5");
            coderBtn.setStyle("-fx-border-color: transparent; -fx-opacity: 0.5");
            testerBtn.setStyle("-fx-border-color: transparent; -fx-opacity: 0.5");
            isTypeChosen = true;
        });

        analystBtn.setOnAction(actionEvent -> {
            title = "Analyst";
            analystBtn.setStyle("-fx-border-color: #4FC3F7");
            managerBtn.setStyle("-fx-border-color: transparent; -fx-opacity: 0.5");
            designerBtn.setStyle("-fx-border-color: transparent; -fx-opacity: 0.5");
            coderBtn.setStyle("-fx-border-color: transparent; -fx-opacity: 0.5");
            testerBtn.setStyle("-fx-border-color: transparent; -fx-opacity: 0.5");
            isTypeChosen = true;
        });

        designerBtn.setOnAction(actionEvent -> {
            title = "Designer";
            designerBtn.setStyle("-fx-border-color: #4FC3F7");
            managerBtn.setStyle("-fx-border-color: transparent; -fx-opacity: 0.5");
            analystBtn.setStyle("-fx-border-color: transparent; -fx-opacity: 0.5");
            coderBtn.setStyle("-fx-border-color: transparent; -fx-opacity: 0.5");
            testerBtn.setStyle("-fx-border-color: transparent; -fx-opacity: 0.5");
            isTypeChosen = true;
        });

        coderBtn.setOnAction(actionEvent -> {
            title = "Programmer";
            coderBtn.setStyle("-fx-border-color: #4FC3F7");
            managerBtn.setStyle("-fx-border-color: transparent; -fx-opacity: 0.5");
            designerBtn.setStyle("-fx-border-color: transparent; -fx-opacity: 0.5");
            analystBtn.setStyle("-fx-border-color: transparent; -fx-opacity: 0.5");
            testerBtn.setStyle("-fx-border-color: transparent; -fx-opacity: 0.5");
            isTypeChosen = true;
        });

        testerBtn.setOnAction(actionEvent -> {
            title = "Tester";
            testerBtn.setStyle("-fx-border-color: #4FC3F7");
            managerBtn.setStyle("-fx-border-color: transparent; -fx-opacity: 0.5");
            designerBtn.setStyle("-fx-border-color: transparent; -fx-opacity: 0.5");
            coderBtn.setStyle("-fx-border-color: transparent; -fx-opacity: 0.5");
            analystBtn.setStyle("-fx-border-color: transparent; -fx-opacity: 0.5");
            isTypeChosen = true;
        });

        backBtn.setOnAction(actionEvent -> {
            if (admin && !loggedUser.isLogged()) manager.viewLoginPage();
            else manager.viewDashboard(loggedUser);
        });

        signUpFinishBtn.setOnAction(actionEvent -> {

            // check whether any field is empty
            if (firstNameField.getText().isEmpty() || emailField.getText().isEmpty() ||
                    passwordField.getText().isEmpty() || phoneNoField.getText().isEmpty() ||
                    nationField.getText().isEmpty() || salaryField.getText().isEmpty())
            {
                Manager.showAlert(Alert.AlertType.ERROR, "Empty Field", "Please, do not leave any empty field.");
                return;
            }


            // check whether phone number consists only digits
            if (!phoneNoField.getText().matches("[-1-9]*"))
            {
                Manager.showAlert(Alert.AlertType.ERROR, "Invalid Phone Number", "Phone number is invalid.");
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
                Manager.showAlert(Alert.AlertType.ERROR, "No title", "Please choose employee title");
                return;
            }

            LocalDate acceptdate = LocalDate.of(year, month, day);
            LocalDate birthdate = birthDatePicker.getValue();
            String accounting = accountingBox.getValue().toString();
            String lang = languageBox.getValue().toString();

            // add user to db
            signUp(id, acceptdate, title, firstNameField.getText(), lastNameField.getText(), emailField.getText(),
                    passwordField.getText(), Long.parseLong(phoneNoField.getText()), birthdate, nationField.getText(),
                    Integer.parseInt(salaryField.getText()), accounting, lang, admin);

            if (loggedUser.isLogged()) manager.viewDashboard(loggedUser);
            else manager.viewLoginPage();
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
     * @param lang Programming language
     * @param admin Is admin of the system
     * @return True if info is in db.
     */
    private boolean signUp(int id, LocalDate acceptDate, String title, String first_name, String last_name,
                                  String email, String password, long phone, LocalDate birthDate, String nationality,
                                  int salary, String accounting, String lang, boolean admin)
    {
        Connection connection = Manager.getConnection();
        PreparedStatement preparedStatement = null;
        String sql = "INSERT INTO company (id, acceptdate, title, first_name, last_name, email, password, phone, " +
                "birthdate, nationality, salary, accounting, lang, admin) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.setDate(2, Date.valueOf(acceptDate));
            preparedStatement.setString(3, title);
            preparedStatement.setString(4, first_name);
            preparedStatement.setString(5, last_name);
            preparedStatement.setString(6, email);
            preparedStatement.setString(7, password);
            preparedStatement.setLong(8, phone);
            preparedStatement.setDate(9, Date.valueOf(birthDate));
            preparedStatement.setString(10, nationality);
            preparedStatement.setInt(11, salary);
            preparedStatement.setString(12, accounting);
            preparedStatement.setString(13, lang);
            preparedStatement.setBoolean(14, admin);
            preparedStatement.executeUpdate();
            System.out.println("New user inserted successfully.");
            Manager.showAlert(Alert.AlertType.INFORMATION, "", "Registration successful.");
            return true;
        } catch (Exception e) {
            System.out.println("Error while inserting new user.");
            Manager.showAlert(Alert.AlertType.ERROR, "Register failed", "There must be an error. Please try again later.");
            return false;
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
}
