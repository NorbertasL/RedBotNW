package tasks;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.exceptions.ContextException;

public class TCTasks {
    public static void sendMessage(TextChannel textChannel, String msg){
        try{
            System.out.println("Sending msg:"+msg);
            textChannel.sendMessage(msg).queue();
        }catch (Exception e){
            System.out.println("sendMessage error:"+e);
        }

    }
}
