public class KMP {
    /** implement of KMP use lps[] or next[] based on PMT(Partial Match Table)*/
    //https://www.coursera.org/lecture/algorithms-part2/knuth-morris-pratt-TAtDr
    //https://www.cs.jhu.edu/~misha/ReadingSeminar/Papers/Knuth77.pdf
    //https://book.douban.com/review/10238312/
    //Another way to implement KMP is DFM(deterministic finite-state automaton)
    // Algorithms (4th Edition) page762
    public static int strStr(String haystack, String needle) {
        int[] lps = new int[needle.length()];
        lps = countLps(needle);
        int hayIndex = 0;
        int neeIndex = 0;
        while(hayIndex < haystack.length() && neeIndex < needle.length()){
            if(haystack.charAt(hayIndex) != needle.charAt(neeIndex)){
                /** if not match, find the overlap and then keep skipping overlap until match or overlap=0*/
                if(neeIndex != 0){
                    /**lps[neeIndex - 1] is overlap value */
                    neeIndex = lps[neeIndex - 1];
                }
                else{
                    hayIndex++;
                }
            }else{
                hayIndex++;
                neeIndex++;
            }  
        }
        if(neeIndex == needle.length()){
                return hayIndex - needle.length();
            }
        return -1;
    }

    public static int[] countLps(String s) {
        char[] str = s.toCharArray();
        int[] lps = new int[s.length()];
        int prev = 0;
        int i = 1;
        while (i < s.length()) {
            if (str[i] != str[prev]) {
                /** if not match, find the overlap and then keep skipping overlap until match or overlap=0*/
                if (prev != 0) {
                    prev = lps[prev - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            } else {
                lps[i] = prev + 1;
                prev++;
                i++;
            }
        }
        return lps;
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
