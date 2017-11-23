package ua.feo.signature

import org.bouncycastle.pqc.jcajce.provider.BouncyCastlePQCProvider

import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import java.security.SecureRandom
import java.security.Security

import static java.security.Signature.*

static signData(byte[] data, PrivateKey key) {
    getInstance("SHA1withDSA").with {
        initSign(key)
        update(data)
        sign()
    }
}

static verifySig(byte[] data, PublicKey key, byte[] sig) {
    getInstance("SHA1withDSA").with {
        initVerify(key)
        update(data)
        verify(sig)
    }
}

static generateKeyPair(long seed) {
    Security.addProvider(new BouncyCastlePQCProvider())
    SecureRandom.getInstance("SHA1PRNG", "SUN").with { rng ->
        rng.setSeed(seed)
        KeyPairGenerator.getInstance("DSA").with {
            initialize(1024, rng)
            genKeyPair()
        }
    }
}