package ua.feo.md5

import java.security.MessageDigest

class MD5 {

    String to_hex(int value) {
        def out = new StringBuilder()
        int[] hex_res = new int[3]

        while(value) {
            for (i in 0 .. 2) hex_res[i] = (char) '\0'
            if (hex_res[1] == (char) '\0') {
                hex_res[1] = hex_res[0]
                hex_res[0] = (char) '0'
                hex_res[2] = (char) '\0'
            }
            out.append(hex_res.toString())
            value /= 256
        }
        return Arrays.toString(out)
    }

    static byte[] format(String hex, String src) {
        MessageDigest digest = MessageDigest.getInstance("MD5")
        digest.update(src.bytes)
        byte[] result = new byte[32]
        def array = new BigInteger(1, digest.digest()).toString(16).toCharArray()
        for (int i = 1; i <= array.length; i++) {
            result[result.length - i] = array[array.length - i] as byte
        }
        return result
    }

    int F(int x, int y, int z) { return ((x & y) | ((~x) & z)) }
    int G(int x, int y, int z) { return (x & z) | (y & (~z)) }
    int H(int x, int y, int z) { return x ^ y ^ z }
    int I(int x, int y, int z) { return y ^ (x | (~z)) }

    int rotate_left(int value, int shift) { return value << shift | value >> (32-shift) }

    byte[] get_md5(String src) {
        int length = src.length()
        int rest = length % 64
        int size

        //1
        if (rest < 56) size = length - rest + 56 + 8
        else size = length + 64 - rest + 56 + 8

        int[] msg_for_decode = new int[size]

        for(int i = 0; i < length; i++) msg_for_decode[i] = src.charAt(i)
        msg_for_decode[length] = 0x80
        for(int i = length + 1; i < size; i++) msg_for_decode[i] = 0

        //2
        long bit_length = (long)((length) << 3)
        for (int i = 0; i < 8; i++) msg_for_decode[size - 8 + i] = (bit_length >> (i << 3))

        //3
        int A = 0x67452301, B = 0xefcdab89, C = 0x98badcfe, D = 0x10325476
        int[] T = new int[64]
        for (int i = 0; i < 64; i++) T[i] = (Math.pow(2, 32)) * Math.abs(Math.sin(i + 1))
        int[] X = msg_for_decode

        //4
        int AA, BB, CC, DD

        for(int i in 0 .. size / 4) {
            AA = A
            BB = B
            CC = C
            DD = D

            //раунд 1
            A = B + rotate_left((A + F(B,C,D) + X[i+ 0] + T[ 0]),  7)
            D = A + rotate_left((D + F(A,B,C) + X[i+ 1] + T[ 1]), 12)
            C = D + rotate_left((C + F(D,A,B) + X[i+ 2] + T[ 2]), 17)
            B = C + rotate_left((B + F(C,D,A) + X[i+ 3] + T[ 3]), 22)

            A = B + rotate_left((A + F(B,C,D) + X[i+ 4] + T[ 4]),  7)
            D = A + rotate_left((D + F(A,B,C) + X[i+ 5] + T[ 5]), 12)
            C = D + rotate_left((C + F(D,A,B) + X[i+ 6] + T[ 6]), 17)
            B = C + rotate_left((B + F(C,D,A) + X[i+ 7] + T[ 7]), 22)

            A = B + rotate_left((A + F(B,C,D) + X[i+ 8] + T[ 8]),  7)
            D = A + rotate_left((D + F(A,B,C) + X[i+ 9] + T[ 9]), 12)
            C = D + rotate_left((C + F(D,A,B) + X[i+10] + T[10]), 17)
            B = C + rotate_left((B + F(C,D,A) + X[i+11] + T[11]), 22)

            A = B + rotate_left((A + F(B,C,D) + X[i+12] + T[12]),  7)
            D = A + rotate_left((D + F(A,B,C) + X[i+13] + T[13]), 12)
            C = D + rotate_left((C + F(D,A,B) + X[i+14] + T[14]), 17)
            B = C + rotate_left((B + F(C,D,A) + X[i+15] + T[15]), 22)

            //раунд 2
            A = B + rotate_left((A + G(B,C,D) + X[i+ 1] + T[16]),  5)
            D = A + rotate_left((D + G(A,B,C) + X[i+ 6] + T[17]),  9)
            C = D + rotate_left((C + G(D,A,B) + X[i+11] + T[18]), 14)
            B = C + rotate_left((B + G(C,D,A) + X[i+ 0] + T[19]), 20)

            A = B + rotate_left((A + G(B,C,D) + X[i+ 5] + T[20]),  5)
            D = A + rotate_left((D + G(A,B,C) + X[i+10] + T[21]),  9)
            C = D + rotate_left((C + G(D,A,B) + X[i+15] + T[22]), 14)
            B = C + rotate_left((B + G(C,D,A) + X[i+ 4] + T[23]), 20)

            A = B + rotate_left((A + G(B,C,D) + X[i+ 9] + T[24]),  5)
            D = A + rotate_left((D + G(A,B,C) + X[i+14] + T[25]),  9)
            C = D + rotate_left((C + G(D,A,B) + X[i+ 3] + T[26]), 14)
            B = C + rotate_left((B + G(C,D,A) + X[i+ 8] + T[27]), 20)

            A = B + rotate_left((A + G(B,C,D) + X[i+13] + T[28]),  5)
            D = A + rotate_left((D + G(A,B,C) + X[i+ 2] + T[29]),  9)
            C = D + rotate_left((C + G(D,A,B) + X[i+ 7] + T[30]), 14)
            B = C + rotate_left((B + G(C,D,A) + X[i+12] + T[31]), 20)

            //раунд 3
            A = B + rotate_left((A + H(B,C,D) + X[i+ 5] + T[32]),  4)
            D = A + rotate_left((D + H(A,B,C) + X[i+ 8] + T[33]), 11)
            C = D + rotate_left((C + H(D,A,B) + X[i+11] + T[34]), 16)
            B = C + rotate_left((B + H(C,D,A) + X[i+14] + T[35]), 23)

            A = B + rotate_left((A + H(B,C,D) + X[i+ 1] + T[36]),  4)
            D = A + rotate_left((D + H(A,B,C) + X[i+ 4] + T[37]), 11)
            C = D + rotate_left((C + H(D,A,B) + X[i+ 7] + T[38]), 16)
            B = C + rotate_left((B + H(C,D,A) + X[i+10] + T[39]), 23)

            A = B + rotate_left((A + H(B,C,D) + X[i+13] + T[40]),  4)
            D = A + rotate_left((D + H(A,B,C) + X[i+ 0] + T[41]), 11)
            C = D + rotate_left((C + H(D,A,B) + X[i+ 3] + T[42]), 16)
            B = C + rotate_left((B + H(C,D,A) + X[i+ 6] + T[43]), 23)

            A = B + rotate_left((A + H(B,C,D) + X[i+ 9] + T[44]),  4)
            D = A + rotate_left((D + H(A,B,C) + X[i+12] + T[45]), 11)
            C = D + rotate_left((C + H(D,A,B) + X[i+15] + T[46]), 16)
            B = C + rotate_left((B + H(C,D,A) + X[i+ 2] + T[47]), 23)

            //раунд 4
            A = B + rotate_left((A + I(B,C,D) + X[i+ 0] + T[48]),  6)
            D = A + rotate_left((D + I(A,B,C) + X[i+ 7] + T[49]), 10)
            C = D + rotate_left((C + I(D,A,B) + X[i+14] + T[50]), 15)
            B = C + rotate_left((B + I(C,D,A) + X[i+ 5] + T[51]), 21)

            A = B + rotate_left((A + I(B,C,D) + X[i+12] + T[52]),  6)
            D = A + rotate_left((D + I(A,B,C) + X[i+ 3] + T[53]), 10)
            C = D + rotate_left((C + I(D,A,B) + X[i+10] + T[54]), 15)
            B = C + rotate_left((B + I(C,D,A) + X[i+ 1] + T[55]), 21)

            A = B + rotate_left((A + I(B,C,D) + X[i+ 8] + T[56]),  6)
            D = A + rotate_left((D + I(A,B,C) + X[i+15] + T[57]), 10)
            C = D + rotate_left((C + I(D,A,B) + X[i+ 6] + T[58]), 15)
            B = C + rotate_left((B + I(C,D,A) + X[i+13] + T[59]), 21)

            A = B + rotate_left((A + I(B,C,D) + X[i+ 4] + T[60]),  6)
            D = A + rotate_left((D + I(A,B,C) + X[i+11] + T[61]), 10)
            C = D + rotate_left((C + I(D,A,B) + X[i+ 2] + T[62]), 15)
            B = C + rotate_left((B + I(C,D,A) + X[i+ 9] + T[63]), 21)

            A += AA
            B += BB
            C += CC
            D += DD
        }

        //Шаг 5.
        def res = format(to_hex(A) + to_hex(B) + to_hex(C) + to_hex(D), src)

        return res
    }

}
