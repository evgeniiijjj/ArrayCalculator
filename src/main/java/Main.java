import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

public class Main {
    static int capacity = 200_000_000;
    static int capacityPerThread = 500_000;

    public static void main(String[] args) {
        int[] array = getArray(capacity);
        int result;

        System.out.println("Суммирование " + capacity + " элементов массива:");
        System.out.println();

        long start = System.currentTimeMillis();
        result = Arrays.stream(array).sum();
        long timeDiff = System.currentTimeMillis() - start;
        System.out.println("Stream API: результат " + result + ", затрачено времени " + timeDiff + " млс");
        System.out.println();

        start = System.currentTimeMillis();
        result = sumArray(array, 0, array.length - 1);
        timeDiff = System.currentTimeMillis() - start;
        System.out.println("Простым перебором: результат " + result + ", затрачено времени " + timeDiff + " млс");
        System.out.println();

        start = System.currentTimeMillis();
        result = sumArrayWithRecursion(array, 0, array.length - 1);
        timeDiff = System.currentTimeMillis() - start;
        System.out.println("Рекурсией: результат " + result + ", затрачено времени " + timeDiff + " млс");
        System.out.println();

        start = System.currentTimeMillis();
        result = sumArrayWithForkJoinPool(array);
        timeDiff = System.currentTimeMillis() - start;
        System.out.println("Разбиением задачи на подзадачи и запуска их в отдельном потоке: результат " + result + ", затрачено времени " + timeDiff + " млс");
    }

    static int[] getArray(int capacity) {
        Random random = new Random();
        int[] res = new int[capacity];
        for (int i = 0; i < capacity; i++) {
            res[i] = random.nextInt(100);
        }
        return res;
    }

    static int sumArray(int[] array, int start, int finish) {
        int result = 0;
        for (int i = start; i < finish + 1; i++) {
            result += array[i];
        }
        return result;
    }

    static int sumArrayWithRecursion(int[] array, int start, int finish) {
        if (finish - start < 2) {
            if (finish > start) return array[start] + array[finish];
            else return array[start];
        }
        int middle = (start + finish) / 2;
        return sumArrayWithRecursion(array, start, middle) + sumArrayWithRecursion(array, middle + 1, finish);
    }

    static int sumArrayWithForkJoinPool(int[] array) {
        return new ForkJoinPool().invoke(new MyRecursiveTask(array, 0, array.length - 1, capacityPerThread));
    }
}
