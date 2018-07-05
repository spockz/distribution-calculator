package me.alessandrovermeulen.distributioncalculator;

import java.util.Random;

public class ExtendedRandom {
    /**
     * Copyright (c) 1995, 2013, Oracle and/or its affiliates. All rights reserved.
     * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
     *
     * Copied from {{java.util.Random}} under the following license:
     *   http://www.oracle.com/technetwork/java/javase/terms/license/index.html
     *
     * Adapted to accept {{{@link Random}}} as argument instead of this reference.
     *
     * @param random the random
     * @param origin
     * @param bound
     * @return
     */
    final static long internalNextLong(Random random, long origin, long bound) {
        long r = random.nextLong();
        if (origin < bound) {
            long n = bound - origin, m = n - 1;
            if ((n & m) == 0L)  // power of two
                r = (r & m) + origin;
            else if (n > 0L) {  // reject over-represented candidates
                for (long u = r >>> 1;            // ensure nonnegative
                     u + m - (r = u % n) < 0L;    // rejection check
                     u = random.nextLong() >>> 1) // retry
                    ;
                r += origin;
            }
            else {              // range not representable as long
                while (r < origin || r >= bound)
                    r = random.nextLong();
            }
        }
        return r;
    }
}
