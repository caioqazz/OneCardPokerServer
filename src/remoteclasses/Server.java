package remoteclasses;

import localclasses.*;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server implements ServerInt {

    private final ArrayList<Users> players;
    private final ArrayList<String> playerNames;
    private final Semaphore playersAccess;

    public Server() throws RemoteException {
        players = new ArrayList();
        playerNames = new ArrayList();
        playersAccess = new Semaphore(1);
    }

    @Override
    public void login(String name, UserInt connection) throws RemoteException {
        System.out.println("Login: " + name);
        Users user = new Users(name, connection);
        try {
            playersAccess.acquire();
            players.add(user);
            playerNames.add(name);
            playersAccess.release();
        } catch (InterruptedException e) {
            System.out.println("Server exception: " + e.toString());
        }
    }

    @Override
    public void logout(String name){
        for (int aux = 0; aux < players.size(); aux++) {
            if (playerNames.get(aux).equals(name)) {
                playerNames.remove(aux);
                players.remove(aux);
            }                
        }
    }
     
    @Override
    public ArrayList getPlayers() throws RemoteException {
        System.out.println("Player List Request");
        ArrayList<ArrayList> allPlayersAndStates = new ArrayList();
        ArrayList<Object> playerAndState;
        for(int aux=0; aux<players.size(); aux++){
            playerAndState = new ArrayList();
            playerAndState.add(players.get(aux).getName());
            if(players.get(aux).isPlaying()){
                playerAndState.add("Jogando");
            }else{
                playerAndState.add("Livre");
            }
            
            allPlayersAndStates.add(playerAndState);
        }
        return allPlayersAndStates;
    }

    @Override
    public boolean challenge(String challenger, String challenged) throws RemoteException {
        System.out.println("Desafio: " + challenger + " " + challenged);
        for (int aux = 0; aux < players.size(); aux++) {
            if (players.get(aux).getName().equals(challenged)) {
                if(players.get(aux).isPlaying()){
                    return false;
                }
                boolean answer = players.get(aux).getConnection().challenge(challenger);
                if (answer == true) {
                    for (int aux2 = 0; aux2 < players.size(); aux2++) {
                        if (players.get(aux2).getName().equals(challenger)) {
                            iniciar(challenged, challenger, aux, aux2);
                        }
                    }
                }
                return answer;
            }
        }
        return false;
    }
    
    private void iniciar(String challenged, String challenger, int challengedPosition, int challengerPosition) {
        new Thread() {
            @Override
            public void run() {
                try {
                    playersAccess.acquire();
                    System.out.println("Em jogo: " + challenger + " " + challenged);
                    players.get(challengedPosition).setPlaying(true);
                    players.get(challengerPosition).setPlaying(true);
                    playersAccess.release();
                    Games game = new Games(players.get(challengedPosition), players.get(challengerPosition));
                    game.startgame();
                    while(!game.isOver());
                    System.out.println("Jogo finalizado");
                    playersAccess.acquire();
                    players.get(challengedPosition).setPlaying(false);
                    players.get(challengerPosition).setPlaying(false);
                    playersAccess.release();
                    game=null;
                } catch (RemoteException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }.start();
    }

    public static void main(String args[]) {
        try {
            Registry registryServer = null;

            System.setSecurityManager(null);
            System.setProperty("java.rmi.server.hostname", "localhost");

            Server server = new Server();
            ServerInt stub = (ServerInt) UnicastRemoteObject.exportObject(server, 0);

            LocateRegistry.createRegistry(1996);
            registryServer = LocateRegistry.getRegistry(1996);

            registryServer.rebind("Server", stub);

            System.out.println("Server ready");

            while (true);
        } catch (Exception e) {
            System.out.println("Server exception: " + e.toString());
            //e.printStackTrace();
        }
    }
}
