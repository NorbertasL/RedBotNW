package data;

import net.dv8tion.jda.api.entities.Member;

public class Event{
    private Member author;
    private String[] cmds;
    private String name;
    public Event(Member caller, String[] cmds){
        this.author  = caller;
        this.cmds = cmds;
        parseName(cmds[0]);
    }
    private void parseName(String title){
        //TODO parse name;
        this.name = "";
    }
    public Member getAuthor() {
        return author;
    }
    public String[] getCmds() {
        return cmds;
    }
    public String getName(){
        return name;
    }

}
