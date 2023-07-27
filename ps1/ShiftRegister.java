///////////////////////////////////
// This is the main shift register class.
// Notice that it implements the ILFShiftRegister interface.
// You will need to fill in the functionality.
///////////////////////////////////

/**
 * class ShiftRegister
 * @author
 * Description: implements the ILFShiftRegister interface.
 */
public class ShiftRegister implements ILFShiftRegister {
    ///////////////////////////////////
    // Create your class variables here
    ///////////////////////////////////
    // TODO:
    int inputSize;
    int inputTap;
    int[] registerArray;


    ///////////////////////////////////
    // Create your constructor here:
    ///////////////////////////////////
    ShiftRegister(int size, int tap) {
        // TODO:
        inputSize = size;
        registerArray = new int[inputSize];
        if (tap < 0 || tap >= size) {
            System.out.println("Error: tap is invalid");
        } else {
            inputTap = tap;
        }
    }

    ///////////////////////////////////
    // Create your class methods here:
    ///////////////////////////////////
    /**
     * setSeed
     * @param seed
     * Description:
     */
    @Override
    public void setSeed(int[] seed) {
        // TODO:
        // Check if seed is valid
        if (seed.length != inputSize) {
            System.out.println("Error: seed does not match size.");
        }
        for (int i = 0; i < inputSize; i++) {
            if (seed[i] == 0 || seed[i] == 1) {
                //reverse the array
                registerArray[i] = seed[inputSize-1-i];
            } else {
                System.out.println("Error: seed contains invalid values.");
            }
        }
    }

    /**
     * shift
     * @return
     * Description:
     */
    @Override
    public int shift() {
        // TODO:
        int feedbackBit = registerArray[0] ^ registerArray[inputSize-1-inputTap];
        int[] tempArray = new int[inputSize];
        for (int i = 0; i < inputSize-1; i++) {
            tempArray[i] = registerArray[i+1];
        }
        tempArray[inputSize-1] = feedbackBit;
        registerArray = tempArray;
        return feedbackBit;
    }

    /**
     * generate
     * @param k
     * @return
     * Description:
     */
    @Override
    public int generate(int k) {
        // TODO:
        int[] extractedBits = new int[k];
        for (int i = 0; i < k; i++) {
            extractedBits[k-1-i] = shift();
        }
        return toDecimal(extractedBits);
    }

    /**
     * Returns the integer representation for a binary int array.
     * @param array
     * @return
     */
    private int toDecimal(int[] array) {
        // TODO:
        int result = 0;
        int pow = 2;
        for (int i = 0; i < array.length; i++) {
            int exp = 1;
            for (int j = i; j > 0; j--) {
                exp *= pow;
            }
            result += (exp * array[i]);
        }
        return result;
    }
}

