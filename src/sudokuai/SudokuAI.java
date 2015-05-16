package sudokuai;

import java.awt.EventQueue;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Main Class to instantiate the application
 * @author Nik Bradley
 */
public class SudokuAI implements Runnable {
    
    private static final int BOXSIZE = 3;

    /**
     * Runs the application by creating a model and a view, giving the view sight of the model
     */
    @Override
    public void run() {
        SudokuSolverModel sudokuModel = new SudokuSolverModel_AI(BOXSIZE);

        SudokuSolverView sudokuView = new SudokuSolverView();
        sudokuView.setSudokuModel(sudokuModel);
        sudokuView.setVisible(true);
    }

    /**
     * starts the GUI
     * @param args 
     */
    public static void main(String args[]) {
        tryToSetSystemLookAndFeel();
        EventQueue.invokeLater(new SudokuAI());
    }

    private static void tryToSetSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            System.out.println("Couldn't set LAF");
        }
    }
}