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
 * This is a demo for get server access token.
 */
public class GetServerAtDemo {

    public static void main(String[] args) throws IOException {

        // NONOR ID OAuth adress for interface "/oauth2/v3/token"
        String tokenUrl = "https://hnoauth-login.cloud.honor.com/oauth2/v3/token";

        // fixed value for get server at interface: "client_credentials"
        String grant_type = "client_credentials";

        // replace with your client_id
        String client_id = "";

        // replace with your client_secret
        String client_secret = "";

        // get At by code
        JSONObject tokens = getServerAt(tokenUrl, client_id, client_secret, grant_type);

        System.out.println(tokens.toJSONString());
    }

    /**
     * get access token by code
     *
     * @param tokenUrl token url
     * @param client_secret client_secret
     * @param client_id client_id
     * @param grant_type grant_type
     * @return JSONObject
     * @throws IOException
     */
    private static JSONObject getServerAt(String tokenUrl, String client_id,
                                          String client_secret, String grant_type) throws IOException {
        HttpPost httpPost = new HttpPost(tokenUrl);
        List<BasicNameValuePair> request = new ArrayList<>();
        request.add(new BasicNameValuePair("client_id", client_id));
        request.add(new BasicNameValuePair("client_secret", client_secret));
        request.add(new BasicNameValuePair("grant_type", grant_type));
        httpPost.setEntity(new UrlEncodedFormEntity(request));

        try (CloseableHttpClient client = HttpClientUtil.getClient();
             CloseableHttpResponse response = client.execute(httpPost)) {

            HttpEntity responseEntity = response.getEntity();

            // access_token are base64 encoded and may have escaping characters, such as: \/
            // if you use json parsing tools such as fastjson or jackson, it will be automatically escaped.
            String ret = responseEntity != null ? EntityUtils.toString(responseEntity) : null;
            JSONObject jsonObject = (JSONObject) JSON.parse(ret);
            EntityUtils.consume(responseEntity);
            return jsonObject;
        }
    }
}
