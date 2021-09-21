package managers;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


public class MsgCommandManager extends ListenerAdapter {
    private String botChannelName;
    public MsgCommandManager(String botChannelName){
        this.botChannelName = botChannelName;
    }
    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        if(!event.getChannel().getName().equalsIgnoreCase(botChannelName)){
            System.out.println("Uninteresting channel");
            return;
        }
        String msg = event.getMessage().getContentRaw();
        if(msg.charAt(0)!='!'){
            System.out.println("Not a command");
        }
        String[] msgData = msg.substring(1).split(" ");
        if(msgData[0].equalsIgnoreCase("Event")){
            //Create new event channel
            event.getGuild().createTextChannel(msgData[1], event.getGuild().getCategories()
                    .stream()
                    .filter(x -> x.getName().equalsIgnoreCase("Events"))
                    .findFirst().get()).queue();
        }
    }



}
