# 荣耀服务端REST示例代码

[![Apache-2.0](https://img.shields.io/badge/license-Apache-blue)](http://www.apache.org/licenses/LICENSE-2.0)
[![Open Source Love](https://img.shields.io/static/v1?label=Open%20Source&message=%E2%9D%A4%EF%B8%8F&color=green)]()
[![Java Language](https://img.shields.io/badge/language-java-green.svg)](https://www.java.com/en/)

[English](README.md) | 中文



## 目录

- [简介](#简介)
- [环境要求](#环境要求)
- [配置](#配置)
- [安装](#安装)
- [示例代码](#示例代码)
- [技术支持](#技术支持)
- [授权许可](#授权许可)



## 简介

REST示例代码荣耀账号服务进行了封装，包含丰富的示例程序，方便您参考或直接使用。Code2AtDemo代码获取AT,AtParserDemo解析用户的AT,GetInfoDemo令牌方式获取用户信息，GetServerAtDemo服务器获取令牌，IDTokenParserDemo解析令牌，Rt2AtDemo刷新令牌。



## 环境要求

JDK 1.8及以上版本。

安装有Windows 10/Windows 7操作系统的计算机（台式机或者笔记本）



## 配置

| 参数           | 描述                                                         |
| -------------- | ------------------------------------------------------------ |
| client_id      | App ID, 需要在[荣耀开发者服务平台](https://developer.honor.com/)申请, 您需要将示例中的client_id替换成您自己应用的 |
| client_secret  | App SECRET, 需要在[荣耀开发者服务平台](https://developer.honor.com/)申请, 您需要将示例中的client_secret替换成您自己应用的 |
| tokenUrl       | 获取access token的请求地址, https://hnoauth-login.cloud.honor.com/oauth2/v3/token |
| getInfoUrl     | 获取用户信息的请求地址, https://account-drcn.platform.hihonorcloud.com/rest.php?nsp_svc=GOpen.User.getInfo |
| parseTokenUrl  | 解析access token的请求地址, https://hnoauth-login.cloud.honor.com/rest.php?nsp_svc=hihonor.oauth2.user.getTokenInfo |
| CERT_URL       | 获取解ID Token的公钥的地址, https://hnoauth-login.cloud.honor.com/oauth2/v3/certs |
| ID_TOKEN_ISSUE | ID Token中ISS字段的值, https://honorid.hihonor.com           |



## 安装

下载demo，配置并下载maven所需要的jar包，能在编译器正常编译启动



## 示例代码

荣耀账号服务的服务端Java示例代码提供了以下使用场景：

1. 在  [GetServerAtDemo.java](./src/main/java/com/hihonor/honorid/demo/GetServerAtDemo.java) 中，获取应用级Access Token，用于应用级接口调用凭证。
2. 在 [Code2AtDemo.java](./src/main/java/com/hihonor/honorid/demo/Code2AtDemo.java) 中，使用[Code换取Access Token](https://developer.honor.com/cn/kitdoc?category=基础服务&kitId=11001&navigation=ref&docId=web-code2at.md&token=), 获取Access Token, Refresh Token凭证。
3. 在 [Rt2AtDemo.java](./src/main/java/com/hihonor/honorid/demo/Rt2AtDemo.java) 中, 使用 [RefreshToken刷新AccessToken](https://developer.honor.com/cn/kitdoc?category=%E5%9F%BA%E7%A1%80%E6%9C%8D%E5%8A%A1&kitId=11001&navigation=ref&docId=web-rt2at.md&token=),  获取新的Access Token, id_token等信息。
4. 在 [AtParserDemo.java](./src/main/java/com/hihonor/honorid/demo/AtParserDemo.java) 中，[解析Access Token](https://developer.honor.com/cn/kitdoc?category=基础服务&kitId=11001&navigation=ref&docId=web-parse-at.md&token=)，获取 open_id, scope , union_id, expire_in, client_id 信息。
5. 在 [IDTokenParserDemo.java](./src/main/java/com/hihonor/honorid/demo/IDTokenParserDemo.java) 中, [本地校验ID Token](https://developer.honor.com/cn/kitdoc?category=%E5%9F%BA%E7%A1%80%E6%9C%8D%E5%8A%A1&kitId=11001&navigation=ref&docId=web-parse-idtoken-local.md&token=), 获取 union_id, given_name, display_name, picture, appid, email等信息。
6. 在 [GetInfoDemo.java](./src/main/java/com/hihonor/honorid/demo/GetInfoDemo.java) 中,  使用[Access Token获取用户信息](https://developer.honor.com/cn/kitdoc?category=%E5%9F%BA%E7%A1%80%E6%9C%8D%E5%8A%A1&kitId=11001&navigation=ref&docId=web-get-userinfo.md&token=), 获取open_id, 头像, 昵称, 邮箱, 手机号, 注册国家码等信息。



## 技术支持

您可在[荣耀开发者社区](https://developer.honor.com/cn/forum/?navation=dh11614886576872095748%2F1) 获取关于账号REST示例代码最新讯息，并与其他开发者交流见解。

如果您对使用该示例代码有疑问，请前往[荣耀开发者社区](https://developer.honor.com/cn/forum/?navation=dh11614886576872095748%2F1)，获取更多意见和建议。

​       

## 授权许可

荣耀账号服务示例代码经过[Apache 2.0授权许可](http://www.apache.org/licenses/LICENSE-2.0)。