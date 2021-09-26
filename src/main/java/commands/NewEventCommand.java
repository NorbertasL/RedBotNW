package commands;

import commands.base.Credentials;
import data.GlobalConstants;
import data.Roles;
import commands.base.AbstractCommand;
import net.dv8tion.jda.api.entities.Message;
import tasks.EventTasks;

public class NewEventCommand extends AbstractCommand {
    private static String command = "Event";
    private Credentials credentials;

    public NewEventCommand(){
        credentials = new Credentials();

        //Setting command credentials
        credentials.setCredentials(Credentials.CredentialsKeys.ROLES , Roles.EVENT_MANAGER.getName());
    }

    @Override
    public CmdResponse getResponse(CommandErrors response) {
        switch (response){
            case OK:
                return new CmdResponse(false);
            case INVALID_RANK:
                return new CmdResponse(true, "-pYou need \"Event Manager\" rank to use this");
        }
        return null;
    }

    protected void execute(Message eventMessage, String vars) {
        System.out.println("NewEvent execute called");
        EventTasks.makeNewEvent(eventMessage, vars);
        //return CommandErrors.OK;

    }

    @Override
    public String getCommand() {
        return command;
    }

    @Override
    public Credentials getCredentials() {
        return credentials;
    }

    public enum NewEventVars{
        TITLE("title"),
        START("start"),
        END("end"),
        DURATION("duration"),
        INFO("info"),
        IMG("img"),
        FOOTER("footer"),
        NO_ATTENDANCE("no-attendance"),
        CUSTOM_REACTIONS("reactions");

        String varName;
        NewEventVars(String varName){
            this.varName = varName;
        }
        public String getVarName(){
            return varName;
        }

    }

}
