import java.util.Scanner;

public class Panel {

    private static final int BOARD_SIZE = 8;
    private BoardState boardState;

    public Panel(BoardState boardState) {
        this.boardState = boardState;
    }

    /**
     * Replaces showWindow. 
     * This starts a loop that listens for console input to simulate mouse clicks.
     */
    public static void showWindow(BoardState boardState) {
        Panel tui = new Panel(boardState);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            tui.render();
            System.out.println("\nTurn: Player " + boardState.turn);
            System.out.print("Enter row and col (e.g., 'f7') or 'q' to quit: ");
            
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("q")) break;

            try {
                if (input.length() != 2) throw new IllegalArgumentException();
                int col = (int)(Character.toLowerCase(input.charAt(0)) - 'a');
                if (col < 0 || col >= BOARD_SIZE) throw new IllegalArgumentException();
                int row = Integer.parseInt(String.valueOf(input.charAt(1))) - 1;
                if (row < 0 || row >= BOARD_SIZE) throw new IllegalArgumentException();


                // Simulate the logic from your MouseListener
                tui.handleInput(col, row);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input. Please enter a valid square.");
                boardState.pieceSelected = false;
                boardState.pieceMoved = false;
            }
        }
        scanner.close();
    }

    private void handleInput(int col, int row) {
        // if (row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE) return;

        System.out.println("Selected tile: (" + row + ", " + col + ")");
        
        if (!boardState.pieceSelected) {
            if (boardState.board[col][row] == boardState.turn) {
                boardState.selectPiece(col, row);
            } else {
                boardState.deselectPiece();
            }
        } else if (!boardState.pieceMoved) {
            if (boardState.possibleMoves.contains(new BoardSpace(col, row))) {
                boardState.movePiece(col, row);
            } else {
                System.out.println("Invalid move.");
                boardState.deselectPiece();
            }
        } else {
            if (boardState.possibleMoves.contains(new BoardSpace(col, row))) {
                boardState.createWall(col, row);
            } else {
                System.out.println("Invalid wall placement. Choice a valid space.");
            }
        }
    }

    public void render() {
        // Clear screen (works in most terminals)
        // System.out.print("\033[H\033[2J");
        System.out.flush();

        // Print Column Headers
        System.out.print("    ");
        for (int i = 0; i < BOARD_SIZE; i++) System.out.print((char)('a' + i) + "   ");
        System.out.println("\n  " + "----".repeat(BOARD_SIZE) + "-");

        for (int row = 0; row < BOARD_SIZE; row++) {
            // Print Row Header
            System.out.print(row + 1 + " | ");

            for (int col = 0; col < BOARD_SIZE; col++) {
                int val = boardState.board[col][row];
                String cell = getSprite(val, col, row);
                
                System.out.print(cell + " | ");
            }
            System.out.println("\n  " + "----".repeat(BOARD_SIZE) + "-");
        }
    }

    private String getSprite(int val, int col, int row) {
        // Check if this cell is a "Possible Move" dot
        if (boardState.possibleMoves != null) {
            for (BoardSpace move : boardState.possibleMoves) {
                if (move.row == row && move.col == col) return ".";
            }
        }

        // Check if this cell is the selected piece
        if (boardState.pieceSelected && boardState.selectedPiece != null) {
            if (boardState.selectedPiece.row == row && boardState.selectedPiece.col == col) {
                return "X"; // Highlight selected with an X
            }
        }

        switch (val) {
            case 1:
                return "1";
            case 2:
                return "2";
            case -1:
                return "@"; // Wall 1
            case -2:
                return "O"; // Wall 2
            default:
                return " "; // Empty space (0)
        }
    }
}
