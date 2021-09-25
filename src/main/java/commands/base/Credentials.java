package commands.base;

import data.Variables;

import java.util.HashMap;

public class Credentials {
    HashMap<CredentialsKeys, String []> value;

    public Credentials(){
        value = new HashMap<>();

        //Full up all values with null
        for (CredentialsKeys key: CredentialsKeys.values()){
            value.put(key, null);
        }

    }
    public void setCredentials(CredentialsKeys key, String... values){
        value.replace(key, values);
    }

    public HashMap<CredentialsKeys, String[]> getHashMap() {
        return this.value;
    }

    public enum CredentialsKeys {
        ROLES,
        LISTEN_CATEGORIES,
        LISTEN_CHANNELS,
        IGNORE_CHANNELS;
    }
}
