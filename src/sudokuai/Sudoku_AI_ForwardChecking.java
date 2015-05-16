package sudokuai;

/**
 * Concrete Sudoku_AI algorithm implementation using Backtracking With Forward
 * Checking
 * @author Nik
 */
class Sudoku_AI_ForwardChecking implements Sudoku_AI {

    //CLASS VARIABLES
    private static int BOXSIZE;                                                                 //Game size standard 3
    private static int BOARDSIZE;                                                               //length of rows & columns
    private static int steps = 0;                                                               //Number of steps used to solve puzzle - debug use only atm
    private boolean emptyDomainFlag;                                                            //Flag to trigger back tracking.
    private static final String NAME = "Backtracking with Forward Checking";                    //Algorithm Name, used by SudokuSolverModel_AI class
    
    //DATA STRUCTURES
    private int[] domains;                                                                      //Domains stored in 1d array
    private Integer[][] solution;                                                               //Game board 2d array storing cells

    /**
     * Constructor for the class, it takes a game box size and sets up the boards for it.
     * @param gameBoxSize box size of the game standard 3
     */
    public Sudoku_AI_ForwardChecking(int gameBoxSize) {
        BOXSIZE = gameBoxSize;
        BOARDSIZE = BOXSIZE * BOXSIZE;
        domains = new int[BOARDSIZE * BOARDSIZE];
    }

    /**
     * Creates BOARDSIZE*BOARDSIZE domains with the value 411 (9 '1' bits in
     * binary)
     */
    private void initDomains() {
        for (int i = 0; i < BOARDSIZE; i++) {
            for (int j = 0; j < BOARDSIZE; j++) {
                int cell = (i * BOARDSIZE) + j;                                                 // index of domains for cell
                domains[cell] = 0x1ff;                                                          // bits represent 1 base values eg 987654321
            }
        }
    }

    /**
     * Overrode method for getSolution, changes to interface include calling
     * initDomains() method and performing an initial forward check
     *
     * @param userInputPuzzle the user input for the game board
     * @return solution to puzzle or empty game board
     */
    @Override
    public Integer[][] getSolution(Integer[][] userInputPuzzle) {
        this.solution = userInputPuzzle;
        emptyDomainFlag = false;
        initDomains();

        initial_ForwardCheck();                                                                 //Setup domains for the board taking the user input values into account

        if (solveSudoku()) {
            return solution;                                                                    //If we get a solved puzzle return it
        } else {
            return new Integer[BOARDSIZE][BOARDSIZE];                                           //No solution, then return a blank puzzle
        }

    }

    /**
     * The solve algorithm performs a recursive search through values in the
     * domains and attempts to assign them to a cell starting from the upper
     * left cell and proceeds to the bottom right (completing a row before
     * moving to the next)
     *
     * @return boolean true if a cell was set and no domains were empty after
     * it.
     */
    private boolean solveSudoku() {
        int[] nextCell = getNextEmptyCell();                                                    //Find the next empty cell to assign

        /* checks if the cell is not empty, if so we have finished */
        if (solution[nextCell[0]][nextCell[1]] != null) {                                       //If the cell we have has data in it, then we have a completed solution
            System.out.println("Steps = " + steps);                                             //Displays in the console the total steps taken to find the solution
            return true;
        }

        int cell = (nextCell[0] * BOARDSIZE) + nextCell[1];                                     //Converts the coords of the cell into a number between 0-80 representing the cell                                          

        int[] domainSave = domains.clone();                                                     //Create a backup copy of the domains for backtracking to restore domains

        /**
         * Loops through all available values in the domain
         *
         * If it can be used within constraints, assign it and then empty the
         * relevant domains.
         *
         * If a domain has become empty of possible values, begin remove the
         * currently assigned value and assign the next possible value
         *
         * No remaining values = backtrack
         */
        int domain = domains[cell];                                                             //Copy of the domain to be used to loop through possible values
        while (domain != 0) {                                                                   //While there are still possible values in the domain run this loop
            steps++;                                                                            //Increment steps Class Variable to track the amount of steps taken to solve puzzle
            int lowestBitIndex = Integer.numberOfTrailingZeros(domain);                         //Find the index of the next possible value in the domain
            domain = domain & ~(1 << lowestBitIndex);                                           //Remove the used value from the copy of the domain

            solution[nextCell[0]][nextCell[1]] = lowestBitIndex + 1;                            //Set the value of the cell to the next possible domain value (starting at 1-9)

            emptyDomains(nextCell);                                                             //Empty the domains of linked cells

            if (!emptyDomainFlag) {                                                             //If no domain became empty from assignment then try to assign the next empty cell.
                if (solveSudoku()) {
                    return true;                                                                //If the puzzle is completed, true will be returned
                }
            }
            solution[nextCell[0]][nextCell[1]] = null;                                          //Backtracking initiated, remove value from cell
            domains = domainSave.clone();                                                       //Restore domains to the original value before assignments were made
            emptyDomainFlag = false;                                                            //Reset backtracking flag to try next possible value

        }
        domains = domainSave;                                                                   //All possible domains result in backtracking, so backtrack 1 step further and restore domains to previous state
        return false;                                                                           //Triggers backtracking
    }

    /**
     * Empties the relevant domains for the cell just updated.
     *
     * @param cell is a 1d array of size two, index 0 = row of cell & index 1 =
     * column of cell
     */
    private void emptyDomains(int[] cell) {
        fcRow(cell);                                                                            //Revise the domains for the row
        if (!emptyDomainFlag) {
            fcCol(cell);                                                                        //If the row domain revision didn't result in an empty domain, revise the column domains
        }
        if (!emptyDomainFlag) {
            fcBox(cell);                                                                        //If the row domain revision didn't result in an empty domain, revise the column domains
        }
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
     * Performs the initial forward check to revise domains
     */
    private void initial_ForwardCheck() {
        fcAllRow();                                                                             //Go through every row and remove ALL assigned values from each domain in the row
        fcAllCol();                                                                             //Go through every column and remove ALL assigned values from each domain in the column
        fcAllBox();                                                                             //Go through every box and remove ALL assigned values from each domain in the box
    }

    /**
     * Remove the values in initialValue from all of the domains in the
     * specified row
     *
     * @param row current row to revise
     * @param initialValues values to be removed from domains
     */
    private void removeRowDomainValues(int row, int initialValues) {
        for (int j = 0; j < BOARDSIZE; j++) {

            int cell = (row * BOARDSIZE) + j;                                                   //Converts the coords of the current cell into a number between 0-80 representing the cell                                          
            domains[cell] &= ~initialValues;                                                    //Remove all initialValues values from the domain

            if (solution[row][j] != null) {                                                     //If the cell contains a value add that back to the domain
                int value = solution[row][j];
                domains[cell] = (1 << value - 1);
            }

            if (domains[cell] < 1) {                                                            //If a domain shrinks to nothing we have an invalid solution, turn back now!
                emptyDomainFlag = true;
                break;
            }
        }
    }

    /**
     * Remove the values in initialValue from all of the domains in the
     * specified column
     *
     * @param col current column to revise
     * @param initialValues values to be removed from domains
     */
    private void removeColDomainValues(int col, int initialValues) {
        for (int j = 0; j < BOARDSIZE; j++) {

            int cell = (j * BOARDSIZE) + col;                                                   //Converts the coords of the current cell into a number between 0-80 representing the cell                                          
            domains[cell] &= ~initialValues;                                                    //Remove all initialValues values from the domain

            if (solution[j][col] != null) {                                                     //If the cell contains a value add that back to the domain
                int value = solution[j][col];
                domains[cell] = (1 << value - 1);
            }

            if (domains[cell] < 1) {                                                            //If a domain shrinks to nothing we have an invalid solution, turn back now!
                emptyDomainFlag = true;
                break;
            }
        }
    }

    /**
     * Remove the values in initialValue from all of the domains in the
     * specified box
     *
     * @param boxRow the row of the cell in the upper left of the box
     * @param boxCol the column of the cell in the upper left of the box
     * @param initialValues values to be removed from domains
     */
    private void removeBoxDomainValues(int boxRow, int boxCol, int initialValues) {

        escape:

        for (int cellRow = boxRow; cellRow < boxRow + BOXSIZE; cellRow++) {                     //Loop through the box starting on the upper row
            for (int cellCol = boxCol; cellCol < boxCol + BOXSIZE; cellCol++) {                 //Loop through the box starting on the left most column

                int cell = (cellRow * BOARDSIZE) + cellCol;                                     //Converts the coords of the current cell into a number between 0-80 representing the cell                                          
                domains[cell] &= ~initialValues;                                                //Remove all initialValues values from the domain

                if (solution[cellRow][cellCol] != null) {                                       //If the cell contains a value add that back to the domain
                    int value = solution[cellRow][cellCol];
                    domains[cell] = (1 << value - 1);
                }

                if (domains[cell] < 1) {                                                        //If a domain shrinks to nothing we have an invalid solution, turn back now!
                    emptyDomainFlag = true;
                    break escape;       //breaks from all loops
                }
            }
        }
    }

    /**
     * Forward Checks every row in the puzzle
     */
    private void fcAllRow() {

        for (int i = 0; i < BOARDSIZE; i++) {                                                   //Starting at the top row loop through every row on the board

            int initialValues = 0;
            for (int j = 0; j < BOARDSIZE; j++) {
                if (solution[i][j] != null) {                                                   //If the current cell contains an assigned value then add it to the initialValues variable
                    initialValues |= (1 << (solution[i][j] - 1));
                }
            }

            if (initialValues != 0) {                                                           //If there are values that need to be removed from domains remove them
                removeRowDomainValues(i, initialValues);
                if (emptyDomainFlag) {                                                          //If we have created an empty domain stop doing forward checking                                                       
                    break;
                }
            }
        }
    }

    /**
     * Forward Checks every column in the puzzle
     */
    private void fcAllCol() {

        for (int i = 0; i < BOARDSIZE; i++) {                                                   //Starting at the left most column loop through every column on the board

            int initialValues = 0;
            /* Get all the values in the column */
            for (int j = 0; j < BOARDSIZE; j++) {
                if (solution[j][i] != null) {                                                   //If the current cell contains an assigned value then add it to the initialValues variable
                    initialValues |= (1 << (solution[j][i] - 1));
                }
            }
            /* If there are values that need to be removed from domains remove them */
            if (initialValues != 0) {                                                           //If there are values that need to be removed from domains remove them
                removeColDomainValues(i, initialValues);
                if (emptyDomainFlag) {                                                          //If we have created an empty domain stop doing forward checking                                                       
                    break;
                }
            }
        }
    }

    /**
     * Forward Checks every box in the puzzle
     */
    private void fcAllBox() {

        int cellRow, cellCol;

        escape:

        /**
         * Loop through all the boxes Finds the top left square of each
         * BOXSIZExBOXSIZE box.
         */
        for (int i = 0; i < BOXSIZE; i++) {                                                     //Starting at the top most BOXSIZE row loop through every BOXSIZE row
            for (int j = 0; j < BOXSIZE; j++) {                                                 //Starting at the left most BOXSIZE column loop through every BOXSIZE column
                int initialValues = 0;

                for (int x = 0; x < BOXSIZE; x++) {                                             //Loops through each cell in a BOXSIZExBOXSIZE box
                    for (int y = 0; y < BOXSIZE; y++) {
                        cellRow = (BOXSIZE * i) + x;                                            //(BOXSIZE * i) = current boxes upper left most cell's row | x = current cells row in box
                        cellCol = (BOXSIZE * j) + y;                                            //(BOXSIZE * j) = current boxes upper left most cell's column | y = current cells column in box

                        if (solution[cellRow][cellCol] != null) {                               //If the current cell contains an assigned value then add it to the initialValues variable
                            initialValues |= (1 << (solution[cellRow][cellCol] - 1));
                        }
                    }
                }

                if (initialValues != 0) {                                                       //If there are values that need to be removed from domains remove them
                    removeBoxDomainValues(i * BOXSIZE, j * BOXSIZE, initialValues);
                    if (emptyDomainFlag) {                                                      //If we have created an empty domain stop doing forward checking                                                       
                        break escape;                                                           //Break all loops
                    }

                }
            }
        }
    }

    /**
     * Forward check all cells in the same row as current cell, removing the
     * value assigned to the current cell from each domain
     *
     * @param currentCell the cell thats just been assigned a value
     */
    private void fcRow(int[] currentCell) {
        int initialValues = 0;
        int currentValue = solution[currentCell[0]][currentCell[1]];
        if (solution[currentCell[0]][currentCell[1]] != null) {                                 //If the current cell contains an assigned value then add it to the initialValues variable
            initialValues = (1 << (currentValue - 1));
        }

        if (initialValues != 0) {                                                               //If there are values that need to be removed from domains remove them
            removeRowDomainValues(currentCell[0], initialValues);
        }
    }

    /**
     * Forward check all cells in the same column as current cell, removing the
     * value assigned to the current cell from each domain
     *
     * @param currentCell the cell thats just been assigned a value
     */
    private void fcCol(int[] currentCell) {
        int initialValues = 0;
        int currentValue = solution[currentCell[0]][currentCell[1]];
        if (solution[currentCell[0]][currentCell[1]] != null) {                                 //If the current cell contains an assigned value then add it to the initialValues variable
            initialValues = (1 << (currentValue - 1));
        }

        if (initialValues != 0) {                                                               //If there are values that need to be removed from domains remove them
            removeColDomainValues(currentCell[1], initialValues);
        }
    }

    /**
     * Forward check all cells in the same box as current cell, removing the
     * value assigned to the current cell from each domain
     *
     * @param currentCell the cell thats just been assigned a value
     */
    private void fcBox(int[] currentCell) {
        int initialValues = 0;
        int currentValue = solution[currentCell[0]][currentCell[1]];

        int boxRow = currentCell[0] - (currentCell[0] % BOXSIZE);                               //Find the row of the top left square of the current BOXSIZExBOXSIZE box
        int boxCol = currentCell[1] - (currentCell[1] % BOXSIZE);                               //Find the column of the top left square of the current BOXSIZExBOXSIZE box

        if (solution[currentCell[0]][currentCell[1]] != null) {                                 //If the current cell contains an assigned value then add it to the initialValues variable
            initialValues = (1 << (currentValue - 1));
        }

        if (initialValues != 0) {                                                               //If there are values that need to be removed from domains remove them
            removeBoxDomainValues(boxRow, boxCol, initialValues);
        }

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
