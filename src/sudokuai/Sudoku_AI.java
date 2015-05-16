package sudokuai;

/**
 * Interface which all AI Algorithms derive from
 *
 * @author Nik Bradley
 */
public interface Sudoku_AI {

    /**
     * @param userInputPuzzle is the puzzle to be solved.
     * @return 2d array (Integer[][]) which contains either filled out puzzle
     * board or empty board.
     */
    public Integer[][] getSolution(Integer[][] userInputPuzzle);

    /**
     * Returns the String ID of the Algorithm
     *
     * @return NAME a string ID of the Algorithm
     */
    public String getName();

}
