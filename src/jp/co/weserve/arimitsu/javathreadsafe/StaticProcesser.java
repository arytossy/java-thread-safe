package jp.co.weserve.arimitsu.javathreadsafe;

import java.time.LocalDateTime;

public final class StaticProcesser {

    private StaticProcesser() {}

    private static final Object heavyLock = new Object();
    private static final Object lightLock = new Object();

    public static void heavyProcess() {
        LocalDateTime start = LocalDateTime.now();
        logging("Start heavy process...");
        try {
            Thread.sleep(3000);
            LocalDateTime end = LocalDateTime.now();
            logging("End heavy process! time: " + (end.compareTo(start)) + "ms");
        } catch (Exception e) {
            logging("Failed heavy process... cause: " + e.getMessage());
        }
    }

    public static void lightProcess() {
        LocalDateTime start = LocalDateTime.now();
        logging("Start light process...");
        try {
            Thread.sleep(500);
            LocalDateTime end = LocalDateTime.now();
            logging("End light process! time: " + (end.compareTo(start)) + "ms");
        } catch (Exception e) {
            logging("Failed light process... cause: " + e.getMessage());
        }
    }

    public static /* synchronized */ void heavyProcessWithLock() {
        synchronized (heavyLock) {
            heavyProcess();
        }
    }

    public static /* synchronized */ void lightProcessWithLock() {
        synchronized (lightLock) {
            lightProcess();
        }
    }

    private static void logging(String message) {
        long threadId = Thread.currentThread().getId();
        String header = String.format("[Thread: %d]", threadId);
        System.out.println(String.format("%s %s", header, message));
    }
}
