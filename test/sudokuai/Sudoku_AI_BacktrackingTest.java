/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudokuai;

import java.util.Arrays;
import java.util.Objects;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
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
public class Sudoku_AI_BacktrackingTest {
    
    private final static int BOXSIZE = 3;
    private final static int BOARDSIZE = BOXSIZE*BOXSIZE;
    
    public Sudoku_AI_BacktrackingTest() {
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
     * Test of setSolution and getSolution methods, of class Sudoku_AI_Backtracking.
     */
    @Test
    public void testGetSolution() {
        System.out.println("setSolution & getSolution");
        Integer[][] puzzle = new Integer[BOARDSIZE][BOARDSIZE];
        puzzle[0 % BOARDSIZE][4 % BOARDSIZE] = 9;
        
        String puzzleSolved = "123495678456178239789236145214357896365819427897624351531762984678943512942581763";
        Integer[][] expected = new Integer[BOARDSIZE][BOARDSIZE];
        
        /**
         * Create the expected grid answer
         */
        for (int i=0;i<BOARDSIZE;i++){
            for (int j=0;j< BOARDSIZE;j++){
                expected[i][j] = Integer.parseInt(""+puzzleSolved.charAt(i*BOARDSIZE+j));
            }            
        }            
        
        Sudoku_AI_Backtracking instance = new Sudoku_AI_Backtracking(BOXSIZE);
        Integer[][] result = instance.getSolution(puzzle);
        
        puzzle = new Integer[BOARDSIZE][BOARDSIZE];                     //References to puzzle passed into instance, need a hard reset.
        puzzle[0 % BOARDSIZE][4 % BOARDSIZE] = 9;
        
        /**
         * Check that the puzzle was set correctly and returns expected result, not a copy of the original
         */
        for (int i=0;i<BOARDSIZE;i++){
            System.out.println("Check puzzle is changed");
            assertThat(puzzle[i], not(equalTo(result[i])));
            
            System.out.println(Arrays.toString(puzzle[i]) +"!="+ Arrays.toString(result[i]));
            
            System.out.println("Check puzzle is solved as expected");
            assertArrayEquals(expected[i], result[i]);
        }
        
        /**
         * Check that the set solution makes a difference
         */        
        System.out.println("Check puzzle is changed");
        System.out.println("Expected should not match Results");
        
        puzzle = new Integer[BOARDSIZE][BOARDSIZE];
        puzzle[0 % BOARDSIZE][5 % BOARDSIZE] = 9;
        result = instance.getSolution(puzzle);
        puzzle = new Integer[BOARDSIZE][BOARDSIZE];                     //References to puzzle passed into instance, need a hard reset.
        puzzle[0 % BOARDSIZE][5 % BOARDSIZE] = 9;
        
        boolean match = true;
        
        all:
        for (int i=0;i<BOARDSIZE;i++){            
            for (int j=0;j<BOARDSIZE;j++){
                if (!Objects.equals(expected[i][j], result[i][j])){                       
                    //As soon as one item in the grid does not match, we are satisfied.
                    System.out.println(expected[i][j] +"!="+ result[i][j]);
                    match = false;
                    break all;
                }            
            }            
        }        
        
        assertTrue(!match);
    }

    /**
     * Test of getName method, of class Sudoku_AI_Backtracking.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        Sudoku_AI_Backtracking instance = new Sudoku_AI_Backtracking(BOXSIZE);
        String expResult = "Backtracking";
        String result = instance.getName();
        assertEquals(expResult, result);
    }
    
}
