package dataStructure;

import java.lang.String;

public class Person {
    public String name;
    private String Ip = null;
    private String Port = null;
    public boolean online = false;

    public Person(String name) {
        this.name = name;
    }

    public void login(String ip, String port){
        this.Ip = ip;
        this.Port = port;
        this.online = true;
    }

    public String getIpAddress(){
        if (online) {
            return Ip;
        }else {
            return "user_is_not_online";
        }
    }

    public String getPortAddress(){
        if (online) {
            return Port;
        }else {
            return "user_is_not_online";
        }

    }

    public boolean logout(){
        Ip = null;
        Port = null;
        online = false;
        return online;
    }
}
