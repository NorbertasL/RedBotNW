package managers;

import commands.DeleteEventCommand;
import commands.NewEventCommand;
import data.Event;
import data.GlobalConstants;
import data.Variables;
import interfaces.Command;
import interfaces.CommandErrors;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import tasks.EventTasks;
import tasks.Helper;
import tasks.TCTasks;

import java.util.ArrayList;
import java.util.List;


public class MsgCommandManager extends ListenerAdapter {

    List<Command> commands = new ArrayList<>();

    public MsgCommandManager(){
        super();
        //Add in all commands
        commands.add(new NewEventCommand());
        commands.add(new DeleteEventCommand());

    }
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        //Ignoring bots won input
        if (event.getAuthor().isBot()) {
            return;
        }
        String msg = event.getMessage().getContentRaw();
        if (msg.charAt(0) != '!') {
            System.out.println("Not a command");
            return;
        }
        msg = msg.substring(1);
        //Write all command as lowercase, no spaces allowed
        String callerCmd = msg.split(" ")[0].toLowerCase();
        String vars = msg.substring(callerCmd.length());
        for (Command command: commands){
            if (command.getCommand().equalsIgnoreCase(callerCmd)){
                CommandErrors response = command.runCommand(event.getMessage(), vars);
                if(response == CommandErrors.OK){
                    event.getMessage().addReaction("U+1F44D").queue();//Thumbs up
                }else{
                    event.getMessage().addReaction("U+1F44E").queue();//Thumbs down
                    //event.getMessage().addReaction("U+2139").queue();//information
                }
                return;
            }
        }
        TCTasks.sendMessage(event.getTextChannel(), "Could not find command:"+callerCmd);

    }

    @Override
    public void onTextChannelCreate(TextChannelCreateEvent event){
        //Only care about channels under Event category
        if(Helper.hasCategory(event.getChannel(), GlobalConstants.EVENT_CATEGORY)){
            Variables variables = Variables.getVariables(event.getGuild());
            String channelID = event.getChannel().getName();
            Event storedEvent = variables.getEventWithId(channelID);
            if(storedEvent == null){
                System.out.println("Could not find event with id:"+channelID);
                return;
            }
            System.out.println("Creating event body for:"+channelID);
            EventTasks.createEventBody(storedEvent, event.getChannel());
        }
    }
}

