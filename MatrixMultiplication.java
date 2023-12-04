import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class MatrixMultiplication {

    public static void main(String[] args) {
        int size = 3000;
        int[][] matrixA = generateRandomMatrix(size, size);
        int[][] matrixB = generateRandomMatrix(size, size);
        int[][] result = new int[size][size];
        int numThreads = 30;
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        List<Callable<Void>> tasks = new ArrayList<>();

        int blockSize = size / numThreads;
        for (int i = 0; i < numThreads; i++) {
            final int rowStart = i * blockSize;
            final int rowEnd = (i == numThreads - 1) ? size : (i + 1) * blockSize;

            tasks.add(() -> {
                multiplyMatrices(matrixA, matrixB, result, rowStart, rowEnd);
                return null;
            });
        }

        long startTime = System.currentTimeMillis();

        try {
            List<Future<Void>> futures = executorService.invokeAll(tasks);

            for (Future<Void> future : futures) {
                future.get();
            }


        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }

        long endTime = System.currentTimeMillis();

        long executionTime = endTime - startTime;
        printMatrix(result);
        System.out.println("Час виконання програми: " + executionTime + " мілісекунд");
    }


    private static int[][] generateRandomMatrix(int rows, int cols) {
        int[][] matrix = new int[rows][cols];
        Random random = new Random();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = random.nextInt(10);
            }
        }

        return matrix;
    }

    private static void multiplyMatrices(int[][] matrixA, int[][] matrixB, int[][] result, int rowStart, int rowEnd) {
        for (int i = rowStart; i < rowEnd; i++) {
            for (int j = 0; j < matrixB[0].length; j++) {
                for (int k = 0; k < matrixB.length; k++) {
                    result[i][j] += matrixA[i][k] * matrixB[k][j];
                }
            }
        }
    }

    private static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }
}

