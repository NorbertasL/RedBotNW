package commands.base;

import data.Roles;
import data.Variables;
import net.dv8tion.jda.api.entities.*;
import tasks.Helper;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public abstract class AbstractCommand {
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
    public abstract Credentials getCredentials();

    private CommandErrors credentialCheck(Member member, TextChannel channel){
        Variables variables = Variables.getVariables(member.getGuild());

        HashMap<Credentials.CredentialsKeys, String []> value = this.getCredentials().getHashMap();
        List<String> roles = new ArrayList<>(Arrays.asList(value.get(Credentials.CredentialsKeys.ROLES)));
        roles.add(Roles.GOD.getName());//Making sure god role always allows all commands

        //Role check
        if(!Helper.hasRank(member, roles.toArray(String[]::new))){
            return CommandErrors.INVALID_RANK;
        }


        //if category == null we listen in all categories
        if(value.get(Credentials.CredentialsKeys.LISTEN_CATEGORIES) != null){
            //If in valid category check
            if(!Helper.hasCategory(channel, value.get(Credentials.CredentialsKeys.LISTEN_CATEGORIES))){
                return CommandErrors.INVALID_CATEGORY;
            }
        }


        //if channels are null then we listen to all channels
        if(value.get(Credentials.CredentialsKeys.LISTEN_CHANNELS) != null){
            //Channel check
            if(!Helper.channelInList(channel, value.get(Credentials.CredentialsKeys.LISTEN_CHANNELS))){
                return CommandErrors.INVALID_CHANNEL;
            }
        }

        //id IGNORE_CHANNELS = null we ignore no channels
        if(Credentials.CredentialsKeys.IGNORE_CHANNELS != null){
            //Ignore channel check
            if(Helper.channelInList(channel, value.get(Credentials.CredentialsKeys.IGNORE_CHANNELS))){
                return CommandErrors.IGNORED_CHANNEL;
            }
        }

        return CommandErrors.OK;
    }

}
