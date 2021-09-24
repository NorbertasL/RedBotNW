package tasks;

import data.Variables;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

import java.nio.channels.Channel;

public class Helper {
    public static boolean hasRank(Member user, String...ranks){
        for(Role role:user.getRoles()){
            if(role.getName().equalsIgnoreCase(Variables.MASTER_RANK)) return true;//Master rank
            for(String r: ranks){
                if(role.getName().equalsIgnoreCase(r)) return true;
            }

        }
        return false;
    }

    public static boolean hasCategory(TextChannel channel, String[] listenCategories) {
        if(listenCategories == null){
            return true;//will always be in right category :D
        }
        if(channel.getParent() == null){
            return false;//No parent = no category so false
        }
        for(String c: listenCategories){
            if(c.equalsIgnoreCase(channel.getParent().getName())){
                return true;
            }
        }
        return false;
    }
    public static boolean channelInList(TextChannel channel, String[] channels){
        for(String c : channels){
            if(channel.getName().equalsIgnoreCase(c)){
                return true;
            }
        }
        return false;
    }
}
