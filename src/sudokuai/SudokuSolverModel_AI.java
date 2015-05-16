package sudokuai;

/**
 * Concrete Model made using SudokuSolverModel. Creates its own algorithm using
 * a Sudoku_AI_Factory
 *
 * @author Nik Bradley
 */
class SudokuSolverModel_AI implements SudokuSolverModel {

    //CLASS VARIABLES
    private Sudoku_AI algorithm;                                                    //Algorithm for the model to use to solve
    private static int BOXSIZE;                                                     //Game box size standard 3
    private static int BOARDSIZE;                                                   //Grid size standard 3*3
    private final Sudoku_AI_Factory AI_Factory = new Sudoku_AI_Factory();           //Factory for generating the algorithm

    public SudokuSolverModel_AI(int gameBoxSize) {
        BOXSIZE = gameBoxSize;
        BOARDSIZE = BOXSIZE*BOXSIZE;
    }

    /**
     * Runs the program solving the supplied puzzle (if possible) and returns it
     * to the controller to be displayed on the view.
     *
     * @param userInputPuzzle a supplied puzzle to be solved
     * @param controller The Controller in charge of managing the view and model
     */
    @Override
    public void solveButtonPressed(Integer[][] userInputPuzzle, SudokuSolverController controller) {

        //TODO: Revise to update the display of each cell as it gets assigned.
        controller.setPuzzleResult(new Integer[BOARDSIZE][BOARDSIZE]);
        System.out.println("Running computation...");

        if (algorithm == null) {                                                     //If no Algorithm has been selected default to backtracking
            algorithm = AI_Factory.getAlgorithm("Backtracking",BOXSIZE);
        }

        long start = System.currentTimeMillis();                                    //Note the start time of the solve

        Integer[][] newSolution = algorithm.getSolution(userInputPuzzle);           //Get the solution from the algorithm

        if (newSolution[0][0] != null) {                                            //If the solution is not empty then update the results to show the solution
            controller.setPuzzleResult(newSolution);
            controller.setPuzzleCompleted(true);
        } else {                                                                    //If the puzzle is blank, set the completed label to false
            controller.setPuzzleCompleted(false);
        }
        long elapsedTime = System.currentTimeMillis() - start;                      //Calculate elapsed time
        controller.setPuzzleTime(String.valueOf(elapsedTime) + "ms");               //Display elapsed time as puzzle time label
        System.out.println("Done!");
    }

    /**
     * Clears the GUI of input and resulted puzzles
     *
     * @param controller The Controller in charge of managing the view and model
     */
    @Override
    public void clearButtonPressed(SudokuSolverController controller) {
        System.out.println("Clearing the puzzle Boards...");
        controller.resetPuzzle();                                                   //Tell the controller to reset the puzzles
        controller.setPuzzleTime("");                                               //Tell the controller to clear the puzzletime label
        controller.setPuzzleCompleted(false);                                       //Tell the controller to clear the puzzlecompleted label
        System.out.println("Done!");
    }

    /**
     * Tells the controller to set up an impossible (for humans) puzzle
     *
     * @param controller The Controller in charge of managing the view and model
     */
    @Override
    public void impossibleMenuOptionPressed(SudokuSolverController controller) {
        System.out.println("Setting Up Impossible Puzzle...");
        controller.setPuzzleTime("");                                               //Tell the controller to clear the puzzletime label
        controller.setPuzzleCompleted(false);                                       //Tell the controller to clear the puzzlecompleted label
        controller.setImpossiblePuzzle();                                           //Tell the controller to set the an impossible puzzle
        System.out.println("Done!");
    }

    /**
     * Tells the controller to set up a hard (for this program) puzzle
     *
     * @param controller The Controller in charge of managing the view and model
     */
    @Override
    public void hardMenuOptionPressed(SudokuSolverController controller) {
        System.out.println("Setting Up Hard Puzzle...");
        controller.setPuzzleTime("");                                               //Tell the controller to clear the puzzletime label
        controller.setPuzzleCompleted(false);                                       //Tell the controller to clear the puzzlecompleted label
        controller.setHardPuzzle();                                                 //Tell the controller to set the a hard puzzle
        System.out.println("Done!");
    }

    /**
     * Tells the controller to set up an easy (for humans) puzzle
     *
     * @param controller The Controller in charge of managing the view and model
     */
    @Override
    public void easyMenuOptionPressed(SudokuSolverController controller) {
        System.out.println("Setting Up Easy Puzzle...");
        controller.setPuzzleTime("");                                               //Tell the controller to clear the puzzletime label
        controller.setPuzzleCompleted(false);                                       //Tell the controller to clear the puzzlecompleted label
        controller.setEasyPuzzle();                                                 //Tell the controller to set the an easy puzzle
        System.out.println("Done!");
    }

    /**
     * Tells the controller to Quit the application
     *
     * @param controller The Controller in charge of managing the view and model
     */
    @Override
    public void quitMenuOptionPressed(SudokuSolverController controller) {
        System.out.println("Quitting Application...");
        controller.quit();                                                          //Tell the controller to close the application
    }

    /**
     * Sets an algorithm for the model
     *
     * @param newAlgorithm The algorithm to be assigned to the model
     */
    private void setAlgorithm(Sudoku_AI newAlgorithm) {
        this.algorithm = newAlgorithm;
    }

    /**
     * Changes the algorithm assigned to the model
     *
     * @param controller The Controller in charge of managing the view and model
     * @param algorithmName The name of the algorithm to be assigned to the
     * model
     */
    @Override
    public void algorithimSelected(SudokuSolverController controller, String algorithmName) {
        System.out.println("New Algorithm Set...");
        this.setAlgorithm(AI_Factory.getAlgorithm(algorithmName,BOXSIZE));
        System.out.println("Algorithm set as:" + algorithm.getName());
    }

}
