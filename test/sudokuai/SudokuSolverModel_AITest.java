/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudokuai;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Nik Bradley
 */
public class SudokuSolverModel_AITest {
    
    private static final int BOXSIZE = 3;
    private static final int BOARDSIZE = BOXSIZE*BOXSIZE;
    
    public SudokuSolverModel_AITest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of solveButtonPressed method, of class SudokuSolverModel_AI.
     * 
     * Bunch of unhandled exceptions are thrown because the controller has no GUI to update 
     */
    @Test
    public void testSolveButtonPressed() {
        //TODO: MOCK GUI
        System.out.println("solveButtonPressed");
        Integer[][] userInputPuzzle = new Integer[BOARDSIZE][BOARDSIZE];
        SudokuSolverController controller = new SudokuSolverController();
        SudokuSolverModel_AI instance = new SudokuSolverModel_AI(BOXSIZE);
        instance.solveButtonPressed(userInputPuzzle, controller);
        
        //if the first cell is not empty then we have a solution!
        assertTrue(userInputPuzzle[0][0] != null);
    }    
}
