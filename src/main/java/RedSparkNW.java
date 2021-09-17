import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;

public class RedSparkNW  {
    public static JDA jda;

    public static void main(String[] args) throws LoginException, InterruptedException {
        final Logger log = LoggerFactory.getLogger(RedSparkNW.class);
        //args[0]) = TOKEN
        jda = JDABuilder.createDefault(args[0]).build().awaitReady();
        jda.addEventListener(new MsgCommands("bot"));
        jda.addEventListener(new VCManager("Temp VC", "Create VC", "Voice"));
        System.out.println("Bot Ready!");
    }


}
