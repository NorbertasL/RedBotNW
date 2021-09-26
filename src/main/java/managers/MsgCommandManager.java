package managers;

import commands.DeleteEventCommand;
import commands.NewEventCommand;
import commands.StartPollCommand;
import data.Emoji;
import data.Event;
import data.GlobalConstants;
import data.Variables;
import commands.base.AbstractCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
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
        abstractCommands.add(new StartPollCommand());

    }
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        //Ignoring private messages
        try{
            if(event.getPrivateChannel() != null){
                return;
            }
        } catch (IllegalStateException e){}//Suppressing so we don't spam the log

        //If bot is posting we need to check if reaction need to be done
        if (event.getAuthor().isBot()) {
            Variables variables = Variables.getVariables(event.getGuild());
            MessageEmbed embed = event.getMessage().getEmbeds().size() == 0? null:event.getMessage().getEmbeds().get(0);
            String id = "";
            //Embeds have ids in footers
            if(embed != null){
                if(!embed.getFooter().getText().isBlank()) {
                    id = embed.getFooter().getText();

                    //TODO translate this for my own use.
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.copyFrom(embed);
                    eb.setFooter(embed.getFooter().getText()+" Edited");
                    event.getMessage().editMessageEmbeds(eb.build()).queue();
                }
            }else {
                //Normal post have ids as the msg itself up to 10 chars
                id = event.getMessage().getContentRaw().length() < 10 ? event.getMessage().getContentRaw() : event.getMessage().getContentRaw().substring(0, 10);
            }

            String[] emoji = variables.getReactionsFor(id);
            if(emoji != null){
                for(String unicode : emoji){
                    if(unicode != null && !unicode.isEmpty()){
                        event.getMessage().addReaction(unicode).queue();
                    }

                }
            }

            return;
        }
        String msg = event.getMessage().getContentRaw();

        //Ignoring non command messages
        if (msg.charAt(0) != '!') {
            return;
        }

        msg = msg.substring(1);//removing the !
        //Write all command as lowercase, no spaces allowed
        String callerCmd = msg.split(" ")[0].toLowerCase();
        String vars = msg.substring(callerCmd.length());

        //Looping tough possible commands
        for (AbstractCommand abstractCommand : abstractCommands){
            if (abstractCommand.getCommand().equalsIgnoreCase(callerCmd)){

                //Executing command
                AbstractCommand.CmdResponse response = abstractCommand.runCommand(event.getMessage(), vars);

                //Checking for responses
                if(response != null){

                    //Checking if response contains a message
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

                    //Checking if response contains a reaction
                    if (response.haveReaction()){
                        for(Emoji emoji: response.getReactions()){
                            event.getMessage().addReaction(emoji.getUnicode()).queue();
                        }
                    }

                    //Checking is called cmd should be deleted
                    if(response.shouldDeleteCmd()){
                        event.getMessage().delete().queue();
                    }
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

