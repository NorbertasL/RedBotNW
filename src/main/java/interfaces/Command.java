package interfaces;

import net.dv8tion.jda.api.entities.*;


public abstract class Command {
    public  CommandErrors runCommand(Message eventMessage, String vars){
        CommandErrors response = credentialCheck(eventMessage.getMember(), eventMessage.getTextChannel());
        if(response == CommandErrors.OK){
            System.out.println("calling execute");
            execute(eventMessage, vars);
        }
        return response;
    }

    protected abstract void execute(Message eventMessage, String vars);

    public abstract String getCommand() ;

    protected abstract CommandErrors credentialCheck(Member member, TextChannel channel);

}
