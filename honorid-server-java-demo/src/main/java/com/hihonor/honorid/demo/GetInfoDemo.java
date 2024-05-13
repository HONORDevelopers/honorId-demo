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
 * This is a demo for get userinfo by access token.
 */
public class GetInfoDemo {

    public static void main(String[] args) throws IOException {

        String getInfoUrl = "https://account-drcn.platform.hihonorcloud.com/rest.php?nsp_svc=GOpen.User.getInfo";

        // Whether to return nickname first. "0": No (default); "1": Yes. Yes is recommended.
        String getNickName = "1";

        // Because the request body used in this example is UrlEncodedFormEntity,
        // the request parameters will be automatically urlencoded,
        // so the parameters do not need to be urlencoded separately;
        // Please replace this accessToken with yours.
        String accessToken = "CgB6e3x9LNBGr/LuJ77Infde0L4ZQPLn3ijp/3JA2gMM+sy5Z+u+JtZU6ZYVP4t6wjep8QnOgoEyL056WmWakI";

        // You need to decode it first, if the accessToken parameter is urlencoded.
        // accessToken = java.net.URLDecoder.decode(accessToken, "utf-8");

        JSONObject userInfo = getInfoByAt(getInfoUrl, accessToken, getNickName);

        System.out.println(userInfo.toJSONString());
    }

    private static JSONObject getInfoByAt(String getInfoUrl, String accessToken, String getNickName) throws IOException {
        HttpPost httpPost = new HttpPost(getInfoUrl);
        List<BasicNameValuePair> request = new ArrayList<>();
        request.add(new BasicNameValuePair("access_token", accessToken));
        request.add(new BasicNameValuePair("getNickName", getNickName));
        httpPost.setEntity(new UrlEncodedFormEntity(request));

        try (CloseableHttpClient client = HttpClientUtil.getClient();
             CloseableHttpResponse response = client.execute(httpPost)) {
            HttpEntity responseEntity = response.getEntity();

            String ret = responseEntity != null ? EntityUtils.toString(responseEntity) : null;
            JSONObject jsonObject = (JSONObject) JSON.parse(ret);
            EntityUtils.consume(responseEntity);
            return jsonObject;
        }
    }
}
