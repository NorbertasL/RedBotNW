package managers;

import data.Variables;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import tasks.EventTasks;
import tasks.TCTasks;


public class MsgCommandManager extends ListenerAdapter {
    private final String EVENT_CMD = "Event";
    private final String EVENT_CLOSE_CMD = "Close";
    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        Variables variables = Variables.getVariables(event.getGuild());
        Category category = event.getTextChannel().getParent();
        if(event.getChannel().getName().equalsIgnoreCase(variables.getBotChannelName())
                || (category != null && category.getName().equalsIgnoreCase(variables.getEventCategoryName()))){
            String msg = event.getMessage().getContentRaw();
            if(msg.charAt(0)!='!'){
                System.out.println("Not a command");
                TCTasks.sendMessage(event.getTextChannel(), "I'm a DC bot, not a chat bot.Plz use proper commands!");
            }
            msg = msg.substring(1);
            if(msg.substring(0, EVENT_CMD.length()).equalsIgnoreCase(EVENT_CMD)){
                System.out.println("Making New Event");
                EventTasks.MakeNewEvent(event.getGuild(), msg.substring(EVENT_CMD.length()));
            }
            if(msg.substring(0, EVENT_CLOSE_CMD.length()).equalsIgnoreCase(EVENT_CLOSE_CMD)){
                if(category == null || category.getName().equalsIgnoreCase(variables.getEventCategoryName())){
                    System.out.println("Trying to close something?");
                    TCTasks.sendMessage(event.getTextChannel(), "WTF are you trying to close?");
                    return;
                }
            }

        }else{
            System.out.println("Not bot channel");
        }

    }



}
