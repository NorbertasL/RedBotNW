package tasks;

import net.dv8tion.jda.api.entities.TextChannel;

public class TCTasks {
    public static void sendMessage(TextChannel textChannel, String msg){
        System.out.println("Sending msg");
        textChannel.sendMessage(msg).queue();
    }
}
