package app;

import app.util.Strings;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.time.LocalDate;

public class MainTests {

    /* Also includes DBUtils and Manager class tests */

    private static Main main;
    private static Manager manager;

    @BeforeAll
    public static void set() {
        main = new Main();
    }





}
