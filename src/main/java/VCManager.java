import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelCreateEvent;
import net.dv8tion.jda.api.events.guild.voice.GenericGuildVoiceUpdateEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.*;
import java.util.stream.Collectors;


public class VCManager extends ListenerAdapter {
    private String privateVoiceCategoryName, privateCreatorChannelName, dynamicVoiceCategoryName;
    public VCManager(String privateVoiceCategoryName, String privateCreatorChannelName, String dynamicVoiceCategoryName){
        this.privateVoiceCategoryName = privateVoiceCategoryName;
        this.privateCreatorChannelName = privateCreatorChannelName;
        this.dynamicVoiceCategoryName = dynamicVoiceCategoryName;
    }

    @Override
    public void onGuildVoiceJoin(GuildVoiceJoinEvent event){
        createVC(event);
    }
    @Override
    public void onGuildVoiceMove(GuildVoiceMoveEvent event){
        createVC(event);
    }
    public void createVC(GenericGuildVoiceUpdateEvent event){
        VoiceChannel vc = event.getVoiceState().getChannel();
        //private channels
        if(vc.getParent().getName().equalsIgnoreCase(privateVoiceCategoryName)
                && vc.getName().equalsIgnoreCase(privateCreatorChannelName)){
            System.out.println("making new voice");
            String userName = event.getMember().getNickname()== null ? event.getMember().getUser().getName() : event.getMember().getNickname();
            event.getGuild().createVoiceChannel(userName+"'s Black Hole!", event.getChannelJoined().getParent()).queue();
        //Dynamic channel generator
        }else if(vc.getParent().getName().equalsIgnoreCase(dynamicVoiceCategoryName)){
            System.out.println("Making copy");
            vc.createCopy().setName(vc.getName()+"#1").queue();
        }
        cleanVC(event);
    }
    private HashMap<String, List<VoiceChannel>> getDynamicChannels(GenericGuildVoiceUpdateEvent event){
        HashMap <String, List<VoiceChannel>> dynamicChannels = new HashMap<>();

        return null;
    }

    //Get Channel list based on what category they are in
    private List<VoiceChannel> getChannelsFromCategory(Guild guild, String... categoryNames){
        List<VoiceChannel> vcList = new ArrayList<>();
        for(VoiceChannel vc: guild.getVoiceChannels()){
            for(String s : categoryNames){
                if(vc.getParent().getName().equalsIgnoreCase(s)) vcList.add(vc);
            }
        }
        return vcList;
    }

    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event){
        cleanVC(event);
    }
    public void cleanVC(GenericGuildVoiceUpdateEvent event){

        List<VoiceChannel> filteredGuildVoiceChannels = getChannelsFromCategory(event.getGuild(),
                privateVoiceCategoryName, dynamicVoiceCategoryName);


        HashMap <String, List<VoiceChannel>> dynamicChannels = new HashMap<>();
        for (VoiceChannel v : filteredGuildVoiceChannels){
            //Looking for private channels
            if(v.getParent().getName().equalsIgnoreCase(privateVoiceCategoryName)){
                //Deleting all empty private channels except the creator one.
                if(!v.getName().equalsIgnoreCase(privateCreatorChannelName)
                        && v.getMembers().size()<=0) v.delete().queue();
            }else{
                //Dealing with dynamic channels
                String name = v.getName().split("#")[0];
                name = name.trim();

                //Creating new kay if it doesn't exist
                if(!dynamicChannels.containsKey(name)){
                    List<VoiceChannel> newList = new ArrayList<VoiceChannel>();
                    dynamicChannels.put(name, newList);
                }
                dynamicChannels.get(name).add(v);
            }
        }
        //Time to clean up unused dynamic channels
        for (List<VoiceChannel> vcList: dynamicChannels.values()){
            System.out.println(vcList.toString());
            boolean coreEmpty = false;
            boolean haveExtraChannel = false;
            for(VoiceChannel vc: vcList){
                if(vc.getName().split("#").length <=1){
                    coreEmpty = vc.getMembers().isEmpty();
                    continue;//not deleting core
                }
                if(coreEmpty){//core empty so we can delete all duplicate channels
                    if(vc.getMembers().isEmpty()) vc.delete().queue();
                }else {
                    if(vc.getMembers().isEmpty()){
                        if(haveExtraChannel){
                            vc.delete().queue();
                        } else{
                            haveExtraChannel = true;
                        }
                    }
                }

            }
        }
        dynamicChannels.clear();//done with this

    }


    @Override
    public void onVoiceChannelCreate(VoiceChannelCreateEvent event){
        //private voice move
        List<VoiceChannel> vc = event.getGuild().getVoiceChannels();
        if(vc.isEmpty()){
            return;
        }
        VoiceChannel privateVoice = vc.stream()
                .filter(x -> x.getParent().getName().equalsIgnoreCase(privateVoiceCategoryName)
                        && x.getName().equalsIgnoreCase(privateCreatorChannelName))
                .findFirst().get();

        if(privateVoice != null) {
            privateVoice.getMembers().stream()
                    .forEach(x -> x.getGuild().moveVoiceMember(x, event.getChannel()).queue());
        }else{
            //dynamic voice create

        }






    }
}
