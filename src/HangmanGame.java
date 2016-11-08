import java.util.Random;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

/**
 * A class that creates the logic for the
 * Hangman game.
 *
 * @author Nikola Ignatov
 * @version 0.1
 */
public class HangmanGame
{
    private static final int MAX_WRONG_GUESSES = 6;
    private static String[] words = {"rhythm", "zephyr", "lynx", "eczema", "nymph", "jazz", "buzz", "cowboy", "czar",
            "xylophone", "gypsy", "why", "kleptomania", "lumberjack", "loquacious", "phobia",
            "syzygy", "incredulous", "sylt", "synth", "hangman", "java"};

    private int wrongGuesses;
    private String word;
    private String hiddenWord;
    private Set<String> incorrectLetters;
    private Set<String> correctLetters;

    /**
     * Constructor for object of class HangmanGame.
     */
    public HangmanGame()
    {
        setup();
    }

    /**
     * Initialise all the fields. This method can be
     * used to reset the game.
     */
    public void setup()
    {
        wrongGuesses = 0;

        word = generateWord();
        hiddenWord = generateHiddenWord();

        incorrectLetters = new HashSet<>();
        correctLetters = new HashSet<>();
    }

    /**
     * Generate a random word from the array
     * of words.
     *
     * @return A random word.
     */
    private String generateWord()
    {
        Random r = new Random();
        int index = r.nextInt(words.length);
        return words[index].toUpperCase();
    }

    /**
     * Generate hidden word, i.e. convert every
     * character into an underscore.
     *
     * @return The word converted into underscores.
     */
    public String generateHiddenWord()
    {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < word.length(); i++) {
            sb.append("_");
        }
        return sb.toString();
    }

    /**
     * Return the hidden word.
     *
     * @return The hidden word.
     */
    public String getHiddenWord()
    {
        return hiddenWord;
    }

    /**
     * Return the word.
     *
     * @return The word.
     */
    public String getWord()
    {
        return word;
    }

    /**
     * Return the number of wrong guesses.
     *
     * @return The number of wrong guesses.
     */
    public int getWrongGuesses()
    {
        return wrongGuesses;
    }

    /**
     * Check if the word contains the guessed 
     * letter.
     *
     * @return True if it does, false otherwise.
     */
    public boolean guessLetter(String letter)
    {
        boolean correctGuess = true;
        if(word.contains(letter)) {
            correctLetters.add(letter);
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < word.length(); i++) {
                if(word.substring(i, i + 1).equals(letter)) {
                    sb.append(letter);
                }
                else {
                    sb.append(hiddenWord.charAt(i));
                }
            }
            hiddenWord = sb.toString();
        }
        else {
            incorrectLetters.add(letter);
            wrongGuesses++;
            correctGuess = false;
        }
        return correctGuess;
    }

    /**
     * Check if the game is won.
     *
     * @return If the game is won.
     */
    public boolean isGameWon()
    {
        boolean isGameWon = true;
        for(int i = 0; i < hiddenWord.length(); i++) {
            Character c = hiddenWord.charAt(i);
            if(c == '_') {
                isGameWon = false;
            }
        }
        return isGameWon;
    }

    /**
     * Check if the game is lost.
     *
     * @return If the game is lost.
     */
    public boolean isGameLost()
    {
        return wrongGuesses >= MAX_WRONG_GUESSES;
    }

    /**
     * Create a string of all the incorrect
     * letters guessed.
     *
     * @return A string of the incorrect letters.
     */
    public String incorrectLettersToString()
    {
        StringBuilder sb = new StringBuilder();
        List<String> lettersList = new ArrayList<>(incorrectLetters);
        for(int i = 0; i < lettersList.size(); i++) {
            sb.append(lettersList.get(i));
            if (i < (lettersList.size() - 1)) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }
}