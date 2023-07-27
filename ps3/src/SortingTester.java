import java.util.Random;

public class SortingTester {
    private static Random rng = new Random(1);
    public static boolean checkSort(ISort sorter, int size) {
        // TODO: implement this
        KeyValuePair[] arrayToSort = new KeyValuePair[size];
        for (int i = 0; i < size; i++) {
            arrayToSort[i] = new KeyValuePair(rng.nextInt(), 0);
        }

        // sort the array
        sorter.sort(arrayToSort);

        // check if array is sorted
        return isSorted(arrayToSort);
    }

    public static boolean isStable(ISort sorter, int size) {
        // TODO: implement this
        boolean isStable = true;
        KeyValuePair[] arrayToSort = new KeyValuePair[size];
        // create an array where each key appears twice with different values.
        for (int i = 0; i < size; i += 2) {
            int currentRandomNumber = rng.nextInt();
            if (size % 2 != 0 && i == size-1) {
                arrayToSort[i] = new KeyValuePair(currentRandomNumber, 1);
            } else {
                arrayToSort[i] = new KeyValuePair(currentRandomNumber, 1);
                arrayToSort[i + 1] = new KeyValuePair(currentRandomNumber, 2);
            }
        }

        // sort the array
        sorter.sort(arrayToSort);

        // check if stable
        for (int j = 0; j < size-1; j++) {
            if (arrayToSort[j].getKey() == arrayToSort[j+1].getKey()) {
                if (arrayToSort[j].getValue() > arrayToSort[j+1].getValue()) {
                    isStable = false;
                }
            }
        }

        return isStable;
    }

    public static boolean isSorted(KeyValuePair[] KVParray) {
        boolean result = true;
        for (int i = 0; i < KVParray.length-1; i++) {
            if (KVParray[i].getKey() > KVParray[i+1].getKey()) {
                result = false;
                break;
            }
        }
        return result;
    }

    /**
     * Method to identify Dr. Evil by presenting a simple array.
     *
     * @param sorter
     * @return boolean representation of whether the array is sorted.
     */
    public static String drEvilFinder(ISort sorter) {
        KeyValuePair[] testArray = {new KeyValuePair(1,0), new KeyValuePair(0,0)};
        sorter.sort(testArray);
        if (isSorted(testArray)) {
            return "This sorter is not Dr. Evil.";
        } else {
            return "This sorter is Dr. Evil.";
        }
    }

    /**
     * Method to construct a strictly decreasing array
     * @param size
     * @return a strictly decreasing array
     */
    public static KeyValuePair[] decreasingArrayMaker(int size) {
        KeyValuePair[] decreasingArray = new KeyValuePair[size];
        for (int i = 0; i < size; i++) {
            decreasingArray[i] = new KeyValuePair(size-1-i, 1);
        }
        return decreasingArray;
    }

    /**
     *  Method to create an array with all same keys
     * @param size
     * @return an array with all same keys
     */
    public static KeyValuePair[] allSameArrayMaker(int size) {
        KeyValuePair[] allSameArray = new KeyValuePair[size];
        for (int i = 0; i < size; i++) {
            allSameArray[i] = new KeyValuePair(1, 1);
        }
        return allSameArray;
    }

    /**
     * Method to create a sorted array
     * @param size
     * @return a sorted array
     */
    public static KeyValuePair[] sortedArrayMaker (int size) {
        KeyValuePair[] sortedArray = new KeyValuePair[size];
        for (int i = 0; i < size; i++) {
            sortedArray[i] = new KeyValuePair(i, 1);
        }
        return sortedArray;
    }

    /**
     * Method to create a random array
     * @param size
     * @return a random array
     */
    public static KeyValuePair[] randomArrayMaker(int size) {
        KeyValuePair[] randomArray = new KeyValuePair[size];
        for (int i = 0; i < size; i++) {
            randomArray[i] = new KeyValuePair(rng.nextInt(size), 1);
        }
        return randomArray;
    }

    /**
     * Method to create a nearly sorted array
     * @param size
     * @return a nearly sorted array
     */
    public static KeyValuePair[] nearlySortedArrayMaker(int size) {
        KeyValuePair[] nearlySortedArray = new KeyValuePair[size];
        for (int i = 0; i < size-1; i++) {
            nearlySortedArray[i] = new KeyValuePair(i+1, 1);
        }
        nearlySortedArray[size-1] = new KeyValuePair(0,1);
        return nearlySortedArray;
    }


    public static void main(String[] args) {
        // TODO: implement this
        ISort sorterATest = new SorterA();
        ISort sorterBTest = new SorterB();
        ISort sorterCTest = new SorterC();
        ISort sorterDTest = new SorterD();
        ISort sorterETest = new SorterE();
        ISort sorterFTest = new SorterF();

        // Check that each sorter sorts correctly
        System.out.println("A sorts correctly: " + checkSort(sorterATest, 100));
        System.out.println("B sorts correctly: " + checkSort(sorterBTest, 100));
        System.out.println("C sorts correctly: " + checkSort(sorterCTest, 100));
        System.out.println("D sorts correctly: " + checkSort(sorterDTest, 100));
        System.out.println("E sorts correctly: " + checkSort(sorterETest, 100));
        System.out.println("F sorts correctly: " + checkSort(sorterFTest, 100));
        System.out.println("");

        // Check stability of each sorter
        System.out.println("A is stable: " + isStable(sorterATest, 100));
        System.out.println("B is stable: " + isStable(sorterBTest, 100));
        System.out.println("C is stable: " + isStable(sorterCTest, 100));
        System.out.println("D is stable: " + isStable(sorterDTest, 100));
        System.out.println("E is stable: " + isStable(sorterETest, 100));
        System.out.println("F is stable: " + isStable(sorterFTest, 100));
        System.out.println("");

        // To identify Dr. Evil
        System.out.println("A: " + drEvilFinder(sorterATest));
        System.out.println("B: " + drEvilFinder(sorterBTest));
        System.out.println("C: " + drEvilFinder(sorterCTest));
        System.out.println("D: " + drEvilFinder(sorterDTest));
        System.out.println("E: " + drEvilFinder(sorterETest));
        System.out.println("F: " + drEvilFinder(sorterFTest));
        System.out.println("");

        // Tests on Sorter A
        System.out.println("TESTS ON SORTER A:");
        System.out.println("big random array vs small random array:");
        System.out.println((double) sorterATest.sort(randomArrayMaker(10000))/(double) sorterATest.sort(randomArrayMaker(100)));
        System.out.println("sorted array vs random array");
        System.out.println((double) sorterATest.sort(sortedArrayMaker(10000))/(double) sorterATest.sort(randomArrayMaker(10000)));
        System.out.println("decreasing array vs random array");
        System.out.println((double) sorterATest.sort(decreasingArrayMaker(10000))/(double) sorterATest.sort(randomArrayMaker(10000)));
        System.out.println("all same array vs random array");
        System.out.println((double) sorterATest.sort(allSameArrayMaker(10000))/(double) sorterATest.sort(randomArrayMaker(10000)));
        System.out.println("");

        // Tests on Sorter C
        System.out.println("TESTS ON SORTER C:");
        System.out.println("big random array vs small random array:");
        System.out.println((double) sorterCTest.sort(randomArrayMaker(10000))/(double) sorterCTest.sort(randomArrayMaker(100)));
        System.out.println("sorted array vs random array");
        System.out.println((double) sorterCTest.sort(sortedArrayMaker(10000))/(double) sorterCTest.sort(randomArrayMaker(10000)));
        System.out.println("decreasing array vs random array");
        System.out.println((double) sorterCTest.sort(decreasingArrayMaker(10000))/(double) sorterCTest.sort(randomArrayMaker(10000)));
        System.out.println("all same array vs random array");
        System.out.println((double) sorterCTest.sort(allSameArrayMaker(10000))/(double) sorterCTest.sort(randomArrayMaker(10000)));
        System.out.println("");

        // Tests on Sorter D
        System.out.println("TESTS ON SORTER D:");
        System.out.println("big random array vs small random array:");
        System.out.println((double) sorterDTest.sort(randomArrayMaker(10000))/(double) sorterDTest.sort(randomArrayMaker(100)));
        System.out.println("sorted array vs random array");
        System.out.println((double) sorterDTest.sort(sortedArrayMaker(10000))/(double) sorterDTest.sort(randomArrayMaker(10000)));
        System.out.println("decreasing array vs random array");
        System.out.println((double) sorterDTest.sort(decreasingArrayMaker(10000))/(double) sorterDTest.sort(randomArrayMaker(10000)));
        System.out.println("all same array vs random array");
        System.out.println((double) sorterDTest.sort(allSameArrayMaker(10000))/(double) sorterDTest.sort(randomArrayMaker(10000)));
        System.out.println("");

        // Tests on Sorter E
        System.out.println("TESTS ON SORTER E:");
        System.out.println("big random array vs small random array:");
        System.out.println((double) sorterETest.sort(randomArrayMaker(10000))/(double) sorterETest.sort(randomArrayMaker(100)));
        System.out.println("sorted array vs random array");
        System.out.println((double) sorterETest.sort(sortedArrayMaker(10000))/(double) sorterETest.sort(randomArrayMaker(10000)));
        System.out.println("decreasing array vs random array");
        System.out.println((double) sorterETest.sort(decreasingArrayMaker(10000))/(double) sorterETest.sort(randomArrayMaker(10000)));
        System.out.println("all same array vs random array");
        System.out.println((double) sorterETest.sort(allSameArrayMaker(10000))/(double) sorterETest.sort(randomArrayMaker(10000)));
        System.out.println("");

        // Tests on Sorter F
        System.out.println("TESTS ON SORTER F:");
        System.out.println("big random array vs small random array:");
        System.out.println((double) sorterFTest.sort(randomArrayMaker(10000))/(double) sorterFTest.sort(randomArrayMaker(100)));
        System.out.println("sorted array vs random array");
        System.out.println((double) sorterFTest.sort(sortedArrayMaker(10000))/(double) sorterFTest.sort(randomArrayMaker(10000)));
        System.out.println("decreasing array vs random array");
        System.out.println((double) sorterFTest.sort(decreasingArrayMaker(10000))/(double) sorterFTest.sort(randomArrayMaker(10000)));
        System.out.println("all same array vs random array");
        System.out.println((double) sorterFTest.sort(allSameArrayMaker(10000))/(double) sorterFTest.sort(randomArrayMaker(10000)));
        System.out.println("");

    }
}

// A: MergeSort
// B: Dr. Evil
// C: BubbleSort
// D: SelectionSort
// E: QuickSort (sorted and decreasing array take longer)
// F: InsertionSort