import managers.MsgCommandManager;
import managers.VCManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;

public class RedSparkNW  {
    public static JDA jda;

    public static void main(String[] args) throws LoginException, InterruptedException {
        String token;
        if(args.length >= 1 && args[0] != null){
            token = args[0];
        }else {
            token = System.getenv("TOKEN");
        }
        jda = JDABuilder.createDefault(token).build().awaitReady();
        //jda.addEventListener(new MsgCommandManager()); //Commands turned off for now
        jda.addEventListener(new VCManager());
        System.out.println("Bot Ready!");
    }


}
