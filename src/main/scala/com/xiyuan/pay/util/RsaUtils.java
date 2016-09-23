package com.xiyuan.pay.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.ByteArrayInputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RsaUtils {

	private static final String RSA = "RSA";

	private static final String SHA1WithRSA = "SHA1WithRSA";

	private static final String UTF_8 = "UTF-8";

	static {
		java.security.Security.addProvider(new BouncyCastleProvider());
	}

	/**
	 * 用私钥对content签名
	 * @param content
	 * @param privateKey
     * @return
     */
	public static String rsaSign(String content, String privateKey) {
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
			KeyFactory keyf = KeyFactory.getInstance(RSA);
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);

			java.security.Signature signature = java.security.Signature
					.getInstance(SHA1WithRSA);

			signature.initSign(priKey);
			signature.update(content.getBytes(UTF_8));

			byte[] signed = signature.sign();

			return Base64.encode(signed);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * rsa验签
	 *
	 * @param content 被签名的内容
	 * @param sign 签名后的结果
	 * @param publicKey rsa公钥
	 * @return 验签结果
	 */
	public static boolean rsaCheck(String content, String sign, String publicKey) {
		try {
			X509EncodedKeySpec x509 = new X509EncodedKeySpec(Base64.decode(publicKey));
			KeyFactory keyf = KeyFactory.getInstance(RSA);
			PublicKey pubKey = keyf.generatePublic(x509);

			Signature signature = Signature.getInstance(SHA1WithRSA);
			signature.initVerify(pubKey);
			signature.update(content.getBytes(UTF_8));
			return signature.verify(Base64.decode(sign));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
