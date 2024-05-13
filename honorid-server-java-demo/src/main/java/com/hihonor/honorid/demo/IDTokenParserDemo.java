/*
Copyright 2021. Honor Device Co.,Ltd. All rights reserved.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package com.hihonor.honorid.demo;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwk.InvalidPublicKeyException;
import com.auth0.jwk.Jwk;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

/**
 * This is a demo for verify ID Token in your local server.
 * The verification steps are as follows:
 *  1) Parse ID Token
 *  2) Check whether the value of the iss field in the ID Token is https://honorid.hihonor.com
 *  3) Check whether the value of the aud field in the ID Token is the CLIENT_ID of your application
 *  4) Get key according to keyId
 *  5) Calculate RSA public key based on key
 *  6) Verify signature and validity period
 *  7) If the above checks are passed, the assembly result will be returned
 * We recommend using local validation instead of accessing the tokeninfo service.
 * The open source software in this demo may have vulnerabilities,
 * please refer to the open source software release website and update to
 * the latest version or replace it with other open source software.
 * You'd better learn more about the JWT and JWK for understanding this demo
 * See more about JWT in https://jwt.io/
 * See more about JWK in http://self-issued.info/docs/draft-ietf-jose-json-web-key.html
 */
public class IDTokenParserDemo {

    /**
     * Please replace this CLIENT_ID with yours app’s client ID (appid)
     */
    private final static String CLIENT_ID = "123456";

    private final static int MAX_PUBLIC_KEY_SIZE = 4;

    private final static Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    //JWK JSON Web Key endpoint, developer can get the JWK of the last two days from this endpoint
    private final static String CERT_URL = "https://hnoauth-login.cloud.honor.com/oauth2/v3/certs";

    /**
     * Cache the public key in this map
     */
    private final Map<String, RSAPublicKey> keyId2PublicKey = new HashMap<>();

    /**
     * ID Token issue
     */
    public static final String ID_TOKEN_ISSUE = "https://honorid.hihonor.com";

    // Main method
    public static void main(String args[]) throws InvalidPublicKeyException {
        // Please replace this idToken with yours
        String idToken = "eyJraWQiOiI5OGMDNjNWU5MmRlMzg0MDU5Y2QidHlwIjoiSldUIiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOLCJhdWQpIjoiN2mJhLTgyFhIn0.DCTfmwp2oaiyTXXQ4B9elTNB7w8ClgmINwQszNdSKVQXKcwtJNxtnzcRuieI8yGlhMt9ZagyMaKI9g1Pt6x4NbEAWioGFgk7N6jJI_2I1i--15CjPfuiQ50djCtrzTEBA5562lYTLMd8Wg1CFFERqpInXFhr69afKVFO81Evoz9DgWbOq4GLT0WPdhi843VP78x2l2TZynGduZJQBdxXmt9DkHUu21asDhAtzxqm9PqlKJfFb4HgZ92NCC7AvK566tA3jLzw18mp9PuImnqiLqzcYa0Fzk5Ynljr1IQ";
        IDTokenParserDemo idTokenParser = new IDTokenParserDemo();
        System.out.println(idTokenParser.verify(idToken));
    }

    /**
     * verify ID Token
     *
     * @param idToken ID Token
     * @return The parsed ID Token
     * @throws InvalidPublicKeyException
     */
    public String verify(String idToken) throws InvalidPublicKeyException {
        try {
            // Parse ID Token
            DecodedJWT decodedJwt = JWT.decode(idToken);
            // Verify the value of iss
            if (!decodedJwt.getIssuer().equals(ID_TOKEN_ISSUE)) {
                return null;
            }
            // Verify your app’s client ID.
            if (decodedJwt.getAudience().size() > 0) {
                if (!decodedJwt.getAudience().get(0).equals(CLIENT_ID)) {
                    return null;
                }
            }
            // get key id
            String keyId = decodedJwt.getKeyId();
            // get RSA public key by keyId
            RSAPublicKey rsaPublicKeyByKid = getRSAPublicKeyByKid(keyId);
            // get JWTVerifier
            Algorithm algorithm = Algorithm.RSA256(rsaPublicKeyByKid, null);
            JWTVerifier verifier = JWT.require(algorithm).build();
            JSONObject jsonObject = JSONObject.parseObject(new String(Base64.decodeBase64(decodedJwt.getPayload()), StandardCharsets.UTF_8));

            // verify signature and validity period
            // If the signature is incorrect, an JWTVerificationException will be thrown
            // If ID Token has expired, an TokenExpiredException will be thrown
            verifier.verify(decodedJwt);
            // return result
            jsonObject.put("alg", decodedJwt.getAlgorithm());
            jsonObject.put("typ", decodedJwt.getType());
            jsonObject.put("kid", decodedJwt.getKeyId());
            return jsonObject.toJSONString();
        } catch (JWTDecodeException e) {
            return null;
        } catch (TokenExpiredException e) {
            // jwt token is expire
            return null;
        } catch (JWTVerificationException e) {
            // VERIFY SIGNATURE failed
            return null;
        }
    }

    /**
     * get the RSAPublicKey by keyId
     * Please cache the RSAPublicKey
     * In the demo we use hashmap to cache the public key
     *
     * @param keyId keyId
     * @return RSAPublicKey
     * @throws InvalidPublicKeyException
     */
    private RSAPublicKey getRSAPublicKeyByKid(String keyId) throws InvalidPublicKeyException {
        if (keyId2PublicKey.get(keyId) != null) {
            return keyId2PublicKey.get(keyId);
        }
        // get keys from /oauth2/v3/certs
        JSONArray keys = getJwks();
        if (keys == null) {
            return null;
        }
        // When the cache size is greater than MAX_PUBLIC_KEY_SIZE, clear the cache
        if (keyId2PublicKey.size() > MAX_PUBLIC_KEY_SIZE) {
            keyId2PublicKey.clear();
        }
        for (int i = 0; i < keys.size(); i++) {
            String kid = keys.getJSONObject(i).getString("kid");
            keyId2PublicKey.put(kid, getRsaPublicKeyByJwk(keys.getJSONObject(i)));
        }
        return keyId2PublicKey.get(keyId);
    }

    /**
     * get jwks from the https://hnoauth-login.cloud.honor.com/oauth2/v3/certs endpoint
     * because the jwk update each day, please cache the jwk
     * See more about JWK in http://self-issued.info/docs/draft-ietf-jose-json-web-key.html
     * the example of jwks as follows:
     * {
     *   "keys": [
     *    {
     *      "kty": "RSA",
     *      "e": "AQAB",
     *      "use": "sig",
     *      "kid": "5c3bfcb2dc234c2157ef996280c0b45a2f46fce4cdb422911a6c73c371f8d08e",
     *      "alg": "RS256",
     *      "n": "AKyQNCv271GO5AUeu_RVLz-Tbp3Yh5oszTP_H-2uySlHD8KMWClOm_ZesYiT_rUclXDwCX7djKdQysbFPUAd6dMYHrNRzYIN_gV4d7cJsM0SAeechC_jEm6Yl__lpoa8sJ2BNywxzu9hnjOnZ3TfPKVtU5TP2jmqSJtxYYnVmCF35uyNmv5sFZhMIUiyw81tUBbSssusl5O4RXxH00hXin-BMOc27qOjFQn5iX_Nh_7pxXshdJqfr7tP2g2XydreriJ-TCQTa4ZB8Hqhl3GHSTP05MCyewWRMUuNSTEg5-D5Q0BRXrzMiCA254jHdIe3-c4fDjDL1bOGzGrjAWHLqDM"
     *    },
     *    {
     *      "kty": "RSA",
     *      "e": "AQAB",
     *      "use": "sig",
     *      "kid": "71cae09d797c1d1f73536649662ee4173c20f002af3ddfca438b6d144476bec5",
     *      "alg": "RS256",
     *      "n": "AMby-19OcRu7Sbe6hImlHPznmJ4mgg8ZzFT847yIS4e17Hw1RspKubWK2rR49T7Bg6edgrwynM7eHhLgba40nvwahcQF-QW6JOjdr9KaIbzYVVnSvDK3f0zuE6xR5WrjcC0PTavbjU0qX-9AcoZjgbKdj_l6vdGHprI0eEjCv8PbmFd_oQtdJjS91OgwuywoEO0yoRYvVsqO7gXjXwBv043xob9BTvDqNDOHd5namkU1jxuJWgzlfAXoRZdgT7ng-WByfaOHDNHwSqU_3DLUiPe3joNJur6smU47reffRYUQxX8AC_xuCZIOtNRJ1d1D0PL04iGBFjQFswa2hn1XZ9s"
     *    }
     *  ]
     * }
     */
    private static JSONArray getJwks() {
        HttpGet httpGet = new HttpGet(CERT_URL);

        CloseableHttpClient httpClient = HttpClientUtil.getClient();

        try( CloseableHttpClient client = HttpClientUtil.getClient();
             CloseableHttpResponse response = client.execute(httpGet)) {

            HttpEntity entity = response.getEntity();

            String result = EntityUtils.toString(entity, DEFAULT_CHARSET);
            return JSONObject.parseObject(result).getJSONArray("keys");
        } catch (Exception e) {
            return null;
        } finally {
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (Exception e) {
                    return null;
                }
            }
        }
    }

    /**
     * get RsaPublicKey from a JWK
     *
     * @param jwkObject jwk
     * @return RSAPublicKey
     * @throws InvalidPublicKeyException
     */
    private static RSAPublicKey getRsaPublicKeyByJwk(JSONObject jwkObject) throws InvalidPublicKeyException {
        Map<String, Object> additionalAttributes = new HashMap<>();
        additionalAttributes.put("n", jwkObject.getString("n"));
        additionalAttributes.put("e", jwkObject.getString("e"));
        List<String> operations = new ArrayList<String>();
        Jwk jwk = new Jwk(
                jwkObject.getString("kid"),
                jwkObject.getString("kty"),
                jwkObject.getString("alg"),
                jwkObject.getString("use"),
                operations,
                null,
                null,
                null,
                additionalAttributes);
        return (RSAPublicKey) jwk.getPublicKey();
    }
}
