package tasks;

import commands.NewEventCommand;
import commands.base.AbstractCommand;
import data.Emoji;
import data.Event;
import data.GlobalConstants;
import data.Variables;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.dv8tion.jda.api.requests.restaction.order.OrderAction;

import java.awt.*;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class EventTasks {

    public static void makeNewEvent(Message eventMessage, String commandInfo){
        String [] cmds = commandInfo.split("[|]");
        Guild guild = eventMessage.getGuild();
        Variables variables = Variables.getVariables(guild);
        String id = variables.getUniqueEventId();
        variables.addEvent(id, eventMessage.getMember(), cmds);

        guild.createTextChannel(id, guild.getCategories()
                .stream()
                .filter(x -> x.getName().equalsIgnoreCase(GlobalConstants.EVENT_CATEGORY[0]))
                .findFirst().get()).queue();

        System.out.println("New Event queued with id:"+id+ " by "+eventMessage.getMember().getEffectiveName());
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

    public static void createEventBody(Event storedEvent, TextChannel channel){

        String [] cmd = storedEvent.getCmds();
        EmbedBuilder eb = new EmbedBuilder();
        if(cmd == null || cmd.length < 1 || cmd[0].isBlank()){
            TCTasks.sendMessage(channel, "Well you fucked something up.Type !eventhelp in this channel");
            return;
        }

        //Channel Name and title are usually the same
        eb.setTitle(cmd[0]);
        channel.getManager().setName(cmd[0]).queue();

        //looping though the cmd and extracting values.
        String [] temp;
        HashMap<String, String> commandValues = new HashMap<>();
        for (int i = 1; i < cmd.length; i++){
            temp = cmd[i].split(":", 2);
            commandValues.put(temp[0].toLowerCase(), temp[1]);
        }

        //Going tough all the keys
        //Title if different form channel name
        if(commandValues.containsKey(NewEventCommand.NewEventVars.TITLE.getVarName())){
            eb.setTitle(commandValues.get(NewEventCommand.NewEventVars.TITLE.getVarName()));
        }

        //Event start
        eb.addField("Event Starts", commandValues.getOrDefault(NewEventCommand.NewEventVars.START.getVarName(), "NOW!"), true);

        //Event end or duration
        if(commandValues.containsKey(NewEventCommand.NewEventVars.END.getVarName())){
            eb.addField("Event Ends", commandValues.get(NewEventCommand.NewEventVars.END.getVarName()), true);
        }else
            eb.addField("Event Ends", commandValues.getOrDefault(NewEventCommand.NewEventVars.DURATION.getVarName(), "???"), true);

        //Event host
        eb.addField("Hosted By", storedEvent.getAuthor().getEffectiveName(), true);

        //Event Information
        if(commandValues.containsKey(NewEventCommand.NewEventVars.INFO.getVarName())){
            eb.addField("Event Information", commandValues.get(NewEventCommand.NewEventVars.INFO.getVarName()), false);
        }

        //Event image
        if(commandValues.containsKey(NewEventCommand.NewEventVars.IMG.getVarName())){
            eb.setImage(commandValues.get(NewEventCommand.NewEventVars.IMG.getVarName()));
        }

        //Event footer
        if (commandValues.containsKey(NewEventCommand.NewEventVars.FOOTER.getVarName())){
            eb.setFooter(commandValues.get(NewEventCommand.NewEventVars.FOOTER.getVarName()));
        }

        //Attendance reactions
        if (commandValues.containsKey(NewEventCommand.NewEventVars.ATTENDANCE)){
            //TODO disable attendance check
        }else {
            //TODO enable attendance check by default.
        }

        //Custom reactions
        if (commandValues.containsKey(NewEventCommand.NewEventVars.CUSTOM_REACT)){
            //TODO add the custom reaction
        }

        channel.sendMessageEmbeds(eb.build()).queue();

    }
    public static void generatePoll(String text, TextChannel channel) {
        String [] cmd = text.split("\n");
        System.out.println(text);
        System.out.println(cmd.length);
        Variables variables = Variables.getVariables(channel.getGuild());

        String id;
        for(String s : cmd){
            id = s.length() < 10 ? s : s.substring(0, 10);
            variables.addReact(id, Emoji.THUMBS_UP);
            channel.sendMessage(s).queue();
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