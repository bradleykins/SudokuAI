package sudokuai;

/**
 * Concrete Sudoku_AI algorithm implementation using Backtracking
 *
 * @author Nik Bradley
 */
public class Sudoku_AI_Backtracking implements Sudoku_AI {

    //CLASS VARIABLES
    private static int BOXSIZE;                                                                 //Game size 3*3 standard 
    private static int steps = 0;   
    private static int BOARDSIZE;                                                               //length of rows & columns                 
    private static final String NAME = "Backtracking";                                          //Algorithm Name, used by SudokuSolverModel_AI class
    
    //DATA STRUCTURES
    private Integer[][] solution;                                                              //Game board 2d array storing cells

    /**
     * Constructor for the backtracking algorithm, takes a box size and generates a matching grid
     * @param gameBoxSize 
     */
    public Sudoku_AI_Backtracking(int gameBoxSize) {
        BOXSIZE = gameBoxSize;
        BOARDSIZE = BOXSIZE * BOXSIZE;
    }

    /**
     * Overrode method for getSolution, sets the puzzle board and then tries to
     * solve it
     *
     * @param userInputPuzzle the user input for the game board
     * @return solution to puzzle or empty game board
     */
    @Override
    public Integer[][] getSolution(Integer[][] userInputPuzzle) {
        this.solution = userInputPuzzle;

        if (solveSudoku()) {
            return solution;                                                                    //If a puzzle has been solved return it
        } else {
            return new Integer[BOARDSIZE][BOARDSIZE];                                           //If puzzle couldnt be solved return blank puzzle
        }
    }

    /**
     * The solve algorithm performs a recursive search through all possible
     * values in the domains and attempts to assign them to a cell starting from
     * the upper left cell and proceeds to the bottom right (completing a row
     * before moving to the next). Domains remain unchanged and are 1-9 for all
     * cells
     *
     * @return boolean true if all cells have been assigned values it.
     */
    private boolean solveSudoku() {
        int[] nextCell = getNextEmptyCell();                                                    //Find the next empty cell to assign

        if (solution[nextCell[0]][nextCell[1]] != null) {                                       //If the cell we have has data in it, then we have a completed solution
            System.out.println("Steps = " + steps);                                             //Displays in the console the total steps taken to find the solution
            return true;
        }

        for (int value = 1; value < BOARDSIZE+1; value++) {                                     //Loop through each value in the domain (domain = 1-9)
            steps++;                                                                            //This is a step so increment them
            if (isSafe(value, nextCell, solution)) {                                            //If the value has not been used in the same row, column or box then proceed

                solution[nextCell[0]][nextCell[1]] = value;                                     //Assign the value to the current cell

                if (solveSudoku()) {                                                            //Try to assign the next cell a value
                    return true;                                                                //If the puzzle is completed, true will be returned
                }

                solution[nextCell[0]][nextCell[1]] = null;                                      //Backtracking initiated, remove value from cell
            }                                                                                   //Try the next value
        }
        return false;                                                                           //triggers backtracking    
    }

    /**
     * Finds the next empty cell and returns it
     *
     * @return cell is a 1d array of size two, index 0 = row of cell & index 1 =
     * column of cell
     */
    private int[] getNextEmptyCell() {
        int[] cell = new int[2];

        for (int i = 0; i < BOARDSIZE; i++) {
            for (int j = 0; j < BOARDSIZE; j++) {
                if (solution[i][j] == null) {                                                   //If the current cell has no value then return it
                    cell[0] = i;
                    cell[1] = j;
                    return cell;
                }
            }
        }
        return cell;                                                                            //Return the co-ords of the 1st cell
    }

    /**
     * Check to see if the currently selected value is used in the current row
     *
     * @param value currently selected value
     * @param row current row
     * @param board a supplied puzzle
     * @return false if used already, true if not used yet
     */
    private boolean isSafeToUseInRow(int value, int row, Integer[][] board) {
        for (int i = 0; i < BOARDSIZE; i++) {
            if (board[row][i] == null) {
            } //Ensure that the cell has a value
            else if (board[row][i] == value) {                                                  //If the cell has a value and it matches the currently selected value then return false as its already in use                  
                return false;
            }
        }
        return true;                                                                            //If the currently selected value has not been used then return true - it can be used safely
    }

    /**
     * Check to see if the currently selected value is used in the current
     * column
     *
     * @param value currently selected value
     * @param column current column
     * @param board a supplied puzzle
     * @return false if used already, true if not used yet
     */
    private boolean isSafeToUseInColumn(int value, int column, Integer[][] board) {
        for (int i = 0; i < BOARDSIZE; i++) {
            if (board[i][column] == null) {
            } //Ensure that the cell has a value
            else if (board[i][column] == value) {                                               //If the cell has a value and it matches the currently selected value then return false as its already in use                  
                return false;
            }
        }
        return true;                                                                            //If the currently selected value has not been used then return true - it can be used safely
    }

    /**
     * Check to see if the currently selected value is used in the current box
     *
     * @param value currently selected value
     * @param cell current cell being assigned
     * @param board a supplied puzzle
     * @return false if used already, true if not used yet
     */
    private boolean isSafeToUseInBox(int value, int[] cell, Integer[][] board) {

        int boxRow = cell[0] - (cell[0] % BOXSIZE);                                             //BoxRow is the starting row of the current box, determined by the cells x coord - the mod by BOXSIZE
        int boxCol = cell[1] - (cell[1] % BOXSIZE);                                             //BoxCol is the starting column of the current box, determined by the cells y coord - the mod by BOXSIZE

        for (int i = 0; i < BOXSIZE; i++) {                                                     //For each row in the box
            for (int j = 0; j < BOXSIZE; j++) {                                                 //For each column in the box
                if (board[i + boxRow][j + boxCol] == null) {
                } //Ensure that the cell has a value
                else if (board[i + boxRow][j + boxCol] == value) {                              //If the cell has a value and it matches the currently selected value then return false as its already in use                  
                    return false;
                }
            }
        }

        return true;                                                                            //If the currently selected value has not been used then return true - it can be used safely
    }

    /**
     * Check to see if the currently selected value is used in the current row,
     * column or box
     *
     * @param value currently selected value
     * @param cell current cell being assigned
     * @param board a supplied puzzle
     * @return false if used already, true if not used yet
     */
    private boolean isSafe(int value, int[] cell, Integer[][] board) {
        return isSafeToUseInRow(value, cell[0], board) && isSafeToUseInColumn(value, cell[1], board) && isSafeToUseInBox(value, cell, board);
    }

    /**
     * Used to get a string ID of the class
     *
     * @return NAME static Class Variable. Contains a string ID for the
     * algorithm class
     */
    @Override
    public String getName() {
        return NAME;
    }

}
