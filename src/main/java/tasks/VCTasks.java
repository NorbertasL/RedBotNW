package tasks;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GenericGuildVoiceUpdateEvent;
import data.Variables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class VCTasks {

    public static void createVC(GenericGuildVoiceUpdateEvent event){
        VoiceChannel vc = event.getMember().getVoiceState().getChannel();
        Variables variables = Variables.getVariables(event.getGuild());
        //private channels
        System.out.println(vc.getParent().getName());
        System.out.println(variables.getPrivateVcCategoryName());
        if(vc.getParent().getName().equalsIgnoreCase(variables.getPrivateVcCategoryName())
                && vc.getName().equalsIgnoreCase(variables.getPrivateChannelName())){
            System.out.println("Making new private voice");
            String userName = event.getMember().getNickname()== null ? event.getMember().getUser().getName() : event.getMember().getNickname();
            event.getGuild().createVoiceChannel(userName+"s Black Hole!", event.getChannelJoined().getParent()).queue();
            //Dynamic channel generator
        }else if(vc.getParent().getName().equalsIgnoreCase(variables.getDynamicVcCategoryName())){
            if(vc.getName().equalsIgnoreCase("AFK")) return;//Ignoring AFK channel
            if(vc.getMembers().size()<=1) {
                List<VoiceChannel> dynamicChannels = getChannelsFromCategory(event.getGuild(), variables.getDynamicVcCategoryName()).stream()
                        .filter(v -> v.getName().split("#")[0].trim()
                                .equalsIgnoreCase(vc.getName().split("#")[0].trim()))
                        .collect(Collectors.toList());

                boolean hasEmptyChannel = false;
                System.out.println("Channel list:"+dynamicChannels.toString());
                for (VoiceChannel v : dynamicChannels){
                    if(v.getMembers().isEmpty()) {
                        hasEmptyChannel = true;
                        break;
                    }
                }
                if(!hasEmptyChannel){
                    System.out.println("Making dynamic copy of:"+vc.getName());

                    int index = vc.getPositionRaw();
                    vc.createCopy().setName(vc.getName().split("#")[0].trim() + " #"
                            + (dynamicChannels.size()+1)).setPosition(index).queue();
                }

            }
        }
    }
    public static void cleanVC(GenericGuildVoiceUpdateEvent event){
        Variables variables = Variables.getVariables(event.getGuild());
        List<VoiceChannel> filteredGuildVoiceChannels = getChannelsFromCategory(event.getGuild(),
                variables.getPrivateVcCategoryName(), variables.getDynamicVcCategoryName());


        HashMap<String, List<VoiceChannel>> dynamicChannels = new HashMap<>();
        for (VoiceChannel v : filteredGuildVoiceChannels){
            //Looking for private channels
            if(v.getParent().getName().equalsIgnoreCase(variables.getPrivateVcCategoryName())){
                //Deleting all empty private channels except the creator one.
                if(!v.getName().equalsIgnoreCase(variables.getPrivateChannelName()) && v.getMembers().size()<=0) {
                    System.out.println("Deleting private channel:" +v.getName());
                    v.delete().queue();
                }
            }else{
                //Dealing with dynamic channels
                String name = v.getName().split("#")[0];
                name = name.trim();

                //Creating new kay if it doesn't exist
                if(!dynamicChannels.containsKey(name)){
                    dynamicChannels.put(name, new ArrayList<>());
                }
                dynamicChannels.get(name).add(v);
            }
        }
        //Time to clean up unused dynamic channels
        for (List<VoiceChannel> vcList: dynamicChannels.values()){
            //System.out.println(vcList.toString());
            boolean coreEmpty = true;
            boolean haveExtraChannel = false;
            for(VoiceChannel vc: vcList){
                if(!vc.getName().contains("#")){//no # means it's a core channel
                    coreEmpty = vc.getMembers().isEmpty();
                    continue;//not deleting core
                }
                if(coreEmpty){//core empty so we can delete all duplicate channels
                    if(vc.getMembers().isEmpty()){
                        System.out.println("Core empty so deleting extra:"+vc.getName());
                        vc.delete().queue();
                    }
                }else {
                    if(vc.getMembers().isEmpty()){
                        if(haveExtraChannel){
                            System.out.println("Core is NOT empty, but we have Extra so deleting:"+vc.getName());
                            vc.delete().queue();
                        } else{
                            System.out.println("This will be our extra channel:"+vc.getName());
                            haveExtraChannel = true;
                        }
                    }
                }

            }
        }
        dynamicChannels.clear();//done with this

    }
    //Get Channel list based on what category they are in
    private static List<VoiceChannel> getChannelsFromCategory(Guild guild, String... categoryNames){
        List<VoiceChannel> vcList = new ArrayList<>();
        for(VoiceChannel vc: guild.getVoiceChannels()){
            for(String s : categoryNames){
                if(vc.getParent().getName().equalsIgnoreCase(s)) vcList.add(vc);
            }
        }
        return vcList;
    }
}
