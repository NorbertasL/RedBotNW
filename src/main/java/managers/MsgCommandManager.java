package managers;

import data.Variables;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import tasks.EventTasks;
import tasks.TCTasks;


public class MsgCommandManager extends ListenerAdapter {
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

        Variables variables = Variables.getVariables(event.getGuild());
        //Bot channel only commands
        msg = msg.substring(1);
        //Write all command as lowercase, no spaces allowed
        String command = msg.split(" ")[0].toLowerCase();
        if (event.getChannel().getName().equalsIgnoreCase(variables.getBotChannelName())) {
            switch (command) {
                case "event":
                    System.out.println("Making New Event cms has been called");
                    EventTasks.makeNewEvent(event.getMessage(), msg.substring(command.length()));
                    return;

                default:
                    System.out.println("Unknown command:" + command);
                    TCTasks.sendMessage(event.getTextChannel(), "Unknown command:" + command);

            }
        }


        Category category = event.getTextChannel().getParent();
        //Event channel commands
        if (category != null && category.getName().equalsIgnoreCase(variables.getEventCategoryName())) {
            switch (command) {
                case "event":
                    System.out.println("Making New Event cms has been called");
                    EventTasks.makeNewEvent(event.getMessage(), msg.substring(command.length()));
                    return;
                case "close":
                    System.out.println("Event close command has been called");
                    EventTasks.closeEvent(event.getMember(), event.getTextChannel(), msg.substring(command.length()));
                    return;
                case "delete":
                    System.out.println("Event delete command has been called");
                    EventTasks.deleteEvent(event.getMember(), event.getTextChannel(), msg.substring(command.length()));
                    return;
                default:
                    System.out.println("Unknown command:" + command);
                    TCTasks.sendMessage(event.getTextChannel(), "Unknown command:" + command);

            }
        }
    }
}

