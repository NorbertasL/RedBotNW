package managers;

import data.Variables;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelCreateEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import tasks.VCTasks;
import java.util.*;



public class VCManager extends ListenerAdapter {

    @Override
    public void onGuildVoiceJoin(GuildVoiceJoinEvent event){
        VCTasks.createVC(event);
    }
    @Override
    public void onGuildVoiceMove(GuildVoiceMoveEvent event){
        VCTasks.createVC(event);
        VCTasks.cleanVC(event);
    }

    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event){
        VCTasks.cleanVC(event);
    }



    @Override
    public void onVoiceChannelCreate(VoiceChannelCreateEvent event){
        Variables variables = Variables.getVariables(event.getGuild());
        //private voice move
        List<VoiceChannel> vc = event.getGuild().getVoiceChannels();
        if(vc.isEmpty()){
            return;
        }
        VoiceChannel privateVoice = vc.stream()
                .filter(x -> x.getParent().getName().equalsIgnoreCase(variables.getPrivateVcCategoryName())
                        && x.getName().equalsIgnoreCase(variables.getPrivateChannelName()))
                .findFirst().get();

        if(privateVoice != null) {
            privateVoice.getMembers().stream()
                    .forEach(x -> x.getGuild().moveVoiceMember(x, event.getChannel()).queue());
        }
    }

}
