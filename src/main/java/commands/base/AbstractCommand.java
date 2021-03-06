package commands.base;

import data.Emoji;
import data.Roles;
import net.dv8tion.jda.api.entities.*;
import tasks.Helper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public abstract class AbstractCommand {
    public  CmdResponse runCommand(Message eventMessage, String vars){
        CommandErrors response = credentialCheck(eventMessage.getMember(), eventMessage.getTextChannel());
        if(response == CommandErrors.OK){
            execute(eventMessage, vars);
        }
        return getResponse(response);
    }

    public abstract CmdResponse getResponse(CommandErrors response);
    protected abstract void execute(Message eventMessage, String vars);

    public abstract String getCommand() ;
    public abstract Credentials getCredentials();

    private CommandErrors credentialCheck(Member member, TextChannel channel){
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


    public enum CommandErrors {
        OK(),
        INVALID_RANK(),
        INVALID_CATEGORY,
        INVALID_CHANNEL,
        IGNORED_CHANNEL;
    }
    public class CmdResponse{
        private boolean deleteCmd;
        private String msg;
        private Emoji [] reactions;
        public CmdResponse(Boolean deleteCmd){
            this.deleteCmd = deleteCmd;
        }
        public CmdResponse(Boolean deleteCmd, String msg){
            this(deleteCmd);
            this.msg = msg;
        }
        public CmdResponse(Boolean deleteCmd, Emoji...reactions){
            this(deleteCmd);
            this.reactions = reactions;
        }
        public CmdResponse(Boolean deleteCmd, String msg, Emoji...reactions){
            this(deleteCmd, msg);
            this.reactions = reactions;
        }
        public boolean shouldDeleteCmd(){
            return deleteCmd;
        }
        public boolean haveMsg(){
            return msg != null && !msg.isEmpty();
        }
        public String getMsg(){
            return msg;
        }
        public boolean haveReaction(){
            return reactions != null;
        }
        public Emoji [] getReactions(){
            return reactions;
        }
    }

}
