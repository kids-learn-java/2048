import java.util.Scanner;

enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

class Board {
    int grid[][];
    int numRows, numCols;
    int maxNum;

    public Board(int theNumRows, int theNumCols, int theMaxNum) {
        maxNum = theMaxNum;
        numRows = theNumRows;
        numCols = theNumCols;
        grid = new int[numRows][numCols];
    }

    public boolean win() {
        int max = 0;
        for (int row = 0; row < numRows; ++row) {
            for (int col = 0; col < numCols; ++col) {
                if (max < grid[row][col]) {
                    max = grid[row][col];
                }
            }
        }
        return max == maxNum;
    }

    public boolean lost() {
        if (countEmpty() != 0) {
            return false;
        }
        for (int row = 0; row < numRows; ++row) {
            for (int col = 0; col < numCols - 1; ++col) {
                if (grid[row][col] == grid[row][col + 1]) {
                    return false;
                }
            }
        }
        for (int row = 0; row < numRows - 1; ++row) {
            for (int col = 0; col < numCols; ++col) {
                if (grid[row][col] == grid[row + 1][col]) {
                    return false;
                }
            }
        }
        return true;
    }

    public String toString() {
        String str = "";
        for (int[] row : grid) {
            for (int n : row) {
                str += String.format("%4d ", n);
            }
            str += "\n";
        }
        return str;
    }

    // return false if it cannot slide
    public void slide(Direction dir) {
        boolean changed = false;

        switch (dir) {
            case UP:
                changed = moveUp();
                break;
            case DOWN:
                changed = moveDown();
                break;
            case LEFT:
                changed = moveLeft();
                break;
            case RIGHT:
                changed = moveRight();
                break;
        }
        if (changed) {
            addRandomNumber();
        } else {
            System.out.println("Nothing moved. Try another direction");
        }
    }

    int countEmpty() {
        int numEmpty = 0;
        for (int row = 0; row < numRows; ++row) {
            for (int col = 0; col < numCols; ++col) {
                if (grid[row][col] == 0) {
                    ++numEmpty;
                }
            }
        }
        return numEmpty;
    }

    boolean addRandomNumber() {
        int numEmpty = countEmpty();
        if (numEmpty == 0) {
            return false;
        }
        int target = (int)(Math.random() * numEmpty);
        numEmpty = 0;
        for (int row = 0; row < numRows; ++row) {
            for (int col = 0; col < numCols; ++col) {
                if (grid[row][col] == 0) {
                    if (numEmpty == target) {
                        grid[row][col] = 2;
                    }
                    ++numEmpty;
                }
            }
        }
        return true;
    }

    boolean moveUp() {
        boolean moved = false;
        for (int i = 0; i < numCols; ++i) {
            boolean m1, m2, m3;
            m1 = moveColUp(i);
            m2 = mergeColUp(i);
            m3 = moveColUp(i);
            moved = moved || m1 || m2 || m3;
        }
        return moved;
    }

    boolean moveDown() {
        boolean moved = false;
        for (int i = 0; i < numCols; ++i) {
            moved |= moveColDown(i);
            moved |= mergeColDown(i);
            moved |= moveColDown(i);
        }
        return moved;
    }

    boolean moveLeft() {
        boolean moved = false;
        for (int i = 0; i < numRows; ++i) {
            moved |= moveRowLeft(i);
            moved |= mergeRowLeft(i);
            moved |= moveRowLeft(i);
        }
        return moved;
    }

    boolean moveRight() {
        boolean moved = false;
        for (int i = 0; i < numRows; ++i) {
            moved |= moveRowRight(i);
            moved |= mergeRowRight(i);
            moved |= moveRowRight(i);
        }
        return moved;
    }

    boolean moveRowLeft(int row) {
        boolean changed = false;
        int to = 0;
        for (int from = 0; from < grid[row].length; ++from) {
            if (grid[row][from] != 0) {
                if (from != to) {
                    grid[row][to] = grid[row][from];
                    grid[row][from] = 0;
                    changed = false;
                }
                ++to;
            }
        }
        return changed;
    }

    boolean moveRowRight(int row) {
        boolean changed = false;
        int to = grid[row].length - 1;
        for (int from = grid[row].length - 1; from >= 0; --from) {
            if (grid[row][from] != 0) {
                if (from != to) {
                    grid[row][to] = grid[row][from];
                    grid[row][from] = 0;
                    changed = true;
                }
                --to;
            }
        }
        return changed;
    }

    boolean moveColUp(int col) {
        boolean changed = false;
        int to = 0;
        for (int from = 0; from < numRows; ++from) {
            if (grid[from][col] != 0) {
                if (from != to) {
                    grid[to][col] = grid[from][col];
                    grid[from][col] = 0;
                    changed = true;
                }
                ++to;
            }
        }
        return changed;
    }

    boolean moveColDown(int col) {
        boolean changed = false;
        int to = numRows - 1;
        for (int from = numRows - 1; from >= 0; --from) {
            if (grid[from][col] != 0) {
                if (from != to) {
                    grid[to][col] = grid[from][col];
                    grid[from][col] = 0;
                    changed = true;
                }
                --to;
            }
        }
        return changed;
    }

    boolean mergeRowLeft(int row) {
        boolean changed = false;
        for (int col = 0; col < numCols - 1; ++col) {
            if (grid[row][col] != 0 && grid[row][col] == grid[row][col + 1]) {
                grid[row][col] *= 2;
                grid[row][col + 1] = 0;
                changed = true;
            }
        }
        return changed;
    }

    boolean mergeRowRight(int row) {
        boolean changed = false;
        for (int col = numCols - 1; col > 0; --col) {
            if (grid[row][col] != 0 && grid[row][col] == grid[row][col - 1]) {
                grid[row][col] *= 2;
                grid[row][col - 1] = 0;
                changed = true;
            }
        }
        return changed;
    }

    boolean mergeColUp(int col) {
        boolean changed = false;
        for (int row = 0; row < numRows - 1; ++row) {
            if (grid[row][col] != 0 && grid[row][col] == grid[row + 1][col]) {
                grid[row][col] *= 2;
                grid[row + 1][col] = 0;
                changed = true;
            }
        }
        return changed;
    }

    boolean mergeColDown(int col) {
        boolean changed = false;
        for (int row = numRows - 1; row > 0; --row) {
            if (grid[row][col] != 0 && grid[row][col] == grid[row - 1][col]) {
                grid[row][col] *= 2;
                grid[row - 1][col] = 0;
                changed = true;
            }
        }
        return changed;
    }

    static void testMoveLeftRight(int before[], int after[], Direction dir) {
        Board board = new Board(1, 4, 2048);
        for (int i = 0; i < before.length; ++i) {
            board.grid[0][i] = before[i];
        }
        if (dir == Direction.LEFT) {
            board.moveRowLeft(0);
        } else {
            board.moveRowRight(0);
        }
        for (int i = 0; i < before.length; ++i) {
            assert board.grid[0][i] == after[i];
        }
    }

    static void testMoveUpDown(int before[], int after[], Direction dir) {
        Board board = new Board(4, 1, 2048);
        for (int i = 0; i < before.length; ++i) {
            board.grid[i][0] = before[i];
        }
        if (dir == Direction.UP) {
            board.moveColUp(0);
        } else {
            board.moveColDown(0);
        }
        for (int i = 0; i < before.length; ++i) {
            assert board.grid[i][0] == after[i];
        }
    }

    static void testMergeLeftRight(int before[], int after[], Direction dir) {
        Board board = new Board(1, 4, 2048);
        for (int i = 0; i < before.length; ++i) {
            board.grid[0][i] = before[i];
        }
        if (dir == Direction.LEFT) {
            board.mergeRowLeft(0);
        } else {
            board.mergeRowRight(0);
        }
        for (int i = 0; i < before.length; ++i) {
            assert board.grid[0][i] == after[i];
        }
    }

    static private void testMergeUpDown(int before[], int after[], Direction dir) {
        Board board = new Board(4, 1, 2048);
        for (int i = 0; i < before.length; ++i) {
            board.grid[i][0] = before[i];
        }
        if (dir == Direction.UP) {
            board.mergeColUp(0);
        } else {
            board.mergeColDown(0);
        }
        for (int i = 0; i < before.length; ++i) {
            assert board.grid[i][0] == after[i];
        }
    }

    public static void test() {
        testMoveLeftRight(new int[] { 0, 0, 0, 0 }, new int[] { 0, 0, 0, 0 }, Direction.LEFT);
        testMoveLeftRight(new int[] { 4, 2, 8, 4 }, new int[] { 4, 2, 8, 4 }, Direction.LEFT);
        testMoveLeftRight(new int[] { 8, 0, 0, 0 }, new int[] { 8, 0, 0, 0 }, Direction.LEFT);
        testMoveLeftRight(new int[] { 0, 0, 0, 8 }, new int[] { 8, 0, 0, 0 }, Direction.LEFT);
        testMoveLeftRight(new int[] { 4, 2, 0, 0 }, new int[] { 4, 2, 0, 0 }, Direction.LEFT);
        testMoveLeftRight(new int[] { 2, 0, 8, 0 }, new int[] { 2, 8, 0, 0 }, Direction.LEFT);
        testMoveLeftRight(new int[] { 8, 0, 0, 2 }, new int[] { 8, 2, 0, 0 }, Direction.LEFT);

        testMoveLeftRight(new int[] { 0, 0, 0, 0 }, new int[] { 0, 0, 0, 0 }, Direction.RIGHT);
        testMoveLeftRight(new int[] { 0, 0, 8, 0 }, new int[] { 0, 0, 0, 8 }, Direction.RIGHT);
        testMoveLeftRight(new int[] { 4, 0, 8, 0 }, new int[] { 0, 0, 4, 8 }, Direction.RIGHT);
        testMoveLeftRight(new int[] { 4, 2, 8, 0 }, new int[] { 0, 4, 2, 8 }, Direction.RIGHT);

        testMoveUpDown(new int[] { 0, 0, 0, 0 }, new int[] { 0, 0, 0, 0 }, Direction.UP);
        testMoveUpDown(new int[] { 4, 2, 8, 4 }, new int[] { 4, 2, 8, 4 }, Direction.UP);
        testMoveUpDown(new int[] { 8, 0, 0, 0 }, new int[] { 8, 0, 0, 0 }, Direction.UP);
        testMoveUpDown(new int[] { 0, 0, 0, 8 }, new int[] { 8, 0, 0, 0 }, Direction.UP);
        testMoveUpDown(new int[] { 4, 2, 0, 0 }, new int[] { 4, 2, 0, 0 }, Direction.UP);
        testMoveUpDown(new int[] { 2, 0, 8, 0 }, new int[] { 2, 8, 0, 0 }, Direction.UP);
        testMoveUpDown(new int[] { 8, 0, 0, 2 }, new int[] { 8, 2, 0, 0 }, Direction.UP);

        testMoveUpDown(new int[] { 0, 0, 0, 0 }, new int[] { 0, 0, 0, 0 }, Direction.DOWN);
        testMoveUpDown(new int[] { 0, 0, 8, 0 }, new int[] { 0, 0, 0, 8 }, Direction.DOWN);
        testMoveUpDown(new int[] { 4, 0, 8, 0 }, new int[] { 0, 0, 4, 8 }, Direction.DOWN);
        testMoveUpDown(new int[] { 4, 2, 8, 0 }, new int[] { 0, 4, 2, 8 }, Direction.DOWN);

        testMergeLeftRight(new int[] { 4, 2, 16, 8 }, new int[] { 4, 2, 16, 8 }, Direction.LEFT);
        testMergeLeftRight(new int[] { 4, 4, 8, 8 }, new int[] { 8, 0, 16, 0 }, Direction.LEFT);
        testMergeLeftRight(new int[] { 2, 4, 4, 8 }, new int[] { 2, 8, 0, 8 }, Direction.LEFT);

        testMergeLeftRight(new int[] { 4, 2, 16, 8 }, new int[] { 4, 2, 16, 8 }, Direction.RIGHT);
        testMergeLeftRight(new int[] { 2, 4, 4, 8 }, new int[] { 2, 0, 8, 8 }, Direction.RIGHT);

        testMergeUpDown(new int[] { 4, 2, 8, 2 }, new int[] { 4, 2, 8, 2 }, Direction.UP);
        testMergeUpDown(new int[] { 4, 4, 8, 8 }, new int[] { 8, 0, 16, 0 }, Direction.UP);
        testMergeUpDown(new int[] { 2, 4, 4, 8 }, new int[] { 2, 8, 0, 8 }, Direction.UP);

        testMergeUpDown(new int[] { 4, 2, 8, 2 }, new int[] { 4, 2, 8, 2 }, Direction.DOWN);
        testMergeUpDown(new int[] { 2, 4, 4, 8 }, new int[] { 2, 0, 8, 8 }, Direction.DOWN);
    }
}

public class App {
    static Direction getInput(Scanner sc) {
        Direction dir = Direction.DOWN;
        while (true) {
            System.out.print("Slide direction (WASD): ");
            boolean valid = true;
            char ch = sc.next().charAt(0);
            switch (ch) {
                case 'w':
                case 'W':
                    dir = Direction.UP;
                    break;
                case 'a':
                case 'A':
                    dir = Direction.LEFT;
                    break;
                case 's':
                case 'S':
                    dir = Direction.DOWN;
                    break;
                case 'd':
                case 'D':
                    dir = Direction.RIGHT;
                    break;
                default:
                    valid = false;
            }
            if (valid) {
                break;
            }
            System.out.format("Invalid input '%c'. Please try again.", ch);
        }
        return dir;
    }

    public static void main(String[] args) throws Exception {
        Board.test();
        Board board = new Board(4, 4, 2048);
        Scanner sc = new Scanner(System.in);
        board.addRandomNumber();
        while (true) {
            System.out.println(board.toString());
            Direction dir = getInput(sc);
            board.slide(dir);
            if (board.lost()) {
                System.out.println("Game over!");
                break;
            }
            if (board.win()) {
                break;
            }
        }
        sc.close();
    }
}
