import java.util.Random;
import java.util.concurrent.ForkJoinPool;

public class Main {
    private static final int ROWS = 1000;
    private static final int COLS = 1000;
    private static final int MIN_VALUE = 1;
    private static final int MAX_VALUE = 100;

    public static void main(String[] args) {
        // Створення та виведення матриці
        int[][] matrix = generateMatrix();
        if(ROWS * COLS < 100) {
            System.out.println("Matrix:");
            printMatrix(matrix);
        }

        // Використання Fork/Join для обчислень
        long startTime = System.nanoTime();
        int[] result;
        try (ForkJoinPool pool = ForkJoinPool.commonPool()) {
            result = pool.invoke(new ColumnSumRecursiveTask(matrix, 0, COLS));
        }
        long endTime = System.nanoTime();

        // Виведення результатів
        System.out.println("\nResults:");
        for (int i = 0; i < result.length; i++) System.out.printf("Column %d: %d%n", i + 1, result[i]);
        System.out.printf("Process finished in: %.2fms%n", (endTime - startTime) / 1_000_000.0);
    }

    private static int[][] generateMatrix() {
        Random random = new Random();
        int[][] matrix = new int[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                matrix[i][j] = random.nextInt(MAX_VALUE - MIN_VALUE + 1) + MIN_VALUE;
            }
        }
        return matrix;
    }

    private static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int value : row) {
                System.out.printf("%4d", value);
            }
            System.out.println();
        }
    }
}