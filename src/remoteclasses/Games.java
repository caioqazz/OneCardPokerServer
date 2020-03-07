package remoteclasses;

import java.rmi.RemoteException;
import localclasses.Users;
import java.util.concurrent.ThreadLocalRandom;

public class Games implements GamesInt {

    private final Users player1, player2;
    private final boolean[] deck;
    private int bank1, bank2, bet1, bet2, card1, card2;
    private final int[] cards1, cards2;
    private boolean over;

    public Games(Users player1, Users player2) {
        this.player1 = player1;
        this.player2 = player2;
        bank1 = bank2 = 100;
        bet1 = bet2 = 0;
        cards1 = new int[2];
        cards2 = new int[2];
        card1 = card2 = 0;
        deck = new boolean[52];
        for (int aux = 0; aux < 2; aux++) {
            cards1[aux] = 0;
            cards2[aux] = 0;
        }
        for (int aux = 0; aux < 52; aux++) {
            deck[aux] = false;
        }
        over = false;
    }

    public void startgame() throws RemoteException, InterruptedException {
        for (int aux = 0; aux < 2; aux++) {
            do {
                cards1[aux] = ThreadLocalRandom.current().nextInt(0, 52);
            } while (deck[cards1[aux]] == true);
            deck[cards1[aux]] = true;

            do {
                cards2[aux] = ThreadLocalRandom.current().nextInt(0, 52);
            } while (deck[cards2[aux]] == true);
            deck[cards2[aux]] = true;
        }

        player1.getConnection().setCard(cards1[0]);
        player1.getConnection().setCard(cards1[1]);
        player2.getConnection().setCard(cards2[0]);
        player2.getConnection().setCard(cards2[1]);

        System.out.println("Cartas " + player1.getName() + ": " + cards1[0] + " " + cards1[1] + " Cartas " + player2.getName() + ": " + cards2[0] + " " + cards2[1]);

        startTurn();
    }

    private void startTurn() throws RemoteException {
        boolean c1, c2;

        c1 = cards2[0] > 26;
        c2 = cards2[1] > 26;
        player1.getConnection().flagSystem(c1, c2);

        c1 = cards1[0] > 26;
        c2 = cards1[1] > 26;
        player2.getConnection().flagSystem(c1, c2);

        turn();
    }

    private void turn() throws RemoteException {
        try {
            turnGetBets();
            System.out.println("Apostas: " + bet1 + " " + bet2);
            turnGetCards();
            System.out.println("Cartas: " + card1 + " " + card2);
            turnCheckResults();
            System.out.println("Vencedor da partida declarado");
            turnCheckWinner();
            System.out.println("Vencedor do jogo verificado");
            System.out.println("bancos: " + bank1 + " " + bank2);
            if (!over) {
                turnSetCards();
                System.out.println("Cartas " + player1.getName() + ": " + cards1[0] + " " + cards1[1] + " Cartas " + player2.getName() + ": " + cards2[0] + " " + cards2[1]);
                startTurn();
            }
        } catch (RemoteException e) {
            try {
                player1.getConnection().testConnection();
            } catch (RemoteException f) {
                endGame(player2.getName());
            }
            try {
                player2.getConnection().testConnection();
            } catch (RemoteException f) {
                endGame(player1.getName());
            }
        }
    }

    private void turnGetBets() throws RemoteException {
        int betAux1 = -1, betAux2 = -1;
        do {
            betAux1 = player1.getConnection().bet(bet2);
            if (betAux1 != -1) {
                bet1 = betAux1;
                betAux2 = player2.getConnection().bet(bet1);
            } else {
                endTurn(player2);
            }
            if (betAux2 == -1) {
                endTurn(player1);
            } else {
                bet2 = betAux2;
            }
        } while (bet1 != bet2);
    }

    private void turnGetCards() throws RemoteException {
        do {
            card1 = player1.getConnection().getCard();
        } while (card1 == -1);

        if (cards1[0] == card1) {
            cards1[0] = -1;
        } else {
            cards1[1] = -1;
        }

        do {
            card2 = player2.getConnection().getCard();
        } while (card2 == -1);

        if (cards2[0] == card2) {
            cards2[0] = -1;
        } else {
            cards2[1] = -1;
        }
    }

    private void turnSetCards() throws RemoteException {
        int aux;

        do {
            aux = ThreadLocalRandom.current().nextInt(0, 52);
        } while (deck[aux] == true);
        deck[aux] = true;
        player1.getConnection().setCard(aux);
        if (cards1[0] == -1) {
            cards1[0] = aux;
        } else {
            cards1[1] = aux;
        }

        do {
            aux = ThreadLocalRandom.current().nextInt(0, 52);
        } while (deck[aux] == true);
        deck[aux] = true;
        player2.getConnection().setCard(aux);
        if (cards2[0] == -1) {
            cards2[0] = aux;
        } else {
            cards2[1] = aux;
        }
    }

    private void turnCheckResults() throws RemoteException {
        if (card1 > card2 || (card1 <= 3 && card2 >= 48)) {
            player1.getConnection().results(player1.getName(), bet1, card1, card2);
            player2.getConnection().results(player1.getName(), bet1, card2, card1);
            bank1 += bet1;
            bank2 -= bet1;
        } else if (card2 > card1 || (card2 <= 3 && card1 >= 48)) {
            player1.getConnection().results(player2.getName(), bet1, card1, card2);
            player2.getConnection().results(player2.getName(), bet1, card2, card1);
            bank1 -= bet1;
            bank2 += bet1;
        }
        deck[card1] = false;
        deck[card2] = false;
        card1 = card2 = -1;
        bet1 = bet2 = 0;
    }

    private void turnCheckWinner() throws RemoteException {
        if (bank1 <= 0) {
            endGame(player2.getName());
        } else if (bank2 <= 0) {
            endGame(player1.getName());
        }
    }

    @Override
    public void forfeit(String player) throws RemoteException {
        if (player1.getName().equals(player)) {
            endGame(player2.getName());
        } else {
            endGame(player1.getName());
        }
    }

    private void endTurn(Users player) throws RemoteException {
        player1.getConnection().results(player.getName(), bet1, card1, card2);
        player2.getConnection().results(player.getName(), bet1, card2, card1);

        if (player == player1) {
            bank1 += bet2;
            bank2 -= bet2;
        } else {
            bank2 += bet1;
            bank1 -= bet1;
        }
        
        card1 = card2 = -1;
        bet1 = bet2 = 0;
        
        turnCheckWinner();
        
        if (!over) {
            turnSetCards();
            System.out.println("Cartas " + player1.getName() + ": " + cards1[0] + " " + cards1[1] + " Cartas " + player2.getName() + ": " + cards2[0] + " " + cards2[1]);
            startTurn();
        }else{
            while(true);
        }
    }

    private void endGame(String player) throws RemoteException {
        player1.getConnection().endGame(player);
        player2.getConnection().endGame(player);

        player1.setPlaying(false);
        player2.setPlaying(false);

        over = true;
    }

    public boolean isOver() {
        return over;
    }
}
