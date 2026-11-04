package com.foodapp.auth_service.security.jwt;

import org.springframework.core.io.Resource;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Loads RSA keys from PEM resources (classpath: or file: paths).
 * Private key is expected in PKCS#8 format, public key in X.509 (SubjectPublicKeyInfo).
 */
public final class PemKeyLoader {

    private PemKeyLoader() {
    }

    public static PrivateKey readPrivateKey(Resource resource) {
        try {
            KeyFactory factory = KeyFactory.getInstance("RSA");
            return factory.generatePrivate(new PKCS8EncodedKeySpec(der(resource)));
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load RSA private key from " + resource, e);
        }
    }

    public static PublicKey readPublicKey(Resource resource) {
        try {
            KeyFactory factory = KeyFactory.getInstance("RSA");
            return factory.generatePublic(new X509EncodedKeySpec(der(resource)));
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load RSA public key from " + resource, e);
        }
    }

    private static byte[] der(Resource resource) throws Exception {
        String pem = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        String base64 = pem
                .replaceAll("-----BEGIN [^-]+-----", "")
                .replaceAll("-----END [^-]+-----", "")
                .replaceAll("\\s", "");
        return Base64.getDecoder().decode(base64);
    }
}
