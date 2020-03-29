package app.tables;

/**
 * Sends all the user information to database.
 * id Randomly generated id.
 * @param acceptDate Date when worker is accepted to company.
 * @param title Job; Manager, Analyst, Designer, etc.
 * @param first_name First name
 * @param last_name Last name
 * @param email Personal email
 * @param password Password to log in
 * @param phone Personal phone number
 * @param birthDate Date of birth
 * @param nationality Nationality from birth
 * @param salary Initial salary
 * @param accounting Accounting program
 * @param lang Programming language
 * @param admin Is admin of the system
 * @return True if info is in db.
 */
public class Company {

    private int id;
//    private Date acceptdate;
    private String title; // job title ex: analyst
    private String first_name;
    private String last_name;
    private String email;
    private String password;
    private long phone;
//    private Date birthdate;
    private String nationality;
    private int salary;
    private String accounting;
    private String lang;
    private boolean admin;
    private boolean isLogged = false;

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

    public Company() {}

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
        return isLogged;
    }

    public void setLogged(boolean logged) {
        isLogged = logged;
    }
}