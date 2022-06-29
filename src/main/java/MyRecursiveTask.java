import java.util.concurrent.RecursiveTask;

class MyRecursiveTask extends RecursiveTask<Integer> {
    private final int[] array;
    private final int start;
    private final int finish;

    private final int capacityPerThread;

    MyRecursiveTask(int[] array, int start, int finish, int capacityPerThread) {
        this.array = array;
        this.start = start;
        this.finish = finish;
        this.capacityPerThread = capacityPerThread;
    }

    int sumArr(int[] array, int start, int finish) {
        int result = 0;
        for (int i = start; i < finish + 1; i++) {
            result += array[i];
        }
        return result;
    }

    @Override
    protected Integer compute() {
        if (finish - start < capacityPerThread) {
            return sumArr(array, start, finish);
        }
        int middle = (finish + start) / 2;
        MyRecursiveTask oneTask = new MyRecursiveTask(array, start, middle, capacityPerThread);
        MyRecursiveTask secondTask = new MyRecursiveTask(array, middle + 1, finish, capacityPerThread);
        oneTask.fork();
        return oneTask.join() + secondTask.compute();
    }
}
