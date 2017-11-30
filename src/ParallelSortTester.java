import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.io.PrintWriter;

/**
 * ParallelSortTester class will run sorting simulations using the parallelized ParallelMergeSorter class and
 * assesses the speedup from using multiple threads (serial run time is included for comparison). Each trial
 * times the sorting speed with different sizes of random number arrays (using the super class's createRandomArray
 * method)
 *
 * @author Jamal Rasool
 */
public class ParallelSortTester extends SortTester {

    /**
     * main method simply calls the runParallelSortTester() method
     */
    public static void main(String[] args) {
        runParallelSortTester();
    }

    /**
     * runParallelSortTester method defines the parameters of each experiment (number of threads) and prints out the
     * run time of each experiment. In each experiment, the array is checked if it has been sorted correctly.
     */
    public static void runParallelSortTester() {
        final int initialArraySize = 1000,   // initial length of array to sort
                initialRun = 15,
                processorsInMachine = Runtime.getRuntime().availableProcessors();

        Integer[] a;

        Comparator<Integer> comp = new Comparator<Integer>() {
            public int compare(Integer d1, Integer d2) {
                return d1.compareTo(d2);
            }
        };

        // Beginning the Output Sequence

        try {
            PrintWriter writer = new PrintWriter("ParallelSortTester_Output.txt", "UTF-8");

            System.out.printf("SYS INFO: \n\tAvailable Processors == %d\n\n", processorsInMachine);
            writer.printf("SYS INFO: \n\tAvailable Processors == %d\n\n", processorsInMachine);
            for (int i = 1; i <= processorsInMachine; i*=2) {
                System.out.printf("Running trial with %d thread(s) ...\n", i);
                writer.printf("Running trial with %d thread(s) ...\n", i);
                for (int q = 0, j = initialArraySize; q < initialRun; ++q, j*=2) {
                    a = createRandomArray(j);
                    // run the algorithm and time how long it takes to sort the elements
                    long startTime = System.currentTimeMillis();
                    ParallelMergeSorter.sort(a, comp, i);
                    long endTime = System.currentTimeMillis();

                    if (!isSorted(a, comp)) {
                        throw new RuntimeException("not sorted afterward: " + Arrays.toString(a));
                    }
                    System.out.printf("%10d elements  =>  %6d ms \n", j, endTime - startTime);
                    writer.printf("%10d elements  =>  %6d ms \n", j, endTime - startTime);
                }
                System.out.print("\n");
                writer.print("\n");
            }

            writer.close();

        } catch (IOException e) {
            System.err.println("Problem writing to the file ParallelSortTester_Output.txt");
        }




    }
}