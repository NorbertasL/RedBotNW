package commands;

import commands.base.Credentials;
import data.GlobalConstants;
import commands.base.AbstractCommand;
import data.Roles;
import net.dv8tion.jda.api.entities.Message;
import tasks.EventTasks;

public class DeleteEventCommand extends AbstractCommand {
    private static String command = "delete";
    private Credentials credentials;
    public DeleteEventCommand(){
        credentials = new Credentials();

        //Setting command credentials
        credentials.setCredentials(Credentials.CredentialsKeys.ROLES , Roles.EVENT_MANAGER.getName());
        credentials.setCredentials(Credentials.CredentialsKeys.LISTEN_CATEGORIES, GlobalConstants.EVENT_CATEGORY);
        credentials.setCredentials(Credentials.CredentialsKeys.IGNORE_CHANNELS, GlobalConstants.EVENT_LOCKED_CHANNELS);
    }

    @Override
    public CmdResponse getResponse(CommandErrors response) {
        switch (response){
            case INVALID_RANK:
                return new CmdResponse(true, "-pYou need \"Event Manager\" rank to use this");
            case INVALID_CATEGORY:
            case INVALID_CHANNEL:
                return new CmdResponse(true, "-pYou can only delete Event channels");
            case IGNORED_CHANNEL:
                return new CmdResponse(true, "-pChannel protected from deletion");
        }
        return null;
    }

    @Override
    protected void execute(Message eventMessage, String vars) {
        EventTasks.deleteEvent(eventMessage, vars);
    }

    @Override
    public String getCommand() {
        return command;
    }

    @Override
    public Credentials getCredentials() {
        return credentials;
    }
}
