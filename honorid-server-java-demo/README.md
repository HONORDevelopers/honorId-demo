# Honor ID Server REST Sample Code

[![Apache-2.0](https://img.shields.io/badge/license-Apache-blue)](http://www.apache.org/licenses/LICENSE-2.0)
[![Open Source Love](https://img.shields.io/static/v1?label=Open%20Source&message=%E2%9D%A4%EF%B8%8F&color=green)]()
[![Java Language](https://img.shields.io/badge/language-java-green.svg)](https://www.java.com/en/)

English| [中文](READNE.MD)



## Contents

- [Introduction](#Introduction)
- [Environment](#Environment)
- [Configuration](#Configuration)
- [Install](#Install)

- [Sample code](#Sample code)
- [Technical support](#Technical support)
- [License](#License)



## Introduction

The REST sample code Honor ID Service has been encapsulated and includes a rich sample program for your reference or direct use. Code2AtDemo code obtains AT, AtParserDemo parses AT, GetInfoDemo token method obtains user information, GetServerAtDemo obtains application-level token, IDTokenParserDemo parses id_token, Rt2AtDemo refreshes token.



## Environment

JDK version 1.8 and above.

Computers with Windows 10/Windows 7 operating system installed (desktop or laptop)



## Configuration

| parameter      | describe                                                     |
| -------------- | ------------------------------------------------------------ |
| client_id      | App ID, access the [HONOR Developers](https://developer.honor.com/) to apply, you need to replace the client id in the example with your own. |
| client_secret  | App Secret, access the [HONOR Developers](https://developer.honor.com/) to apply, you need to replace the client id in the example with your own. |
| tokenUrl       | The request address for obtaining the access token, https://hnoauth-login.cloud.honor.com/oauth2/v3/token |
| getInfoUrl     | The request address for obtaining user information,, https://account-drcn.platform.hihonorcloud.com/rest.php?nsp_svc=GOpen.User.getInfo |
| parseTokenUrl  | The request address for parsing the access token, https://hnoauth-login.cloud.honor.com/rest.php?nsp_svc=hihonor.oauth2.user.getTokenInfo |
| CERT_URL       | The request address for obtaining the public key of parsing ID Token, https://hnoauth-login.cloud.honor.com/oauth2/v3/certs |
| ID_TOKEN_ISSUE | The value of the ISS field in the ID Token, https://honorid.hihonor.com |



## Install

Download the demo, configure and download the jar package required by maven, and can compile these demos normally.



## Sample code

The Java sample code for the server of Honor ID Service provides the following usage scenarios:

1. In [GetServerAtDemo.java](./src/main/java/com/hihonor/honorid/demo/GetServerAtDemo.java) , obtain the application level Access Token，which is used for credentials in application-level interfaces.
2. In [Code2AtDemo.java](./src/main/java/com/hihonor/honorid/demo/Code2AtDemo.java) , use Code to exchange for Access Token, obtain Access Token, and Refresh Token. 
3. In [Rt2AtDemo.java](./src/main/java/com/hihonor/honorid/demo/Rt2AtDemo.java) , obtain new Access Token and new id_token by Refresh Token.
4. In [AtParserDemo.java](./src/main/java/com/hihonor/honorid/demo/AtParserDemo.java) , parse Access Token and obtain open_id, scope, union_id, expire_in, client_id.
5. In [IDTokenParserDemo.java](./src/main/java/com/hihonor/honorid/demo/IDTokenParserDemo.java) , verify ID Token locally and obtain union_id, given_name, display_name, picture, appid, email.
6. In [GetInfoDemo.java](./src/main/java/com/hihonor/honorid/demo/GetInfoDemo.java), obtain open_id, avatar, nickname, email, mobile phone number,  country code by using Access Token.



## Technical support

You can get the latest information about the Honor ID REST sample code in the  [HONOR Developers Community](https://developer.honor.com/cn/forum/?navation=dh11614886576872095748%2F1) and exchange insights with other developers.

If you have questions about using this sample code, please go to the [HONOR Developers Community](https://developer.honor.com/cn/forum/?navation=dh11614886576872095748%2F1)  for more opinions and suggestions.



## License

The Honor ID Service sample code has been licensed by [Apache 2.0](http://www.apache.org/licenses/LICENSE-2.0).

