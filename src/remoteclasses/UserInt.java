package remoteclasses;

import java.rmi.*;

public interface UserInt extends Remote{
    public boolean challenge(String nome) throws RemoteException; //implementado
    public void flagSystem(boolean card1, boolean card2) throws RemoteException;
    public void results(String winner, int winnings, int cartaDoJogador, int cartaDoOponente) throws RemoteException;
    public void endGame(String winner) throws RemoteException;
    public void setCard(int card) throws RemoteException;
    public int getCard() throws RemoteException;
    public int bet(int oponentBet) throws RemoteException;
    public void testConnection()throws RemoteException;
}
