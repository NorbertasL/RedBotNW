import net.dv8tion.jda.api.entities.Guild;

import java.util.ArrayList;
import java.util.List;

public class Variables {
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
        System.out.println("New Variables list is:");
        variables.stream().forEach(i -> System.out.println(i.getGuildID()));
    }
    public Variables getVariables(Guild guild){
        for(Variables v: variables){
            if(v.getGuildID().equalsIgnoreCase(guild.getId())){
                return v;
            }
        }
        System.out.println("Guild Variables could not be found");
        Variables v = new Variables(guild);
        return v;
    }
}
