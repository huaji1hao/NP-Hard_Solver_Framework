package com.aim.project.uzf.instance;

/**
 * @author Warren G Jackson
 * @since 1.0.0 (22/03/2024)
 */
public record Location (int iLocationId, int x, int y) {

    static int id = 0;

    public Location(int x, int y) {

        this(id++, x, y);
    }
}
