
package sudokuai;

/**
 * @author Nik Bradley
 * $date $time
 */

/**
 * Sudoku_AI algorithm factory, takes a string with the name of the algorithm and
 * returns an instantiation of that algorithm.
 */
public class Sudoku_AI_Factory {
    
    public Sudoku_AI getAlgorithm(String algorithm, int gameBoxSize){
        if (algorithm == null){
            return null;
        }
        
        switch(algorithm){
            case "Backtracking":
                return new Sudoku_AI_Backtracking(gameBoxSize);
            case "Backtracking - FC":
                return new Sudoku_AI_ForwardChecking(gameBoxSize);
            default:
                return null;
        }
        
    }

}
