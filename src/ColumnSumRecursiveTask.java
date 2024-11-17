import java.util.concurrent.RecursiveTask;

public class ColumnSumRecursiveTask extends RecursiveTask<int[]> {
    private static final int THRESHOLD = 100; // Гранична кількість стовпців для прямого обчислення
    private final int[][] matrix;
    private final int startCol;
    private final int endCol;

    public ColumnSumRecursiveTask(int[][] matrix, int startCol, int endCol) {
        this.matrix = matrix;
        this.startCol = startCol;
        this.endCol = endCol;
    }

    @Override
    protected int[] compute() {
        int numCols = endCol - startCol;

        if (numCols <= THRESHOLD) return computeDirectly();

        int mid = startCol + numCols / 2;

        ColumnSumRecursiveTask leftTask = new ColumnSumRecursiveTask(matrix, startCol, mid);
        ColumnSumRecursiveTask rightTask = new ColumnSumRecursiveTask(matrix, mid, endCol);

        leftTask.fork();
        int[] rightResult = rightTask.compute();
        int[] leftResult = leftTask.join();

        return mergeResults(leftResult, rightResult);
    }

    private int[] computeDirectly() {
        int[] columnSums = new int[endCol - startCol];
        for (int col = startCol; col < endCol; col++) {
            int sum = 0;
            for (int[] row : matrix) {
                sum += row[col];
            }
            columnSums[col - startCol] = sum;
        }
        return columnSums;
    }

    private int[] mergeResults(int[] leftResult, int[] rightResult) {
        int[] merged = new int[leftResult.length + rightResult.length];
        System.arraycopy(leftResult, 0, merged, 0, leftResult.length);
        System.arraycopy(rightResult, 0, merged, leftResult.length, rightResult.length);
        return merged;
    }
}
