import java.util.*;


/**
 * A class that contains a group of sorting algorithms.
 * The input to the sorting algorithms is assumed to be
 * an array of integers.
 * 
 * @author Donald Chinn
 * @version September 19, 2003
 */
public class Sort {

    // Constructor for objects of class Sort
    public Sort() {
    }


    /**
     * Given an array of integers and an integer k, sort the array
     * (ascending order) using k-way mergesort.
     * @param data  an array of integers
     * @param k     the k in k-way mergesort
     */
    public static void kwayMergesort (int[] data, int k) {
        kwayMergesortRecursive (data, 0, data.length - 1, k);
    }
    
    /**
     * The recursive part of k-way mergesort.
     * Given an array of integers (data), a low index, high index, and an integer k,
     * sort the subarray data[low..high] (ascending order) using k-way mergesort.
     * @param data  an array of integers
     * @param low   low index
     * @param high  high index
     * @param k     the k in k-way mergesort
     */
    public static void kwayMergesortRecursive (int[] data, int low, int high, int k) {
        if (low < high) {
            for (int i = 0; i < k; i++) {
                kwayMergesortRecursive (data,
                                        low + i*(high-low+1)/k,
                                        low + (i+1)*(high-low+1)/k - 1,
                                        k);
            }
            merge (data, low, high, k);
        }
    }
    

    /**
     * Given an array of integers (data), a low index, a high index, and an integer k,
     * sort the subarray data[low..high].  This method assumes that each of the
     * k subarrays  data[low + i*(high-low+1)/k .. low + (i+1)*(high-low+1)/k - 1],
     * for i = 0..k-1, are sorted.
     * @param data  an array of integers
     * @param high  the high index
     * @param k     the k in k-way mergesort
     * @param low   the low index
     */
    public static void merge (int[] data, int low, int high, int k) {

        if (high < low + k) {
            // the subarray has k or fewer elements
            // just make one big heap and do deleteMins on it
            Comparable[] subarray = new MergesortHeapNode[high - low + 1];
            for (int i = 0, j = low; i < subarray.length; i++, j++) {
                subarray[i] = new MergesortHeapNode(data[j], 0);
            }
            BinaryHeap heap = BinaryHeap.buildHeap(subarray);
            for (int j = low; j <= high; j++) {
                try {
                    data[j] = ((MergesortHeapNode) heap.deleteMin()).getKey();
                } catch (EmptyHeapException e) {
                    System.out.println("Tried to delete from an empty heap.");
                }
            }

        }
        else
        {
            // divide the array into k subarrays and do a k-way merge

            //Create an auxiliary array to hold merged values
            int[] auxArray = new int[high - low + 1];
            //divide the array into k subarrays
            //taking the top value index - low value index + 1 and then divide the size by k
            int subArraySize = (high - low + 1) / k;
            //Keep track of the start and end index of the subarrays
            int[] subArrayStartIndex = new int[k];
            int[] subArrayEndIndex = new int[k];

            //calculate the starting and ending index for each subarray
            for (int i = 0; i < k; i++) {
                subArrayStartIndex[i] = low + i*(high-low+1)/k;
                subArrayEndIndex[i] = low + (i+1)*(high-low+1)/k - 1;
            }
            //adjust the ending index for the last subarray to ensure all elements are covered
            subArrayEndIndex[k - 1] = high;
            //create a binary heap to store the smallest elements in each subarray
            BinaryHeap heap = new BinaryHeap();
            //Initialize the heap with the smallest elements from each subarray
            for (int i = 0; i < k; i++) {
                //Compare values in start and end index subarrays
                if (subArrayStartIndex[i] <= subArrayEndIndex[i]) {
                    int value = data[subArrayStartIndex[i]];
                    //Insert the smallest element from each subarray into the heap
                    heap.insert(new MergesortHeapNode(value, i));
                    //Move pointer to the next element in the subarray
                    subArrayStartIndex[i]++;
                }
            }
            //Merge the subarrays into the original array, base off the 'if' statement
            for (int i = 0; i < auxArray.length; i++) {
                try {
                    //Get the smalles element from the heap
                    MergesortHeapNode node = (MergesortHeapNode) heap.deleteMin();
                    int subarrayIndex = node.getWhichSubarray();
                    int val = node.getKey();
                    auxArray[i] = val;

                    //Check if there are more elements in the current subarray
                    if (subArrayStartIndex[subarrayIndex] <= subArrayEndIndex[subarrayIndex]) {
                        //Get the next element in the current subarray
                        int nextVal = data[subArrayStartIndex[subarrayIndex]];
                        //Send the next element into the heap
                        heap.insert(new MergesortHeapNode(nextVal, subarrayIndex));
                        //Move the pointer to the next element in the subrray
                        subArrayStartIndex[subarrayIndex]++;
                    }
                } catch (EmptyHeapException e) {
                    System.out.println("Tried to delete from an empty heap.");
                }

            }

            // Copy the merged values from the auxiliary array back to the original array
            for (int i = 0; i < auxArray.length; i++) {
                data[low + i] = auxArray[i];
            }

        }
    }
    
    
    /**
     * Given an integer size, produce an array of size random integers.
     * The integers of the array are between 0 and size (inclusive) with
     * random uniform distribution.
     * @param size  the number of elements in the returned array
     * @return      an array of integers
     */
    public static int[] getRandomArrayOfIntegers(int size) {
        int[] data = new int[size];
        for (int i = 0; i < size; i++) {
            data[i] = (int) ((size + 1) * Math.random());
        }
        return data;
    }
    

    /**
     * Given an integer size, produce an array of size random integers.
     * The integers of the output array are between 0 and size-1 with
     * exactly one of each in the array.  Each permutation is generated
     * with random uniform distribution.
     * @param size  the number of elements in the returned array
     * @return      an array of integers
     */
    public static int[] getRandomPermutationOfIntegers(int size) {
        int[] data = new int[size];
        for (int i = 0; i < size; i++) {
            data[i] = i;
        }
        // shuffle the array
        for (int i = 0; i < size; i++) {
            int temp;
            int swap = i + (int) ((size - i) * Math.random());
            temp = data[i];
            data[i] = data[swap];
            data[swap] = temp;
        }
        return data;
    }

    /**
     * Validate if the data is sorted
     * @param data an array of integers.
     * @return a boolean value
     */
    public static boolean isSorted(int[] data)
    {
        for (int i = 0; i<data.length-1; i++)
        {
            if (data[i] > data[i+1])
            {
                return false;
            }
        }
        return true;
    }
    /**
     * Perform checks to see if the algorithm has a bug.
     */
    private static void testCorrectness() {
        int[] data = getRandomPermutationOfIntegers(100);
        
        for (int i = 0; i < data.length; i++) {
            System.out.println("data[" + i + "] = " + data[i]);
        }
        
        int k = 100;
        kwayMergesort(data, k);
        
        // verify that data[i] = i
        for (int i = 0; i < data.length; i++) {
            if (data[i] != i) {
                System.out.println ("Error!  data[" + i + "] = " + data[i] + ".");
            }
        }
    }
    
    
    /**
     * Perform timing experiments.
     */
    private static void testTiming () {
        // timer variables
        long totalTime = 0;
        long startTime = 0;
        long finishTime = 0;

        // start the timer
        Date startDate = new Date();
        startTime = startDate.getTime();

        int n = 3200000;    // n = size of the array
        int k = 50;         // k = k in k-way mergesort
        int[] data = getRandomArrayOfIntegers(n);
        //printArray(data);
        kwayMergesort(data, k);

        // stop the timer
        Date finishDate = new Date();
        finishTime = finishDate.getTime();
        totalTime += (finishTime - startTime);

        if(isSorted(data))
        {
            System.out.println("** Results for k-way mergesort:");
            System.out.println("    " + "n = " + n + "    " + "k = " + k);
            System.out.println("    " + "Time: " + totalTime + " ms.");
            //printArray(data);
        }
        else
        {
            System.out.println("Error: Data is not sorted in non-decreasing order!");
            printArray(data);

        }
    }

    /**
     * Prints the array
     * @param data the input array
     */
    private static void printArray(int[] data) {
        for (int i = 0; i < data.length; i++) {
            System.out.print(data[i] + " ");
        }
        System.out.println();
    }


    /**
     * code to test the sorting algorithms
     */
    public static void main (String[] argv) {
        testCorrectness();
        testTiming();
    }
}
