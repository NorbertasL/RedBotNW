import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.internal.JDAImpl;

import javax.security.auth.login.LoginException;

public class RedSparkNW  {
    public static JDA jda;
    public static void main(String[] args) throws LoginException, InterruptedException {
        //args[0]) = TOKEN
        jda = JDABuilder.createDefault(args[0]).build().awaitReady();
        jda.addEventListener(new MsgCommands());
        jda.addEventListener(new VCManager("Temp VC", "Create VC"));

        System.out.println("Bot is ready!");


    }


}
