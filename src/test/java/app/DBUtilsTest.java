package app;

import app.model.Team;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import app.Manager;

public class DBUtilsTest {
    public static DBUtils db;
    @BeforeAll
    public static void set(){
        db = new DBUtils();
    }
    @Test
    void creation() {
       Manager.openConnection();

    }
}
