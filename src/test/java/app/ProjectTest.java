package app;

import app.model.Employee;
import app.model.Project;
import app.model.Team;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;

public class ProjectTest{
    public static Project pr;
    @BeforeAll
    public static void set(){
        LocalDate creation=LocalDate.of(2020,1,30);
        LocalDate start=LocalDate.of(2020,2,20);
        LocalDate due=LocalDate.now();
        pr = new Project("proje1","türkçe",start,due,creation,"yazilimkalite");
    }

    @Test
    void getName(){
        Assertions.assertEquals("proje1",pr.getName());
    }

    @Test
    void setName(){
        pr.setName("proje2");
        Assertions.assertEquals("proje2",pr.getName());
    }


    @Test
    void setLanguage(){
        pr.setLanguage("ingilizce");
        Assertions.assertEquals("ingilizce",pr.getLanguage());
    }

    @Test
    void getTeam(){
        Assertions.assertNotNull(pr.getTeam());
    }

    @Test
    void setTeam(){
        Team tm=new Team("takim",2,3,4,5,3);
        pr.setTeam(tm);
        Assertions.assertEquals(tm,pr.getTeam());
    }

    @Test
    void getStart(){
        Assertions.assertNotNull(pr.getStartDate());
    }

    @Test
    void setDest(){
        pr.setDescription("açıklama");
        Assertions.assertEquals("açıklama",pr.getDescription());
    }
}
