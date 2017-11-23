package ua.feo.rsa

import java.security.PrivateKey
import java.security.PublicKey

class Main {

    static main(String[] args) {
        new Main()
    }

    Main() {
        def scanner = new Scanner(System.in)
        def path = ""
        def name = ""
        def originalText = ""
        def cipherText = new byte[0]
        while (true) {
            print "you say>"
            scanner.nextLine().split(" ").with { c ->
                if (c.length == 0) return
                switch (c[0]) {
                    case "path":
                        if (c.length == 2) {
                            path = c[1]
                        } else {
                            printError("set correct path")
                        }
                        break
                    case "name":
                        if (c.length == 2) {
                            name = c[1]
                        } else {
                           printError("set correct name")
                        }
                        break
                    case "generate":
                        if (c.length == 1 && path != "" && name != "") {
                            generateKeys(path, name)
                        } else if (c.length == 3) {
                            path = c[1]
                            name = c[2]
                            generateKeys(path, name)
                        } else {
                            printError("set name and path")
                        }
                        break
                    case "encrypt":
                        if (c.length == 2 && path != "" && name != "") {
                            generateKeys(path, name)
                            originalText = c[1]
                            def (byte[] cipher, _, long encryptedTime) = encrypt(originalText, path, name)
                            cipherText = cipher
                            printEncrypt(originalText, cipher, encryptedTime)
                        } else if (c.length == 4) {
                            generateKeys(path, name)
                            path = c[1]
                            name = c[2]
                            originalText = c[3]
                            def (byte[] cipher, _, long encryptedTime) = encrypt(originalText, path, name)
                            cipherText = cipher
                            printEncrypt(originalText, cipher, encryptedTime)
                        } else {
                            printError("encrypt failed")
                        }
                        break
                    case "decrypt":
                        if (c.length == 1 && cipherText != null && path != "" && name != "") {
                            def (String plainText, _, long decryptedTime) = decrypt(cipherText, path, name)
                            printDecrypt(cipherText, plainText, decryptedTime)
                        } else if (c.length == 3 && cipherText != null && path != "" && name != "") {
                            path = c[1]
                            name = c[2]
                            def (String plainText, _, long decryptedTime) = decrypt(cipherText, path, name)
                            printDecrypt(cipherText, plainText, decryptedTime)
                        } else {
                            printError("decrypt faoled")
                        }
                        break
                    case "test":
                        if (c.length == 2 && path != "" && name != "") {
                            generateKeys(path, name)
                            originalText = c[1]
                            def (byte[] cipher, PublicKey publicKey, long encryptedTime) = encrypt(originalText, path, name)
                            def (String plainText, PrivateKey privateKey, long decryptedTime) = decrypt(cipherText, path, name)
                            printTest(originalText, cipher, plainText, encryptedTime, decryptedTime, publicKey, privateKey)
                        } else if (c.length == 4) {
                            generateKeys(path, name)
                            path = c[1]
                            name = c[2]
                            originalText = c[3]
                            def (byte[] cipher, PublicKey publicKey, long encryptedTime) = encrypt(originalText, path, name)
                            def (String plainText, PrivateKey privateKey, long decryptedTime) = decrypt(cipherText, path, name)
                            printTest(originalText, cipher, plainText, encryptedTime, decryptedTime, publicKey, privateKey)
                        } else {
                            printError("test failed")
                        }
                        break
                    case "exit":
                        if (c.length == 1) {
                            System.exit(0)
                        } else {
                            printError("exit failed")
                        }
                        break
                    case "help":
                        if (c.length == 1) {
                            printHelp()
                        } else {
                            printError("help failed")
                        }
                        break
                }
            }
        }
    }

    static generateKeys(String path, String name) {
        if (!RSA.areKeysPresent(publicName(path, name), privateName(path, name)))
            RSA.generateKey(publicName(path, name), privateName(path, name))
    }

    static publicName(String path, String name) {
        "$path/${name}.rsa_pub"
    }

    static privateName(String path, String name) {
        "$path/${name}.rsa_prv"
    }

    static encrypt(String originalText, String path, String name) {
        new ObjectInputStream(new FileInputStream(publicName(path, name))).withCloseable { stream ->
            def startTimer = System.currentTimeMillis()
            final publicKey = stream.readObject() as PublicKey
            def cipherText = RSA.encrypt(originalText, publicKey)
            new Tuple(cipherText, publicKey, System.currentTimeMillis() - startTimer)
        }
    }

    static decrypt(byte[] cipherText, String path, String name) {
        new ObjectInputStream(new FileInputStream(privateName(path, name))).withCloseable { stream ->
            def startTimer = System.currentTimeMillis()
            final privateKey = stream.readObject() as PrivateKey
            final plainText = RSA.decrypt(cipherText, privateKey)
            new Tuple(plainText, privateKey, System.currentTimeMillis() - startTimer)
        }
    }

    static printEncrypt(String originalText, byte[] cipherText, long encryptedTime) {
        println """
Original: $originalText
Encrypted: $cipherText
Encrypted time RSA: $encryptedTime ms.
"""
    }

    static printDecrypt(byte[] cipherText, String plainText, long decryptedTime) {
        println """
CipherText: $cipherText
Decrypted: $plainText
Decrypted time RSA: $decryptedTime ms.
"""
    }

    static printTest(String originalText, byte[] cipherText, String plainText,
                  long encryptedTime, long decryptedTime, PublicKey publicKey, PrivateKey privateKey){
        println """
Original: $originalText
Encrypted: $cipherText
Decrypted: $plainText
Encrypted time RSA: $encryptedTime ms.
Decrypted time RSA: $decryptedTime ms.
_________________________________
Private key: ${Base64.getEncoder().encodeToString(privateKey.encoded)}
Public key: ${Base64.getEncoder().encodeToString(publicKey.encoded)}
"""
    }

    static printHelp() {
        println """
path
    (string path)
    set file path
name
    (string name)
    set file name
generate
    ()
    (string path, string name)
    generate keys
encrypt
    (string message)
    (string path, string name, string message)
    encrypt message
decrypt
    ()
    (string path, string name)
    decrypt last message
test
    (string message)
    (string path, string name, string message)
    encrypt message, decrypt and print results
help
    ()
    show help
exit
    ()
    exit
"""
    }

    static printError(String s) {
        println "[ERROR]: $s"
    }

}