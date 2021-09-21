package tasks;

import data.Variables;
import net.dv8tion.jda.api.entities.Guild;

public class EventTasks {
    public static void MakeNewEvent(Guild guild, String eventInfo){
        Variables variables = Variables.getVariables(guild);
        guild.createTextChannel("TEST", guild.getCategories()
                .stream()
                .filter(x -> x.getName().equalsIgnoreCase(variables.getEventCategoryName()))
                .findFirst().get()).queue();
    }

}
