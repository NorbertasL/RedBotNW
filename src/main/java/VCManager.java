import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelCreateEvent;
import net.dv8tion.jda.api.events.guild.voice.GenericGuildVoiceUpdateEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class VCManager extends ListenerAdapter {
    private String categoryName, creatorChannelName, dynamicVCCategoryName;
    public VCManager(String categoryName, String creatorChannelName, String dynamicVCCategoryName){
        this.categoryName = categoryName;
        this.creatorChannelName = creatorChannelName;
        this.dynamicVCCategoryName = dynamicVCCategoryName;
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
        if(vc.getParent().getName().equalsIgnoreCase(categoryName)
                && vc.getName().equalsIgnoreCase(creatorChannelName)){
            System.out.println("making new voice");
            String userName = event.getMember().getNickname()== null ? event.getMember().getUser().getName() : event.getMember().getNickname();
            event.getGuild().createVoiceChannel(userName, event.getChannelJoined().getParent()).queue();
        //Dynamic channel generator
        }else if(vc.getParent().getName().equalsIgnoreCase(dynamicVCCategoryName)){
            System.out.println("Making copy");
            vc.createCopy().setName(vc.getName()+"#1").queue();
        }
        cleanVC(event);
    }

    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event){
        cleanVC(event);
    }
    public void cleanVC(GenericGuildVoiceUpdateEvent event){
        try {
            Category category = event.getChannelLeft().getParent();
            if(category!=null && category.getName().equalsIgnoreCase(categoryName)){
                category.getVoiceChannels().stream()
                        .filter(vc -> !(vc.getName().equalsIgnoreCase(creatorChannelName)) && vc.getMembers().size() == 0)
                        .forEach(vc -> vc.delete().queue());
            }
        }catch (NullPointerException e){
            System.out.println("onGuildVoiceLeave Null point");
        }
    }


    @Override
    public void onVoiceChannelCreate(VoiceChannelCreateEvent event){
        VoiceChannel vc = event.getGuild().getVoiceChannels().stream()
                .filter(x -> x.getParent().getName().equalsIgnoreCase(categoryName)
                        && x.getName().equalsIgnoreCase(creatorChannelName))
                .findFirst().get();
        vc.getMembers().stream()
                .forEach(x -> x.getGuild().moveVoiceMember(x, event.getChannel()).queue());


    }
}
