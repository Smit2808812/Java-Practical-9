import java.util.concurrent.atomic.AtomicLong;

public class ParallelSum {

    static long unsafeTotal = 0;
    static long safeTotal = 0;

    static AtomicLong atomicTotal = new AtomicLong();

    static synchronized void addSafe(long n) {
        safeTotal += n;
    }

    public static void main(String[] args)
            throws InterruptedException {

        int size = 200000;
        int threads = 8;

        int[] data = new int[size];

        java.util.Arrays.fill(data, 1);

        Thread[] ts = new Thread[threads];

        for (int t = 0; t < threads; t++) {

            final int from = t * size / threads;
            final int to = (t + 1) * size / threads;

            ts[t] = new Thread(() -> {

                for (int i = from; i < to; i++) {
                    unsafeTotal += data[i];
                }
            });

            ts[t].start();
        }

        for (Thread t : ts) {
            t.join();
        }

        System.out.println(
                "Unsynchronized total: " + unsafeTotal
        );

        for (int t = 0; t < threads; t++) {

            final int from = t * size / threads;
            final int to = (t + 1) * size / threads;

            ts[t] = new Thread(() -> {

                long local = 0;

                for (int i = from; i < to; i++) {
                    local += data[i];
                }

                addSafe(local);
            });

            ts[t].start();
        }

        for (Thread t : ts) {
            t.join();
        }

        System.out.println(
                "Synchronized total: " + safeTotal
        );

        for (int t = 0; t < threads; t++) {

            final int from = t * size / threads;
            final int to = (t + 1) * size / threads;

            ts[t] = new Thread(() -> {

                for (int i = from; i < to; i++) {
                    atomicTotal.addAndGet(data[i]);
                }
            });

            ts[t].start();
        }

        for (Thread t : ts) {
            t.join();
        }

        System.out.println(
                "AtomicLong total: " + atomicTotal.get()
                + ", expected: " + size
        );
    }
}
	
