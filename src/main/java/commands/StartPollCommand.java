package commands;

import commands.base.AbstractCommand;
import commands.base.Credentials;
import data.GlobalConstants;
import data.Roles;
import net.dv8tion.jda.api.entities.Message;
import tasks.EventTasks;

public class StartPollCommand extends AbstractCommand {
    private static String command = "poll";
    private Credentials credentials;
    public StartPollCommand(){
        credentials = new Credentials();

        //Setting command credentials
        credentials.setCredentials(Credentials.CredentialsKeys.ROLES , Roles.EVENT_MANAGER.getName());
        credentials.setCredentials(Credentials.CredentialsKeys.LISTEN_CATEGORIES, GlobalConstants.EVENT_CATEGORY);
        credentials.setCredentials(Credentials.CredentialsKeys.IGNORE_CHANNELS, GlobalConstants.EVENT_LOCKED_CHANNELS);
    }
    @Override
    public CmdResponse getResponse(CommandErrors response) {
        return new CmdResponse(true);
    }

    @Override
    protected void execute(Message eventMessage, String vars) {
        EventTasks.generatePoll(vars, eventMessage.getTextChannel());
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
