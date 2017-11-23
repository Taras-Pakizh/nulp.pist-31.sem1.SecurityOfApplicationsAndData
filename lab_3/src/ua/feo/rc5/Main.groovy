package ua.feo.rc5

import ua.feo.md5.MD5
import ua.feo.rnd.Random
import java.util.function.BinaryOperator
import static java.lang.Math.pow

static void main(String[] args) {
    def word = "Слава Україні"
    def password = new Random(pow(2, 14) - 1 as int, pow(6, 5) as int, 5, 32).next()
    def flag = false
    if (args.length == 3) {
        word = args[0]
        password = args[1]
        flag = Boolean.valueOf(args[2])
    } else if (args.length == 2) {
        word = args[0]
        password = args[1]
    } else if (args.length == 1) {
        word = args[0]
    }

    final int keySize = 32

    RC5 rc5 = new RC5()

    def enBuffer = new ArrayList<Integer>()
    def deBuffer = new ArrayList<Integer>()

    if (word.length() % 2 == 1) { word += " " }
    def byteWord = word.toCharArray() as int[]
    def key = new MD5().get_md5(password.toString())
    for (int i = 0; i < byteWord.length; i += 2) {
        rc5.setup(key)
        def en = rc5.encrypt([byteWord[i], byteWord[i + 1]] as int[])
        Arrays.stream(en).forEach{e -> enBuffer.add(e)}
        if (!flag) {
            for (int j = 0; j < keySize; j++) {
                key[j] = en[0] % (255 - j) as byte
            }
        }
        def de = rc5.decrypt([en[0], en[1]] as int[])
        Arrays.stream(de).forEach{e -> deBuffer.add(e)}
    }
    String result = deBuffer.stream().map{e -> (e as char).toString()}.reduce("", { acc, e -> acc + e} as BinaryOperator<String>)

    println "Ключ = " + password
    println "Текст = " + word
    println "Закодований текст = " + enBuffer.toString()
    println "Розкодований текст = " + deBuffer.toString()
    println "Розкодований текст = " + result
}