import java.util.Comparator;

/**
 * ParallelMergeSorter class performs the Merge Sort algorithm in a "divide and conquer" manner using multithreading.
 * The maximum number of threads used is dependent on the system's available processors (physical + logical)
 *
 * @author Jamal Rasool
 */

public class ParallelMergeSorter extends MergeSorter {

    /**
     * Designed to check and see what number of threads that the program is able to work with in order to achieve a
     * parallel sort through the system.
     * @param x Passing through the number of threads that the program is able to be allotted.
     * @return Returns to see what power of two the number of threads is.
     */
    private static int depth(int x) {
        if ((x & (x - 1)) == 0) {
            for (int h = 0; h < 100; ++h) {
                if (Math.pow(2, h) == x) {
                    return h;
                }
            }
        }
        // Single Core Processors?
        return 1;
    }

    /**
     * sort method is similar to the super classes' implementation however has an extra parameter to include the number
     * of available processors.
     *
     * @param a refers to the array to be sorted
     * @param comp refers to the Comparator object that is defined in the callee
     * @param threads refers to the number of threads that is available in the machine present
     */
    public static <E> void sort(E[] a, Comparator<? super E> comp, int threads){
        int depth = depth(threads);

        parallelMergeSort(a, 0, a.length - 1, comp, depth);
    }

    /**
     * parallelMergeSort is similar to the super classes mergeSort method however utilizes multithreading to divide the
     * work evenly amongst the available threads. The method works with a variety of data types (ints, doubles ...)
     * as long as there is a properly defined Comparator object.
     *
     * @param a refers to the array to be sorted recursively
     * @param from indicates the index of the beginning of the array to sort
     * @param to indicates the index of the last element of the array to sort
     * @param comp is the Comparator object which is defined in the ParallelSort class
     * @param depth refers to the number of levels to allocate threads
     */
    private static <E> void parallelMergeSort(E[] a, int from, int to, Comparator<? super E> comp, int depth) {
        if (from == to) {
            return;
        }
        if (depth == 0) {
            mergeSort(a, from, to, comp);
            return;
        }

        int mid = (from + to) / 2;
        // Sort the first and the second half
        Thread firstHalf = new Thread() {
            public void run() {
                parallelMergeSort(a, from, mid, comp, depth - 1);
            }
        };
        Thread secondHalf = new Thread() {
            public void run() {
                parallelMergeSort(a, mid + 1, to, comp, depth - 1);
            }
        };

        firstHalf.start();
        secondHalf.start();

        try {
            firstHalf.join();
            secondHalf.join();
        }
        catch (InterruptedException i) {
            i.printStackTrace();
        }

        merge(a, from, mid, to, comp);
    }
}