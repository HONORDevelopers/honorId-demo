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
 * This is a demo for parse access token.
 */
public class AtParserDemo {

    public static void main(String[] args) throws IOException {

        String parseTokenUrl = "https://hnoauth-login.cloud.honor.com/rest.php?nsp_svc=hihonor.oauth2.user.getTokenInfo";

        // fixed value: OPENID. If you need to get openid, then this parameter is required
        String openId = "OPENID";

        // Because the request body used in this example is UrlEncodedFormEntity,
        // the request parameters will be automatically urlencoded,
        // so the parameters do not need to be urlencoded separately;
        // Please replace this accessToken with yours.
        String accessToken = "CgB6e3x9LNBGr/LuJ77Infde0L4ZQPLn3ijp/3JA2gMM+sy5Z+u+JtZU6ZYVP4t6wjep8QnOgoEyL056WmWakI";

        // You need to decode it first, if the accessToken parameter is urlencoded.
        // accessToken = java.net.URLDecoder.decode(accessToken, "utf-8");

        JSONObject atValue = parseAt(parseTokenUrl, accessToken, openId);

        System.out.println(atValue.toJSONString());
    }

    /**
     * parse access token
     *
     * @param parseTokenUrl hihonor.oauth2.user.getTokenInfo
     * @param accessToken access_token
     * @param openId OPENID
     * @return JSONObject
     * @throws IOException
     */
    private static JSONObject parseAt(String parseTokenUrl, String accessToken, String openId) throws IOException {
        HttpPost httpPost = new HttpPost(parseTokenUrl);

        List<BasicNameValuePair> request = new ArrayList<>();
        request.add(new BasicNameValuePair("access_token", accessToken));
        request.add(new BasicNameValuePair("open_id", openId));
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
