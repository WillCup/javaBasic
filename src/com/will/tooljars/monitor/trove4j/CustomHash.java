package com.will.tooljars.monitor.trove4j;

import gnu.trove.map.hash.TCustomHashMap;
import gnu.trove.strategy.HashingStrategy;

public class CustomHash {
    public static void main(String[] args) {
        char[] foo = new char[]{'a', 'b', 'c'};
        char[] bar = new char[]{'a', 'b', 'c'};
        TCustomHashMap<char[], String> ch = new TCustomHashMap<char[], String>(new CharArrayStrategy());
        ch.put(foo, "John");
        ch.put(bar, "Tom");
    }

    static class CharArrayStrategy implements HashingStrategy<char[]> {
        public int computeHashCode(char[] c) {
            // use the shift-add-xor class of string hashing functions
            // cf. Ramakrishna and Zobel, "Performance in Practice
            // of String Hashing Functions"
            int h = 31; // seed chosen at random
            for (int i = 0; i < c.length; i++) { // could skip invariants
                h = h ^ ((h << 5) + (h >> 2) + c[i]); // L=5, R=2 works well for
                                                      // ASCII input
            }
            return h;
        }

        public boolean equals(char[] c1, char[] c2) {
            if (c1.length != c2.length) { // could drop this check for fixed-length
                                          // keys
                return false;
            }
            for (int i = 0, len = c1.length; i < len; i++) { // could skip
                                                             // invariants
                if (c1[i] != c2[i]) {
                    return false;
                }
            }
            return true;
        }
    }
}
