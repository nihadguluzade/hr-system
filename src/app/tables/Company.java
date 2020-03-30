package app.tables;

import java.time.LocalDate;

public class Company {

    private int id; // Randomly generated id.
    private LocalDate acceptdate; // Date when worker is accepted to company.
    private String title; // Job; Manager, Analyst, Designer, etc.
    private String first_name; // First name
    private String last_name; // Last name
    private String email; // Personal email
    private String password; // Password to log in
    private long phone; // Personal phone number
    private LocalDate birthdate; // Date of birth
    private String nationality; // Nationality from birth
    private int salary; // Initial salary
    private String accounting; // Accounting program
    private String lang; // Programming language
    private boolean admin; // Is admin of the system
    private boolean logged = false; // Is logged to the system

    public Company(int id, String title, String first_name, String last_name, String email, String password, long phone,
                   String nationality, int salary, String accounting, String lang, boolean admin) {
        this.id = id;
        this.title = title;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.nationality = nationality;
        this.salary = salary;
        this.accounting = accounting;
        this.lang = lang;
        this.admin = admin;
    }

    public Company(boolean admin, boolean logged) {
        this.admin = admin;
        this.logged = logged;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getAccounting() {
        return accounting;
    }

    public void setAccounting(String accounting) {
        this.accounting = accounting;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isLogged() {
        return logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    public String getFullName() {
        return first_name + " " + last_name;
    }
}
