import java.util.*;

public class BoardState {

    public int[][] board;
    public int turn;
    public boolean pieceSelected;
    public boolean pieceMoved;
    public BoardSpace selectedPiece;
    List<BoardSpace> possibleMoves;

    public static final int SIZE = 8;

    public BoardState() {
        this.board = new int[SIZE][SIZE];
        int center = SIZE / 2;
        this.board[center - 1][center - 1] = 1; // player 1
        this.board[center][center - 1] = 2;     // player 2
        this.board[center - 1][center] = 2;     // player 2
        this.board[center][center] = 1;         // player 1
        this.turn = 1;
        this.pieceSelected = false;
        this.pieceMoved = false;
        this.selectedPiece = new BoardSpace(0, 0);
        this.possibleMoves = new ArrayList<BoardSpace>();
    }

    public void selectPiece(int col, int row) {
        this.selectedPiece = new BoardSpace(col, row);
        this.pieceSelected = true;

        this.findPossibleMoves();
    }

    public void deselectPiece() {
        this.pieceSelected = false;
        this.pieceMoved = false;

        this.possibleMoves.clear();
    }


    public void movePiece(int col, int row) {
        this.board[col][row] = this.board[this.selectedPiece.col][this.selectedPiece.row]; // move piece
        this.board[this.selectedPiece.col][this.selectedPiece.row] = 0; // clear old spot
        this.selectedPiece = new BoardSpace(col, row); // set selectedPiece to the new location
        this.pieceMoved = true;

        this.findPossibleMoves();
    }
    
    public void createWall(int col, int row) {
        this.board[col][row] = -this.turn; // place wall

        this.switchTurns();
    }
    
    public void switchTurns() {
        this.turn = turn == 1 ? 2 : 1;
        this.deselectPiece();
    }

    public void findPossibleMoves() {
        this.possibleMoves.clear();

        int row = this.selectedPiece.row;
        int col = this.selectedPiece.col;

        int numRows = this.board.length;
        int numCols = this.board[0].length;

        // 8 directions: N, S, E, W, NE, NW, SE, SW
        int[][] directions = {
            {0, 1},
            {0, -1},
            {1, 0},
            {-1, 0},
            {1, 1},
            {1, -1},
            {-1, 1},
            {-1, -1}
        };

        for (int[] dir : directions) {
            int dRow = dir[0];
            int dCol = dir[1];

            int r = row + dRow;
            int c = col + dCol;

            // keep moving in this direction
            while (r >= 0 && r < numRows && c >= 0 && c < numCols) {
                // stop if space is not empty
                if (this.board[c][r] != 0) {
                    break;
                }

                this.possibleMoves.add(new BoardSpace(c, r));

                r += dRow;
                c += dCol;
            }
        }
    }
}