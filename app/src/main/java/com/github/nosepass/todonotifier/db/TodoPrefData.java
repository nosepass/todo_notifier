package com.github.nosepass.todonotifier.db;

/**
 * Pojo for sqlite data stored by Cupboard. Specifically this ends up being a one-row
 * table storing various settings, instead of just using pref keys. Because reasons.
 */
public class TodoPrefData {
    public static int INITIAL_INTERVAL_MINS = 15;

    public Long _id;
    public boolean enable; // Whether the nagger is enabled
    public int interval; // how often to nag, in minutes

    public TodoPrefData() {}
    public TodoPrefData(int interval) {
        enable = false;
        this.interval = interval;
    }
}
