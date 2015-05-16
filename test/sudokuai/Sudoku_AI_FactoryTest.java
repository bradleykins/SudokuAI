/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudokuai;

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
public class Sudoku_AI_FactoryTest {
    
    public static final int BOXSIZE = 3;
    
    public Sudoku_AI_FactoryTest() {
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
     * Test of getAlgorithm method, of class Sudoku_AI_Factory.
     * using Backtracking call
     */
    @Test
    public void testGetBacktrackingAlgorithm() {
        System.out.println("getAlgorithm - Backtracking");
        String algorithm = "Backtracking";
        Sudoku_AI_Factory instance = new Sudoku_AI_Factory();
        Sudoku_AI expResult = new Sudoku_AI_Backtracking(BOXSIZE);
        Sudoku_AI result = instance.getAlgorithm(algorithm,BOXSIZE);
        assertEquals(expResult.getName(), result.getName());
    }
    
    /**
     * Test of getAlgorithm method, of class Sudoku_AI_Factory.
     * using Backtracking with Forward Checking call
     */
    @Test
    public void testGetBacktrackingFCAlgorithm() {
        System.out.println("getAlgorithm - Backtracking with Forward Checking");
        String algorithm = "Backtracking - FC";
        Sudoku_AI_Factory instance = new Sudoku_AI_Factory();
        Sudoku_AI expResult = new Sudoku_AI_ForwardChecking(BOXSIZE);
        Sudoku_AI result = instance.getAlgorithm(algorithm,BOXSIZE);
        assertEquals(expResult.getName(), result.getName());
    }
    
}
