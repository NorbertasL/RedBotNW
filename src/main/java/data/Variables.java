package data;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.util.*;

public class Variables {


    //HashMap<String, HashMap<CommandKeys, String[]>> commandVars = new HashMap<>();


    public static final String MASTER_RANK = "Merlin";
    private static List<Variables> variables = new ArrayList<>();
    private String guildID;
    public String getGuildID(){
        return this.guildID;
    }
    public Variables(Guild guild){
        this.guildID = guild.getId();
        System.out.println("Making new guild variables class for:"+guild.getName());
        System.out.println("ID is:"+this.guildID);
        variables.add(this);
        System.out.println("New settings.Variables list is:");
        variables.stream().forEach(i -> System.out.println(i.getGuildID()));

    }
    public static Variables getVariables(Guild guild){
        for(Variables v: variables){
            if(v.getGuildID().equalsIgnoreCase(guild.getId())){
                System.out.println("Pre-Existing Variables found for:"+v.guildID);
                return v;
            }
        }
        System.out.println("Guild settings.Variables could not be found");
        Variables v = new Variables(guild);
        return v;
    }


    public String getPrivateVcCategoryName(){
        return GlobalConstants.PRIVATE_VC_CATEGORY;
    }
    public String getPrivateChannelName(){
        return GlobalConstants.PRIVATE_VC_NAME;
    }
    public String getDynamicVcCategoryName() {
        return GlobalConstants.DYNAMIC_VC_CATEGORY;
    }

    private List<Event> eventList = new ArrayList<>();
    public void addEvent(String id,Member caller, String[] cmds) {
            eventList.add(new Event(id, caller, cmds));
    }
    public Event getEventWithId(String id){
        for (int i =0; i<eventList.size(); i++){
            if(eventList.get(i).getId().equalsIgnoreCase(id)){
                Event event = eventList.get(i);
                eventList.remove(i);
                return event;
            }
        }
        return null;
    }
    public String getUniqueEventId() {
        String id= "";
        Random rand = new Random();
        while (id.isEmpty()){
            id = String.valueOf(rand.nextInt(100000));
            System.out.println("Event id is:"+id);
            for (Event e: eventList){
                if(id.equalsIgnoreCase(e.getId())){
                    System.out.println("Generates same id 0_0, what bad luck!");
                    id = "";
                    continue;
                }
            }
        }
        System.out.println("Returning event id:"+id);
        return id;
    }

    private HashMap<String, String[]> reactions = new HashMap<>();
    public void addReact(String id, String... emoji){
        id = id.trim();
        System.out.println("NEW REACTION:"+id);
        reactions.put(id, emoji);
    }
    public String[] getReactionsFor(String id){
        id = id.trim();
        System.out.println("Getting reqctions for mes id:"+id);
        String [] emojis = reactions.get(id);
        reactions.remove(id);
        //System.out.println("emojis are:"+emojis.toString());
        return emojis;
    }

    /**
    public HashMap<CommandKeys, String[]> getCommandVarsFor(String command) {
        if(commandVars.get(command.toLowerCase()) == null){
            System.out.println("Generating null vars for command:"+ command);
            commandVars.put(command.toLowerCase(), createDefaultVar());
        }
        return commandVars.get(command.toLowerCase());
    }

    private HashMap<CommandKeys, String[]> createDefaultVar() {
        HashMap<CommandKeys, String[]> values = new HashMap<>();
        for(CommandKeys key : CommandKeys.values()){
            values.put(key, null);
        }
        return values;
    }
     **/
}
