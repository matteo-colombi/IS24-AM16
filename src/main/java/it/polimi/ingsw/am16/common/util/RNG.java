package it.polimi.ingsw.am16.common.util;

import java.util.Random;

/**
 * Singleton class that holds a single instance of {@link java.util.Random}.
 * Used to handle all the random events of the game.
 * Initialized with a seed (by default, the system's time, obtained through {@link System}'s <code>nanoTime()</code>).
 */
public class RNG extends Random {

    private static RNG instance = null;
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

    public synchronized String nextAlphNumString(int length) {
        if (instance == null) instance = new RNG(System.nanoTime());

        if (length < 1) throw new IllegalArgumentException("Length must be at least 1");

        StringBuilder builder = new StringBuilder();
        for(int i = 0; i<length; i++)
            builder.append(symbols[instance.nextInt(symbols.length)]);

        return builder.toString();
    }
}
