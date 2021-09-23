package tasks;

import data.Variables;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;

import java.nio.channels.Channel;

public class EventTasks {
    private static String role = "Event Manager";
    public static void MakeNewEvent(Member caller, TextChannel channel, String commandInfo){
        if(!Helper.hasRank(caller,role)){
            TCTasks.sendMessage(channel, String.format("Sorry, you need \"%s\" role to make new events", role));
            return;
        }
        String [] cmds = commandInfo.split("[|]");
        //System.out.println(cmds[0]);
        Guild guild = channel.getGuild();
        Variables variables = Variables.getVariables(guild);
        variables.addEvent(caller, cmds);
        guild.createTextChannel(cmds[0], guild.getCategories()
                .stream()
                .filter(x -> x.getName().equalsIgnoreCase(variables.getEventCategoryName()))
                .findFirst().get()).queue();
    }

    public static void CloseEvent(Member caller, TextChannel channel, String commandInfo) {
        //TODO Handle event closing method
    }
}
