package app;


import app.model.Employee;
import app.model.Project;
import app.model.Team;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import app.Main;
import javafx.stage.Stage;


import java.time.LocalDate;

public class MainTests {

    public static Main main;
    public static Stage stage;

    @BeforeAll
    public static void set(){
        main=new Main();
    }

    @Test
    void exampleTest() {
        main.start(stage);
    }

}
