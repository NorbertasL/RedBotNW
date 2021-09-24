package data;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Variables {
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
        return GlobalConstants.PRIVATE_VC_CATEGORY.getName();
    }
    public String getPrivateChannelName(){
        return GlobalConstants.PRIVATE_VC_NAME.getName();
    }
    public String getDynamicVcCategoryName() {
        return GlobalConstants.DYNAMIC_VC_CATEGORY.getName();
    }
    public String getBotChannelName() {
        return GlobalConstants.BOT_CHANNEL_NAME.getName();
    }
    public String getEventCategoryName() {
        return GlobalConstants.EVENT_CATEGORY.getName();
    }

    private List<Event> eventList = new ArrayList<>();
    public void addEvent(String id,Member caller, String[] cmds) {
            eventList.add(new Event(id, caller, cmds));
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
}
