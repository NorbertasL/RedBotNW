package commands;

import commands.base.AbstractCommand;
import commands.base.Credentials;
import net.dv8tion.jda.api.entities.Message;

//TODO EventEditCommand
public class EventEditCommand extends AbstractCommand {
    @Override
    public CmdResponse getResponse(CommandErrors response) {
        return null;
    }

    @Override
    protected void execute(Message eventMessage, String vars) {

    }

    @Override
    public String getCommand() {
        return null;
    }

    @Override
    public Credentials getCredentials() {
        return null;
    }
}
