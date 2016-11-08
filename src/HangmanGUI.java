import java.awt.*;
import javax.swing.*;

import java.util.List;
import java.util.ArrayList;

/**
 * HangmanGUI is the main class of the Hangman Game.
 * It builds and displays the game's GUI and initialises
 * all other components.
 *
 * @author Nikola Ignatov
 * @version 0.1
 */
public class HangmanGUI
{
    private static final String VERSION = "Version 0.1";
    private static final String AUTHOR = "Nikola Ignatov";
    private static String[] alphabet = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
            "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    private HangmanGame game;
    private JFrame frame;
    private JPanel wordText;
    private JLabel incorrectLettersText;
    private JLabel wrongGuessesText;
    private JLabel hangingMan;

    private List<JButton> lettersButtons;
    private List<JLabel> wordLabels;

    /**
     * Create the GUI and initialise the logic part
     * of the game. Display it on the screen.
     */
    public HangmanGUI()
    {
        game = new HangmanGame();
        wordLabels = new ArrayList<>();
        lettersButtons = new ArrayList<>();

        setupLettersButtons();

        makeFrame();
    }

    public static void main(String[] args) {
        HangmanGUI gui = new HangmanGUI();
    }

    /**
     * Create the frame and its content.
     */
    private void makeFrame()
    {
        frame = new JFrame("Hangman");
        frame.setMinimumSize(new Dimension(450, 600));
        frame.setPreferredSize(new Dimension(450, 600));

        makeMenuBar();

        JPanel contentPane = (JPanel) frame.getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

        hangingMan = new JLabel(new ImageIcon("0.png"));
        hangingMan.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPane.add(hangingMan);

        wordText = new JPanel();
        wordText.setAlignmentX(Component.CENTER_ALIGNMENT);
        createDisplayWord();
        contentPane.add(wordText);

        createGuessesText(contentPane);

        JPanel lettersButtonsPanel = new JPanel();
        lettersButtonsPanel.setPreferredSize(new Dimension(450, 100));
        lettersButtonsPanel.setMaximumSize(new Dimension(450, 100));
        for(JButton button : lettersButtons) {
            lettersButtonsPanel.add(button);
        }
        contentPane.add(lettersButtonsPanel);

        JButton newWordButton = new JButton("New Word");
        newWordButton.setBackground(Color.WHITE);
        newWordButton.setForeground(Color.BLACK);
        newWordButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        newWordButton.addActionListener(e -> reset());
        contentPane.add(newWordButton);

        contentPane.add(Box.createVerticalGlue());

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Create the menu bar.
     */
    private void makeMenuBar()
    {
        JMenuBar menubar = new JMenuBar();
        frame.setJMenuBar(menubar);

        JMenu menu;
        JMenuItem menuItem;

        // create the Game menu
        menu = new JMenu("Game");
        menubar.add(menu);

        menuItem = new JMenuItem("New Word");
        menuItem.addActionListener(e -> reset());
        menu.add(menuItem);

        menuItem = new JMenuItem("Quit");
        menuItem.addActionListener(e -> quit());
        menu.add(menuItem);

        // create the Help menu
        menu = new JMenu("Help");
        menubar.add(menu);

        menuItem = new JMenuItem("About");
        menuItem.addActionListener(e -> showAbout());
        menu.add(menuItem);

        menuItem = new JMenuItem("Rules");
        menuItem.addActionListener(e -> showRules());
        menu.add(menuItem);
    }

    /**
     * Quit the game.
     */
    private void quit()
    {
        System.exit(0);
    }

    /**
     * Show the about box.
     */
    private void showAbout()
    {
        JOptionPane.showMessageDialog(frame,
                VERSION + "\n" +
                        "Author: " + AUTHOR,
                "About Hangman",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Show the rules box.
     */
    private void showRules()
    {
        JOptionPane.showMessageDialog(frame,
                "A word is picked randomly. You have to\n" +
                        "correctly guess all the letters in order to\n" +
                        "win. The maximum number of wrong guesses\n" +
                        "is 6. Reaching that number would mean that\n" +
                        "you have lost.",
                "Rules",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Iterate over the alphabet array and create a button
     * representing each letter. Add a listener to each button
     * to perform all the action required when clicked.
     */
    private void setupLettersButtons()
    {
        for (int i = 0; i < alphabet.length; i++) {
            lettersButtons.add(new JButton(alphabet[i]));
            lettersButtons.get(i).addActionListener(e -> {
                JButton button = (JButton) e.getSource();
                String letter = button.getText();
                if(game.guessLetter(letter)) {
                    updateDisplayWord();
                }
                else {
                    updateHangingMan();
                    wrongGuessesText.setText(String.valueOf(game.getWrongGuesses()));
                    incorrectLettersText.setText(game.incorrectLettersToString());
                }
                button.setEnabled(false);
                if (game.isGameLost()) {
                    JOptionPane.showMessageDialog(frame, "You lost. The word was: " + game.getWord());
                    reset();
                }
                else if (game.isGameWon()) {
                    JOptionPane.showMessageDialog(frame, "You correctly guessed the word: " + game.getWord());
                    reset();
                }
            });
        }
    }

    /**
     * Create the text displaying the number of wrong guesses
     * and the incorrect letters guessed.
     *
     * @param panel The panel they should be added to.
     */
    private void createGuessesText(JPanel panel)
    {
        // create a panel that holds both lines of text
        JPanel guessesText = new JPanel();
        guessesText.setLayout(new BoxLayout(guessesText, BoxLayout.Y_AXIS));
        guessesText.setMaximumSize(new Dimension(450, 40));
        guessesText.setAlignmentX(Component.CENTER_ALIGNMENT);

        // create a panel for the first line
        JPanel numberOfGuesses = new JPanel();
        numberOfGuesses.setMaximumSize(new Dimension(450, 20));

        // separate labels for the text and the numbers
        JLabel wrongGuesses = new JLabel("Number of wrong guesses: ");
        wrongGuessesText = new JLabel("" + game.getWrongGuesses());

        numberOfGuesses.add(wrongGuesses);
        numberOfGuesses.add(wrongGuessesText);
        guessesText.add(numberOfGuesses);

        // create a panel for the second line
        JPanel incorrectGuesses = new JPanel();
        incorrectGuesses.setMaximumSize(new Dimension(450, 20));

        // separate labels for the text and the letters
        JLabel incorrectLettersGuessed = new JLabel("Incorrect letters guessed: ");
        incorrectLettersText = new JLabel();

        incorrectGuesses.add(incorrectLettersGuessed);
        incorrectGuesses.add(incorrectLettersText);
        guessesText.add(incorrectGuesses);

        panel.add(guessesText);
    }

    /**
     * Convert the characters of the hidden word
     * into labels to display. Add them to the list
     * of labels and to the panel they should be in.
     */
    private void createDisplayWord()
    {
        String displayWord = game.getHiddenWord();
        for (int i = 0; i < displayWord.length(); i++) {
            Character c = displayWord.charAt(i);
            JLabel label = new JLabel(String.valueOf(c));
            label.setFont(new Font("Courier", Font.BOLD, 48));
            wordText.add(label);
            wordLabels.add(label);
        }
    }

    /**
     * Check for any guessed letters and update
     * the display word.
     */
    private void updateDisplayWord()
    {
        String displayWord = game.getHiddenWord();
        for (int i = 0; i < displayWord.length(); i++) {
            JLabel label = wordLabels.get(i);
            Character c = displayWord.charAt(i);
            label.setText(String.valueOf(c));
        }
    }

    /**
     * Remove all the labels from the list and the panel.
     * Repaint the panel.
     */
    private void clearDisplayWord()
    {
        for (int i = wordLabels.size() - 1; i >= 0; i--) {
            JLabel label = wordLabels.get(i);
            wordText.remove(label);
            wordLabels.remove(i);
        }
        wordText.repaint();
    }

    /**
     * Check the number of wrong guesses and set the label
     * to display the appropriate icon.
     */
    private void updateHangingMan()
    {
        int numberOfWrongGuesses = game.getWrongGuesses();
        switch(numberOfWrongGuesses) {
            case 1:  hangingMan.setIcon((new ImageIcon("1.png")));
                break;
            case 2:  hangingMan.setIcon((new ImageIcon("2.png")));
                break;
            case 3:  hangingMan.setIcon((new ImageIcon("3.png")));
                break;
            case 4:  hangingMan.setIcon((new ImageIcon("4.png")));
                break;
            case 5:  hangingMan.setIcon((new ImageIcon("5.png")));
                break;
            case 6:  hangingMan.setIcon((new ImageIcon("6.png")));
                break;
            default: hangingMan.setIcon((new ImageIcon("0.png")));
                break;
        }
    }

    /**
     * Set all the components back to their
     * initial state.
     */
    private void clearGUI()
    {
        updateHangingMan();

        clearDisplayWord();
        createDisplayWord();

        wrongGuessesText.setText(String.valueOf(game.getWrongGuesses()));
        incorrectLettersText.setText(game.incorrectLettersToString());

        for(JButton button : lettersButtons) {
            button.setEnabled(true);
        }
    }

    /**
     * Reset the whole game and start over
     * with a new word.
     */
    private void reset()
    {
        game.setup();
        clearGUI();
        frame.pack();
        frame.setLocationRelativeTo(null);
    }
}
