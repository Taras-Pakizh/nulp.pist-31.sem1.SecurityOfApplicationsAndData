package ua.feo.md5

import java.security.MessageDigest

static String format(String hex, String src) {
    MessageDigest digest = MessageDigest.getInstance("MD5")
    digest.update(src.bytes)
    return new BigInteger(1, digest.digest()).toString(16).padLeft(32, '0')
}
