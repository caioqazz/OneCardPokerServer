package remoteclasses;

import java.rmi.*;
import java.util.*;

public interface ServerInt extends Remote{
    public void login(String name, UserInt connection) throws RemoteException;//implementado
    public void logout(String name)throws RemoteException;//implementado
    public ArrayList getPlayers() throws RemoteException;//implementado
    public boolean challenge(String challenger, String challenged) throws RemoteException;//implementado
}