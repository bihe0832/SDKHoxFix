# Android-HoxFix-SDK-Native-Java（Android 上SDK的代码热更方案）

## 更新记录

2016-04-29：完成demo代码和文档大纲。
2016-10-26：完成项目介绍、Demo运行方式优化。

**由于近期工作的原因，暂时先列一个文档大纲，后续尽快补齐。**

## 项目介绍

继插件化后，热补丁技术在2015年开始爆发，目前已经是非常热门的Android开发技术。关于热修复以及这些方案的优缺点，之前微信团队已经分享过，而且分析的很好，因此这里就不重点介绍了。感兴趣的同学可以点击了解：[微信Android热补丁实践演进之路](http://mp.weixin.qq.com/s?__biz=MzAwNDY1ODY2OQ==&mid=2649286306&idx=1&sn=d6b2865e033a99de60b2d4314c6e0a25&mpshare=1&scene=1&srcid=10266hBPguvWvTgHybtNDiCy#rd)

目前的热修复方案中比较著名的有淘宝的Dexposed、支付宝的AndFix以及Qzone的multidex和微信的tinker。然而这些热修复方案基本上都有以下两个特点：

1. 都是APP级别的的热修复解决方案，对于SDK的开发者来说很多地方需要一些调整
2. 在理解、开发上成本都比较高；而且很多更多的是方案的推广。

因此为了解决SDK的热更新方案，也为了方便大家了解Android热更新，因此开发这个项目。

### 一些说明

**该项目主要是提供给SDK的开发者使用，提供了SDK开发者如何实现SDK自身热更新（包括Java代码和Native），如果是APP的开发者了解应用的热更新，建议参考dodola的HotFix项目，里面介绍的更全面。**


**为了降低项目的理解难度，关于java热更新，该项目暂时不会涉及怎么在代码中插桩、怎么生成版本差异包、怎么将差异包编为dex文件等内容，这部分内容我会在另外的项目来介绍。**

**为了降低项目的理解难度，关于so的热更新，该项目暂时仅适用arm的so，关于如何在热更时根据so的类型来选择下发什么类型的so，请参考作者之前的文章 [SDK热更之如何获取应用在当前设备上的so对应的指令集
](http://blog.bihe0832.com/SDK_hotfix_so_abi.html)。**

该项目是基于QQ空间终端开发团队的技术文章实现的，然后补充了Native的So的动态加载相关的内容。文章地址：[安卓App热补丁动态修复技术介绍](http://zhuanlan.zhihu.com/magilu/20308548)

项目代码前期有参考dodola的HotFix项目，项目地址为：[https://github.com/dodola/HotFix](https://github.com/dodola/HotFix)

## 体验Demo

#### 直接下载体验：
	
- [点击下载](http://blog.bihe0832.com/public/resource/Hotfix-debug.apk)
	
- 扫码下载APK：
	
![Demo下载二维码](http://blog.bihe0832.com/public/images/gradle-test-hotfix-apk-download.png)

#### 运行项目体验：

1. 生成支持热更的SDK

	- 修改MD5下local.properies中的ndk.dir和sdk.dir的环境配置
	- 修改MD5下gradle/wrapper/gradle-wrapper.properties 关于使用的gradle版本的地址的修改
	- 修改MD5下build.gradle中对于使用的maven库的声明
	- 修改MD5下HotFixConsts中关于热更补丁下载地址的配置：`PATCH_DOWNLOAD_URL`，建议其余配置不要修改
	- 在整个根目录执行命令 `/bin/bash ./build.sh md5`
	- 执行命令结束以后，根目录bin目录会生成如下文件
	
			└── MD5
			    │
			    ├── bihe0832MD5.jar ：支持热更的SDK的版本
			    │
			    ├── bihe0832MD5_old.jar ：SDK编译生成的原始jar
			    │
			    ├── armeabi
			    │		│
			    │		└─── libbihe0832MD5.so ：arm指令集下的so文件
			    │
			    └── …… 其余so文件，因为我们仅关注arm，因此忽略其余的指令集


2. 生成集成了支持热更的SDK的apk

	- 修改GradleTest下local.properies中的ndk.dir和sdk.dir的环境配置
	- 修改GradleTest下gradle/wrapper/gradle-wrapper.properties 关于使用的gradle版本的地址的修改
	- 修改GradleTest下build.gradle中对于使用的maven库的声明
	- 拷贝步骤1生成的支持热更的SDK的jar文件bihe0832MD5.jar到GradleTest项目下app目录中的libs目录下
	- 拷贝步骤1生成的armeabi文件夹到GradleTest项目下app目录的src/main/jniLibs下
	- 运行项目，生成apk文件

3. 生成热更使用的补丁包

	- 修改MD5项目目录下FixInfo中的VERSION_NAME、VERSION_CODE、测试热更的bug函数
	- 修改MD5项目目录下MD5文件中的getLowerMD5函数的bug
	- 修改MD5项目目录下com_bihe0832_md5_MD5.cpp中的VERSION
	- 在整个根目录执行命令 `/bin/bash ./build.sh patch`
	- 
4. 配置补丁
5. 验证热更

### Demo相关说明：为了方便XXX，只使用armso

## 代码介绍

### 工程介绍

	Android-HoxFix-SDK-Native-Java
		│
		├─── SDKPatch SDKpatch生成的插件工程
		│
		├─── DemoRes 根据项目代码生成的jar和so的更新包以及热更相关的资源，开发者自己搭建环境体验demo功能时可以直接使用
		│
		├─── MD5 SDK项目的工程，最终打包后对外提供jar包和so，本项目主要也是介绍他的热更新
		│
		├─── GradleTest 普通的Android应用工程，调用了SDK提供的相关函数，也就是SDK的使用者
		|
		├─── build.sh 生成SDK的jar和so，生成Demo Apk的自动构建脚本
		│
	   	└─── README.md 项目介绍

### 目录结构

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
