package sudokuai;

/**
 * @author Nik Bradley $date $time
 */
import java.awt.*;
import java.awt.event.KeyEvent;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.border.*;

/**
 * View which constructs every component and creates it's own controller.
 */
public class SudokuSolverView extends JFrame {

    private static final String VERSION = "Sudoku Solver 0.03";
    private static final Insets SIXPANELINSET = new Insets(6, 6, 6, 6);
    private static final int BOXSIZE = 3;                                           //Default BoxSize = 3 (3x3) 

    private final SudokuSolverController controller;

    /**
     * Sets the Sudoku model that the view will control
     *
     * @param listener The actual model for the view
     */
    public void setSudokuModel(SudokuSolverModel listener) {
        controller.setListener(listener);
    }

    /**
     * Constructor for the View, defaults to Sudoku board with two 3*3 boards
     * and some controls for the user to select algorithm or set puzzles
     */
    public SudokuSolverView() {
        controller = new SudokuSolverController();
        setTitle(VERSION);
        getContentPane().add(createCenterPanel(), BorderLayout.CENTER);
        getContentPane().add(createBottomPanel(), BorderLayout.SOUTH);
        setJMenuBar(createMenuBar());
        setMinimumSize(new Dimension(600, 300));
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createMenu());
        return menuBar;
    }

    private JMenu createMenu() {
        JMenu menu = new JMenu("Menu");
        menu.setMnemonic(KeyEvent.VK_M);
        menu.getAccessibleContext().setAccessibleDescription(
                "This menu allows various actions including setting an easy or hard sudoku puzzle and exiting the application");
        menu.add(createMenuEasyExample());
        menu.add(createMenuHardExample());
        menu.add(createMenuImpossibleExample());
        menu.addSeparator();
        menu.add(createMenuExitOption());
        return menu;
    }

    private JMenuItem createMenuEasyExample() {
        JMenuItem item = new JMenuItem("Easy Sudoku Puzzle", KeyEvent.VK_E);
        item.getAccessibleContext().setAccessibleDescription(
                "Generates an Easy Puzzle on the left");
        controller.bindEasyPuzzleMenuOption(item);
        return item;
    }

    private JMenuItem createMenuHardExample() {
        JMenuItem item = new JMenuItem("Hard Sudoku Puzzle", KeyEvent.VK_H);
        item.getAccessibleContext().setAccessibleDescription(
                "Generates a Hard Puzzle on the left");
        controller.bindHardPuzzleMenuOption(item);
        return item;
    }

    private JMenuItem createMenuImpossibleExample() {
        JMenuItem item = new JMenuItem("Impossible Sudoku Puzzle", KeyEvent.VK_I);
        item.getAccessibleContext().setAccessibleDescription(
                "Generates an Impossible Puzzle on the left");
        controller.bindImpossiblePuzzleMenuOption(item);
        return item;
    }

    private JMenuItem createMenuExitOption() {
        JMenuItem item = new JMenuItem("Exit", KeyEvent.VK_Q);
        item.getAccessibleContext().setAccessibleDescription(
                "Quits the application");
        controller.bindQuitMenuOption(item);
        return item;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new GridBagLayout());
        JLabel leftLabel = createLabel("left");
        JLabel rightLabel = createLabel("right");

        controller.bindLeftLabel(leftLabel);
        controller.bindRightLabel(rightLabel);

        bottomPanel.add(leftLabel, getWholeCellConstraints());
        bottomPanel.add(new JSeparator(JSeparator.VERTICAL));
        bottomPanel.add(rightLabel, getWholeCellConstraints());

        bottomPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        return bottomPanel;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(JLabel.CENTER);
        return label;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.add(createLeftPanel(), getWholeCellConstraints());
        centerPanel.add(createUserOptionsPanel(), getPreferredSizeConstraint());
        centerPanel.add(createRightPanel(), getWholeCellConstraints());
        return centerPanel;
    }

    private JPanel createUserOptionsPanel() {
        final GridLayout gridLayout = new GridLayout(2, 1, 1, 1);
        gridLayout.setHgap(1);
        gridLayout.setVgap(1);
        JPanel userOptionsPanel = new JPanel(gridLayout);
        userOptionsPanel.add(createAlgorithmRadioPanel(), getPreferredSizeConstraint());
        userOptionsPanel.add(createCentralButtonPanel(), getPreferredSizeConstraint());
        return userOptionsPanel;
    }

    private GridBagConstraints getPreferredSizeConstraint() {
        // default will do
        return new GridBagConstraints();
    }

    private JPanel createCentralButtonPanel() {
        JPanel buttonPanel = createButtonPanel(1);
        buttonPanel.add(createSolveButton(), getPreferredSizeConstraint());
        buttonPanel.add(createClearButton(), getPreferredSizeConstraint());
        return buttonPanel;
    }

    private JButton createSolveButton() {
        JButton goButton = new JButton("SOLVE");
        controller.bindRunButton(goButton);
        return goButton;
    }

    private JButton createClearButton() {
        JButton clearButton = new JButton("CLEAR");
        controller.bindClearButton(clearButton);
        return clearButton;
    }

    private JPanel createAlgorithmRadioPanel() {
        JPanel buttonPanel = createButtonPanel(1);
        JRadioButton backtracking = createBacktrackingRadio();
        JRadioButton backtrackingFC = createBacktrackingForwardCheckingRadio();
        buttonPanel.add(backtracking, getPreferredSizeConstraint());
        buttonPanel.add(backtrackingFC, getPreferredSizeConstraint());
        ButtonGroup algorithmGroup = new ButtonGroup();
        algorithmGroup.add(backtracking);
        algorithmGroup.add(backtrackingFC);
        return buttonPanel;
    }

    private JRadioButton createBacktrackingRadio() {
        JRadioButton backtrackingRadio = new JRadioButton("Backtracking");
        backtrackingRadio.setSelected(true);
        controller.bindBacktrackingRadio(backtrackingRadio);
        return backtrackingRadio;
    }

    private JRadioButton createBacktrackingForwardCheckingRadio() {
        JRadioButton backtrackingForwardCheckingRadio = new JRadioButton("Backtracking - FC");
        controller.bindBacktrackingFCRadio(backtrackingForwardCheckingRadio);
        return backtrackingForwardCheckingRadio;
    }

    private JPanel createRightPanel() {
        JPanel rightPanel = createBOXSIZEPanel(6);
        for (int i = 0; i < BOXSIZE; i++) {
            for (int j = 0; j < BOXSIZE; j++) {
                JPanel BOXSIZEpanel = createBOXSIZEPanel(2);
                fillPanelWithNonEditable(BOXSIZEpanel, i, j);
                rightPanel.add(BOXSIZEpanel);

            }
        }
        rightPanel.setBorder(new EmptyBorder(SIXPANELINSET));
        return rightPanel;
    }

    private JPanel createLeftPanel() {
        JPanel leftPanel = createBOXSIZEPanel(6);
        for (int i = 0; i < BOXSIZE; i++) {
            for (int j = 0; j < BOXSIZE; j++) {
                JPanel BOXSIZEpanel = createBOXSIZEPanel(2);
                fillPanelWithEditable(BOXSIZEpanel, i, j);
                leftPanel.add(BOXSIZEpanel);
            }
        }
        leftPanel.setBorder(new EmptyBorder(SIXPANELINSET));
        return leftPanel;
    }

    private GridBagConstraints getWholeCellConstraints() {
        GridBagConstraints wholePanelCnstr = getPreferredSizeConstraint();
        wholePanelCnstr.fill = java.awt.GridBagConstraints.BOTH;
        wholePanelCnstr.weightx = 1.0;
        wholePanelCnstr.weighty = 1.0;
        return wholePanelCnstr;
    }

    private void fillPanelWithEditable(JPanel panel, int boxRow, int boxCol) {
        for (int cellRow = 0; cellRow < BOXSIZE; cellRow++) {
            for (int cellCol = 0; cellCol < BOXSIZE; cellCol++) {
                final JFormattedTextField editableField = createEditableField();
                int column = boxCol * BOXSIZE + cellCol;
                int row = boxRow * BOXSIZE + cellRow;
                controller.bindUserInputPuzzle(row, column, editableField);
                panel.add(editableField);
            }
        }
    }

    private void fillPanelWithNonEditable(JPanel panel, int boxRow, int boxCol) {
        for (int cellRow = 0; cellRow < BOXSIZE; cellRow++) {
            for (int cellColumn = 0; cellColumn < BOXSIZE; cellColumn++) {
                final JFormattedTextField editableField = createNonEditableField();
                int column = boxCol * BOXSIZE + cellColumn;
                int row = boxRow * BOXSIZE + cellRow;
                controller.bindSolvedSudoku(row, column, editableField);
                panel.add(editableField);
            }
        }
    }

    private JPanel createButtonPanel(int gap) {
        final GridLayout gridLayout = new GridLayout(2, 1, 1, 1);
        gridLayout.setHgap(gap);
        gridLayout.setVgap(gap);
        JPanel panel = new JPanel(gridLayout);
        return panel;
    }

    private JPanel createBOXSIZEPanel(int gap) {
        final GridLayout gridLayout = new GridLayout(BOXSIZE, BOXSIZE, 1, 1);
        gridLayout.setHgap(gap);
        gridLayout.setVgap(gap);
        JPanel panel = new JPanel(gridLayout);
        return panel;
    }

    private JFormattedTextField createNonEditableField() {
        JFormattedTextField field = createEditableField();
        field.setEditable(false);
        field.setBackground(Color.WHITE); // otherwise non-editable gets gray
        field.setFont(new Font("Serif", Font.PLAIN, 24));
        return field;
    }

    private JFormattedTextField createEditableField() {
        JFormattedTextField field = new JFormattedTextField();
        // accept only one digit and nothing else
        try {
            field.setFormatterFactory(new DefaultFormatterFactory(new MaskFormatter("#")));
        } catch (java.text.ParseException ex) {
        }
        field.setPreferredSize(new Dimension(16, 30));
        field.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        field.setText(" ");
        field.setBorder(null);
        field.setFont(new Font("Serif", Font.PLAIN, 24));
        return field;
    }

}
