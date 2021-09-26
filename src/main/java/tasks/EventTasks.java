package tasks;

import commands.NewEventCommand;
import data.Emoji;
import data.Event;
import data.GlobalConstants;
import data.Variables;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Stream;

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
        //TODO handle event closing and archiving
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
            TCTasks.sendMessage(channel, "Well you fucked something up!");
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
            if(temp.length < 2){
                System.out.println("null value cmd for :"+temp[0]);
                commandValues.put(temp[0].toLowerCase(), "null");
            }else {
                commandValues.put(temp[0].toLowerCase(), temp[1]);
            }

        }

        //Going tough all the keys
        //Title if different form channel name
        if(commandValues.containsKey(NewEventCommand.NewEventVars.TITLE.getVarName())){
            eb.setTitle(commandValues.get(NewEventCommand.NewEventVars.TITLE.getVarName()));
        }

        //Event start
        eb.addField("Event Starts", commandValues.getOrDefault(NewEventCommand.NewEventVars.START.getVarName(), "NOW!"), true);

        //Event end or duration //TODO add time formatting with snowflake and unixtime
        if(commandValues.containsKey(NewEventCommand.NewEventVars.END.getVarName())){
            eb.addField("Event Ends", commandValues.get(NewEventCommand.NewEventVars.END.getVarName()), true);
        }else if(commandValues.containsKey(NewEventCommand.NewEventVars.DURATION)){
            eb.addField("Event Ends", commandValues.get(NewEventCommand.NewEventVars.DURATION.getVarName()), true);
        }else {
            eb.addBlankField(true);//Blank field to fill in gap
        }

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

        //Using footers to store ids for the event
        String id = "ID:"+(long)Math.floor(Math.random()*(10000000-100000+1)+10000000);
        eb.setFooter(id);

        //Attendance reactions
        String [] attEmoji = new String[0];
        if (!commandValues.containsKey(NewEventCommand.NewEventVars.NO_ATTENDANCE.getVarName())){
            attEmoji = new String[]{Emoji.THUMBS_UP.getUnicode(), Emoji.THUMBS_DOWN.getUnicode()};
        }

        //Custom reactions
        String [] customEmoji = new String[0];;
        if (commandValues.containsKey(NewEventCommand.NewEventVars.CUSTOM_REACTIONS.getVarName())){
            String emojiString = commandValues.get(NewEventCommand.NewEventVars.CUSTOM_REACTIONS.getVarName());
            if(emojiString != null){
                customEmoji = emojiString.split(" ");//Reaction are separated by spaces
            }

        }

        //Combining all reacts into one
        Variables.getVariables(channel.getGuild()).addReact(id,
                Stream.concat(Arrays.stream(attEmoji), Arrays.stream(customEmoji)).toArray(String[]::new));
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
            variables.addReact(id, Emoji.THUMBS_UP.getUnicode());
            channel.sendMessage(s).queue();
        }
    }
}