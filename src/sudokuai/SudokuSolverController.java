package sudokuai;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JRadioButton;

/**
 * Controller for the SudokuSolverView handles interactions between the View and
 * the model
 *
 * @author Nik Bradley
 */
public class SudokuSolverController {

    //CLASS VARIABLES
    private static final int BOXSIZE = 3;                                           // Game box size standard is 3
    private static final int BOARDSIZE = BOXSIZE * BOXSIZE;                           // Game grid size standard is 3*3
    private final JFormattedTextField[][] userInputPuzzle, solvedPuzzle;
    private SudokuSolverModel listener;
    private Thread backGroundThread;
    private JLabel leftLabel, rightLabel;
    private JButton goButton, clearButton;
    private JMenuItem easyMenuOption, hardMenuOption, impossibleMenuOption, quitMenuOption;
    private JRadioButton back, backFC;

    /**
     * Constructor creates two puzzles in memory
     */
    public SudokuSolverController() {
        userInputPuzzle = new JFormattedTextField[BOARDSIZE][BOARDSIZE];
        solvedPuzzle = new JFormattedTextField[BOARDSIZE][BOARDSIZE];
    }

    /**
     * Binds a Label on the view to the controller to add a listener to This
     * label is for the puzzle time
     *
     * @param label label to be bound
     */
    public void bindLeftLabel(JLabel label) {
        leftLabel = label;
    }

    /**
     * Binds a Label on the view to the controller to add a listener to This
     * label is for the puzzle completed status
     *
     * @param label label to be bound
     */
    public void bindRightLabel(JLabel label) {
        rightLabel = label;
    }

    /**
     * Binds fields in the view to the created puzzle representation in the
     * controller and adds a listener looking for data being changed on that
     * field
     *
     * @param row row of the current field (cell)
     * @param column column of the current field (cell)
     * @param field the field to be bound
     */
    public void bindUserInputPuzzle(final int row, final int column, JFormattedTextField field) {
        field.addPropertyChangeListener("value", (PropertyChangeEvent evt) -> {
            if (evt.getNewValue() != null && evt.getNewValue() != "") {
                String newValue = (String) evt.getNewValue();
                userEditedValueAt(row, column, Integer.valueOf(newValue));
            }
        });
        userInputPuzzle[row][column] = field;
    }

    /**
     * Displays a message in the console of which field the user has changed and
     * to what value
     *
     * @param row row of the current field (cell)
     * @param column column of the current field (cell)
     * @param value the value being set
     */
    private void userEditedValueAt(int row, int column, int value) {
        System.out.println("Value changed at row:" + row + ", column:" + column + " to " + value);
    }

    /**
     * Binds fields in the view to the created puzzle representation in the
     * controller
     *
     * @param row row of the current field (cell)
     * @param column column of the current field (cell)
     * @param field the field to be bound
     */
    public void bindSolvedSudoku(int row, int column, JFormattedTextField field) {
        solvedPuzzle[row][column] = field;
    }

    /**
     * Displays console log of the sudoku input and the generated solution
     */
    private void spitOutSudokus() {
        System.out.println("User Input:");
        System.out.println(getPrettyPrinted(userInputPuzzle));
        System.out.println("Solved Puzzle:");
        System.out.println(getPrettyPrinted(solvedPuzzle));
    }

    /**
     * Gets the values of a sudoku text field 2d array and formats them for
     * output in the console.
     */
    private String getPrettyPrinted(JFormattedTextField[][] sudoku) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < BOARDSIZE; i++) {
            sb.append("|");
            for (int j = 0; j < BOARDSIZE; j++) {
                if (sudoku[i][j] != null) {
                    sb.append(sudoku[i][j].getText());
                } else {
                    sb.append("-");
                }
                sb.append(" ");
            }
            sb.append("|\n");
        }
        return sb.toString();
    }

    /**
     * Binds the run button to the controller from the view, and adds an action
     * listener
     *
     * @param goButton the button from the view
     */
    public void bindRunButton(JButton goButton) {
        this.goButton = goButton;
        goButton.addActionListener((ActionEvent e) -> {
            goButtonPressed();
        });
    }

    /**
     * Binds the run button to the controller from the view, and adds an action
     * listener
     *
     * @param clearButton the button from the view
     */
    public void bindClearButton(JButton clearButton) {
        this.clearButton = clearButton;
        clearButton.addActionListener((ActionEvent e) -> {
            clearButtonPressed();
        });
    }

    /**
     * Sets a SudokuSolverModel for the controller to talker to
     *
     * @param listener The supplied model
     */
    public void setListener(SudokuSolverModel listener) {
        this.listener = listener;
    }

    /**
     * Handles the actions when the go button is pressed, and creates a new
     * thread to be run, passing the information to the model
     */
    private void goButtonPressed() {
        if (listener != null) {
            if (backGroundThread == null || (backGroundThread != null && !backGroundThread.isAlive())) {
                backGroundThread = new Thread() {

                    @Override
                    public void run() {
                        listener.solveButtonPressed(getUserInputPuzzle(), SudokuSolverController.this);
                    }
                };
                backGroundThread.start();
            }
        }
    }

    /**
     * Handles the actions when the clear button is pressed, and creates a new
     * thread to be run, passing the information to the model
     */
    private void clearButtonPressed() {
        if (listener != null) {
            if (backGroundThread == null || (backGroundThread != null && !backGroundThread.isAlive())) {
                backGroundThread = new Thread() {

                    @Override
                    public void run() {
                        listener.clearButtonPressed(SudokuSolverController.this);
                    }
                };
                backGroundThread.start();
            }
        }
    }

    /**
     * Updates the GUI to clear the puzzle data
     */
    public void resetPuzzle() {
        EventQueue.invokeLater(() -> {
            for (int i = 0; i < BOARDSIZE; i++) {
                for (int j = 0; j < BOARDSIZE; j++) {
                    userInputPuzzle[i][j].setValue("");
                    solvedPuzzle[i][j].setValue("");
                }
            }
        });
        System.out.println("Game reset");
    }

    /**
     * Gets the data from the fields in a 2d array of Integers
     *
     * @return the array of Integers
     */
    private Integer[][] getUserInputPuzzle() {
        Integer[][] values = new Integer[BOARDSIZE][BOARDSIZE];
        for (int i = 0; i < BOARDSIZE; i++) {
            for (int j = 0; j < BOARDSIZE; j++) {
                if (!userInputPuzzle[i][j].getText().equals(" ")) {
                    values[i][j] = Integer.valueOf(userInputPuzzle[i][j].getText());
                }
            }
        }
        return values;
    }

    /**
     * Updates the GUI to set the solution to a supplied puzzle
     *
     * @param result the solution
     */
    public void setPuzzleResult(final Integer[][] result) {
        EventQueue.invokeLater(() -> {
            for (int i = 0; i < BOARDSIZE; i++) {
                for (int j = 0; j < BOARDSIZE; j++) {
                    solvedPuzzle[i][j].setValue(String.valueOf(result[i][j]));
                }
            }
        });        
    }

    /**
     * Updates the GUI to set the puzzle time
     *
     * @param time the time taken to solve the puzzle
     */
    public void setPuzzleTime(final String time) {
        EventQueue.invokeLater(() -> {
            leftLabel.setText("<html>Running time: <b>" + time);
        });
    }

    /**
     * Updates the GUI to set the puzzle completion status
     *
     * @param completed the status of the puzzle solution (solved=true|false)
     */
    public void setPuzzleCompleted(final boolean completed) {
        EventQueue.invokeLater(() -> {
            rightLabel.setText("<html>Completely Solved: <b>" + completed);
            if (completed) {
                spitOutSudokus();
            }
        });
    }

    /**
     * Binds a JMenuItem to the controller from the view, and adds an action
     * listener
     *
     * @param item the JMenuItem from the view for the quit option
     */
    public void bindQuitMenuOption(JMenuItem item) {
        this.quitMenuOption = item;
        quitMenuOption.addActionListener((ActionEvent e) -> {
            quitMenuOptionPressed();
        });
    }

    /**
     * Binds a JMenuItem to the controller from the view, and adds an action
     * listener
     *
     * @param item the JMenuItem from the view for the set easy puzzle option
     */
    public void bindEasyPuzzleMenuOption(JMenuItem item) {
        this.easyMenuOption = item;
        easyMenuOption.addActionListener((ActionEvent e) -> {
            easyMenuOptionPressed();
        });
    }

    /**
     * Binds a JMenuItem to the controller from the view, and adds an action
     * listener
     *
     * @param item the JMenuItem from the view for the set hard puzzle option
     */
    public void bindHardPuzzleMenuOption(JMenuItem item) {
        this.hardMenuOption = item;
        hardMenuOption.addActionListener((ActionEvent e) -> {
            hardMenuOptionPressed();
        });
    }

    /**
     * Binds a JMenuItem to the controller from the view, and adds an action
     * listener
     *
     * @param item the JMenuItem from the view for the set impossible puzzle
     * option
     */
    public void bindImpossiblePuzzleMenuOption(JMenuItem item) {
        this.impossibleMenuOption = item;
        impossibleMenuOption.addActionListener((ActionEvent e) -> {
            impossibleMenuOptionPressed();
        });
    }

    /**
     * Binds a JRadioButton to the controller from the view, and adds an action
     * listener
     *
     * @param item the JRadioButton from the view for the backtracking algorithm
     * selection
     */
    public void bindBacktrackingRadio(JRadioButton item) {
        this.back = item;
        back.addActionListener((ActionEvent e) -> {
            algorithimSelected(back.getText());
        });
    }

    /**
     * Binds a JRadioButton to the controller from the view, and adds an action
     * listener
     *
     * @param item the JRadioButton from the view for the backtracking with FC
     * algorithm selection
     */
    public void bindBacktrackingFCRadio(JRadioButton item) {
        this.backFC = item;
        backFC.addActionListener((ActionEvent e) -> {
            algorithimSelected(backFC.getText());
        });
    }

    /**
     * Handles the setting of the models algorithm, based on the selection by
     * the user from the view
     *
     * @param algorithmName the name of the algorithm to be set to the model
     */
    private void algorithimSelected(String algorithmName) {
        if (listener != null) {
            if (backGroundThread == null || (backGroundThread != null && !backGroundThread.isAlive())) {
                backGroundThread = new Thread() {

                    @Override
                    public void run() {
                        listener.algorithimSelected(SudokuSolverController.this, algorithmName);
                    }
                };
                backGroundThread.start();
            }
        }
    }

    /**
     * Handles the selection of the quit menu option, based on the selection by
     * the user from the view
     */
    private void quitMenuOptionPressed() {
        if (listener != null) {
            if (backGroundThread == null || (backGroundThread != null && !backGroundThread.isAlive())) {
                backGroundThread = new Thread() {

                    @Override
                    public void run() {
                        listener.quitMenuOptionPressed(SudokuSolverController.this);
                    }
                };
                backGroundThread.start();
            }
        }
    }

    /**
     * Handles the selection of the set easy puzzle menu option, based on the
     * selection by the user from the view
     */
    private void easyMenuOptionPressed() {
        if (listener != null) {
            if (backGroundThread == null || (backGroundThread != null && !backGroundThread.isAlive())) {
                backGroundThread = new Thread() {

                    @Override
                    public void run() {
                        listener.easyMenuOptionPressed(SudokuSolverController.this);
                    }
                };
                backGroundThread.start();
            }
        }
    }

    /**
     * Handles the selection of the set hard puzzle menu option, based on the
     * selection by the user from the view
     */
    private void hardMenuOptionPressed() {
        if (listener != null) {
            if (backGroundThread == null || (backGroundThread != null && !backGroundThread.isAlive())) {
                backGroundThread = new Thread() {

                    @Override
                    public void run() {
                        listener.hardMenuOptionPressed(SudokuSolverController.this);
                    }
                };
                backGroundThread.start();
            }
        }
    }

    /**
     * Handles the selection of the set impossible puzzle menu option, based on
     * the selection by the user from the view
     */
    private void impossibleMenuOptionPressed() {
        if (listener != null) {
            if (backGroundThread == null || (backGroundThread != null && !backGroundThread.isAlive())) {
                backGroundThread = new Thread() {

                    @Override
                    public void run() {
                        listener.impossibleMenuOptionPressed(SudokuSolverController.this);
                    }
                };
                backGroundThread.start();
            }
        }
    }

    /**
     * Quits the application
     */
    public void quit() {
        System.exit(0);
    }

    /**
     * Handles when the model tells the controller to set an easy puzzle
     */
    public void setEasyPuzzle() {
        //TODO turn into factory with multiple puzzles
        /* Easy Puzzles for humans */
        //String puzzle = "000007000300060050700920010080030001000000506900608000000013600000500120090000703";
        String puzzle = "249180000000460091100000070000500900701892000608000105872941350010700040500008700";
        setPuzzle(puzzle);
        System.out.println("Easy Puzzle Generated.");
    }

    /**
     * Handles when the model tells the controller to set a hard puzzle
     */
    public void setHardPuzzle() {
        //TODO turn into factory with multiple puzzles
        /* Difficult for brute force (backtracking) AI to solve as its a set so it backtracks (a lot) */
        String puzzle = "000000000000003085001020000000507000004000100090000000500000073002010000000400009";
        setPuzzle(puzzle);
        System.out.println("Hard Puzzle Generated.");
    }

    /**
     * Handles when the model tells the controller to set an impossible puzzle
     */
    public void setImpossiblePuzzle() {
        //TODO turn into factory with multiple puzzles
        /* Devised by Arto Inkala, a Finnish mathematician, and is specifically designed to be unsolvable to all but the sharpest minds. */
        String puzzle = "800000000003600000070090200050007000000045700000100030001000068008500010090000400";
        setPuzzle(puzzle);
        System.out.println("This puzzle was devised by Arto Inkala, a Finnish mathematician, and is specifically designed to be unsolvable to all but the sharpest minds.");
        System.out.println("Impossible Puzzle Generated.");
    }

    /**
     * Handles when a puzzle is to be set, updates GUI with values gotten from
     * the parameter puzzle
     *
     * @param puzzle a string representing the puzzle to be set
     */
    private void setPuzzle(String puzzle) {
        EventQueue.invokeLater(() -> {
            int c = 0;
            for (int i = 0; i < BOARDSIZE; i++) {
                for (int j = 0; j < BOARDSIZE; j++) {
                    String num = "" + puzzle.charAt(c);

                    if (num.equals("0")) {
                        num = "";
                    }

                    userInputPuzzle[i][j].setValue(num);
                    solvedPuzzle[i][j].setValue("");
                    c++;
                }
            }
        });
    }
}
