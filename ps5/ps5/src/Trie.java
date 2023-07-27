import java.util.ArrayList;

public class Trie {

    // Wildcards
    final char WILDCARD = '.';
    TrieNode root;

    private class TrieNode {
        // TODO: Create your TrieNode class here.

        /**
         * Array holds trie nodes represented as such:
         * index 0-9 -> 0-9
         * index 10-36 -> A-Z
         * index 37-62 -> a-z
         */
        TrieNode[] keys;
        boolean isEndFlag = false;

        public TrieNode() {
            this.keys = new TrieNode[62];
        }

        public TrieNode findNode(int index) {
            return this.keys[index];
        }

        public void insertNode(int index, TrieNode node) {
            if (this.keys[index] == null) {
                this.keys[index] = node;
            }
        }

        public void markEnd() {
            this.isEndFlag = true;
        }

    }

    public Trie() {
        // TODO: Initialise a trie class here.
        this.root = new TrieNode();
    }

    /**
     * Converts a char's ASCII value to an array index value.
     * @param asciiValue ASCII value of a char
     * @return Index of the char in the TrieNode array.
     */
    int asciiValueToArrayIndex(int asciiValue) {
        if (asciiValue < 58) {
            return asciiValue - 48;
        } else if (asciiValue < 91) {
            return asciiValue - 54;
        } else {
            return asciiValue - 60;
        }
    }

    /**
     * Converts an array index of a char to its ASCII value.
     * @param index Index value of the char in the TrieNode array.
     * @return The ASCII value of the char.
     */
    public int arrayIndexToAsciiValue(int index) {
        if (index < 10) {
            return (index + 48);
        } else if (index < 36) {
            return (index + 54);
        } else {
            return (index + 60);
        }
    }

    /**
     * Inserts string s into the Trie.
     *
     * @param s string to insert into the Trie
     */
    void insert(String s) {
        // TODO
        TrieNode currentNode = root;
        for (int i = 0; i < s.length(); i++) {
            int charAsciiValue = (int) s.charAt(i);
            int charIndex = asciiValueToArrayIndex(charAsciiValue);
            if (currentNode.findNode(charIndex) == null) {
                TrieNode newNode = new TrieNode();
                currentNode.insertNode(charIndex, newNode);
            }
            currentNode = currentNode.findNode(charIndex);
        }
        currentNode.markEnd();

    }

    /**
     * Checks whether string s exists inside the Trie or not.
     *
     * @param s string to check for
     * @return whether string s is inside the Trie
     */
    boolean contains(String s) {
        // TODO
        TrieNode currentNode = root;
        for (int i = 0; i < s.length(); i++) {
            int charAsciiValue = (int) s.charAt(i);
            int charIndex = asciiValueToArrayIndex(charAsciiValue);
            currentNode = currentNode.findNode(charIndex);
            if (currentNode == null) return false;
        }
        return currentNode.isEndFlag;
    }


    /**
     * Searches for strings with prefix matching the specified pattern sorted by lexicographical order. This inserts the
     * results into the specified ArrayList. Only returns at most the first limit results.
     *
     * @param s       pattern to match prefixes with
     * @param results array to add the results into
     * @param limit   max number of strings to add into results
     */
    void prefixSearch(String s, ArrayList<String> results, int limit) {
        // TODO
        prefixSearchHelper(s, results, limit, root, 0, "");
    }

    void prefixSearchHelper(String s, ArrayList<String> results, int limit, TrieNode node, int index, String word) {
        if (s.length() == index) {
            if (node.isEndFlag && results.size() <= limit) {
                results.add(word);
            }
            for (int i = 0; i < node.keys.length; i++) {
                if (results.size() >= limit) break;
                TrieNode tempNode = node.keys[i];
                char letter = (char) arrayIndexToAsciiValue(i);
                if (tempNode != null) {
                    prefixSearchHelper(s, results, limit, tempNode, index, word + letter);
                }
            }
        } else {
            char currentChar = s.charAt(index);
            int currentCharAscii = (int) currentChar;
            if (currentChar == WILDCARD) {
                for (int i = 0; i < node.keys.length; i++) {
                    TrieNode tempNode = node.keys[i];
                    char letter = (char) arrayIndexToAsciiValue(i);
                    if (tempNode != null) {
                        prefixSearchHelper(s, results, limit, tempNode, index + 1, word + letter);
                    }
                }
            } else {
                TrieNode currentNode = node.findNode(asciiValueToArrayIndex(currentCharAscii));
                if (currentNode != null) {
                    prefixSearchHelper(s, results, limit, currentNode, index + 1, word + currentChar);
                }
            }
        }
    }


    // Simplifies function call by initializing an empty array to store the results.
    // PLEASE DO NOT CHANGE the implementation for this function as it will be used
    // to run the test cases.
    String[] prefixSearch(String s, int limit) {
        ArrayList<String> results = new ArrayList<>();
        prefixSearch(s, results, limit);
        return results.toArray(new String[0]);
    }


    public static void main(String[] args) {
        Trie t = new Trie();
        t.insert("peter");
        t.insert("piper");
        t.insert("picked");
        t.insert("a");
        t.insert("peck");
        t.insert("of");
        t.insert("pickled");
        t.insert("peppers");
        t.insert("pepppito");
        t.insert("pepi");
        t.insert("pik");

        String[] result1 = t.prefixSearch("p...", 10);
        String[] result2 = t.prefixSearch("pe.", 10);
        // result1 should be:
        // ["peck", "pepi", "peppers", "pepppito", "peter"]
        // result2 should contain the same elements with result1 but may be ordered arbitrarily
        System.out.println(java.util.Arrays.toString(result1));
    }
}
