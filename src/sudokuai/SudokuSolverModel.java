package sudokuai;

/**
 * Interface which all Sudoku Solver Models derive from
 *
 * @author Nik Bradley
 */
public interface SudokuSolverModel {

    /**
     * Runs the program solving the supplied puzzle (if possible) and returns it
     * to the controller to be displayed on the view.
     *
     * @param userInputPuzzle a supplied puzzle to be solved
     * @param controller The Controller in charge of managing the view and model
     */
    public void solveButtonPressed(Integer[][] userInputPuzzle, SudokuSolverController controller);

    /**
     * Clears the GUI of input and resulted puzzles
     *
     * @param controller The Controller in charge of managing the view and model
     */
    public void clearButtonPressed(SudokuSolverController controller);

    /**
     * Tells the controller to set up an impossible (for humans) puzzle
     *
     * @param controller The Controller in charge of managing the view and model
     */
    public void easyMenuOptionPressed(SudokuSolverController controller);

    /**
     * Tells the controller to set up a hard (for this program) puzzle
     *
     * @param controller The Controller in charge of managing the view and model
     */
    public void hardMenuOptionPressed(SudokuSolverController controller);

    /**
     * Tells the controller to set up an easy (for humans) puzzle
     *
     * @param controller The Controller in charge of managing the view and model
     */
    public void impossibleMenuOptionPressed(SudokuSolverController controller);

    /**
     * Tells the controller to Quit the application
     *
     * @param controller The Controller in charge of managing the view and model
     */
    public void quitMenuOptionPressed(SudokuSolverController controller);

    /**
     * Changes the algorithm assigned to the model
     *
     * @param controller The Controller in charge of managing the view and model
     * @param Name The name of the algorithm to be assigned to the model
     */
    public void algorithimSelected(SudokuSolverController controller, String Name);

}
