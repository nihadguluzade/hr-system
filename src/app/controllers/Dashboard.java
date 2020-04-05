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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

public class Dashboard {

    @FXML private AnchorPane MainPane;
    @FXML private TilePane projectsPane;
    @FXML private TilePane teamsTilePane;
    @FXML private Label todaysDate;
    @FXML private Label userLabel;
    @FXML private Button logOutBtn;
    @FXML private Button importCandidateBtn;
    @FXML private Button createProjectBtn;
    @FXML private Button formTeamBtn;

    private ObservableList<Project> projects = FXCollections.observableArrayList();
    private ObservableList<Team> teams = FXCollections.observableArrayList();


    public void start(final Manager manager, final Employee user) {

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
        loadTeams();

        for (Node node: projectsPane.lookupAll(".project-tile"))
        {
            node.setOnMouseEntered(mouseEvent -> {
                node.setStyle("-fx-border-width: 2px; -fx-border-color: #01579B");
            });

            node.setOnMouseExited(mouseEvent ->  {
                node.setStyle("-fx-border-width: 1px; -fx-border-color: #4FC3F7");
            });

            node.setOnMouseClicked(mouseEvent ->
            {
                GridPane gridPane = (GridPane) mouseEvent.getSource();
                Label nameLabel = (Label) gridPane.lookup("#projectNameLabel");
                Project project = findProject(nameLabel.getText());
                manager.popProjectInfo(project, user);
            });
        }



        logOutBtn.setOnAction(actionEvent -> {
            user.setLogged(false);
            manager.viewLoginPage();
        });

        importCandidateBtn.setOnAction(actionEvent -> {
            manager.viewSignUpPage(user);
        });

        createProjectBtn.setOnAction(actionEvent -> {
            manager.viewNewProjectPane(user);
        });

        formTeamBtn.setOnAction(actionEvent -> {
            manager.viewNewTeam(user);
        });
    }

    /**
     * Load projects from DB and display them on dashboard.
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
     * Load projects from DB and display them on dashboard.
     */
    private void loadTeams() {

        teamsTilePane.getChildren().clear(); // clear the view
        teams.clear();

        try {
            Connection connection = Manager.getConnection();
            ResultSet rs = connection.createStatement().executeQuery("select * from teams");

            // store all data in observable list
            while (rs.next())
            {
                teams.add(new Team(rs.getString("t_name"), rs.getInt("manager"), rs.getInt("analyst"),
                        rs.getInt("designer"), rs.getInt("programmer"), rs.getInt("tester")));
            }

            // now add those teams to view
            for (Team t: teams)
            {
                AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/app/view/team_tile.fxml"));
                anchorPane.getStyleClass().add("teams-parent-tile");

                Label teamName = (Label) anchorPane.getChildren().get(0);
                teamName.setText(t.getName());

                TilePane teamsParentTile = (TilePane) anchorPane.getChildren().get(1);

                // create tile for titles; manager, analyst...
                if (t.getManager() != 0) { // check if there is no person with this title
                    AnchorPane managerTile = FXMLLoader.load(getClass().getResource("/app/view/employee.fxml"));
                    managerTile.getStyleClass().add("teams-indv-tile");
                    Label name = (Label) managerTile.getChildren().get(0);
                    name.setText(getEmployeeName(t.getManager()));
                    Label title = (Label) managerTile.getChildren().get(1);
                    title.setText("Manager");
                    teamsParentTile.getChildren().add(managerTile);
                }

                if (t.getAnalyst() != 0) {
                    AnchorPane analystTile = FXMLLoader.load(getClass().getResource("/app/view/employee.fxml"));
                    analystTile.getStyleClass().add("teams-indv-tile");
                    Label name = (Label) analystTile.getChildren().get(0);
                    name.setText(getEmployeeName(t.getAnalyst()));
                    Label title = (Label) analystTile.getChildren().get(1);
                    title.setText("Analyst");
                    teamsParentTile.getChildren().add(analystTile);
                }

                if (t.getDesigner() != 0) {
                    AnchorPane designerTile = FXMLLoader.load(getClass().getResource("/app/view/employee.fxml"));
                    designerTile.getStyleClass().add("teams-indv-tile");
                    Label name = (Label) designerTile.getChildren().get(0);
                    name.setText(getEmployeeName(t.getDesigner()));
                    Label title = (Label) designerTile.getChildren().get(1);
                    title.setText("Designer");
                    teamsParentTile.getChildren().add(designerTile);
                }

                if (t.getProgrammer() != 0) {
                    AnchorPane programmerTile = FXMLLoader.load(getClass().getResource("/app/view/employee.fxml"));
                    programmerTile.getStyleClass().add("teams-indv-tile");
                    Label name = (Label) programmerTile.getChildren().get(0);
                    name.setText(getEmployeeName(t.getProgrammer()));
                    Label title = (Label) programmerTile.getChildren().get(1);
                    title.setText("Programmer");
                    teamsParentTile.getChildren().add(programmerTile);
                }

                if (t.getTester() != 0) {
                    AnchorPane testerTile = FXMLLoader.load(getClass().getResource("/app/view/employee.fxml"));
                    testerTile.getStyleClass().add("teams-indv-tile");
                    Label name = (Label) testerTile.getChildren().get(0);
                    name.setText(getEmployeeName(t.getTester()));
                    Label title = (Label) testerTile.getChildren().get(1);
                    title.setText("Tester");
                    teamsParentTile.getChildren().add(testerTile);
                }

                // add parent pane to the dashboard
                teamsTilePane.getChildren().add(anchorPane);
            }

        } catch (SQLException | IOException e) {
            Manager.showAlert(Alert.AlertType.WARNING, "", "Couldn't load teams.");
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

            Team team = null;

            if (resultSet.next())
            {
               team = new Team(resultSet.getString("t_name"), resultSet.getInt("manager"),
                       resultSet.getInt("analyst"), resultSet.getInt("designer"), resultSet.getInt("programmer"),
                       resultSet.getInt("tester"));
            }

            return team;

        } catch (Exception e) {
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
            e.printStackTrace();
            return null;
        }
    }
}
