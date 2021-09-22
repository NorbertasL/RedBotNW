package tasks;

import data.Variables;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.nio.channels.Channel;

public class EventTasks {
    private static String role = "Event Manager";
    public static void MakeNewEvent(Member caller, TextChannel channel, String commandInfo){
        if(!Helper.hasRank(caller,role)){
            TCTasks.sendMessage(channel, "Sorry, you need \"{role}\" role to make new events");
            return;
        }
        Guild guild = channel.getGuild();
        Variables variables = Variables.getVariables(guild);
        guild.createTextChannel("TEST", guild.getCategories()
                .stream()
                .filter(x -> x.getName().equalsIgnoreCase(variables.getEventCategoryName()))
                .findFirst().get()).queue();
    }

    public static void CloseEvent(Member caller, TextChannel channel, String commandInfo) {
        //TODO Handle event closing method
    }
}
