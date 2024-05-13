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
package com.hihonor.iddemo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hihonor.cloudservice.common.ApiException;
import com.hihonor.cloudservice.support.account.HonorIdSignInManager;
import com.hihonor.cloudservice.support.account.request.SignInOptionBuilder;
import com.hihonor.cloudservice.support.account.request.SignInOptions;
import com.hihonor.cloudservice.support.account.result.SignInAccountInfo;
import com.hihonor.cloudservice.support.api.entity.auth.Scope;
import com.hihonor.cloudservice.tasks.OnFailureListener;
import com.hihonor.cloudservice.tasks.OnSuccessListener;
import com.hihonor.cloudservice.tasks.Task;
import com.hihonor.honorid.core.helper.handler.ErrorStatus;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity_TAG";
    private TextView mTvResult;
    private EditText mEtAppid;
    private SignInAccountInfo mAccount;

    // replace the mClientID with your AppID which applied on HONOR Developers website
    private String mClientID = "12345";

    private static final String SCOPE_OPENID = "openid";
    private static final String SCOPE_PROFILE = "profile";
    public static final String SCOPE_EMAIL = "email";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTvResult = findViewById(R.id.tv_result);
        mEtAppid = findViewById(R.id.et_appid);
        mEtAppid.setText(mClientID);
        mEtAppid.setEnabled(false);
    }

    private String getAppid() {
        return mEtAppid.getText().toString().trim();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (1001 == requestCode) {
            // parse auth result of the callback after authorization
            Task<SignInAccountInfo> accountTask = HonorIdSignInManager.parseAuthResultFromIntent(resultCode, data);
            if (accountTask.isSuccessful()) {
                SignInAccountInfo signInAccountInfo = accountTask.getResult();
                mAccount = signInAccountInfo;
                logPrintln(signInAccountInfo);
            } else {
                Exception exception = accountTask.getException();
                if (exception instanceof ApiException) {
                    ApiException apiException = (ApiException) exception;
                    logPrintlnToMain("errCode : " + apiException.getStatusCode() + " , errMsg = " + apiException.getMessage());
                }
            }
        }
    }

    /**
     * Whether to log in
     *
     * @param view
     */
    public void isLogin(View view) {
        boolean login = HonorIdSignInManager.isLogin(this);
        logPrintlnToMain("isLogin ："+login);
    }

    /**
     * jump to authorization page
     *
     * @param view
     */
    public void jumpAuthorization(View view) {
        String appid = getAppid();
        if (TextUtils.isEmpty(appid)) {
            Toast.makeText(this, "AppID cannot be null!", Toast.LENGTH_SHORT).show();
            return;
        }
        SignInOptions signInOptions = new SignInOptionBuilder(SignInOptions.DEFAULT_AUTH_REQUEST_PARAM)
                .setClientId(appid)
                .createParams();
        Intent signInIntent = HonorIdSignInManager.getService(this, signInOptions).getSignInIntent();
        if (null == signInIntent) {
            logPrintlnToMain("Honor version too low");
            return;
        }
        startActivityFromChild(this, signInIntent, 1001);
    }

    /**
     * cancel authorization
     *
     * @param view
     */
    public void cancelAuthorization(View view) {
        Log.e(TAG, "cancelAuthorization");
        if (null == mAccount) {
            Toast.makeText(this, "Account cannot be null!", Toast.LENGTH_SHORT).show();
            return;
        }
        String appid = getAppid();
        if (TextUtils.isEmpty(appid)) {
            Toast.makeText(this, "AppID cannot be null!", Toast.LENGTH_SHORT).show();
            return;
        }
        SignInOptions signInOptions = new SignInOptionBuilder()
                .setClientId(appid)
                .createParams();

        HonorIdSignInManager.getService(this, signInOptions)
                .cancelAuthorization()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "cancelAuthorization Success");
                        logPrintlnToMain("cancelAuthorization Success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.e(TAG, "cancelAuthorization fail");
                        logPrintlnToMain("cancelAuthorization fail：" + e.toString());
                    }
                });
    }

    /**
     * silent sign in
     *
     * @param view
     */
    public void silentSignIn(View view) {
        String appid = getAppid();
        if (TextUtils.isEmpty(appid)) {
            Toast.makeText(this, "AppID cannot be null!", Toast.LENGTH_SHORT).show();
            return;
        }
        SignInOptions signInOptions = new SignInOptionBuilder(SignInOptions.DEFAULT_AUTH_REQUEST_PARAM)
                .setClientId(appid)
                .createParams();

        HonorIdSignInManager.getService(MainActivity.this, signInOptions)
                .silentSignIn()
                .addOnSuccessListener(new OnSuccessListener<SignInAccountInfo>() {
                    @Override
                    public void onSuccess(SignInAccountInfo signInAccountInfo) {
                        mAccount = signInAccountInfo;
                        logPrintln(signInAccountInfo);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        ApiException exception = (ApiException) e;
                        if ((exception.getStatusCode() == ErrorStatus.ERROR_SCOPES_NOT_AUTHORIZE)
                                || exception.getStatusCode() == ErrorStatus.ACCOUNT_NON_LOGIN) {
                            // If the silentSignIn interface return "55: scopes not authorize"
                            // or "31: Account hasnot login", then jump to the authorization page;
                            jumpAuthorization(null);
                            return;
                        }
                        String message = exception.getMessage();
                        Log.e(TAG, "error : " + message);
                        logPrintlnToMain(message);
                    }
                });
    }

    /**
     * add auth scopes
     *
     * @param view
     */
    public void addAuthScopes(View view) {
        String appid = getAppid();
        if (TextUtils.isEmpty(appid)) {
            Toast.makeText(this, "AppID cannot be null!", Toast.LENGTH_SHORT).show();
            return;
        }
        // the scope will be added
        ArrayList<Scope> scopes = new ArrayList<>();
        scopes.add(new Scope(SCOPE_EMAIL));
        SignInOptions accountAuthParams = new SignInOptionBuilder(SignInOptions.DEFAULT_AUTH_REQUEST_PARAM)
                .setClientId(appid)
                .setScopeList(scopes)
                .createParams();

        HonorIdSignInManager.addAuthScopes(this, 1001, accountAuthParams);
    }

    /**
     * print log
     *
     * @param log
     */
    public void logPrintlnToMain(String log) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTvResult.setText(log);
            }
        });
    }

    /**
     * print signInAccountInfo
     *
     * @param signInAccountInfo
     */
    private void logPrintln(SignInAccountInfo signInAccountInfo) {
        if (null == signInAccountInfo) {
            Log.e(TAG, "logPrintln : signInAccountInfo is null");
            return;
        }
        StringBuffer buffer = new StringBuffer()
                .append("openId : ").append(signInAccountInfo.getOpenId()).append("\n").append("\n")
                .append("authorizationCode : ").append(signInAccountInfo.getAuthorizationCode()).append("\n").append("\n")
                .append("unionId : ").append(signInAccountInfo.getUnionId()).append("\n").append("\n")
                .append("ID Token : ").append(signInAccountInfo.getIdToken()).append("\n").append("\n");

        Log.d(TAG, buffer.toString());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTvResult.setText(buffer.toString());
            }
        });
    }
}