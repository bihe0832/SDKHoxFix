# Android-HoxFix-SDK-Native-Java（Android SDK代码热更方案）

## 更新记录

2016-04-29：完成demo代码和文档大纲。

**由于近期工作的原因，暂时先列一个文档大纲，后续尽快补齐。**

## 项目介绍

项目代码前期有参考dodola的HotFix项目，项目地址为：[https://github.com/dodola/HotFix](https://github.com/dodola/HotFix)

该项目同样是基于QQ空间终端开发团队的技术文章实现的，然后补充了Native的So的动态加载相关的内容。文章地址：[安卓App热补丁动态修复技术介绍](http://zhuanlan.zhihu.com/magilu/20308548)

**该项目主要是提供给SDK的开发者使用，提供了第三方SDK如何实现自身热更新（包括Java代码和Native），如果是APP的开发者了解应用的热更新，建议参考dodola的HotFix项目，里面介绍的更全面。**


## 体验Demo


备注：**目前该热更的Demo下载更新时下载的so为ARM-v8的CPU类型的so**，如果因为机型不支持，建议通过adb push方式把对应CPU的so Push到应用私有目录的files目录下（不同CPU的so保存在根目录的DemoRes下）。或者直接修改源码中的下载地址，下载自己服务器的更新。

#### Demo 下载：
	
- [点击下载](http://blog.bihe0832.com/public/resource/Hotfix-debug.apk)
	
- 扫码下载APK：
	
![Demo下载二维码](http://blog.bihe0832.com/public/images/gradle-test-hotfix-apk-download.png)

## 代码介绍

### 目录结构


	Android-HoxFix-SDK-Native-Java
		│
		├─── MD5 SDK工程，最终打包后对外提供jar包和so，本项目主要也是介绍他的热更新
		|
		├─── build.sh 生成SDK的jar和so，生成Demo Apk的自动构建脚本
		│
		├─── GradleTest 普通的Android应用工程，调用了SDK提供的相关函数
		│
		├─── DemoRes 根据项目代码生成的jar和so的更新包以及热更相关的资源，体验demo功能时可以直接使用
		│
	   	└─── README.md 项目介绍

### 工程介绍

待补充

### 接口调用流程

待补充

## 项目运行（DIY）

### 1. 关键文件介绍

#### 默认patch

#### 补丁patch

### 2. 默认patch生成

### 3. 修改补丁相关配置并生成SDK 

### 4. 集成SDK到APP工程生成APK

### 5. 生成补丁包

### 6. 发布补丁包

### 6. 验证补丁包


## 待优化点

### 关于安全
	
- 热更内容下发
- 加载热更新前没有验证有效性
- 没有做本地代码防篡改的策略
- 没有做版本匹配
- 没有odex校验

### 关于运行项目

- 目前Fix类手动添加，没有用工具
- SDK的生成只能用命令行，因为分包是用的shell完成
- 下载更新直接在主线程，而且没有回调

### 关于SO的更新

- 目前的demo只支持arm-v8的CPU添加CPU类型
- 热更怎么选择配型的 so
- 热更和版本的匹配怎么维护
