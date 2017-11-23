package ua.feo.signature

import java.security.Security
import org.bouncycastle.pqc.jcajce.provider.BouncyCastlePQCProvider

import static ua.feo.signature.Signature.*
import static ua.feo.signature.Util.*

static def main(String[] args) {
    def scanner = new Scanner(System.in)
    while(true) {
        println "Input text seedA and seedB"
        print "you say> "
        def c = scanner.nextLine().split(" ")
        if (c[0] == "exit") break
        if (c.length != 3) continue
        try {
            def text = c[0]
            def seedA = Long.valueOf(c[1])
            def seedB = Long.valueOf(c[2])
            start(text, seedA, seedB)
        } catch (Exception e) {
            println "[ERROR]: try again"
        }
    }
}

static start(String text, long seedA, long seedB) {
    Security.addProvider(new BouncyCastlePQCProvider())
    def data = Arrays.stream(text.toCharArray()).map{e -> e as byte}.toArray() as byte[]

    def keyPair = generateKeyPair(seedA)
    printKeyPair keyPair
    def digitalSignature = signData(data, keyPair.getPrivate())
    printVerify verifySig(data, keyPair.getPublic(), digitalSignature)

    keyPair = generateKeyPair(seedB)
    printKeyPair keyPair
    printVerify verifySig(data, keyPair.getPublic(), digitalSignature)
}