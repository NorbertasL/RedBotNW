package data;

public enum Emoji {
    THUMBS_UP("U+1F44D"),
    THUMBS_DOWN("U+1F44E");
    String unicode;
    Emoji(String unicode){
        this.unicode = unicode;
    }
    public String getUnicode(){
        return unicode;
    }
}
