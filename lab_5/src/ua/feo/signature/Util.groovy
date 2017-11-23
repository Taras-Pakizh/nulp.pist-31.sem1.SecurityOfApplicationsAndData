package ua.feo.signature

import java.security.KeyPair

static def printKeyPair(KeyPair keyPair) {
    println "Public Key: ${Base64.getEncoder().encodeToString(keyPair.getPublic().encoded)}"
    println "Private Key:${Base64.getEncoder().encodeToString(keyPair.getPrivate().encoded)}"
}

static def printVerify(boolean verify) {
    println "Verify: $verify"
}