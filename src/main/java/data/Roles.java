package data;

public enum Roles {
    GOD("Merlin"),
    EVENT_MANAGER("Event Manager");

    private String name;
    Roles(String name){
        this.name = name;
    }
    public String getName(){return name;}
}
