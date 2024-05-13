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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * This is a demo for get access token by refresh token.
 */
public class Rt2AtDemo {

    public static void main(String[] args) throws IOException {

        String tokenUrl = "https://hnoauth-login.cloud.honor.com/oauth2/v3/token";

        String grantType = "refresh_token";

        // replace with your client_id
        String clientId = "";

        // replace with your client_secret
        String clientSecret = "";

        // Because the request body used in this example is UrlEncodedFormEntity,
        // the request parameters will be automatically urlencoded,
        // so the parameters do not need to be urlencoded separately;
        // Please replace this refreshToken with yours.
        String refreshToken = "CgB6e3x9jobYuEj0SyXK5QbCM/uu44F1kA9eZCB2BRrEYlokRYVHnKcMq/D2lZIdRnvnP7p26+RdVuwJknwkor";

        // You need to decode it first, if the refreshToken parameter is urlencoded.
        // refreshToken = java.net.URLDecoder.decode(refreshToken,   "utf-8");

        JSONObject tokens = getAtByRt(tokenUrl, grantType, clientId, clientSecret, refreshToken);
        System.out.println(tokens.toJSONString());
    }

    /**
     *  get access token by refresh token.
     *
     * @param tokenUrl /oauth2/v3/token
     * @param grantType refresh_token
     * @param clientId client_id
     * @param clientSecret client_secret
     * @param refreshToken refresh_token
     * @return JSONObject
     * @throws IOException
     */
    private static JSONObject getAtByRt(String tokenUrl, String grantType, String clientId, String clientSecret,
                                             String refreshToken) throws IOException {
        HttpPost httpPost = new HttpPost(tokenUrl);

        List<BasicNameValuePair> request = new ArrayList<>();
        request.add(new BasicNameValuePair("grant_type", grantType));
        request.add(new BasicNameValuePair("client_id", clientId));
        request.add(new BasicNameValuePair("client_secret", clientSecret));
        request.add(new BasicNameValuePair("refresh_token", refreshToken));
        httpPost.setEntity(new UrlEncodedFormEntity(request));

        try (CloseableHttpClient client = HttpClientUtil.getClient();
             CloseableHttpResponse response = client.execute(httpPost)) {
            HttpEntity responseEntity = response.getEntity();

            // access_token and id_token are base64 encoded and may have escaping characters, such as: \/
            // if you use json parsing tools such as fastjson or jackson, it will be automatically escaped.
            String ret = responseEntity != null ? EntityUtils.toString(responseEntity) : null;
            JSONObject jsonObject = (JSONObject) JSON.parse(ret);
            EntityUtils.consume(responseEntity);
            return jsonObject;
        }
    }
}
