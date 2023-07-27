/**
 * The Optimization class contains a static routine to find the maximum in an array that changes direction at most once.
 */
public class Optimization {

    /**
     * A set of test cases.
     */
    static int[][] testCases = {
            {1, 3, 5, 7, 9, 11, 10, 8, 6, 4},
            {67, 65, 43, 42, 23, 17, 9, 100},
            {4, -100, -80, 15, 20, 25, 30},
            {2, 3, 4, 5, 6, 7, 8, 100, 99, 98, 97, 96, 95, 94, 93, 92, 91, 90, 89, 88, 87, 86, 85, 84, 83},
            {}, null
    };

    /**
     * Returns the maximum item in the specified array of integers which changes direction at most once.
     *
     * @param dataArray an array of integers which changes direction at most once.
     * @return the maximum item in data Array
     */
    public static int searchMax(int[] dataArray) {
        // TODO: Implement this
        int result = 0;

        if (dataArray == null || dataArray.length <= 0) {
            // invalid array
            result = 0;

        } else if (dataArray.length == 1) {
            result = dataArray[0];

        } else if (dataArray.length == 2) {
            result = dataArray[0] > dataArray[1] ? dataArray[0] : dataArray[1];

        } else {
            if (dataArray[0] < dataArray[1]) {
                // array increases then decreases
                // use binary search to find peak
                int begin = 0;
                int end = dataArray.length-1;
                while (begin <= end) {
                    int mid = begin + (end-begin)/2;
                    if (mid == dataArray.length-1 && dataArray[mid] > dataArray[mid-1]) {
                        //array strictly increases and does not change direction
                        result = dataArray[mid];
                        break;
                    } else if (dataArray[mid] > dataArray[mid+1] && dataArray[mid-1] < dataArray[mid]) {
                        //found the peak
                        result = dataArray[mid];
                        break;
                    } else if (dataArray[mid-1] > dataArray[mid]) {
                        //recurse on left half
                        end = mid;
                    } else if (dataArray[mid+1] > dataArray[mid]) {
                        //recurse on right half
                        begin = mid + 1;
                    }
                }

            } else {
                // array decreases then increases
                if (dataArray[0] < dataArray[dataArray.length - 1]) {
                    //start < end
                    result = dataArray[dataArray.length - 1];
                } else {
                    //start > end
                    result = dataArray[0];
                }
            }
        }
        return result;
    }

    /**
     * A routine to test the searchMax routine.
     */
    public static void main(String[] args) {
        for (int[] testCase : testCases) {
            System.out.println(searchMax(testCase));
        }
    }
}
