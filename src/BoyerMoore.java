/**
 * The BoyerMoore class has the search method
 * which can search a pattern in a text and
 * returns true if the pattern occurs in the
 * text given.
 */
class BoyerMoore {
    static boolean search(char[] text, char[] pattern) {
        int txtLen = text.length;
        int ptrLen = pattern.length;

        int shiftVal = 0;

        while (shiftVal <= (txtLen - ptrLen)) {
            int compIndex = ptrLen - 1;

            while (compIndex >= 0 && pattern[compIndex] == text[shiftVal + compIndex]) {
                compIndex--;
            }

            if (compIndex < 0) {
                return true;
            } else {
                shiftVal++;
            }
        }
        return false;
    }
}
