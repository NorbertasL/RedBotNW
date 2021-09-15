import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelCreateEvent;
import net.dv8tion.jda.api.events.guild.voice.GenericGuildVoiceUpdateEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class VCManager extends ListenerAdapter {
    private String categoryName, creatorChannelName;
    public VCManager(String categoryName, String creatorChannelName){
        this.categoryName = categoryName;
        this.creatorChannelName = creatorChannelName;
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
        if(vc.getParent().getName().equalsIgnoreCase(categoryName)
                && vc.getName().equalsIgnoreCase(creatorChannelName)){
            System.out.println("making new voice");
            String userName = event.getMember().getNickname()== null ? event.getMember().getUser().getName() : event.getMember().getNickname();
            event.getGuild().createVoiceChannel(userName, event.getChannelJoined().getParent()).queue();
        }
        cleanVC(event);
    }

    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event){
        cleanVC(event);
    }
    public void cleanVC(GenericGuildVoiceUpdateEvent event){
        Category category = event.getChannelLeft().getParent();
        if(category!=null && category.getName().equalsIgnoreCase(categoryName)){
            category.getVoiceChannels().stream()
                    .filter(vc -> !(vc.getName().equalsIgnoreCase(creatorChannelName)) && vc.getMembers().size() == 0)
                    .forEach(vc -> vc.delete().queue());
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
