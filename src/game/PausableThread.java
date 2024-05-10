package game;

public class PausableThread extends Thread{

    private boolean paused = false;
    private final Object lock = new Object();

    public void pause() {
        paused = true;
    }
    protected void lock() {
        synchronized (lock) {
            if (paused) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
    public void continueThread() {
        synchronized (lock) {
            paused = false;
            lock.notifyAll();
        }
    }
}
