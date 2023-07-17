/*
    This is a console based BlackJack Game for any number of 
    players to play. It follows standard BlackJack rules. 
*/

package BlackJack;

import java.util.ArrayList;
import java.util.Scanner;

public class BlackJackGame {
    static Scanner keyboard = new Scanner(System.in);
    static final String CLEAR_CONSOLE = "\033[H\033[2J";

    /**
     * Checks if a given input is a valid whole number
     * @param string input that will be checked
     * @return true or false
     */
    public static boolean isWholeNumber(String string) {
        boolean isWholeNumber = true;

        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) < 48 || string.charAt(i) > 57) {
                isWholeNumber = false;
            }
        }

        if (string.equals("")) {
            isWholeNumber = false;
        }

        return isWholeNumber;
    }

    /**
     * Outputs the continue message with another message in front and a custom delay time before
     * @param message string to be printed in front of the continue message
     * @param delayTime amount of time before messages appear
     * @throws InterruptedException
     */
    public static void continueMessage(String message, int delayTime) throws InterruptedException {
        Thread.sleep(delayTime);
        System.out.println("\n" + message + " Enter any key to continue.");
        keyboard.nextLine();
        System.out.println(CLEAR_CONSOLE);
    }

    /**
     * Prints continue message to break flow of game
     * @throws InterruptedException
     */
    public static void continueMessage() throws InterruptedException {
        Thread.sleep(3000);
        System.out.println("\nEnter any key to continue.");
        keyboard.nextLine();
        System.out.println(CLEAR_CONSOLE);
    }

    /**
     * Prints out a players cards
     * @param player the given player
     */
    public static void printCards(Player player) {
        System.out.println("\nYour cards:");

        for (int i = 0; i < player.getHand().size(); i++) {
            System.out.println("Card " + (i + 1) + ": " + player.getHand().get(i).getName());
        }
        System.out.println();
    }

    /**
     * Creates the menu for a player move
     * @param deck current state of deck
     * @param player current player
     * @return players getd points after move
     * @throws InterruptedException
     */
    public static void playerTurn(BlackJackEngine game, Player player) throws InterruptedException {
        System.out.println(player.getName() + "'s turn.\n");
        boolean menuSelectionFlag = true;

        do {
            menuSelectionFlag = true;
            printCards(player);
            System.out.println("Make your move\n\t1) Hit\n\t2) Stand\nEnter your choice");
            String menuSelection = keyboard.nextLine();

            // Hit
            if (menuSelection.equals("1")) {
                Card randomCard = game.dealCard();

                player.hit(randomCard);

                System.out.println("\nYou got the " + randomCard.getName() + "\n");
                
                continueMessage();
            }

            // Stand
            else if (menuSelection.equals("2")) {
                player.stand();
            }
            
            // Invalid input
            else {
                System.out.println("Error. enter a valid number.");
                menuSelectionFlag = false;
                continueMessage();
            }
        } while (!menuSelectionFlag);
    }
    
    /**
     * Prints out the result of the game for each player
     * @param game.getPlayers() players who did not bust
     * @param bustedPlayer players who busted
     * @param dealer dealers hand
     */
    public static void endingMessage(BlackJackEngine game) {
    
        Player player;
        // Dealer got a black jack
        if (game.getPlayers().get(0).isBlackJack() == true) {
            System.out.println("The dealer got a black jack.");

            for (int i = 1; i < game.getPlayers().size(); i++) {
                player = game.getPlayers().get(i);

                // If any players got a black jack
                if (player.isBlackJack() == true) {
                    System.out.println(player.getName() + " also got a black jack. They tied with the Dealer. This means they retain their $" + player.getBet() + " bet.");
                } 
                
                // Player loses without busting
                else if (player.getStatus() == Player.Status.STOOD) {
                    System.out.println(player.getName() + " did not get a black jack so they lost. This means they lose their $" + player.getBet() + " bet.");
                } 
                
                // Player loses with busting
                else {
                    System.out.println(player.getName() + " busted so they lost. This means they lose their $" + player.getBet() + " bet.");
                }
            }

        }

        // Dealer stood without a black jack
        else if (game.getPlayers().get(0).getStatus() == Player.Status.STOOD) {
            System.out.println("The dealer got " + game.getPlayers().get(0).getPoints() + " points.");

            for (int i = 1; i < game.getPlayers().size(); i++) {
                player = game.getPlayers().get(i);

                // If any players got a black jack
                if (player.isBlackJack() == true) {
                    System.out.println(player.getName() + " got a black jack. Therefore they beat the dealer. This means they win 1.5 times their original bet which is $" + Math.round(player.getBet() * 1.5));
                }

                // Player loses with busting
                else if (player.getStatus() == Player.Status.BUST) {
                    System.out.println(player.getName() + " busted so they lost. This means they lose their $" + player.getBet() + " bet.");
                }

                // If any players beat the dealer
                else if (player.getPoints() > game.getPlayers().get(0).getPoints()) {
                    System.out.println(player.getName() + " got " + player.getPoints() + " points. Therefore they beat the dealer. This means they win their $ " + player.getBet() + " bet.");
                }

                // If any players tied the dealer
                else if (player.getPoints() == game.getPlayers().get(0).getPoints()) {
                    System.out.println(player.getName() + " got the same amount of points as the dealer. They retain their $" + player.getBet() + " bet.");
                } 
                
                // Player loses without busting
                else {
                    System.out.println(player.getName() + " got " + player.getPoints() + " points. Therefore they lost. This means they lose their $" + player.getBet() + " bet.");
                }
            }
            
        }

        // Dealer busted
        else {
            System.out.println("The dealer busted");

            for (int i = 1; i < game.getPlayers().size(); i++) {
                player = game.getPlayers().get(i);
                
                // If any players got a black jack
                if (player.isBlackJack() == true) {
                    System.out.println(player.getName() + " got a black jack. Therefore they beat the dealer. This means they win 1.5 times their original bet which is $" + (Math.round((player.getBet() * 1.5) * 100)/100) + ".");
                }

                // All other stood players won
                else if (player.getStatus() == Player.Status.STOOD) {
                    System.out.println(player.getName() + " did not bust. Therefore they beat the dealer. This means they won their $" + player.getBet() + " bet.");
                }

                // Player loses with busting
                else {
                    System.out.println(player.getName() + " busted so they lost. This means they lose their $" + player.getBet() + " bet.");
                }

            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        
        BlackJackEngine game = new BlackJackEngine();

        // TO DO: Add intro message with rule set

        // Get input for number of decks for the game
        boolean deckNumInputFlag = true;
        String deckNumInput = "";

        do {
            deckNumInputFlag = true;
            System.out.println("How many regular card decks would you like in the BlackJack deck? You can have between 1 and 8.");
            deckNumInput = keyboard.nextLine();

            // Check if input is a whole number
            if (!isWholeNumber(deckNumInput)) {
                deckNumInputFlag = false;
                System.out.println("Error, input is not a whole number. Only whole number inputs are accepted.");
            } else {

                // Check if input is below 1
                if (Integer.parseInt(deckNumInput) < 1 || Integer.parseInt(deckNumInput) > 8) {
                    deckNumInputFlag = false;
                    System.out.println("Error, input is lower than 1 or greater than 8. Only numbers greater than 0 are accepted and less than 9 accepted.");
                }
            }

        } while(!deckNumInputFlag);

        int numDecks = Integer.parseInt(deckNumInput);

        // Get input for number of players in game
        boolean playerNumInputFlag = true;
        String playerNumInput = "";

        do {
            playerNumInputFlag = true;
            System.out.println("How many players are you playing with? You can have between 1 and 8.");
            playerNumInput = keyboard.nextLine();

            // Check if input is a whole number
            if (!isWholeNumber(playerNumInput)) {
                playerNumInputFlag = false;
                System.out.println("Error, input is not a whole number. Only whole number inputs are accepted.");
            } else {

                // Check if input is below 1
                if (Integer.parseInt(playerNumInput) < 1 || Integer.parseInt(playerNumInput) > 8) {
                    playerNumInputFlag = false;
                    System.out.println("Error, input is lower than 1 or greater than 8. Only numbers greater than 0 are accepted and less than 9 accepted.");
                }
            }

        } while(!playerNumInputFlag);

        System.out.println();

        int numPlayers = Integer.parseInt(playerNumInput) + 1;
        ArrayList<String> playerNames = new ArrayList<>();
        playerNames.add("Dealer");

         // Set player bets and names
         for (int i = 1; i < numPlayers; i++) {

            boolean playerNameInputFlag = true;
            String playerName = "";

            do {
                playerNameInputFlag = true;
                System.out.println("Enter the name of player " + i);
                playerName = keyboard.nextLine();

                // Check if name exists already
                if (playerNames.contains(playerName)) {
                    playerNameInputFlag = false;
                    System.out.println("\nName is already taken try again.\n");
                }

            } while(!playerNameInputFlag);

            boolean playerBetInputFlag = true;
            String playerBetInput = "";

            do {
                playerBetInputFlag = true;
                System.out.println("\n" + playerName + " enter your bet for this game, in dollars:");
                playerBetInput = keyboard.nextLine();

                System.out.println();

                if (!isWholeNumber(playerBetInput)) {
                    System.out.println("Error, input is not a whole number. Only whole number inputs are accepted.\n");
                    playerBetInputFlag = false;
                } else {

                    if (Integer.parseInt(playerBetInput) < 1) {
                        System.out.println("Error, input is lower than 1. Only numbers greater than 0 are accepted.\n");
                        playerBetInputFlag = false;
                    }
                }
            } while(!playerBetInputFlag);
            int playerBet = Integer.parseInt(playerBetInput);

            playerNames.add(playerName);
            game.addPlayer(playerName, playerBet);
        }

        continueMessage("Start Game?", 2000);

        Card[][] listOfCards;
        if (numDecks == 1) {
            listOfCards = game.initializeGame();
        } else {
            listOfCards = game.initializeGame(numDecks);
        }
        

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < numPlayers; j++) {
                
                // Hide dealer's hole card
                if (i != 1 || j != 0) {
                    System.out.println(playerNames.get(j) + " got: " + listOfCards[i][j].getName() + "\n");
                    Thread.sleep(2000);
                }
            }
        }

        continueMessage("All players have recieved their second card.", 3000);

        while (game.keepPlaying() == true) {
            for (int i = numPlayers - 1; i > -1; i--) {
                Player player = game.getPlayers().get(i);
                if (player.keepPlaying() == true) {
                    if (i == 0) {
                        game.dealerTurn();
                    } else {
                        playerTurn(game, player);

                        if (player.getStatus() == Player.Status.STOOD && player.findAces().size() != 0) {
                            ArrayList<Card> aces = player.findAces();

                            boolean aceValueInputFlag = true;
                            String aceValueInput = "";

                            System.out.println("\nYou have " + aces.size() + " ace(s) what final values would you like to give each of them. 1 or 11?");

                            for (int j = 0; j < aces.size(); j++) {

                                do {
                                    aceValueInputFlag = true;
                                    System.out.println("What value would you like to give the " + aces.get(j).getName());
                                    aceValueInput = keyboard.nextLine();

                                    // Check if input is whole number
                                    if (!isWholeNumber(aceValueInput)) {
                                        aceValueInputFlag = false;
                                        System.out.println("Error, input is not a whole number. Only whole number inputs are accepted\n");
                                    } else {

                                        // Check if input is 1 or 11
                                        if (Integer.parseInt(aceValueInput) != 1 && Integer.parseInt(aceValueInput) != 11) {
                                            aceValueInputFlag = false;
                                            System.out.println("Error, input is not 1 or 11. Aces can only have a value of 1 or 11\n");
                                        }
                                    }
                                } while(!aceValueInputFlag);

                                if (Integer.parseInt(aceValueInput) == 11) {
                                    aces.get(j).setAceValueSwitch(true);
                                }
                            }
                            continueMessage();
                        }
                        
                    }

                    if (player.getStatus() == Player.Status.BUST) {
                        System.out.println(CLEAR_CONSOLE);
                        continueMessage(playerNames.get(i) + " busted.", 2000);
                    } else if (player.getStatus() == Player.Status.STOOD) {
                        System.out.println(CLEAR_CONSOLE);
                        continueMessage(playerNames.get(i) + " stood.", 2000);
                    } else {
                        continueMessage();
                    }
                }
            }
        }

        endingMessage(game);
    }
}
