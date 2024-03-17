package it.polimi.ingsw.am16.common.util;

import java.util.Random;

/**
 * Singleton class that holds a single instance of {@link java.util.Random}.
 * Used to handle all the random events of the game.
 * Initialized with a seed (by default, the system's time, obtained through {@link System}'s <code>nanoTime()</code>).
 */
public class RNG extends Random {

    private static RNG instance = null;

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
}
