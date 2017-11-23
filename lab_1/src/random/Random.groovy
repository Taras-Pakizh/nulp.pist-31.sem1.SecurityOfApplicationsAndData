package random

class Random {

    private def m
    private def a
    private def c
    public def x0

    private def x

    Random(int m, int a, int c, int x0) {
        this.m = m
        this.a = a
        this.c = c
        this.x0 = x0
        this.x = x0
    }

    int next(){
        def currentX = x
        x = (a * x + c) % m
        return currentX
    }

    int peek() {
        return x
    }

    void reset() {
        x = x0
    }

}
