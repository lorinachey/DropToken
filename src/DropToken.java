/**
 * DropToken class runs each game of Drop Token.
 *
 * Created by Lorin on 12/22/2018.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class DropToken {

    // The data structure for the game board
    private int[][] board;

    // Keeps track of which player is currently playing (1 = player one, 2 = player two)
    private int player;

    // Width and height of the square board
    private static final int BOARD_DIMENSION = 4;

    // Keeps a list of the columns that have had tokens successfully put in them
    private List<Integer> columnsPlayed;

    public DropToken() {
        this.board = new int[BOARD_DIMENSION][BOARD_DIMENSION];
        this.player = 1;
        this.columnsPlayed = new ArrayList<Integer>();
    }

    public static void main(String[] args) {
        Scanner scanIn = new Scanner(System.in);
        DropToken dropTokenGame = new DropToken();
        boolean gameOver = false;
        printWelcomeMessage();

        while (!gameOver) {
            String playerMove = scanIn.nextLine().toUpperCase();

            if (playerMove.contains("PUT ")) {
                boolean isSuccess = dropTokenGame.putTokenOnBoard(playerMove);
                if (isSuccess) {
                    if (dropTokenGame.checkForWin()) {
                        System.out.println("WIN");
                    } else if(dropTokenGame.checkForDraw()){
                        System.out.println("DRAW");
                    } else {
                        System.out.println("OK");
                    }
                    dropTokenGame.changePlayerTurn();
                } else {
                    System.out.println("ERROR");
                }
            } else {
                switch (playerMove) {
                    case "GET":
                        dropTokenGame.printColumnsPlayed();
                        break;
                    case "BOARD":
                        dropTokenGame.printCurrentBoard();
                        break;
                    case "EXIT":
                        gameOver = true;
                        break;
                    default:
                        printHelpMessage();
                        break;
                }
            }
        }
        scanIn.close();
    }

    /*
     A draw occurs when there are no more moves and there has not been a win
     The win is checked for by the switch statement in the main method, so
     this method only needs to check the size of the columns played.
     */
    private boolean checkForDraw() {
        return (columnsPlayed.size() == 16);
    }

    /*
     Checks for a win by checking rows, then columns, then both diagonals.
     */
    private boolean checkForWin() {
        // check rows
        int rowCount = 0;
        for (int i = 0; i < BOARD_DIMENSION; i++) {
            int rowMatch = this.board[i][0];
            rowCount = 0;
            for (int j = 0; j < BOARD_DIMENSION; j++) {
                if (this.board[i][j] == rowMatch
                        && rowMatch != 0) {
                    rowCount++;
                    if (rowCount == BOARD_DIMENSION) {
                        return true;
                    }
                }
            }
        }

        // check columns
        int col = 0;
        for (int i = 0; i < BOARD_DIMENSION; i++) {
            int colMatch = this.board[i][col];
            int colCount = 0;
            for (int j = 0; j < BOARD_DIMENSION; j++) {
                if (this.board[j][i] == colMatch
                        && colMatch != 0) {
                    colCount++;

                    if (colCount == BOARD_DIMENSION) {
                        return true;
                    }
                }
            }
            col++;
        }

        // check right to left diagonal
        int diagonalCount = 0;
        int diagMatch = this.board[0][0];
        for (int i = 0; i < BOARD_DIMENSION; i++) {
            if (this.board[i][i] == diagMatch
                    && diagMatch != 0) {
                diagonalCount++;
            }
        }
        if (diagonalCount == BOARD_DIMENSION) {
            return true;
        }

        //check left to right diagonal
        diagonalCount = 0;
        diagMatch = this.board[0][BOARD_DIMENSION - 1];
        for (int i = 0; i < BOARD_DIMENSION; i++) {
            if (this.board[i][(BOARD_DIMENSION - i) - 1] == diagMatch
                    && diagMatch != 0) {
                diagonalCount++;
            }
        }
        if (diagonalCount == BOARD_DIMENSION) {
            return true;
        }

        return false;
    }

    private void changePlayerTurn() {
        if (player == 1) {
            player = 2;
        } else {
            player = 1;
        }
    }

    /*
     Adds a token to the board if possible. Returns true if the
     token is successfully added and false otherwise.
     */
    private boolean putTokenOnBoard(String playerMove) {
        String[] args = playerMove.split(" ");

        if (args.length > 2 || args[1].length() > 1) {
            printHelpMessage();
            return false;
        }

        try {
            int column = parseInt(args[1]) - 1;

            if (column < 0 || column > 3) {
                printHelpMessage();
                return false;
            }
            for (int i = 3; i >= 0; i--) {
                if (this.board[i][column] == 0) {
                    this.board[i][column] = this.player;
                    this.columnsPlayed.add(column + 1);
                    return true;
                }
            }
        } catch (NumberFormatException e) {
            printHelpMessage();
        }
        return false;
    }

    private void printCurrentBoard() {
        for (int i = 0; i < BOARD_DIMENSION; i++) {
            System.out.print("\n|");
            for (int j = 0; j < BOARD_DIMENSION; j++) {
                System.out.print(" " + this.board[i][j]);
            }
            System.out.println();
        }
        System.out.println("\n+--------");
        System.out.println("  1 2 3 4");
    }

    private void printColumnsPlayed() {
        columnsPlayed.forEach((value) -> System.out.println(value));
    }

    private static void printWelcomeMessage() {
        System.out.println( "\nWelcome to Drop Token, a two-player game of strategy!\n" +
                "      Type PUT <Column Number> to place a token.\n" +
                "      Type GET to see the columns played, and BOARD to see the board.\n" +
                "      Type EXIT to quit the game.\n" +
                "Have fun! \n");
    }

    private static void printHelpMessage() {
        System.out.println("\n It appears your instruction is not valid. \n" +
                " Please try again using only the following commands:" +
                " PUT <Column #1-4>, GET, BOARD, EXIT \n");
    }
}
