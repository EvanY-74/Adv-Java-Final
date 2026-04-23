public class BoardSpace {
    public int row;
    public int col;

    public BoardSpace(int col, int row) {
        this.col = col;
        this.row = row;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BoardSpace other = (BoardSpace) o;
        return this.col == other.col && this.row == other.row;
    }
}
