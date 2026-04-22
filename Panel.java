import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Panel extends JPanel {

    private static final int TILE_SIZE = 80;
    private static final int BOARD_SIZE = 8;

    private BoardState boardState;

    public Panel(BoardState boardState) {
        this.boardState = boardState;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = e.getX() / TILE_SIZE;
                int row = e.getY() / TILE_SIZE;
                System.out.println("Clicked tile: (" + row + ", " + col + ")");
                if (!boardState.pieceSelected) {
                    if (boardState.board[col][row] == boardState.turn) {
                        boardState.selectPiece(col, row);
                    } else boardState.deselectPiece();
                } else {
                    
                }


                // Example: update game state
                // boardState.makeMove(row, col);

                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g, boardState);
    }

    Color[] boardColors = new Color[] { new Color(245, 245, 220), new Color(139, 69, 19) };

    // public void drawBoard(Graphics g, BoardState boardState) {

    //     for (int row = 0; row < BOARD_SIZE; row++) {
    //         for (int col = 0; col < BOARD_SIZE; col++) {

    //             if ((row + col) % 2 == 0) {
    //                 g.setColor(boardColors[0]);
    //             } else {
    //                 g.setColor(boardColors[1]);
    //             }

    //             g.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    //         }
    //     }

    //     // Draw turn text
    //     g.setColor(Color.BLACK);
    //     g.drawString("Turn: Player " + (boardState.turn + 1), 10, 20);
    // }
    public void drawBoard(Graphics g, BoardState boardState) {
        Graphics2D g2 = (Graphics2D) g;

        // Smooth rendering (important for circles)
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // 1. Draw tiles
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {

                if ((row + col) % 2 == 0) {
                    g2.setColor(boardColors[0]);
                } else {
                    g2.setColor(boardColors[1]);
                }

                g2.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }

        // 2. Highlight selected piece
        if (boardState.pieceSelected && boardState.selectedPiece != null) {
            int row = boardState.selectedPiece.row;
            int col = boardState.selectedPiece.col;

            g2.setColor(new Color(255, 255, 0, 120));
            g2.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        // 3. Possible moves (dots)
        if (boardState.possibleMoves != null) {
            g2.setColor(new Color(0, 0, 0, 80));

            int dotSize = TILE_SIZE / 4;

            for (BoardSpace move : boardState.possibleMoves) {
                int x = move.col * TILE_SIZE + TILE_SIZE / 2 - dotSize / 2;
                int y = move.row * TILE_SIZE + TILE_SIZE / 2 - dotSize / 2;

                g2.fillOval(x, y, dotSize, dotSize);
            }
        }

        // 4. Draw pieces (THIS WAS MISSING)
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {

                int val = boardState.board[row][col];
                if (val == 0) continue;

                int padding = TILE_SIZE / 10;
                int size = TILE_SIZE - 2 * padding;
                int x = col * TILE_SIZE + padding;
                int y = row * TILE_SIZE + padding;

                switch (val) {

                    // PLAYER PIECES (circles)
                    case 1 -> {
                        g2.setColor(Color.WHITE);
                        g2.fillOval(x, y, size, size);
                        g2.setColor(Color.BLACK);
                        g2.drawOval(x, y, size, size);
                    }

                    case 2 -> {
                        g2.setColor(Color.BLACK);
                        g2.fillOval(x, y, size, size);
                        g2.setColor(Color.WHITE);
                        g2.drawOval(x, y, size, size);
                    }

                    // WALLS (squares)
                    case -1 -> {
                        g2.setColor(Color.WHITE.darker());
                        g2.fillRect(x, y, size, size);
                    }

                    case -2 -> {
                        g2.setColor(Color.BLACK.darker());
                        g2.fillRect(x, y, size, size);
                    }
                }
            }
        }

        // 5. Turn text
        g2.setColor(Color.BLACK);
        g2.drawString("Turn: Player " + (boardState.turn + 1), 10, 20);
    }


    @Override
    public Dimension getPreferredSize() {
        return new Dimension(TILE_SIZE * BOARD_SIZE, TILE_SIZE * BOARD_SIZE);
    }

    // Create and show window
    public static void showWindow(BoardState boardState) {
        JFrame frame = new JFrame("8x8 Board Game");
        Panel panel = new Panel(boardState);

        frame.add(panel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}