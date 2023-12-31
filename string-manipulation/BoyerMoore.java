import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/** implementation of Boyer Moore algorithms
 * reference:https://go.dev/src/strings/search.go
 * reference:https://github.com/hit9/code-snippets/blob/master/string/string-search-boyer-moore/main.c
 * reference:https://writings.sh/post/algorithm-string-searching-boyer-moore#好后缀的办法-1;
*/

public class BoyerMoore{
    //final static int ALPHABET_SIZE = 256;

/** Bad Character skip rule
 * 
 * compare Pattern P and Text T from right to left when mismatch happens, for mismatched character c in Test:
 * 1.if P does not contain c, skip the whole length of pattern.
 * badCharSkip[b] is len(pattern).
 * 2.if P contains c, shift P to align the right-most occurrence of c in P with c.
 * badCharSkip[b] is the distance between the last byte of pattern
 * and the rightmost occurrence of b in pattern. 
 *
 */

/**
 *         ABABDAAAACAAAABCABAB
 *         AAACAAAA
 *
 *         */

public static int strStr(String text, String pattern){
    char[] textArray = text.toCharArray();
    char[] patternArray = pattern.toCharArray();

    /** preprocess to get the bad character table and the good suffix table*/
    int[] badCharSkipTable = makeBadCharSkipTable(patternArray);
    int[] goodSuffixSkipTable = makeGoodSuffixSkipTable(patternArray);

    /** compare text and pattern from right to left
     * textIndex start from pattern.length()-1,increment by max value of bad character rule and good suffix rule
     * patternIndex start from pattern.length()-1,return to pattern.length -1 if mismatch happens
     * */
    int textPointer = pattern.length() - 1;
    int patternPointer = pattern.length() - 1;
    /** loop through the whole text string*/
    while(textPointer < text.length()){
        /** compare from right to left*/
        while(patternPointer >= 0 && textArray[textPointer] == patternArray[patternPointer]){
            textPointer--;
            patternPointer--;
        }
        /** found the index*/
        if(patternPointer < 0){
            return textPointer + 1;
        }else {
            /** not found, update the textPointer and patternPointer*/
            int skip = Math.max(badCharSkipTable[textArray[textPointer]], goodSuffixSkipTable[patternPointer]);
            textPointer += skip;
            patternPointer = pattern.length() - 1;
        }
    }
    return -1;
 }


 /**bad character rule:
  * 1.if not in pattern string, skip pattern.length(move the entirety of pattern to past the mismatch point)
  * 2.if in pattern string,skip = lastindex - index of rightmost mismatch character
  * 3.overlook the last character in pattern string
  */
    private static int[] makeBadCharSkipTable(char[] pattern) {
        int ALPHABET_SIZE = 256;
        int[] badCharSkipTable = new int[ALPHABET_SIZE];
        int lastIndex = pattern.length - 1 ;
        for (int i = 0; i < ALPHABET_SIZE; i++) {
    /** set up all the value to length; */
            badCharSkipTable[i] = pattern.length;
        }
    /** The loop condition is < instead of <= to make sure we don't align in the wrong direction
     * */
        for (int i = 0; i < lastIndex; i++) {
    /** calculate the ASCII number for badCharSkip[i] */
            int ascii = (int)pattern[i];
    /**update the value of the characters in pattern */
            badCharSkipTable[ascii] = lastIndex - i;
        }
        return badCharSkipTable;
    }

    /** eg:
     * Pattern              ABCDABEA
     * index i              0123456
     * unique char           A   B   C   D   E          ?            (? means all the rest char)
     * array index          65  66  67  68  69   0-64 && 70-255
     * set to length        8    8   8   8   8        8
     * last index = 7
     * update(lastindex-i)  3    2   5   4   1        8
     */

    /** alternative:use hashmap to get badCharSkip numbers*/
private static HashMap makeBadCharMap(char[] pattern){
    HashMap<Character, Integer> map = new HashMap();
    for(int i = 0; i < pattern.length - 1; i++)
        map.put(pattern[i],(int)(pattern.length - 1 - i));
    return map;
}

/** Good Suffix skip table
 * case 1. The matched suffix occurs elsewhere in pattern (with a different
 * byte preceding it that might match). In this case, we can
 * shift the matching frame to align with the next suffix chunk.
 * Text:        XISMISSISSI
 * Pattern:     MISSISSI
 * shift:          MISSISSI
 * For
 *example, the pattern "mississi" has the suffix "issi" next occurring
 * (in right-to-left order) at index 1,the longest common prefix length is 5,
 * the common suffix length is 4 so goodSuffixSkipTable[3] =
 * pattern.length + len(suffix) - len(prefix)= 8+4-5 = 7.
 * 
 * case 2. If the matched suffix does not occur elsewhere in pattern, then the
 * matching frame may share part of its prefix with the end of the
 * matching suffix. In this case, goodSuffixSkipTable[i] will
 * contain how far to shift the frame to align this portion of the prefix to the
 * suffix. For example, in the pattern "ABCEFCABC",if mismatch happened at index 3
 * Text:        XXXXFCABCEFABC
 * pattern:     ABCEFCABC
 * Shift:             ABCEFCABC
 * First three character in pattern are same as last three.
 * The length of prefix is 3.The length of suffix is
 * pattern.length - 1- mismatchIndex = 9-1-3=5
 * goodSuffixSkipTable[3] == pattern.length + len(suffix) - len(prefix)= 9 + 5 -3 == 11.
 * 
 * cast 3. no matched suffix occurs; no prefix equal to suffix.then shift P by m (length of P) places to the right.
 * it is a special case of case 2 when the prefix length = 0
 */

private static int[] makeGoodSuffixSkipTable(char[] pattern){
    int[] goodSuffixSkipTable = new int[pattern.length];
    makeGoodSuffixSkipTableCase2And3(pattern,goodSuffixSkipTable);
    makeGoodSuffixSkipTableCase1(pattern,goodSuffixSkipTable);
    return goodSuffixSkipTable;
}

 /** goodSuffixSkipTable[i] means the increment value for index in Text string*/
 private static void makeGoodSuffixSkipTableCase2And3(char[] pattern, int[] goodSuffixSkipTable){
/** case 3
 * goodSuffixSkipTable[k] = pattern.length + suffix_length;
 * case 3 is a special case in case 2 when prefix_length == 0
*/

/** case 2
 * goodSuffixSkipTable[k] = pattern.length + suffixLength - prefixLength;
 */

    int lastIndex = pattern.length - 1;
/**mismatch in last index means no suffix,
 * good suffix rule doesn't have meaning, set value to 1*/
    goodSuffixSkipTable[lastIndex] = 1;
    /** count the prefix */
    int[] prefixLengthTable = countPrefixLength(pattern);
    for(int i = lastIndex - 1; i >= 0; i--){
        int suffixLength = lastIndex - i;
        goodSuffixSkipTable[i] = pattern.length + suffixLength - prefixLengthTable[i];
    }
}

/** case 1
 * overwrite for conditions if there is case 1 
 * goodSuffixSkipTable[k] = pattern.length + suffixLength - prefixLength;
 * suffixLength means shift back to last index
 * pattern.length - prefixLength means extra shift
 */
private static void makeGoodSuffixSkipTableCase1(char[] pattern,int[] goodSuffixSkipTable){
    /** start from last index to get the longest suffix in case following case happens:
     pattern: BCDACDECD
     *        BCDACDEC
     *        BCDACDE
     *        DCBACD  longest common suffix length is 2
     *        BCDAC
     *        BCDA
     *        BCD  longest common suffix length is 2, happened before,overlook
     *        BC
     * */

    /** use a set to store all the longestCommonSuffixLength to overlook the shorter prefix length */
    Set<Integer> longestLengthSet = new HashSet<>();
    int lastIndex = pattern.length-1;
    /** overlook the last index because there is no suffix
     * overlook the first index because meaningful prefix length starts from 2*/
    for(int i = lastIndex - 1; i >= 1 ; i--){
        int prefixLength = i + 1;
        /** make a copy array to compare it with the pattern array to get the longest common suffix
         * pattern:    A  D  A  C  D  B  C  D  B  C  D
         * length: 11
         * index       0  1  2  3  4  5  6  7  8  9
         * start from index 9, prefix length = 10
         * copyPrefix:A  D  A  C  D  B  C  D  B  C
         * compute the longest common suffix of pattern and copyPrefix
         * */
        char[] prefix = new char[prefixLength];
        System.arraycopy(pattern,0, prefix, 0, prefixLength);
        int longestCommonSuffixLength = countLongestCommonSuffixLength(pattern,prefix);
        /** if the longest common suffix length is in the set, skip*/
        if(longestCommonSuffixLength > 0 && !longestLengthSet.contains(longestCommonSuffixLength)){
            /** if longestCommonSuffixLength is not in set,
             * add into the set first
             * then compute the mismatched index and update the goodSuffixSkipTable*/
            longestLengthSet.add(longestCommonSuffixLength);
            /** eg:
             * pattern:    A  D  A  C  D  B  C  D  B  C  D
             * lastIndex:10
             * index       0  1  2  3  4  5  6  7  8  9  10
             * compare:    A  D  A  C  D  B  C  D  B  C  D
             * with   :    A  D  A  C  D
             * longestCommonSuffixLength = 2
             *  mismatched index = lastIndex - longestCommonSuffixLength=10-2=8
             *
             * */
            int mismatchedIndex = lastIndex - longestCommonSuffixLength;
            int suffixLength = longestCommonSuffixLength;
            goodSuffixSkipTable[mismatchedIndex] = pattern.length + suffixLength - prefixLength ;
        }
    }
}
/**
 * eg:                              A  D  A  C  D  B  C  D  B  C  D
 * index i                          0  1  2  3  4  5  6  7  8  9  10
 * longestCommonSuffixLength           1        2        5
 * mismatch index                               4        7     9
 * 
 * longestCommonSuffixLength = 1 means longest common string for the following two string is 1
 *          AD
 * ADACDBCDBCD   
 * 
 * LongestCommonSuffixLength is 2:
 *       ADACD                     
 * ADACDBCDBCD    
 * 
 * LongestCommonSuffixLength is 5:
 *    ADACDBCD
 * ADACDBCDBCD  
 *                    
*/
private static int countLongestCommonSuffixLength(char[] pattern, char[] prefix){
    /**
     *    ADACD
     * ACACDACD
     * */
    int i;
    for(i = 0; i < pattern.length && i < prefix.length;i++){
        if(pattern[pattern.length - 1- i] != prefix[prefix.length - 1- i]){
            break;
        }
    }
    return i;
}

/**prefixLengthTable[i] means the length of same prefix and suffix at mismatched index i
*eg pattern            DDDBDDD
*prefixLengthTable[i]  3333217
*index                 0123456
 * if mismatch happend at index 4
 * compare      DD
 * with         DDDBDDD
 * to get same prefix length = 2
 */
private static int[] countPrefixLength(char[] pattern){
    int lastIndex = pattern.length - 1;
    int lastPrefixLength = 0;
    int[] prefixLengthTable = new int[pattern.length];
    prefixLengthTable[lastIndex] = 0;
    /** first meaningful mismatched index should be lastindex - 1;*/
    for(int i = lastIndex - 1; i >= 0; i--){
        int prefixLength = 0;
        int suffixLength = lastIndex - i;
        /** make a new suffix char[] */
        char[] suffix = new char[suffixLength];
        System.arraycopy(pattern,i + 1, suffix, 0, suffixLength);
        /** compare pattern array with suffix array*/
        for(int j = 0; j < suffixLength ;j++){
            if(pattern[j] == suffix[j]){
                prefixLength++;  
            }else{
                prefixLength = lastPrefixLength;
                break;
            }
        }
        lastPrefixLength = prefixLength;
        prefixLengthTable[i] = lastPrefixLength;
    }
    return prefixLengthTable;
}

    public static void main(String[] args) {
        String text1 = new String("ABCDABEABDCBCDDBBCDBACD");
        String pattern1 = new String("BCDBACD");
        System.out.println(16==strStr(text1,pattern1));

        String pattern2 = new String("BCDDBB");
        String pattern3 = new String("EABDCB");
        String pattern4 = new String("BBCDBD");
        String pattern5 = new String("DDBBCD");
        System.out.println(11==strStr(text1,pattern2));
        System.out.println(6==strStr(text1,pattern3));
        System.out.println(-1==strStr(text1,pattern4));
        System.out.println(13==strStr(text1,pattern5));

        String text2 = new String("BBBBBBBBBBBBBBBBBBBBBABBBBB");
        String pattern6 = new String("ABBBBB");
        System.out.println(21==strStr(text2,pattern6));

        String text3 = new String("ABABDAAAACAAAABCABAB");
        String pattern7 = new String("AAACAAAA");
        System.out.println(6==strStr(text3,pattern7));

        String text4 = new String("BBC ABCDAB ABCDABCDABDE");
        String pattern8 = new String("ABCDABD");
        System.out.println(15==strStr(text4,pattern8));

        String text5 = new String("ABAAACAAAAAACAAAABCABAAAACAAAAFDLAAACAAAAAACAAAA");
        String pattern9 = new String("AAACAAAA");
        System.out.println(2==strStr(text5,pattern9));

        String text6 = new String("GCATCGCAGAGAGTATACAGTACG");
        String pattern10 = new String("GCAGAGAG");
        System.out.println(5==strStr(text6,pattern10));

    }
}
