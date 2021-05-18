package test;

import java.io.UnsupportedEncodingException;

/**
 * User: liguanyuinfo@jd.com
 * Date: 15-05-27
 * Time: 18.27
 */
final public class CityHash {


    public static Long hash64(String str) throws UnsupportedEncodingException{
            byte[] bytes=str.getBytes("UTF-8");
            return hash64(bytes,bytes.length);
    }

    public static Long hash64(final byte[] data,int length){
        return hash64(data,length,0xEE6B27EB);
    }

    public static Long hash64(final byte[] data, int length, int seed){
        final long m = 0x5bd1e995L;
        final int r = 24;

        long h1 = (seed & 0xffffffffl) ^ length;
        long h2 = 0;


        int i = 0;

        while (length - i >= 8) {

            long k1 = ((long) data[i + 0] & 0xff) + (((long) data[i + 1] & 0xff) << 8)
                    + (((long) data[i + 2] & 0xff) << 16) + (((long) data[i + 3] & 0xff) << 24);
            k1 *= m;
            k1 ^= k1 >> r;
            k1 *= m;
            h1 *= m;
            h1 ^= k1;
            i += 4;


            long k2 = ((long) data[i + 0] & 0xff) + (((long) data[i + 1] & 0xff) << 8)
                    + (((long) data[i + 2] & 0xff) << 16) + (((long) data[i + 3] & 0xff) << 24);

            k2 *= m;
            k2 ^= k2 >> r;
            k2 *= m;
            h2 *= m;
            h2 ^= k2;
            i += 4;
        }

        if (length - i >= 4) {
            long k1 = ((long) data[i + 0] & 0xff) + (((long) data[i + 1] & 0xff) << 8)
                    + (((long) data[i + 2] & 0xff) << 16) + (((long) data[i + 3] & 0xff) << 24);
            k1 *= m;
            k1 ^= k1 >> r;
            k1 *= m;
            h1 *= m;
            h1 ^= k1;
            i += 4;
        }

        switch (length - i) {
            case 3:
                h2 ^= data[i + 2] << 16;
            case 2:
                h2 ^= data[i + 1] << 8;
            case 1:
                h2 ^= data[i + 0];
                h2 *= m;
                break;
            case 0:
                break;
            default:
                throw  new RuntimeException("cityhash error");
        }


        h1 ^= h2 >> 18;
        h1 *= m;
        h2 ^= h1 >> 22;
        h2 *= m;
        h1 ^= h2 >> 17;
        h1 *= m;
        h2 ^= h1 >> 19;
        h2 *= m;

        long h = h1;

        h = (h << 32) | h2;

        return h;
    }

}
