package tasks;

import data.Variables;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

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
}
