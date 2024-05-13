# 荣耀账号示例代码

## 简介

```
方便了解荣耀账号的授权登录，提供了荣耀账号示例程序供您参考或使用，荣耀账号示例包括安卓端Java示例代码和服务端REST示例代码，主要介绍荣耀账号是如何进行各种授权登录和服务端的接口解析，安卓端java示例代码实现了静默登录、授权登录、取消登录和增量授权功能，服务的REST示例代码主要解析接口的主要方法和参数的传递与响应。
```



## 接口说明

### 安卓端

| Package Name                                                 | Description                                  |
| ------------------------------------------------------------ | -------------------------------------------- |
| [com.hihonor.cloudservice.support.account](https://developer.honor.com/cn/kitdoc?category=基础服务&kitId=11001&navigation=ref&docId=com.hihonor.cloudservice.support.account/HonorIdSignInManager.md) | 提供账号登录授权服务接口                     |
| [com.hihonor.cloudservice.support.account.service](https://developer.honor.com/cn/kitdoc?category=基础服务&kitId=11001&navigation=ref&docId=com.hihonor.cloudservice.support.account.service/HonorIDSignInService.md) | 包含静默登录, 取消授权, 获取SignInIntent对象 |
| [com.hihonor.cloudservice.support.account.request](https://developer.honor.com/cn/kitdoc?category=基础服务&kitId=11001&navigation=ref&docId=com.hihonor.cloudservice.support.account.request/SignInOptions.md) | 账号登录授权服务请求对象                     |
| [com.hihonor.cloudservice.support.account.result](https://developer.honor.com/cn/kitdoc?category=基础服务&kitId=11001&navigation=ref&docId=com.hihonor.cloudservice.support.account.result/SignInAccountInfo.md) | 包含账号服务的登录类                         |

### 服务端

| 类                                         | 实现接口                         | 描述                            |
| ------------------------------------------ | -------------------------------- | ------------------------------- |
| com.hihonor.honorid.demo.AtParserDemo      | hihonor.oauth2.user.getTokenInfo | 解析 Access Token               |
| com.hihonor.honorid.demo.Code2AtDemo       | /oauth2/v3/token                 | Code 换 Access Token            |
| com.hihonor.honorid.demo.GetInfoDemo       | GOpen.User.getInfo               | Access Token 获取用户信息       |
| com.hihonor.honorid.demo.GetServerAtDemo   | /oauth2/v3/token                 | 获取应用级 Access Token         |
| com.hihonor.honorid.demo.IDTokenParserDemo | -                                | 本地解析 ID Token               |
| com.hihonor.honorid.demo.Rt2AtDemo         | /oauth2/v3/token                 | Refresh Token 刷新 Access Token |

## 流程图

![image](image/image.png)

