package jp.co.weserve.arimitsu.javathreadsafe;

public class Counter {

    private volatile int count;

    public int increment() {
        return ++this.count;
    }

    public int decrement() {
        return --this.count;
    }

    public int getResult() {
        return this.count;
    }
}
