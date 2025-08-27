package project;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.Timer;

public class MemoryGame extends JFrame implements ActionListener {

    private ArrayList<Color> colors;
    private ArrayList<Color> selectedColors;
    private ArrayList<JButton> buttons;
    private Timer timer;
    private int firstCardIndex;
    private int secondCardIndex;
    private int pairsFound;
    private int rows;
    private int columns;
    private int totalPairs;

    public MemoryGame(int rows, int columns, int totalPairs) {
        this.rows = rows;
        this.columns = columns;
        this.totalPairs = totalPairs;
        setTitle("Memory Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(rows, columns));

        int totalCards = rows * columns;
        colors = new ArrayList<>();
        selectedColors = new ArrayList<>();
        buttons = new ArrayList<>();
        pairsFound = 0;

        initButtons(totalCards);
        initColors();

        timer = new Timer(1000, this);
        timer.setRepeats(false);

        setSize(100 * columns, 100 * rows);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initColors() {
        Random random = new Random();
        for (int i = 0; i < totalPairs; i++) {
            int r = random.nextInt(256);
            int g = random.nextInt(256);
            int b = random.nextInt(256);
            Color color = new Color(r, g, b);
            colors.add(color);
            colors.add(color);
        }
        Collections.shuffle(colors);
    }

    private void initButtons(int totalCards) {
        for (int i = 0; i < totalCards; i++) {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(100, 100));
            button.setBackground(Color.LIGHT_GRAY);
            button.addActionListener(e -> revealCard(button));
            add(button);
            buttons.add(button);
        }
    }

    private void revealCard(JButton button) {
        int index = buttons.indexOf(button);
        button.setBackground(colors.get(index));
        button.setEnabled(false);

        if (selectedColors.size() == 0) {
            firstCardIndex = index;
        } else {
            secondCardIndex = index;
            timer.start();
        }

        selectedColors.add(colors.get(index));
        if (selectedColors.size() == 2) {
            if (selectedColors.get(0).equals(selectedColors.get(1))) {
                pairsFound++;
                if (pairsFound == totalPairs) {
                    handleGameOver();
                }
            } else {
                handleMismatch();
            }
            selectedColors.clear();
        }
    }

    private void handleMismatch() {
        Timer mismatchTimer = new Timer(1000, e -> {
            buttons.get(firstCardIndex).setBackground(Color.LIGHT_GRAY);
            buttons.get(firstCardIndex).setEnabled(true);
            buttons.get(secondCardIndex).setBackground(Color.LIGHT_GRAY);
            buttons.get(secondCardIndex).setEnabled(true);
        });
        mismatchTimer.setRepeats(false);
        mismatchTimer.start();
    }

    private void handleGameOver() {
        int choice = JOptionPane.showConfirmDialog(this, "Congratulations! You won!\nDo you want to play again?", "Game Over", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            resetGame();
        } else {
            dispose();
        }
    }

    private void resetGame() {
        pairsFound = 0;
        for (JButton button : buttons) {
            button.setBackground(Color.LIGHT_GRAY);
            button.setEnabled(true);
        }
        Collections.shuffle(colors);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        buttons.get(firstCardIndex).setEnabled(false);
        buttons.get(secondCardIndex).setEnabled(false);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
        	JOptionPane.showMessageDialog(null,"WELCOME TO MEMORY GAME");
            try {
                int rows = Integer.parseInt(JOptionPane.showInputDialog("Enter the number of rows:"));
                int columns = Integer.parseInt(JOptionPane.showInputDialog("Enter the number of columns:"));
                int totalPairs = Integer.parseInt(JOptionPane.showInputDialog("Enter the number of pairs:"));
                new MemoryGame(rows, columns, totalPairs);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter valid numbers.");
            }
        });
    }
}