package app.controllers;

import app.Manager;
import app.classes.Employee;
import app.classes.Project;
import app.classes.Team;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.Calendar;
import javafx.application.HostServices;

public class Dashboard {

    @FXML private AnchorPane MainPane;
    @FXML private TilePane projectsPane;
    @FXML private Label todaysDate;
    @FXML private Label userLabel;
    @FXML private Button logOutBtn;
    @FXML private Button importCandidateBtn;
    @FXML private Button createProjectBtn;
    @FXML private Button formTeamBtn;
    @FXML private GridPane optionSeniors;
    @FXML private GridPane optionEmployees;
    @FXML private GridPane optionTeams;
    @FXML private Button salaryCalc1;
    @FXML private Button salaryCalc2;
    @FXML private Button compCalc1;
    @FXML private Button compCalc2;

    private ObservableList<Project> projects = FXCollections.observableArrayList();

    public void start(final Employee user) {

        Stage stage = (Stage) MainPane.getScene().getWindow();
        stage.sizeToScene();
        stage.setTitle("Dashboard");
        stage.setResizable(false);

        Scene scene = MainPane.getScene();
        scene.getStylesheets().add("app/resources/styles/style.css");
        scene.getStylesheets().add("app/resources/styles/dashboard_style.css");

        // set the today's date
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        todaysDate.setText(day + "/" + (month + 1) + "/" + year);

        userLabel.setText(user.getFullName());

        loadProjects();

        // detect mouse on projects tile on Projects tab of dashboard
        for (Node node: projectsPane.lookupAll(".project-tile"))
        {
            node.setOnMouseEntered(mouseEvent -> {
                node.setStyle("-fx-border-width: 2px; -fx-border-color: #01579B");
            });

            node.setOnMouseExited(mouseEvent ->  {
                node.setStyle("-fx-border-width: 1px; -fx-border-color: #4FC3F7");
            });

            node.setOnMouseClicked(mouseEvent -> {
                GridPane gridPane = (GridPane) mouseEvent.getSource();
                Label nameLabel = (Label) gridPane.lookup("#projectNameLabel");
                Project project = findProject(nameLabel.getText());
                Manager.popProjectInfo(project, user);
            });
        }

        logOutBtn.setOnAction(actionEvent -> {
            user.setLogged(false);
            Manager.viewLoginPage();
        });

        importCandidateBtn.setOnAction(actionEvent -> {
            Manager.viewSignUpPage(user);
        });

        createProjectBtn.setOnAction(actionEvent -> {
            Manager.viewNewProjectPane(user);
        });

        formTeamBtn.setOnAction(actionEvent -> {
            Manager.viewNewTeam(user);
        });

        // when Teams option is clicked on dahsbooard->company tab
        optionTeams.setOnMouseClicked(mouseEvent -> Manager.viewTeams(user));

        // when Seniors option is clicked on dashboard->company tab
        optionSeniors.setOnMouseClicked(mouseEvent -> Manager.viewSeniors(user));

        // when Employees option is clicked on dashboard->company tab
        optionEmployees.setOnMouseClicked(mouseEvent -> Manager.viewEmployees(user));

        // salary and compensation calculators
        salaryCalc1.setOnAction(event -> {
            try {
                Desktop.getDesktop().browse(new URI("https://www.calculator.net/salary-calculator.html"));
            } catch (URISyntaxException | IOException e) {
                e.printStackTrace();
            }
        });

        salaryCalc2.setOnAction(event -> {
            try {
                Desktop.getDesktop().browse(new URI("https://www.pcpayroll.co.uk/uk-salary-calculator/"));
            } catch (URISyntaxException | IOException e) {
                e.printStackTrace();
            }
        });

        compCalc1.setOnAction(event -> {
            try {
                Desktop.getDesktop().browse(new URI("https://www.calcxml.com/calculators/total-compensation?skn=73"));
            } catch (URISyntaxException | IOException e) {
                e.printStackTrace();
            }
        });

        compCalc2.setOnAction(event -> {
            try {
                Desktop.getDesktop().browse(new URI("https://www.cpasitesolutions.com/content/calcs/calcloader.php?calc=bus08"));
            } catch (URISyntaxException | IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Load projects from DB and display them on dashboard. Also, refreshes the page.
     */
    private void loadProjects() {

        projectsPane.getChildren().clear(); // clear the view
        projects.clear();

        try {
            Connection connection = Manager.getConnection();
            ResultSet rs = connection.createStatement().executeQuery("select * from projects");

            // store all data in observable list
            while (rs.next())
            {
                projects.add(new Project(rs.getString("pr_name"), rs.getString("lang"), new Team(rs.getString("team")),
                        rs.getDate("startDate").toLocalDate(), rs.getDate("dueDate").toLocalDate(),
                        rs.getDate("creationDate").toLocalDate(), rs.getString("description")));
            }

            // now add those projects to view
            for (Project p: projects)
            {
                // load view
                AnchorPane loader = FXMLLoader.load(getClass().getResource("/app/view/project.fxml"));
                GridPane gridPane = (GridPane) loader.getChildren().get(0);

                gridPane.getStyleClass().add("project-tile");

                // set the project image (same for all)
                Image image = new Image("/app/resources/images/icons8-cashbook-80.png", 70, 70, true, false);
                ImageView imageView = (ImageView) gridPane.getChildren().get(0);
                imageView.setImage(image);
                //centerImage(imageView);

                // set the name of project
                Label nameLabel = (Label) gridPane.getChildren().get(1);
                nameLabel.setText(p.getName());
                nameLabel.setId("projectNameLabel");

                // add these elements to the view now
                projectsPane.getChildren().add(loader);
            }

        } catch (SQLException | IOException e) {
            Manager.showAlert(Alert.AlertType.WARNING, "", "Couldn't load projects.");
        }
    }

    /**
     * Gets project record from the DB.
     * @return Project
     */
    private Project findProject(String name) {

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "select * from projects where pr_name = ?";
        Connection connection = Manager.getConnection();

        try{
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();

            if(!resultSet.next())
            {
                Manager.showAlert(Alert.AlertType.ERROR, "", "Something is not right. Project record is not found on the database.");
                return null;
            }
            else {
                Project project = new Project(resultSet.getString("pr_name"),
                        resultSet.getString("lang"),
                        resultSet.getDate("startDate").toLocalDate(),
                        resultSet.getDate("dueDate").toLocalDate(),
                        resultSet.getDate("creationDate").toLocalDate(),
                        resultSet.getString("description"));
                project.setTeam(findTeam(resultSet.getString("team")));
                return project;
            }
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Centers image inside of ImageView in FXML.
     * @author https://stackoverflow.com/questions/32674393/centering-imageview-using-javafx
     */
    private static void centerImage(ImageView imgView) {

        Image img = imgView.getImage();

        if (img != null) {
            double w = 0;
            double h = 0;

            double ratioX = imgView.getFitWidth() / img.getWidth();
            double ratioY = imgView.getFitHeight() / img.getHeight();

            double reducCoeff = 0;
            reducCoeff = Math.min(ratioX, ratioY);

            w = img.getWidth() * reducCoeff;
            h = img.getHeight() * reducCoeff;

            imgView.setX((imgView.getFitWidth() - w) / 2);
            imgView.setY((imgView.getFitHeight() - h) / 2);

        }
    }

    /**
     * Gets employee record from the DB by title.
     * @return Employee
     */
    protected static Employee findEmployeeByTitle(String name, String title) {

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "select * from company where first_name = ? and last_name = ? and title = ?";
        Connection connection = Manager.getConnection();

        String fullName = name;
        String firstName = "";
        String lastName = "";

        if(fullName.split("\\w+").length>1)
        {
            lastName = fullName.substring(fullName.lastIndexOf(" ") + 1);
            firstName = fullName.substring(0, fullName.lastIndexOf(' '));
        }
        else firstName = fullName;

        try{
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, title);

            resultSet = preparedStatement.executeQuery();

            if(!resultSet.next())
            {
                Manager.showAlert(Alert.AlertType.ERROR, "",
                        "Something is not right. Employee record is not found on the database.");
                return null;
            }
            else {
                Employee record = new Employee(
                        resultSet.getInt("id"),
                        resultSet.getDate("acceptdate").toLocalDate(),
                        resultSet.getString("title"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getLong("phone"),
                        resultSet.getDate("birthdate").toLocalDate(),
                        resultSet.getString("nationality"),
                        resultSet.getInt("salary"),
                        resultSet.getString("accounting"),
                        resultSet.getString("skills"),
                        resultSet.getBoolean("admin")
                );
                return record;
            }
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets employee record from the DB by ID.
     * @return Employee
     */
    protected static Employee findEmployeeByID(int ID) {

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "select * from company where id = ?";
        Connection connection = Manager.getConnection();

        try{
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, ID);

            resultSet = preparedStatement.executeQuery();

            if(!resultSet.next())
            {
                Manager.showAlert(Alert.AlertType.ERROR, "",
                        "Something is not right. Employee record is not found on the database.");
                return null;
            }
            else {
                Employee record = new Employee(
                        resultSet.getInt("id"),
                        resultSet.getDate("acceptdate").toLocalDate(),
                        resultSet.getString("title"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getLong("phone"),
                        resultSet.getDate("birthdate").toLocalDate(),
                        resultSet.getString("nationality"),
                        resultSet.getInt("salary"),
                        resultSet.getString("accounting"),
                        resultSet.getString("skills"),
                        resultSet.getBoolean("admin")
                );
                return record;
            }
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Utility method to get the team record from database.
     * @param teamName Name of the team to find it in database
     * @return Team that works on the project
     */
    protected static Team findTeam(String teamName) {
        try {
            Connection connection = Manager.getConnection();
            String sql = "select * from teams where t_name = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, teamName);
            ResultSet resultSet = statement.executeQuery();
            Team team;

            if (resultSet.next())
            {
                team = new Team(resultSet.getString("t_name"), resultSet.getInt("manager"),
                        resultSet.getInt("analyst"), resultSet.getInt("designer"), resultSet.getInt("programmer"),
                        resultSet.getInt("tester"));
                return team;
            }

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get employee's name from database given his/her id.
     * @param id ID of employee
     * @return Full name of the employee
     */
    protected static String getEmployeeName(int id) {
        try {
            Connection connection = Manager.getConnection();
            String sql = "select * from company where id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            result.next();
            return result.getString("first_name") + " " + result.getString("last_name");
        } catch (SQLException e) {
            System.out.println("Exception at getEmployeeName:454 : Employee name not found.");
            return null;
        }
    }

    private void updateTeam(String teamName, String newName) {

    }
}
