package jp.co.weserve.arimitsu.javathreadsafe;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class CounterHolder {

    private static final Map<String, Counter> counters
        = new HashMap<>();
        // = Collections.synchronizedMap(new HashMap<>());

    private CounterHolder() {}

    public static void newCounter(String name) {
        counters.put(name, new Counter());
    }

    public static Counter getCounter(String name) {
        return counters.get(name);
    }
}
