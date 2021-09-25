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
        System.out.println("DeleteEvent execute called");
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

    /**
    @Override
    protected CommandErrors credentialCheck(Member member, TextChannel channel) {
        Variables variables = Variables.getVariables(member.getGuild());

        HashMap<CommandKeys, String []> value = variables.getCommandVarsFor(getCommand());

        String[] roles = value.get(CommandKeys.ROLES) == null ? defaultRoles: value.get(CommandKeys.ROLES);
        String[] listenCategories  = value.get(CommandKeys.LISTEN_CATEGORIES) == null ? defaultListenCategories: value.get(CommandKeys.LISTEN_CATEGORIES);
        String[] lockedChannels = value.get(CommandKeys.IGNORE_CHANNELS) == null ? defaultLockedChannels: value.get(CommandKeys.IGNORE_CHANNELS);

        if(!Helper.hasRank(member, roles)){
            return CommandErrors.INVALID_RANK;
        }
        if(!Helper.hasCategory(channel, listenCategories) || Helper.channelInList(channel, lockedChannels)){
            return CommandErrors.INVALID_CHANNEL;
        }
        return CommandErrors.OK;

    }
    **/
}
