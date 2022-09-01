package model;

import java.util.TimerTask;

public class Timer {
    private java.util.Timer timer;
    private Runnable callback;
    private long delay;
    private boolean isRunning;

    public Timer() {
        this.timer = new java.util.Timer();
        this.isRunning = false;
    }

    public void setCallback(Runnable callback) {
        this.callback = callback;
    }

    public void start(long delay) {
        this.delay = delay;
        this.isRunning = true;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                callback.run();
            }
        }, delay);
    }

    public void restart() {
        this.timer.cancel();
        this.isRunning = false;
        this.timer = new java.util.Timer();
        this.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                callback.run();
            }
        }, delay);
        this.isRunning = true;
    }

    public boolean isRunning() {
        return isRunning;
    }
}
