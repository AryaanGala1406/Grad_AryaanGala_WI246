import java.util.*;
import java.util.concurrent.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class BikeRacingGame {

    static final int TOTAL_BIKERS = 10;

    static CountDownLatch startLatch = new CountDownLatch(1);
    static CyclicBarrier finishBarrier;

    static List<Biker> finishedBikers = new ArrayList<>();

    static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static void main(String[] args) throws Exception {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String[] names = new String[TOTAL_BIKERS];

        System.out.println("Enter names of 10 bikers:");
        for (int i = 0; i < TOTAL_BIKERS; i++) {
            names[i] = br.readLine();
        }

        System.out.print("Enter race distance (in KM): ");
        int distanceKm = Integer.parseInt(br.readLine());
        int totalMeters = distanceKm * 1000;

        // CyclicBarrier action (runs once after all bikers finish racing)
        finishBarrier = new CyclicBarrier(TOTAL_BIKERS, () -> {
            System.out.println("\nALL BIKERS FINISHED THE RACE");
            showDashboard();
        });

        System.out.println("\nAll bikers ready...");

        // Executor Service
        ExecutorService executor = Executors.newFixedThreadPool(TOTAL_BIKERS);

        for (String name : names) {
            executor.submit(new Biker(name, totalMeters));
        }

        // Countdown
        for (int i = 3; i >= 1; i--) {
            System.out.println("Race starts in " + i + "...");
            Thread.sleep(1000);
        }

        System.out.println("GO !!!\n");

        // Same start time for all bikers
        Biker.startTime = LocalTime.now();
        startLatch.countDown();

        executor.shutdown();
    }

    // Dashboard
    static void showDashboard() {

        finishedBikers.sort(
                Comparator.comparingLong(b -> b.timeTakenSeconds));

        System.out.println("\nFINAL DASHBOARD");
        System.out.println("---------------------------------------------------------------");
        System.out.printf("%-5s %-10s %-15s %-15s %-10s\n",
                "Rank", "Name", "Start Time", "End Time", "Time(s)");

        int rank = 1;
        for (Biker b : finishedBikers) {
            System.out.printf("%-5d %-10s %-15s %-15s %-10d\n",
                    rank++,
                    b.name,
                    b.startTime.format(timeFormatter),
                    b.endTime.format(timeFormatter),
                    b.timeTakenSeconds);
        }
    }

    // Biker Task
    static class Biker implements Runnable {

        String name;
        int distance;

        static LocalTime startTime;
        LocalTime endTime;

        long timeTakenSeconds;

        Biker(String name, int distance) {
            this.name = name;
            this.distance = distance;
        }

        public void run() {
            try {
                // Wait for race start
                startLatch.await();

                int covered = 0;
                Random rand = new Random();

                while (covered < distance) {
                    int speed = (rand.nextInt(5) + 1) * 50; // 50–250 m/s
                    Thread.sleep(1000);
                    covered += speed;

                    int display = Math.min(covered, distance);
                    if (display % 100 == 0 || display >= distance) {
                        System.out.println(name + " covered " + display + " meters");
                    }
                }

                endTime = LocalTime.now();
                timeTakenSeconds = Duration.between(startTime, endTime).getSeconds();
                finishedBikers.add(this);

                // Wait for all bikers to finish
                finishBarrier.await();

            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}
