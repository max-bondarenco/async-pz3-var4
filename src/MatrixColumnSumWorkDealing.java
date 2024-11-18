import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class MatrixColumnSumWorkDealing {
    private final ExecutorService executor;
    private final int[][] matrix;
    private final int cols;
    private final int colsPerThread;

    public MatrixColumnSumWorkDealing(ExecutorService executor, int[][] matrix, int cols, int colsPerThread) {
        this.executor = executor;
        this.matrix = matrix;
        this.cols = cols;
        this.colsPerThread = colsPerThread;
    }

    public int[] compute() {
        List<Future<int[]>> futures = new ArrayList<>();
        for (int batch = 0; batch < cols/colsPerThread; batch++) {
            int startingCol = batch*colsPerThread;

            futures.add(executor.submit(() -> {
                int[] sum = new int[colsPerThread];

                for (int[] row : matrix)
                    for (int col = 0; col < colsPerThread; col++)
                        sum[col] += row[startingCol + col];

                return sum;
            }));
        }

        List<Integer> columnSums = new ArrayList<>();
        int[] batch;

        for (Future<int[]> future: futures) {
            try {
                batch = future.get();
                for(int sum: batch) columnSums.add(sum);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (ExecutionException e) {
                System.out.println("Error occurred during execution" + e);
            }
        }

        return columnSums.stream().mapToInt(i->i).toArray();
    }
}
