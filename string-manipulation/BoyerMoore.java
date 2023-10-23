import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/** implementation of Boyer Moore algorithms
 * reference:https://go.dev/src/strings/search.go
 * reference:https://github.com/hit9/code-snippets/blob/master/string/string-search-boyer-moore/main.c
 * reference:https://writings.sh/post/algorithm-string-searching-boyer-moore#好后缀的办法-1;
*/

public class BoyerMoore{
    final static int ALPHABET_SIZE = 256;


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

public static int strStr(String text, String pattern){
    char[] textArray = text.toCharArray();
    char[] patternArray = pattern.toCharArray();

    /** preprocess*/
    int[] badCharSkipTable = makeBadCharSkipTable(patternArray);
    //HashMap<Character, Integer> map = makeBadCharMap(patternArray);
    int[] goodSuffixSkipTable = makeGoodSuffixSkipTable(patternArray);

    /** */
    int tIndex = pattern.length() - 1;
    while(tIndex < text.length()){
        int pIndex = pattern.length() - 1;
        while(pIndex >= 0 && textArray[tIndex] == patternArray[pIndex]){
            tIndex--;
            pIndex--;
        }
        if(pIndex < 0){
            return tIndex + 1;
        }else {
            int skip = Math.max(badCharSkipTable[textArray[tIndex]], goodSuffixSkipTable[pIndex]);
            tIndex += skip;
        }
    }
    return -1;
 }


private static int[] makeBadCharSkipTable(char[] pattern) {
    // 256 Unicode characters
    int[] badCharSkipTable = new int[ALPHABET_SIZE];
    int last = pattern.length - 1 ;
    for (int i = 0; i < ALPHABET_SIZE; i++) {
    // set up all the value to -1;
        badCharSkipTable[i] = pattern.length;
    }
    // The loop condition is < instead of <= so that the last byte does not
    // have a zero distance to itself. Finding this byte out of place implies
    // that it is not in the last position.
    for (int i = 0; i < last; i++) {
    // calculate the ASCII number for badCharSkip[i]
        int ascii = (int)pattern[i];
    //update the value of the characters in pattern 
        badCharSkipTable[ascii] = last - i;//update 
    }
    return badCharSkipTable;
}

/** use hashmap to get badCharSkip numbers*/
private static HashMap makeBadCharMap(char[] pattern){
    HashMap<Character, Integer> map = new HashMap();
    for(int i = 0; i < pattern.length - 1; i++)
        map.put(pattern[i],(int)(pattern.length - 1 - i));
    return map;
}

/** Good Suffix skip table
 * case 1. The matched suffix occurs elsewhere in pattern (with a different
 * byte preceding it that we might possibly match). In this case, we can
 * shift the matching frame to align with the next suffix chunk. For
 * example, the pattern "mississi" has the suffix "issi" next occurring
 * (in right-to-left order) at index 1, so goodSuffixSkip[3] ==
 * shift+len(suffix) == 3+4 == 7.
 * 
 * case 2. If the matched suffix does not occur elsewhere in pattern, then the
 * matching frame may share part of its prefix with the end of the
 * matching suffix. In this case, goodSuffixSkip[i] will contain how far
 * to shift the frame to align this portion of the prefix to the
 * suffix. For example, in the pattern "abcxxxabc", when the first
 * mismatch from the back is found to be in position 3, the matching
 * suffix "xxabc" is not found elsewhere in the pattern. However, its
 * rightmost "abc" (at position 6) is a prefix of the whole pattern, so
 * goodSuffixSkip[3] == shift+len(suffix) == 6+5 == 11.
 * 
 * cast 3. no matched suffix occurs; no prefix equal to suffix.
 */

private static int[] makeGoodSuffixSkipTable(char[] pattern){
    int[] goodSuffixSkipTable = new int[pattern.length];
    makeGoodSuffixSkipTableCase2And3(pattern,goodSuffixSkipTable);
    makeGoodSuffixSkipTableCase1(pattern,goodSuffixSkipTable);
    return goodSuffixSkipTable;
}

 /** goodSuffixSkipTable[i] means the increment value for index in Text string*/
 private static void makeGoodSuffixSkipTableCase2And3(char[] pattern, int[] goodSuffixSkipTable){
/* case 3 
 * goodSuffixSkipTable[k] = pattern.length + suffix_length;
 * case 3 is a special case in case 2 when prefix_length == 0
*/

/* case 2
 * goodSuffixSkipTable[k] = pattern.length + suffixLength - prefixLength;
 */

    int lastIndex = pattern.length - 1;
//mismatch in last index means no suffix, good suffix rule doesn't have meaning, set value to 1
    goodSuffixSkipTable[lastIndex] = 1;
    int[] prefixLengthTable = countPrefixLength(pattern);
    for(int i = lastIndex - 1; i >= 0; i--){
        int suffixLength = lastIndex - i;
        goodSuffixSkipTable[i] = pattern.length + suffixLength - prefixLengthTable[i];
    }
}

/* case 1
 * overwrite for conditions if there is case 1 
 * goodSuffixSkipTable[k] = pattern.length + suffixLength - prefixLength;
 * +suffixLength means shift back to last index 
 * pattern.length - prefixLength means extra shift
 */
private static void makeGoodSuffixSkipTableCase1(char[] pattern,int[] goodSuffixSkipTable){
    /** start from last index to get the longest suffix in case following case happens:
     * BCDACDECD
     *    DCBACD choose longest prefix
     *       BCD  overlook
     * */
    Set<Integer> longestLengthSet = new HashSet<>();
    for(int i = pattern.length-2; i >= 1 ; i--){
        int prefixLength = i + 1;
        char[] prefix = new char[prefixLength];
        System.arraycopy(pattern,0, prefix, 0, prefixLength);
        int longestCommonSuffixLength = countLongestCommonSuffixLength(pattern,prefix);
        if(longestCommonSuffixLength > 0 && !longestLengthSet.contains(longestCommonSuffixLength)){
            longestLengthSet.add(longestCommonSuffixLength);
            int mismatchedIndex = pattern.length - 1 - longestCommonSuffixLength;
            goodSuffixSkipTable[mismatchedIndex] = pattern.length + longestCommonSuffixLength - prefixLength;
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
    int i;
    for(i = 0; i < pattern.length && i < prefix.length;i++){
        if(pattern[pattern.length - 1- i] != prefix[prefix.length - 1- i]){
            break;
        }
    }
    return i;
}

//prefixLengthTable[i] means the length of same prefix and suffix at mismatched index i
//eg pattern           DDDBDDD
//prefixLengthTable[i]  3333217
//index                0123456
private static int[] countPrefixLength(char[] pattern){
    int lastIndex = pattern.length - 1;
    int lastPrefixLength = 0;
    int[] prefixLengthTable = new int[pattern.length];
    prefixLengthTable[pattern.length - 1] = pattern.length;
    //meaningful mismatched index should start from pattern.length - 2;
    for(int i = lastIndex - 1; i >= 0; i--){
        int prefixLength = 0;
        int suffixLength = lastIndex - i;
        char[] suffix = new char[suffixLength];
        System.arraycopy(pattern,i + 1, suffix, 0, suffixLength);
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
