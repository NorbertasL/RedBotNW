package managers;

import commands.DeleteEvent;
import commands.NewEvent;
import data.Variables;
import interfaces.Command;
import interfaces.CommandErrors;
import net.dv8tion.jda.api.entities.Category;
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
        commands.add(new NewEvent());
        commands.add(new DeleteEvent());

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
                if(response != CommandErrors.INVALID_CHANNEL)
                    TCTasks.sendMessage(event.getTextChannel(), response.toString());
                return;
            }
        }
        TCTasks.sendMessage(event.getTextChannel(), "Could not find command:"+callerCmd);

    }
}

