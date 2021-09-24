package tasks;

import data.Event;
import data.GlobalConstants;
import data.Variables;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

import java.awt.*;

public class EventTasks {

    public static void makeNewEvent(Message eventMessage, String commandInfo){
        String callerName = eventMessage.getMember().getEffectiveName();
        String [] cmds = commandInfo.split("[|]");
        //System.out.println(cmds[0]);
        Guild guild = eventMessage.getGuild();
        Variables variables = Variables.getVariables(guild);
        String id = variables.getUniqueEventId();
        variables.addEvent(id, eventMessage.getMember(), cmds);
        guild.createTextChannel(id, guild.getCategories()
                .stream()
                .filter(x -> x.getName().equalsIgnoreCase(GlobalConstants.EVENT_CATEGORY[0]))
                .findFirst().get()).queue();
        System.out.println("New Event queued with id:"+id+ " by "+callerName);
    }

    public static void closeEvent(Member caller, TextChannel channel, String commandInfo) {



        //TODO handle event archiving
    }
    public static void deleteEvent(Message eventMessage, String commandInfo) {
        String callerName = eventMessage.getMember().getEffectiveName();

        TextChannel channel = eventMessage.getTextChannel();

        System.out.println("Deleting channel:"+channel.getName()+" as requested by:"+callerName);
        channel.delete().queue();


    }

    public static void createEventBody(Event storedEvent, TextChannel channel) {
        String [] cmd = storedEvent.getCmds();
        if(cmd == null || cmd.length < 1 || cmd[0].isBlank()){
            TCTasks.sendMessage(channel, "Well you fucked something up.Type !eventhelp in this channel");
            return;
        }
        channel.getManager().setName(cmd[0]).queue();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setAuthor("RED");
        eb.setColor(Color.CYAN);
        eb.setTitle("BLABLA");
        MessageEmbed messageEmbed = eb.build();
        channel.sendMessageEmbeds(messageEmbed).queue();




    }
}
