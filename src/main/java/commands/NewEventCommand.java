package commands;

import data.GlobalConstants;
import data.Variables;
import interfaces.Command;
import interfaces.CommandErrors;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tasks.EventTasks;
import tasks.Helper;

import java.util.HashMap;

public class NewEventCommand extends Command {
    private static String command = "Event";
    private static String [] defaultRoles= GlobalConstants.EVENT_MANAGER_ROLE;
    private static String[] defaultListenCategories = GlobalConstants.EVENT_CATEGORY;


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
    protected CommandErrors credentialCheck(Member member, TextChannel channel) {
        Variables variables = Variables.getVariables(member.getGuild());

        HashMap<CommandKeys, String []> value = variables.getCommandVarsFor(getCommand());

        String[] roles = value.get(CommandKeys.ROLES) == null ? defaultRoles: value.get(CommandKeys.ROLES);
        String[] listenCategories  = value.get(CommandKeys.LISTEN_CATEGORIES) == null ? defaultListenCategories: value.get(CommandKeys.LISTEN_CATEGORIES);

        if(!Helper.hasRank(member, roles)){
            return CommandErrors.INVALID_RANK;
        }
        if(!Helper.hasCategory(channel, listenCategories)){
            return CommandErrors.INVALID_CHANNEL;
        }
        return CommandErrors.OK;
    }
}
