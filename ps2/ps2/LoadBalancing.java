/**
 * Contains static routines for solving the problem of balancing m jobs on p processors
 * with the constraint that each processor can only perform consecutive jobs.
 */
public class LoadBalancing {

    /**
     * Checks if it is possible to assign the specified jobs to the specified number of processors such that no
     * processor's load is higher than the specified query load.
     *
     * @param jobSizes the sizes of the jobs to be performed
     * @param queryLoad the maximum load allowed for any processor
     * @param p the number of processors
     * @return true iff it is possible to assign the jobs to p processors so that no processor has more than queryLoad load.
     */
    public static boolean isFeasibleLoad(int[] jobSizes, int queryLoad, int p) {
        // TODO: Implement this
        int numJobs = jobSizes.length - 1;
        if (jobSizes == null || jobSizes.length == 0) {
            return false;
        } else {
            int processorLoad = 0;
            int processorsUsed = 1;
            int jobIndex = 0;
            while (jobIndex <= numJobs) {
                if (jobSizes[jobIndex] > queryLoad) {
                    return false;
                } else if (processorLoad + jobSizes[jobIndex] <= queryLoad) {
                    processorLoad += jobSizes[jobIndex];
                    jobIndex++;
                } else {
                    processorsUsed++;
                    processorLoad = 0;
                }
            }
            return processorsUsed <= p;
        }
    }

    /**
     * Returns the minimum achievable load given the specified jobs and number of processors.
     *
     * @param jobSizes the sizes of the jobs to be performed
     * @param p the number of processors
     * @return the maximum load for a job assignment that minimizes the maximum load
     */
    public static int findLoad(int[] jobSizes, int p) {
        // TODO: Implement this

        // Conduct binary search on the queryLoad parameter
        // foo is an arbitrary param to find bounds for binary search
        int foo = 0;

        for (int i = 0; i < jobSizes.length; i++) {
            foo += jobSizes[i];
        }

        int begin = 0;
        int end = foo;

        if (jobSizes == null || jobSizes.length == 0 || p <= 0) {
            return -1;

        } else {
            while (begin < end) {
                int mid = begin + (end-begin)/2;
                if (isFeasibleLoad(jobSizes, mid, p) && !isFeasibleLoad(jobSizes, mid-1, p)) {
                    // minimum load where load will not be feasible if queryLoad is lower
                    return mid;
                } else if (isFeasibleLoad(jobSizes, mid-1, p)) {
                    // queryLoad too high, can afford to lower
                    end = mid;
                } else {
                    // queryLoad too low, go higher
                    begin = mid + 1;
                }
            }
            return begin;
        }
    }

    // These are some arbitrary testcases.
    public static int[][] testCases = {
            {1, 3, 5, 7, 9, 11, 10, 8, 6, 4},
            {67, 65, 43, 42, 23, 17, 9, 100},
            {4, 100, 80, 15, 20, 25, 30},
            {2, 3, 4, 5, 6, 7, 8, 100, 99, 98, 97, 96, 95, 94, 93, 92, 91, 90, 89, 88, 87, 86, 85, 84, 83},
            {7}
    };

    /**
     * Some simple tests for the findLoad routine.
     */
    public static void main(String[] args) {
        for (int p = 1; p < 30; p++) {
            System.out.println("Processors: " + p);
            for (int[] testCase : testCases) {
                System.out.println(findLoad(testCase, p));
            }
        }
    }
}
