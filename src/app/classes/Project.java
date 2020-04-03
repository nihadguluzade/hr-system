package app.classes;

import java.time.LocalDate;

public class Project {

    private String name;
    private String language;
    private Team team;
    private LocalDate startDate;
    private LocalDate dueDate;
    private LocalDate creationDate;
    private String description;

    public Project(String name, String language, Team team, LocalDate startDate, LocalDate dueDate, LocalDate creationDate) {
        this.name = name;
        this.language = language;
        this.team = team;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.creationDate = creationDate;
    }

    public Project(String name, String language, Team team, LocalDate startDate, LocalDate dueDate, LocalDate creationDate, String description) {
        this.name = name;
        this.language = language;
        this.team = team;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.creationDate = creationDate;
        this.description = description;
    }

    public Project(String name, String language, LocalDate startDate, LocalDate dueDate, LocalDate creationDate, String description) {
        this.name = name;
        this.language = language;
        this.team = new Team();
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.creationDate = creationDate;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
