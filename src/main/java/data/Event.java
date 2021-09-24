package data;

import net.dv8tion.jda.api.entities.Member;

public class Event{
    private Member author;
    private String[] cmds;
    private String id;
    public Event(String id, Member caller, String[] cmds){
        this.id = id;
        this.author  = caller;
        this.cmds = cmds;
    }

    public Member getAuthor() {
        return author;
    }
    public String[] getCmds() {
        return cmds;
    }
    public String getId(){
        return id;
    }

}
