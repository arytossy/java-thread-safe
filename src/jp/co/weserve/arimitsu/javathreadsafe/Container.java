package jp.co.weserve.arimitsu.javathreadsafe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Container {

    // private static int id = 0;

    // private final Map<Integer, Counter> counters
    //     = new HashMap<>();
    //     // = Collections.synchronizedMap(new HashMap<>());

    private final List<Counter> counters
        // = new ArrayList<>();
        = Collections.synchronizedList(new ArrayList<>());

    public void newCounter() {
        // this.counters.put(id++, new Counter());
        this.counters.add(new Counter());
    }

    public int getSize() {
        return this.counters.size();
    }
}
