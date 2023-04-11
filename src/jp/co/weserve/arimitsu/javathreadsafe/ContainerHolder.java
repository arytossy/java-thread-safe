package jp.co.weserve.arimitsu.javathreadsafe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ContainerHolder {

    // private static int id = 0;

    // private static final Map<Integer, Container> containers
    //     = new HashMap<>();
    //     // = Collections.synchronizedMap(new HashMap<>());

    private static final List<Container> containers
        // = new ArrayList<>();
        = Collections.synchronizedList(new ArrayList<>());

    private ContainerHolder() {}

    public static void newContainer() {
        // containers.put(id++, new Container());
        containers.add(new Container());
    }

    public static Container getNewerContainer() {
        // return containers.get(id - 1);
        return containers.get(containers.size() - 1);
    }

    public static int getSize() {
        return containers.size();
    }
}
