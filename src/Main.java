import java.util.concurrent.CyclicBarrier;

public class Main {
    static final int CARS_COUNT = 15;
    public static int[] winners = new int[3]; // Массив для хранения мест победителей
    public static int winnerCount = 0; // Счетчик победителей
    public static final int TONNEL_LIMIT = 3;
    private static final CyclicBarrier cb = new CyclicBarrier(CARS_COUNT + 1); // Создаем барьер

    public static void main(String[] args) {
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");

        // Создаем гонку с этапами
        Race race = new Race(new Road(60), new Tunnel(), new Road(40));

        Thread[] threads = new Thread[CARS_COUNT]; // Массив потоков
        for (int i = 0; i < CARS_COUNT; i++) {
            Car car = new Car(race, 2 + (int) (Math.random() * 15), cb);
            threads[i] = new Thread(car); // Создаем поток для каждого гонщика
            threads[i].start(); // Запускаем поток
        }

        // Ожидаем, пока все гонщики не будут готовы
        try {
            cb.await(); // Ждем, пока все гонщики не дойдут до барьера
            System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Ожидаем завершения гонщиков
        for (Thread thread : threads) {
            try {
                thread.join(); // Ожидание завершения каждого потока
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
        System.out.println();
        Car.printWinner();
    }
}
