package managers;

import data.Variables;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import tasks.EventTasks;
import tasks.TCTasks;


public class MsgCommandManager extends ListenerAdapter {
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

            //Write all command as lowercase, no spaces allowed
            String command = msg.split(" ")[0].toLowerCase();



            switch (command){
                case "event":
                    System.out.println("Making New Event cms has been called");
                    EventTasks.MakeNewEvent(event.getMember(), event.getTextChannel(), msg.substring(command.length()));
                    break;
                case "close":
                    System.out.println("Event close command has been called");
                    EventTasks.CloseEvent(event.getMember(), event.getTextChannel(), msg.substring(command.length()));
                    break;


                default:
                    System.out.println("Unknown command:"+command);
                    TCTasks.sendMessage(event.getTextChannel(), "Unknown command:"+command);
            }

            /**
            String eventManagerRole = "Event Manager";
            if(event.getMember().getRoles().stream().filter(r->r.getName().equalsIgnoreCase(eventManagerRole)).findFirst()!=null){

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

            }
             **/



        }else{
            System.out.println("Not bot channel");
        }

    }



}
