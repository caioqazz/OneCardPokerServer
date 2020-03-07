package localclasses;

import remoteclasses.UserInt;

public class Users implements java.io.Serializable{
    private final String name;
    private boolean playing;
    private final UserInt connection;
    
    public Users(String name, UserInt connection){
        this.name=name;
        this.connection=connection;
        playing=false;
    }
    
    public void setPlaying(boolean state){
        playing=state;
    }
    
    public String getName(){
        return name;
    }
    
    public boolean isPlaying(){
        return playing;
    }
    
    public UserInt getConnection(){
        return connection;
    }
}
