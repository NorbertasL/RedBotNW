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


    }
    public static void generatePoll(String text, TextChannel channel) {
        String [] cmd = text.split("\n");
        System.out.println(text);
        System.out.println(cmd.length);
        for(String s : cmd){
            channel.sendMessage("--------------\n"+s).queue();
        }
    }
}
/**
 EmbedBuilder eb = new EmbedBuilder();
 eb.setTitle("**GUILD NAME POLL**");
 eb.setColor(Color.RED);
 eb.addField("Event Closes","<t:1632664800:F>", true);
 eb.addField("Event Host","Red_Spark", true);
 eb.addBlankField(false);
 eb.addField("**Event Info**","**Look here you filthy casuals this is how it will work:** " +
 "\n1: You can thumb up all the names you like." +
 "\n2: You can thumb down all the names you dislike." +
 "\n3: :thumbup: = +1 and :thumbdown: = -1 to the name. " +
 "\n4: Top 5 guild names with the highest score go to the finals." , false);
 eb.addBlankField(false);
 eb.addField("NOTE", "No typing in this channel. Also if I missed a name, TOO FUCKING BAD <_<.", false);

 eb.setFooter("Don't make me hurt you 0_o");
 eb.setImage("http://memecrunch.com/meme/C3U9V/dont-fuck-this-up-this-time/image.jpg");

 MessageEmbed messageEmbed = eb.build();
 channel.sendMessageEmbeds(messageEmbed).queue();
 **/