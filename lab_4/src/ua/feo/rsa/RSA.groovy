package ua.feo.rsa

import javax.crypto.Cipher
import java.security.Key
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey

class RSA {

    static final String ALGORITHM = "RSA"
    static final int KEY_SIZE = 1024

    static generateKey(String publicKeyFilePath, String privateKeyFilePath) {
        KeyPairGenerator.getInstance(ALGORITHM).with {
            initialize(KEY_SIZE)
            generateKeyPair().with {
                toFile(publicKeyFilePath, getPublic())
                toFile(privateKeyFilePath, getPrivate())
            }
        }
    }

    static toFile(String filePath, Key key) {
        new File(filePath).with { file ->
            if (getParentFile()) getParentFile().mkdirs()
            createNewFile()
            new ObjectOutputStream(new FileOutputStream(file)).withCloseable { stream ->
                stream.writeObject(key)
            }
        }
    }

    static areKeysPresent(String publicKeyFilePath, String privateKeyFilePath) {
        new File(privateKeyFilePath).exists() && new File(publicKeyFilePath).exists()
    }

    static encrypt(String text, PublicKey key) {
        Cipher.getInstance(ALGORITHM).with {
            init(ENCRYPT_MODE, key)
            doFinal(text.getBytes())
        }
    }

    static decrypt(byte[] text, PrivateKey key) {
        Cipher.getInstance(ALGORITHM).with {
            init(DECRYPT_MODE, key)
            new String(doFinal(text))
        }
    }

}