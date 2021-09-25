package managers;

import commands.DeleteEventCommand;
import commands.NewEventCommand;
import data.Emoji;
import data.Event;
import data.GlobalConstants;
import data.Variables;
import commands.base.AbstractCommand;
import net.dv8tion.jda.api.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import tasks.EventTasks;
import tasks.Helper;
import tasks.TCTasks;

import java.util.ArrayList;
import java.util.List;


public class MsgCommandManager extends ListenerAdapter {

    List<AbstractCommand> abstractCommands = new ArrayList<>();

    public MsgCommandManager(){
        super();
        //Add in all commands
        abstractCommands.add(new NewEventCommand());
        abstractCommands.add(new DeleteEventCommand());

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
        for (AbstractCommand abstractCommand : abstractCommands){
            if (abstractCommand.getCommand().equalsIgnoreCase(callerCmd)){
                AbstractCommand.CmdResponse response = abstractCommand.runCommand(event.getMessage(), vars);
                if(response != null){
                    System.out.println("have respons");
                    if(response.haveMsg()) {
                        String responseMsg = response.getMsg();
                        //if starts with -p its a private message
                        if (responseMsg.length() >=2 && responseMsg.substring(0, 2).equalsIgnoreCase("-p")){
                            event.getAuthor().openPrivateChannel().queue((channel) ->
                                    channel.sendMessage(responseMsg.substring(2)).queue()
                            );
                        }else {
                            TCTasks.sendMessage(event.getTextChannel(), responseMsg);
                        }
                    }
                    if (response.haveReaction()){
                        for(Emoji emoji: response.getReactions()){
                            event.getMessage().addReaction(emoji.getUnicode()).queue();
                        }
                    }
                    if(response.shouldDeleteCmd()){
                        System.out.println("deleting msg");
                        event.getMessage().delete().queue();
                    }
                }

                return;
            }
        }
        if(callerCmd.equals("poll")){
            EventTasks.generatePoll(msg.substring(4), event.getTextChannel());
            return;
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

