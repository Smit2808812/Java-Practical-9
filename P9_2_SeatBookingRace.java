class Booking {

    int seatsLeft = 5;
    int successful = 0;

    boolean bookUnsafe() {

        if (seatsLeft > 0) {

            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            seatsLeft--;
            successful++;

            return true;
        }

        return false;
    }

    synchronized boolean bookSafe() {

        if (seatsLeft > 0) {

            seatsLeft--;
            successful++;

            return true;
        }

        return false;
    }
}

public class SeatBookingRace {

    public static void main(String[] args)
            throws InterruptedException {

        for (boolean safe : new boolean[]{false, true}) {

            Booking b = new Booking();

            Thread[] ts = new Thread[10];

            for (int i = 0; i < 10; i++) {

                ts[i] = new Thread(() -> {

                    if (safe) {
                        b.bookSafe();
                    } else {
                        b.bookUnsafe();
                    }
                });

                ts[i].start();
            }

            for (Thread t : ts) {
                t.join();
            }

            System.out.println(
                    (safe ? "Synchronized" : "Unsynchronized")
                    + ": successful=" + b.successful
                    + ", seatsLeft=" + b.seatsLeft
            );
        }
    }
}
