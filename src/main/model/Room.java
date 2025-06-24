package model;

public class Room {
    private String id;
    private String name;
    private Integer capacity;

    // Constructeur
    public Room(String id, String name, Integer capacity) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
    }

    // Getter pour id
    public String getId() {
        return id;
    }

    // Setter pour id
    public void setId(String id) {
        this.id = id;
    }

    // Getter pour name
    public String getName() {
        return name;
    }

    // Setter pour name
    public void setName(String name) {
        this.name = name;
    }

    // Getter pour capacity
    public Integer getCapacity() {
        return capacity;
    }

    // Setter pour capacity
    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
}
