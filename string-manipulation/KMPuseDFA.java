public class KMPuseDFA {
    /** implement of KMP use dfa[][] or next[] based on DFM(deterministic finite-state automaton)*/
    //https://www.coursera.org/lecture/algorithms-part2/knuth-morris-pratt-TAtDr
    //https://www.cs.jhu.edu/~misha/ReadingSeminar/Papers/Knuth77.pdf
    //https://book.douban.com/review/10238312/
    // Algorithms (4th Edition) page762
    public static int strStr(String text, String pattern) {
        int[][] dfa = makeDfa(pattern);
        int j;
        int i;
        for(i = 0,j = 0; i < text.length() && j < pattern.length();i++){
                j = dfa[text.charAt(i)][j];
            }
        if(j == pattern.length()){
            return i - pattern.length();
        }
        return -1;
    }

    public static int[][] makeDfa(String p) {
        int ALPHABET_SIZE = 256;
        char[] pat = p.toCharArray();
        int[][] dfa = new int[ALPHABET_SIZE][p.length()];

        /** update dfa[str[0]][0] to 1*/
        dfa[pat[0]][0] = 1;
        /** lps start from 0*/
        int lps = 0;
        for(int j = 1; j < p.length();j++) {
            for (int c = 0; c < 256; c++) {
                dfa[c][j] = dfa[c][lps];/** Copy mismatch cases.*/
            }
            dfa[pat[j]][j] = j + 1; /** Set match case.*/
            lps = dfa[pat[j]][lps];/** update the overlap.*/
        }
        return dfa;
    }

    public static void main (String[]args){
        String text1 = new String("ABCDABEABDCBCDDBBCDBACD");
        String pattern1 = new String("BCDBACD");
        System.out.println(16 == strStr(text1, pattern1));

        String pattern2 = new String("BCDDBB");
        String pattern3 = new String("EABDCB");
        String pattern4 = new String("BBCDBD");
        String pattern5 = new String("DDBBCD");
        System.out.println(11 == strStr(text1, pattern2));
        System.out.println(6 == strStr(text1, pattern3));
        System.out.println(-1 == strStr(text1, pattern4));
        System.out.println(13 == strStr(text1, pattern5));

        String text2 = new String("BBBBBBBBBBBBBBBBBBBBBABBBBB");
        String pattern6 = new String("ABBBBB");
        System.out.println(21 == strStr(text2, pattern6));

        String text3 = new String("ABABDAAAACAAAABCABAB");
        String pattern7 = new String("AAACAAAA");
        System.out.println(6 == strStr(text3, pattern7));

        String text4 = new String("BBC ABCDAB ABCDABCDABDE");
        String pattern8 = new String("ABCDABD");
        System.out.println(15 == strStr(text4, pattern8));

        String text5 = new String("ABAAACAAAAAACAAAABCABAAAACAAAAFDLAAACAAAAAACAAAA");
        String pattern9 = new String("AAACAAAA");
        System.out.println(2 == strStr(text5, pattern9));

        String text6 = new String("GCATCGCAGAGAGTATACAGTACG");
        String pattern10 = new String("GCAGAGAG");
        System.out.println(5 == strStr(text6, pattern10));

        String text7 = new String("BBABBABABAAABBABBBBABBABBBABA");
        String pattern11 = new String("BBABBBABA");
        System.out.println(20 == strStr(text7, pattern11));
    }
}
