package app;

import app.classes.Team;
import app.controllers.*;
import app.classes.Employee;
import app.classes.Project;
import app.resources.Strings;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Manager extends Main {

    private static Scene mainScene;
    private static Scene projectInfoScene;
    private static Connection connection;
    private final static String[] ACCOUNTING_PROGRAMS = {"PC Payroll", "Calculator.Net"};
    private final static String[] PROGRAMMING_LANGS = {"C", "Java", "Python", "Go", "JavaScript", ".NET"};

    public Manager(Scene scene) {
        this.mainScene = scene;
    }

    public static void openConnection() {
        try {
            connection = DBUtils.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void DBCheckCompany() {
        String db = "use ysofthr;";
        String sql = "create table if not exists company (" +
                "    id int," +
                "    acceptdate Date," +
                "    title varchar(255)," +
                "    first_name varchar(255)," +
                "    last_name varchar(255)," +
                "    email varchar(255)," +
                "    password varchar(255)," +
                "    phone bigint," +
                "    birthdate Date," +
                "    nationality varchar(255)," +
                "    salary int," +
                "    accounting varchar(255)," +
                "    skills varchar(255)," +
                "    admin boolean" +
                ");";
        try {
            PreparedStatement statement = connection.prepareStatement(db);
            statement.execute();
            statement = connection.prepareStatement(sql);
            statement.execute();
        } catch (Exception e) {
            e.printStackTrace();
            Manager.showAlert(Alert.AlertType.ERROR, "Exception",
                    "Exception occured while creating tables. Check your MySQL files and try again.");
        }
        System.out.println("Table `company` checked.");
    }

    public void DBCheckProjects() {
        String db = "use ysofthr;";
        String sql = "create table if not exists projects (" +
                "    pr_name varchar(255)," +
                "    lang varchar(255)," +
                "    team varchar(255)," +
                "    startDate Date," +
                "    dueDate Date," +
                "    creationDate Date," +
                "    description varchar(255)" +
                ");";
        try {
            PreparedStatement statement = connection.prepareStatement(db);
            statement.execute();
            statement = connection.prepareStatement(sql);
            statement.execute();
        } catch (Exception e) {
            e.printStackTrace();
            Manager.showAlert(Alert.AlertType.ERROR, "Exception",
                    "Exception occured while creating tables. Check your MySQL files and try again.");
        }
        System.out.println("Table `projects` checked.");
    }

    public void DBCheckTeams() {
        String db = "use ysofthr;";
        String sql = "create table if not exists teams (" +
                "    t_name varchar(255) not null," +
                "    manager int references company(id)," +
                "    analyst int references company(id)," +
                "    designer int references company(id)," +
                "    programmer int references company(id)," +
                "    tester int references company(id)" +
                ");";
        try {
            // create table if not exists
            PreparedStatement statement = connection.prepareStatement(db);
            statement.execute();
            statement = connection.prepareStatement(sql);
            statement.execute();
        } catch (Exception e) {
            e.printStackTrace();
            Manager.showAlert(Alert.AlertType.ERROR, "Exception",
                    "Exception occured while creating tables. Check your MySQL files and try again.");
        }
        System.out.println("Table `teams` checked.");
    }

    public static ArrayList<String> DBGetTitles() {
        String db = "use ysofthr;";
        String sql = "select column_name from information_schema.columns where table_name = 'teams'";
        ResultSet resultSet;
        ArrayList<String> titles = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(db);
            statement.execute();
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            while (resultSet.next()) { // get the values with Capital letter
                titles.add(resultSet.getString(1).substring(0, 1).toUpperCase() + resultSet.getString(1).substring(1));
            }
            titles.remove(0); // remove first item because it is t_name
            return titles;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.initModality(Modality.APPLICATION_MODAL); // set window on top of everyone
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    public static ArrayList<String> getTeams() {
        String sql = "select * from teams";

        try {

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            ArrayList<String> teamList = new ArrayList<>();
            while (resultSet.next()) {
                teamList.add(resultSet.getString("t_name"));
            }

            return teamList;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void viewLoginPage() {
        try {
            FXMLLoader loader = new FXMLLoader(Manager.class.getResource("view/login.fxml"), Strings.GetBundle());
            mainScene.setRoot(loader.load());
            Login controller = loader.getController();
            controller.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void viewSignUpPage(Employee loggedUser) {
        try {
            FXMLLoader loader = new FXMLLoader(Manager.class.getResource("view/employee_register.fxml"), Strings.GetBundle());
            mainScene.setRoot(loader.load());
            EmployeeRegister controller = loader.getController();
            controller.start(loggedUser, ACCOUNTING_PROGRAMS, PROGRAMMING_LANGS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void viewDashboard(Employee user) {
        try {
            FXMLLoader loader = new FXMLLoader(Manager.class.getResource("view/dashboard.fxml"), Strings.GetBundle());
            mainScene.setRoot(loader.load());
            Dashboard controller = loader.getController();
            controller.start(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void viewEmployeeInfo(Employee employee, Employee user) {
        try {
            FXMLLoader loader = new FXMLLoader(Manager.class.getResource("view/employee_info.fxml"), Strings.GetBundle());
            mainScene.setRoot(loader.load());
            EmployeeInfo controller = loader.getController();
            controller.start(employee, user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void viewNewProjectPane(Employee user) {
        try {
            FXMLLoader loader = new FXMLLoader(Manager.class.getResource("view/new_project.fxml"), Strings.GetBundle());
            mainScene.setRoot(loader.load());
            NewProject controller = loader.getController();
            controller.start(PROGRAMMING_LANGS, user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void popProjectInfo(Project project, Employee user) {
        try {
            FXMLLoader loader = new FXMLLoader(Manager.class.getResource("view/project_info.fxml"), Strings.GetBundle());
            projectInfoScene = new Scene(loader.load()); // create new scene, otherwise scene will not change after
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(projectInfoScene);
            stage.show();
            ProjectInfo controller = loader.getController();
            controller.start(project, user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void viewProjectInfo(Project project, Employee user) {
        try {
            FXMLLoader loader = new FXMLLoader(Manager.class.getResource("view/project_info.fxml"), Strings.GetBundle());
            projectInfoScene.setRoot(loader.load());
            ProjectInfo controller = loader.getController();
            controller.start(project, user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void viewProjectEdit(Project project, Employee user) {
        try {
            FXMLLoader loader = new FXMLLoader(Manager.class.getResource("view/project_edit.fxml"), Strings.GetBundle());
            projectInfoScene.setRoot(loader.load());
            ProjectEdit controller = loader.getController();
            controller.start(project, PROGRAMMING_LANGS, user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void viewNewTeam(Employee user) {
        try {
            FXMLLoader loader = new FXMLLoader(Manager.class.getResource("view/new_team.fxml"), Strings.GetBundle());
            mainScene.setRoot(loader.load());
            FormTeam controller = loader.getController();
            controller.start(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void viewTeamEdit(Team team, final Employee user) {
        try {
            FXMLLoader loader = new FXMLLoader(Manager.class.getResource("view/team_edit.fxml"), Strings.GetBundle());
            mainScene.setRoot(loader.load());
            TeamEdit controller = loader.getController();
            controller.start(team, user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void viewTeams(Employee user) {
        try {
            FXMLLoader loader = new FXMLLoader(Manager.class.getResource("view/teams_list.fxml"), Strings.GetBundle());
            mainScene.setRoot(loader.load());
            TeamsList controller = loader.getController();
            controller.start(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void viewAdmins(Employee user) {
        try {
            FXMLLoader loader = new FXMLLoader(Manager.class.getResource("view/supervisors_list.fxml"), Strings.GetBundle());
            mainScene.setRoot(loader.load());
            SupervisorsList controller = loader.getController();
            controller.start(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void viewEmployees(Employee user) {
        try {
            FXMLLoader loader = new FXMLLoader(Manager.class.getResource("view/employee_list.fxml"), Strings.GetBundle());
            mainScene.setRoot(loader.load());
            EmployeeList controller = loader.getController();
            controller.start(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Return employee's id given his/her full name.
     * @param fullName First and last name
     * @return ID
     */
    public static Integer getEmployeeId(String fullName) {
        try {

            if (fullName == null) return null;

            String firstName = "";
            String lastName = "";

            if(fullName.split("\\w+").length>1)
            {
                lastName = fullName.substring(fullName.lastIndexOf(" ") + 1);
                firstName = fullName.substring(0, fullName.lastIndexOf(' '));
            }
            else firstName = fullName;

            Connection connection = Manager.getConnection();
            String sql = "select * from company where first_name = ? and last_name = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("id");
            }

            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets employee names according to title from database.
     * @param title Title; Manager, Analyst..
     * @return Employee names in ArrayList.
     */
    public static ArrayList<String> getEmployeeList(String title) {
        try {

            Connection connection = Manager.getConnection();
            String sql = "select * from company where title = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, title);
            ResultSet resultSet = statement.executeQuery();

            ArrayList<String> employees = new ArrayList<>(); // store employee names here

            while (resultSet.next()) {
                String fullName = resultSet.getString("first_name") + " " + resultSet.getString("last_name");
                employees.add(fullName);
            }

            return employees;
        } catch (SQLException e) {
            Manager.showAlert(Alert.AlertType.WARNING, "Exception", "ERR_GET_EMPLOYEES");
            e.printStackTrace();
            return null;
        }
    }

    public Scene getMainScene() {
        return mainScene;
    }

    public static Connection getConnection() {
        return connection;
    }

    public static String[] getProgrammingLangs() {
        return PROGRAMMING_LANGS;
    }
}
