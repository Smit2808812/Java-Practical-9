   class SharedCounter {

    int value = 0;

    void increment() {
        value++;
    }
}

class SafeCounter {

    int value = 0;

    synchronized void increment() {
        value++;
    }
}

public class CounterRace {

    public static void main(String[] args)
            throws InterruptedException {

        int threads = 10;
        int loops = 100000;

        SharedCounter unsafe = new SharedCounter();

        Thread[] a = new Thread[threads];

        for (int i = 0; i < threads; i++) {

            a[i] = new Thread(() -> {
                for (int j = 0; j < loops; j++) {
                    unsafe.increment();
                }
            });

            a[i].start();
        }

        for (Thread t : a) {
            t.join();
        }

        System.out.println(
                "Without synchronization: " + unsafe.value
        );

        SafeCounter safe = new SafeCounter();

        Thread[] b = new Thread[threads];

        for (int i = 0; i < threads; i++) {

            b[i] = new Thread(() -> {
                for (int j = 0; j < loops; j++) {
                    safe.increment();
                }
            });

            b[i].start();
        }

        for (Thread t : b) {
            t.join();
        }

        System.out.println(
                "With synchronization: " + safe.value
                + " (expected " + (threads * loops) + ")"
        );
    }
}
