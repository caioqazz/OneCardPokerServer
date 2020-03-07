package remoteclasses;

import java.rmi.*;

public interface GamesInt extends Remote{
    public void forfeit(String player) throws RemoteException;
}
