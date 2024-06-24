package it.polimi.ingsw.am16.common.util;

import java.io.Serial;
import java.util.List;
import java.util.Random;

/**
 * Singleton class that holds a single instance of {@link java.util.Random}.
 * Used to handle all the random events of the game.
 * Initialized with a seed (by default, the system's time, obtained through {@link System}'s <code>nanoTime()</code>).
 */
public class RNG extends Random {

    @Serial
    private static final long serialVersionUID = 1354633369837069111L;

    private static RNG instance = null;
    @SuppressWarnings("SpellCheckingInspection")
    private static final char[] symbols = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();

    /**
     * Instantiates the RNG with the given seed.
     *
     * @param seed The seed.
     */
    private RNG(long seed) {
        super(seed);
    }

    /**
     * Sets the RNG seed.
     *
     * @param seed The seed.
     */
    public static synchronized void setRNGSeed(long seed) {
        if (instance == null) {
            instance = new RNG(seed);
            return;
        }
        instance.setSeed(seed);
    }

    /**
     * Returns the RNG instance.
     *
     * @return The RNG.
     */
    public static synchronized RNG getRNG() {
        if (instance == null) {
            instance = new RNG(System.nanoTime());
        }
        return instance;
    }

    /**
     * Generates a new alphanumeric string of the specified length. The character set from which strings will be created contains only capital letters and digits from 0 to 9.
     * No checks are in place for what possible words this method produces.
     * @param length The length of the alphanumeric string to be generated.
     * @return The generated alphanumeric string.
     */
    public synchronized String nextAlphNumString(int length) {
        if (instance == null) instance = new RNG(System.nanoTime());

        if (length < 1) throw new IllegalArgumentException("Length must be at least 1");

        StringBuilder builder = new StringBuilder();
        for(int i = 0; i<length; i++)
            builder.append(symbols[instance.nextInt(symbols.length)]);

        return builder.toString();
    }

    /**
     * Extracts a random element from the given list.
     * @param list The list from which an element should be extracted.
     * @return The extracted element.
     * @param <T> The static type of the elements of the list and of the element returned by this method.
     */
    public synchronized <T> T randomFromList(List<T> list) {
        return list.get(this.nextInt(list.size()));
    }
}
