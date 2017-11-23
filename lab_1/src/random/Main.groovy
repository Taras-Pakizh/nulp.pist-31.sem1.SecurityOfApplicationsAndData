package random

import static java.lang.Math.pow

static void main(String[] args) {
    def rnd
    if (args.length == 0) {
        rnd = new Random((int) pow(2, 14) - 1, (int) pow(6, 5), 5, 32)
    } else {
        rnd = new Random(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]))
    }

    println(rnd.next())
    def i = 0
    while(rnd.x0 != rnd.peek()) {
        println(rnd.next())
        i++
    }
    println "i = $i"
}