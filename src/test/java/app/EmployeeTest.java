package app;

import app.model.Employee;
import app.model.Team;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import java.time.LocalDate;

public class EmployeeTest {
    public static Employee emp1;
    public static Employee emp2;
    @BeforeAll
    public static void set(){
        LocalDate accept=LocalDate.of(2020,1,1);
        LocalDate birth=LocalDate.of(1996,2,17);
        emp1 = new Employee(1,accept,"yeni çalışan","Furkan","Kayacık","aaa@gmail.com","123",537,birth,"türk",7500,"1","tester,programmer",false);
        emp2 = new Employee(true,true);
    }

    @Test
    void getId(){
        Assertions.assertEquals(1,emp1.getId());
    }

    @Test
    void setId(){
        emp2.setId(2);
        Assertions.assertEquals(2,emp2.getId());
    }

    @Test
    void getAccept(){
        Assertions.assertNotNull(emp1.getAcceptdate());
    }

    @Test
    void setAccept(){
        LocalDate accept=LocalDate.of(2020,2,2);
        emp2.setAcceptdate(accept);
        Assertions.assertEquals(accept,emp2.getAcceptdate());
    }

    @Test
    void setTitle(){
        emp2.setTitle("yeni çalışan 2");
        Assertions.assertEquals("yeni çalışan 2",emp2.getTitle());
    }

    @Test
    void getName(){
        Assertions.assertEquals("Furkan",emp1.getFirstName());
        Assertions.assertEquals("Kayacık",emp1.getLastName());
    }

    @Test
    void setName(){
        emp2.setFirstName("Ali");
        emp2.setLastName("Deneme");
        Assertions.assertEquals("Ali",emp2.getFirstName());
        Assertions.assertEquals("Deneme",emp2.getLastName());
        Assertions.assertEquals("Ali Deneme",emp2.getFullName());
    }

    @Test
    void Email(){
        emp2.setEmail("bb@hotmail.com");
        Assertions.assertEquals("aaa@gmail.com",emp1.getEmail());
        Assertions.assertEquals("bb@hotmail.com",emp2.getEmail());
    }

    @Test
    void password(){
        emp2.setPassword("asdf");
        Assertions.assertEquals("123",emp1.getPassword());
        Assertions.assertEquals("asdf",emp2.getPassword());
    }

    @Test
    void phone(){
        emp2.setPhone(9632);
        Assertions.assertEquals(537,emp1.getPhone());
        Assertions.assertEquals(9632,emp2.getPhone());
    }

    @Test
    void birth(){
        LocalDate birth=LocalDate.of(1966,3,2);
        emp2.setBirthdate(birth);
        Assertions.assertEquals(birth,emp2.getBirthdate());
        Assertions.assertNotNull(emp1.getBirthdate());
    }

    @Test
    void nationality(){
        emp2.setNationality("ingiliz");
        Assertions.assertEquals("ingiliz",emp2.getNationality());
        Assertions.assertEquals("türk",emp1.getNationality());
    }

    @Test
    void salary(){
        emp2.setSalary(8000);
        Assertions.assertEquals(8000,emp2.getSalary());
        Assertions.assertEquals(7500,emp1.getSalary());
    }

    @Test
    void Accounting(){
        emp2.setAccounting("2");
        Assertions.assertEquals("2",emp2.getAccounting());
        Assertions.assertEquals("1",emp1.getAccounting());
    }

    @Test
    void skill(){
        emp2.setSkill("manager");
        Assertions.assertEquals("manager",emp2.getSkill());
        Assertions.assertEquals("tester,programmer",emp1.getSkill());
    }

    @Test
    void admin(){
        Assertions.assertTrue(emp2.isAdmin());
        Assertions.assertFalse(emp1.isAdmin());
    }

    @Test
    void logged(){
        emp1.setLogged(false);
        Assertions.assertFalse(emp1.isLogged());
        Assertions.assertTrue(emp2.isLogged());
    }


}
