package app;

import app.model.Team;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

public class TeamTest {
    public static Team tm1;
    public static Team tm2;
    @BeforeAll
    public static void setUp(){
        tm1=new Team("takim",2,3,4,5,3);
        tm2=new Team();
    }

    @Test
    void getName(){
        Assertions.assertEquals("takim",tm1.getName());
    }

    @Test
    void setName(){
        tm2.setName("takim2");
        Assertions.assertEquals("takim2",tm2.getName());
    }

    @Test
    void getManager(){
        Assertions.assertEquals(2,tm1.getManager());
    }

    @Test
    void setManager(){
        tm2.setManager(4);
        Assertions.assertEquals(4,tm2.getManager());
    }

    @Test
    void getAnalyst(){
        Assertions.assertEquals(3,tm1.getAnalyst());
    }

    @Test
    void setAnalyst(){
        tm2.setAnalyst(11);
        Assertions.assertEquals(11,tm2.getAnalyst());
    }

    @Test
    void getDesigner(){
        Assertions.assertEquals(4,tm1.getDesigner());
    }

    @Test
    void setDesigner(){
        tm2.setDesigner(8);
        Assertions.assertEquals(8,tm2.getDesigner());
    }

    @Test
    void getProgrammer(){
        Assertions.assertEquals(5,tm1.getProgrammer());
    }

    @Test
    void setProgrammer(){
        tm2.setProgrammer(9);
        Assertions.assertEquals(9,tm2.getProgrammer());
    }

    @Test
    void getTester(){
        Assertions.assertEquals(3,tm1.getTester());
    }

    @Test
    void setTester(){
        tm2.setTester(4);
        Assertions.assertEquals(4,tm2.getTester());
    }
}
