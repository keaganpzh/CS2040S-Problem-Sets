import java.util.HashMap;
import java.util.Random;

/**
 * This is the main class for your Markov Model.
 *
 * Assume that the text will contain ASCII characters in the range [1,255].
 * ASCII character 0 (the NULL character) will be treated as a non-character.
 *
 * Any such NULL characters in the original text should be ignored.
 */
public class MarkovModel {

	// Use this to generate random numbers as needed
	private Random generator = new Random();

	// This is a special symbol to indicate no character
	public static final char NOCHARACTER = (char) 0;
	private int order = 0;
	private HashMap<String, HashMap<Integer, Integer>> hMapMain = new HashMap<>();
	private HashMap<String, Integer> freqMap = new HashMap<>();


	/**
	 * Constructor for MarkovModel class.
	 *
	 * @param order the number of characters to identify for the Markov Model sequence
	 * @param seed the seed used by the random number generator
	 */
	public MarkovModel(int order, long seed) {
		// Initialize your class here
		this.order = order;
		// Initialize the random number generator
		generator.setSeed(seed);
	}

	/**
	 * Builds the Markov Model based on the specified text string.
	 */
	public void initializeText(String text) {
		// Build the Markov model here
		int start = 0;
		int end = order;
		while (end < text.length()) {
			String prefix = text.substring(start, end);
			int nextCharAscii = text.charAt(end);
			if (freqMap.containsKey(prefix)) {
				int freq = freqMap.get(prefix);
				freq++;
				freqMap.put(prefix, freq);
			} else {
				freqMap.put(prefix, 1);
			}
			// prefix not in hashmap, so initialise inner hashmap.
			if (!hMapMain.containsKey(prefix)) {
				HashMap<Integer, Integer> hMapInner = new HashMap<>();
				hMapInner.put(nextCharAscii, 0);
				hMapMain.put(prefix, hMapInner);
			}
			// next char at k+1 not inside inner hashmap.
			if (!hMapMain.get(prefix).containsKey(nextCharAscii)) {
				hMapMain.get(prefix).put(nextCharAscii, 0);
			}
			// next char is already in inner hashmap. +1 occurrence.
			if (hMapMain.get(prefix).containsKey(nextCharAscii)) {
				int currCount = hMapMain.get(prefix).get(nextCharAscii);
				currCount++;
				hMapMain.get(prefix).put(nextCharAscii, currCount);
			}
			start++;
			end++;
		}
	}

	/**
	 * Returns the number of times the specified kgram appeared in the text.
	 */
	public int getFrequency(String kgram) {
		if (kgram.length() != order) {
			return 0;
		}
		return freqMap.get(kgram);
	}


	/**
	 * Returns the number of times the character c appears immediately after the specified kgram.
	 */
	public int getFrequency(String kgram, char c) {
		if (kgram.length() != order || !hMapMain.get(kgram).containsKey((int) c)) {
			return 0;
		}
		return hMapMain.get(kgram).get((int) c);
	}

	/**
	 * Generates the next character from the Markov Model.
	 * Return NOCHARACTER if the kgram is not in the table, or if there is no
	 * valid character following the kgram.
	 */
	public char nextCharacter(String kgram) {
		// See the problem set description for details
		// on how to make the random selection.
		if (kgram.length() != order || !hMapMain.containsKey(kgram) || hMapMain.get(kgram).isEmpty()) {
			return NOCHARACTER;
		}
		int numChoices = getFrequency(kgram);
		int[] allChoices = new int[numChoices];
		int index = 0;
		HashMap<Integer, Integer> hMapInner = hMapMain.get(kgram);
		for (int i = 0; i <= 256; i++) {
			if (hMapInner.containsKey(i)) {
				for (int j = 0; j < hMapInner.get(i); j++) {
					allChoices[index] = i;
					index++;
				}
			}
		}
		int randomInt = generator.nextInt(numChoices);
		return (char) allChoices[randomInt];
	}
}
