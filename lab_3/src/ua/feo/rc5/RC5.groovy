package ua.feo.rc5

class RC5 {

    private static final int w = 32  //word length
    private static final int r = 8  //rounds
    private static final int b = 32  //key
    private static final int c = b / 4
    private static final int t = 26

    private int[] S = new int[t]
    private int P = 0xb7e1
    private int Q = 0x9e37

    int[] encrypt(int[] pt){
        int A = pt[0] + S[0]
        int B = pt[1] + S[1]
        for (int i = 1; i <= r; i++){
            A = (((A ^ B) << (B & (w - 1))) | ((A ^ B)>>>(w - (B & (w - 1))))) + S[2 * i]
            B = (((B ^ A) << (A & (w - 1))) | ((B ^ A)>>>(w - (A & (w - 1))))) + S[2 * i + 1]
        }
        return [A, B] as int[]
    }


    int[] decrypt(int[] ct){
        int B = ct[1]
        int A = ct[0]
        for (int i = r; i > 0; i--){
            B = (((B - S[2 * i + 1])>>>(A & (w - 1))) | ((B - S[2 * i + 1]) << (w - (A & (w - 1))))) ^ A
            A = (((A - S[2 * i])>>>(B & (w - 1))) | ((A - S[2 * i]) << (w - (B & (w - 1))))) ^ B
        }
        return [A - S[0], B - S[1]]
    }

    void setup(byte[] K){
        int i, j, k, A, B
        int u = w / 8
        int[] L = new int[c]

        for ({i = b - 1; L[c - 1] = 0}.run(); i != -1; i--){
            L[i / u as int] = (L[i / u as int] << 8) + K[i]
        }
        for ({S[0] = P; i = 1}.run(); i < t; i++) {
            S[i] = S[i - 1] + Q
        }
        for (A = B = i = j = k = 0; k < 3 * t; {k++; i = (i + 1) % t; j = (j + 1) % c}.run()) {
            A = S[i] = (((S[i] + (A + B)) << (3 & (w - 1))) | ((S[i] + (A + B))>>>(w - (3 & (w - 1)))))
            B = L[j] = (((L[j] + (A + B)) << ((A + B) & (w - 1))) | ((L[j] + (A + B))>>>(w - ((A + B) & (w - 1)))))
        }
    }

}
