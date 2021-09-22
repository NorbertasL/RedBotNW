package data;

import net.dv8tion.jda.api.entities.Guild;

import java.util.ArrayList;
import java.util.List;

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
}
