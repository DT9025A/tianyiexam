# 天一联考查分客户端 (Android)
---
## 写在前面
> 这个是通过解析网页内容来实现数据提取的。不得不说天一的网站做的真的牛啤，连json都不给，直接把数据嵌入页面，你甚至可以在网页源码里找到大段的缩进和注释。而且天一的服务器还天天崩溃，服气 (迫真)

## 程序功能
- 实现了查分
- 现在文理都可以了
- 考砸了，友尽

## 编写环境 : AIDE & JAVA
API Level Requirement: 14 & later

## 申请权限
1. android.permission.INTERNET    用于发送HTTP请求，获取数据
2. android.permission.ACCESS_NETWORK_STATE    用于获取当前网络状态

## APK签名信息
```
CN=Backspace
OU=LGFZ Dev Group
O=Field Congratulations
L=Luoyang
ST=Henan
C=CN
```

## 使用方法
- 安装APK，API<14 / IOS / WINDOWS 是不能安装的哟
- 点击桌面上的图标
- 点击 "查询" 来获取考试信息
> example: 高三二连(Q)
- 输入考生号 (可与上一步对调,没什么大问题)
- 再次点击 "查询" 进行信息查询
- 捕获 java.io.FileNotFoundException (划掉)
- 查询完成
- 该自闭的自闭，该开心的开心，该膨胀的膨胀
- 退出程序


## 版权信息(?)
- 数据: COPYRIGHT © 1995 - 2018 河南天一文化传播股份有限公司
- 程序: COPYRIGHT © 2016 - 2018 FieldCongratulations Org.
- 开源协议: MIT LICENSE
