package tasks;

import data.Variables;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class EventTasks {
    private static String role = "Event Manager";
    public static void makeNewEvent(Message eventMessage, String commandInfo){
        String callerName = eventMessage.getMember().getEffectiveName();
        if(!Helper.hasRank(eventMessage.getMember(),role)){
            System.out.println();
            TCTasks.sendMessage(eventMessage.getTextChannel(), String.format("Sorry %s, you need \"%s\" role to make new events", callerName, role));
            return;
        }
        String [] cmds = commandInfo.split("[|]");
        //System.out.println(cmds[0]);
        Guild guild = eventMessage.getGuild();
        Variables variables = Variables.getVariables(guild);
        String id = variables.getUniqueEventId();
        variables.addEvent(id, eventMessage.getMember(), cmds);
        guild.createTextChannel(id, guild.getCategories()
                .stream()
                .filter(x -> x.getName().equalsIgnoreCase(variables.getEventCategoryName()))
                .findFirst().get()).queue();
        System.out.println("New Event queued with id:"+id+ " by "+callerName);
    }

    public static void closeEvent(Member caller, TextChannel channel, String commandInfo) {
        //TODO handle event close
    }
    public static void deleteEvent(Member caller, TextChannel channel, String commandInfo) {
        Variables variables = Variables.getVariables(channel.getGuild());
        if(channel.getParent() == null || !channel.getParent().getName().equalsIgnoreCase(variables.getEventCategoryName())){
            return;
        }
        String callerName = caller.getEffectiveName();
        if(!Helper.hasRank(caller,role)){
            System.out.println();
            TCTasks.sendMessage(channel, String.format("Sorry %s, you need \"%s\" role to delete events", callerName, role));
            return;
        }
        System.out.println("Deleting channel:"+channel.getName()+" as requested by:"+callerName);
        channel.delete().queue();


    }
}
