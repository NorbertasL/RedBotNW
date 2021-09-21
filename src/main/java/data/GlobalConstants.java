package data;

//Using this until I set up a DB and make these more dynamic.

public enum GlobalConstants {
    EVENT_CATEGORY("Events"),
    PRIVATE_VC_CATEGORY("Temp VC"),
    PRIVATE_VC_NAME("Create VC"),
    DYNAMIC_VC_CATEGORY("Voice"),
    BOT_CHANNEL_NAME("bot");

    private final String name;
    GlobalConstants(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }



}
