import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

//Using this until I set up a DB and make these more dynamic.

public enum GlobalConstants {
    EVENT_CATEGORY("Events"),
    PRIVATE_VC_CATEGORY("Private Voice"),
    PRIVATE_VC_NAME("Create VC"),
    DYNAMIC_VC_CATEGORY("Voice");

    private final String name;
    GlobalConstants(String name){
        this.name = name;
    }



}
