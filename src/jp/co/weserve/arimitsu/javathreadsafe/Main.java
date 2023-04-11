package jp.co.weserve.arimitsu.javathreadsafe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws Exception {

        // counterTest();
        // mapTest();
        lockTest();
    }

    private static void counterTest() throws Exception {

        CounterHolder.newCounter("one");
        CounterHolder.newCounter("two");
        CounterHolder.newCounter("three");

        ExecutorService pool = Executors.newCachedThreadPool();

        // Random random = new Random();

        // counters
        for (String name : new String[]{"one", "two", "three"}) {

            // users
            for (int i = 0; i < 3; i++) {

                // each thread
                pool.submit(() -> {

                    long threadId = Thread.currentThread().getId();
                    // Counter counter = CounterHolder.getCounter(name);
                    List<Integer> incrementResult = new ArrayList<>();
                    List<Integer> decrementResult = new ArrayList<>();

                    for (int j = 0; j < 300; j++) {
                        incrementResult.add(CounterHolder.getCounter(name).increment());
                        // int millisec = (random.nextInt(5) + 1) * 100;
                        // try {
                        //     Thread.sleep(millisec);
                        // } catch (Exception e) {}
                    }

                    for (int k = 0; k < 300; k++) {
                        decrementResult.add(CounterHolder.getCounter(name).decrement());
                        // int millisec = (random.nextInt(5) + 1) * 100;
                        // try {
                        //     Thread.sleep(millisec);
                        // } catch (Exception e) {}
                    }

                    // String message = ""
                    //     + "////////// Thread: " + threadId + " ////////////////////"
                    //     + "\n"
                    //     + "Counter: " + name
                    //     + "\n"
                    //     + ">>> Increment: " + incrementResult.stream()
                    //                             .map(num -> num.toString())
                    //                             .collect(Collectors.joining(","))
                    //     + "\n"
                    //     + ">>> Decrement: " + decrementResult.stream()
                    //                             .map(num -> num.toString())
                    //                             .collect(Collectors.joining(","))
                    // ;

                    // System.out.println(message);
                });
            }
        }

        pool.shutdown();
        pool.awaitTermination(10, TimeUnit.SECONDS);

        System.out.println("==================================================");
        System.out.println("Counter one result: " + CounterHolder.getCounter("one").getResult());
        System.out.println("Counter two result: " + CounterHolder.getCounter("two").getResult());
        System.out.println("Counter three result: " + CounterHolder.getCounter("three").getResult());
    }

    private static void mapTest() throws Exception {

        ExecutorService pool = Executors.newCachedThreadPool();

        for (int i = 0; i < 100; i++) {
            pool.submit(() -> {
                for (int j = 0; j < 100; j++) {
                    ContainerHolder.newContainer();
                }
            });
        }

        pool.shutdown();
        pool.awaitTermination(10, TimeUnit.SECONDS);

        pool = Executors.newCachedThreadPool();

        for (int k = 0; k < 100; k++) {
            pool.submit(() -> {
                for (int l = 0; l < 100; l++) {
                    ContainerHolder.getNewerContainer().newCounter();
                }
            });
        }

        pool.shutdown();
        pool.awaitTermination(10, TimeUnit.SECONDS);

        System.out.println("Containers: " + ContainerHolder.getSize());
        System.out.println("Counters: " + ContainerHolder.getNewerContainer().getSize());
    }

    private static void lockTest() throws Exception {

        ExecutorService pool = null;

        Consumer<String> logging = (String message) -> {
            long threadId = Thread.currentThread().getId();
            String header = String.format("[Thread: %d]", threadId);
            System.out.println(String.format("%s %s", header, message));
        };

        Runnable callHeavyWithoutLock = () -> {
            logging.accept("Calling heavy process *WITHOUT* lock...");
            StaticProcesser.heavyProcess();
        };
        Runnable callHeavyWithLock = () -> {
            logging.accept("Calling heavy process with lock...");
            StaticProcesser.heavyProcessWithLock();
        };
        Runnable callLightWithoutLock = () -> {
            logging.accept("Calling light process *WITHOUT* lock...");
            StaticProcesser.lightProcess();
        };
        Runnable callLightWithLock = () -> {
            logging.accept("Calling light process with lock...");
            StaticProcesser.lightProcessWithLock();
        };

        System.out.println("========== NOT Thread Safe .....");
        pool = Executors.newCachedThreadPool();
        for (int i = 0; i < 3; i++) {
            pool.submit(callHeavyWithoutLock);
        }
        for (int j = 0; j < 5; j++) {
            pool.submit(callLightWithoutLock);
        }
        pool.shutdown();
        pool.awaitTermination(30, TimeUnit.SECONDS);

        System.out.println("========== Thread Safe .....");
        pool = Executors.newCachedThreadPool();
        for (int i = 0; i < 3; i++) {
            pool.submit(callHeavyWithLock);
        }
        for (int j = 0; j < 5; j++) {
            pool.submit(callLightWithLock);
        }
        pool.shutdown();
        pool.awaitTermination(30, TimeUnit.SECONDS);
    }

}
