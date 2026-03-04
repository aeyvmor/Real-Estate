package fiestaSystem.model;

public abstract class User {

    protected String id;
    protected String name;
    protected String role;

    public User(String id, String name, String role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    public abstract void login();

    // Getters
    public String getId()   { return id; }
    public String getName() { return name; }
    public String getRole() { return role; }
}