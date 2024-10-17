import java.util.concurrent.CyclicBarrier;

public class Car implements Runnable {
    private static int CARS_COUNT;
    private static int winnerFound = 0; // Индекс для победителей

    static {
        CARS_COUNT = 0;
    }

    private Race race;
    private int speed;
    private String name;
    private CyclicBarrier cb;
    private int id; // Новый идентификатор участника

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    public Car(Race race, int speed, CyclicBarrier cb) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.id = CARS_COUNT; // Присваиваем уникальный ID
        this.name = "Участник #" + id;
        this.cb = cb;
    }

    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int) (Math.random() * 800));
            System.out.println(this.name + " готов");

            cb.await(); // Ожидаем, пока все гонщики не будут готовы

            // Проходим этапы гонки
            for (int i = 0; i < race.getStages().size(); i++) {
                race.getStages().get(i).go(this);
            }

            checkWin(this); // Проверяем победителя
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private synchronized void checkWin(Car car) {
        // Добавляем условие проверки победителей при завершении гонки
        if (winnerFound < 3) {
            Main.winners[winnerFound] = car.id; // Используем id для сохранения победителей
            winnerFound++;
        }
    }

    public static void printWinner() {
        for (int i = 0; i < Main.winners.length; i++) {
            System.out.println("Участник " + Main.winners[i] + " оказался на месте " + (i + 1));
        }
    }
}
