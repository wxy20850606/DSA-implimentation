/** implementation of Boyer Moore algorithms 
 * reference:https://go.dev/src/strings/search.go
 * reference:https://github.com/hit9/code-snippets/blob/master/string/string-search-boyer-moore/main.c
 * reference:https://writings.sh/post/algorithm-string-searching-boyer-moore#好后缀的办法-1;
*/

public class BM {
    final int ALPHABET_SIZE = 256;
    private int[]  badCharSkipTable = new int[ALPHABET_SIZE];
    private int[]  goodSuffixSkipTable;
    private int[] prefixLengthTable;
/** Bad Character skip rule
 * 
 * compare Pattern P and Text T from right to left when mismatch happens, for mismatched character c in Test:
 * 1.if P does not contain c, skip the whole length of pattern.
 * badCharSkip[b] is len(pattern).
 * 2.if P contans c, shift P to align the right-most occurrence of c in P with c. 
 * badCharSkip[b] is the distance between the last byte of pattern
 * and the rightmost occurrence of b in pattern. 
 *
 */    

public int strStr(String text, String pattern){
    badCharSkip(pattern,badCharSkipTable);
    goodSuffixSkip(pattern,goodSuffixSkipTable);
    return -1;
 }


private void badCharSkip(char[] pattern,int[] badCharSkipTable) {
    // 256 Unicode characters
    int last = pattern.length - i ;
    for (int i = 0; i < ALPHABET_SIZE; i++) {
    // set up all the value to -1;
        badCharSkipTable[i] = pattern.length;
    }
    // The loop condition is < instead of <= so that the last byte does not
    // have a zero distance to itself. Finding this byte out of place implies
    // that it is not in the last position.
    for (int i = 0; i < last; i++) {
    // calculate the ASCII number for badCharSkip[i]
        int ascii = (int)character[i]; 
    //update the value of the characters in pattern 
        badCharSkipTable[ascii] = last - i;//update 
    }
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

 /* goodSuffixSkipTable[i] means the increment value for index in Text string*/

 private void goodSuffixSkipCase2And3(char[] pattern, int[] goodSuffixSkipTable){

/* case 3 
 * goodSuffixSkipTable[k] = pattern.length + suffix_length;
 * case 3 is a special case in case 2 when prefix_length == 0
*/

/* case 2
 * goodSuffixSkipTable[k] = pattern.length + suffixLength - prefixLength;
 */

    goodSuffixSkipTable = new int[pattern.length];
    int lastIndex = pattern.length - 1;
//mismatch in last index means no suffix, good suffix rule doesn't have meaning, set value to 1
    goodSuffixSkipTable[lastIndex] = 1;

    countPrefixLength(pattern,prefixLengthTable);
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
private void goodSuffixSkipCase1(char[] pattern,int[] goodSuffixSkipTable){
    for(int i = 1; i < pattern.length-1; i++){
        int prefixLength = i + 1;
        char[] prefix = new char[prefixLength];
        System.arraycopy(pattern,0, prefix, 0, prefixLength);
        int longestCommonSuffixLength = countLongestCommonSuffixLength(pattern,prefix);
        if(longestCommonSuffixLength > 0){
            mismatchedIndex = pattern.length - 1 - longestCommonSuffixLength;
            goodSuffixSkipTable[mismatchedIndex] = pattern.length + longestCommonSuffixLength - prefixLength;
        }
    }
}



/*
 * eg:                              A  D  A  C  D  B  C  D  B  C  D
 * mismatch index i                 0  1  2  3  4  5  6  7  8  9  10
 * longestCommonSuffixLength           1        2        5   
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

private int countLongestCommonSuffixLength(char[] pattern, char[] prefix){
    for(int i = 0; i < pattern.length && i < prefix.length;i++){
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
private void countPrefixLength(char[] pattern,int[] prefixLengthTable){
    int lastIndex = pattern.length - 1;
    int lastPrefixLength = 0;
    
    prefixLength able = new int[pattern.length];
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
}

}
