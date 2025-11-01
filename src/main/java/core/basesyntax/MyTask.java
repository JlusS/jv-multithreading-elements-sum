package core.basesyntax;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class MyTask extends RecursiveTask<Long> {
    private int startPoint;
    private int finishPoint;

    public MyTask(int startPoint, int finishPoint) {
        this.startPoint = startPoint;
        this.finishPoint = finishPoint;
    }

    @Override
    protected Long compute() {
        if (finishPoint - startPoint <= 10) {
            long sum = 0;
            for (int i = startPoint; i < finishPoint; i++) {
                sum += i;
            }
            System.out.println("Computing sum from " + startPoint + " to "
                    + (finishPoint - 1) + ": " + sum);
            return sum;
        }

        System.out.println("Splitting task: " + startPoint + " to " + finishPoint);

        int middle = startPoint + (finishPoint - startPoint) / 2;

        List<RecursiveTask<Long>> subTasks = createSubTasks(startPoint, middle, finishPoint);

        for (RecursiveTask<Long> subTask : subTasks) {
            subTask.fork();
        }

        long result = 0;
        for (RecursiveTask<Long> subTask : subTasks) {
            result += subTask.join();
        }

        return result;
    }

    private List<RecursiveTask<Long>> createSubTasks(int start, int middle, int finish) {
        List<RecursiveTask<Long>> subTasks = new ArrayList<>();

        RecursiveTask<Long> first = new MyTask(start, middle);
        RecursiveTask<Long> second = new MyTask(middle, finish);

        subTasks.add(first);
        subTasks.add(second);
        return subTasks;
    }
}
