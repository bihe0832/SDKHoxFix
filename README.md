# SDKHotFix

## 关键词

Android-HoxFix-SDK-Native-Java（Android 上SDK的代码热更方案）

**README介绍项目相关的一些核心内容，个人会陆续通过一系列文档来介绍这个项目的实现原理及运行方法的详细内容。如想了解更多细节，请点击[http://blog.bihe0832.com/sdk_hotfix_project.html](http://blog.bihe0832.com/sdk_hotfix_project.html)前往个人博客了解。**

## 一、项目介绍

继插件化后，热补丁技术在2015年开始爆发，目前已经是非常热门的Android开发技术。关于热更新以及这些方案的优缺点，之前微信团队已经分享过，而且分析的很好，因此这里就不重点介绍了。感兴趣的同学可以点击了解：[微信Android热补丁实践演进之路](http://mp.weixin.qq.com/s?__biz=MzAwNDY1ODY2OQ==&mid=2649286306&idx=1&sn=d6b2865e033a99de60b2d4314c6e0a25&mpshare=1&scene=1&srcid=10266hBPguvWvTgHybtNDiCy#rd)

目前的热更新方案中比较著名的有淘宝的Dexposed、支付宝的AndFix以及Qzone的multidex和微信的tinker。然而这些热更新方案基本上都有以下两个特点：

1. 都是APP级别的的热更新解决方案，对于SDK的开发者来说很多地方需要一些调整
2. 在理解、开发上成本都比较高；而且很多更多的是方案的推广。

因此为了解决SDK的热更新方案，也为了方便大家了解Android热更新，因此开发这个项目。


**该项目主要是提供给SDK的开发者使用，提供了SDK开发者如何实现SDK自身热更新（包括Java代码和Native），如果是APP的开发者了解应用的热更新，建议参考dodola的HotFix项目，里面介绍的更全面。**

**为了降低项目的理解难度，关于java热更新，该项目暂时不会涉及怎么管理版本号、同时将怎么在代码中插桩、怎么生成版本差异包、怎么将差异包编为dex文件等内容封装在构建脚本中，这部分内容在体验时可以不用关注。**

**由于本项目重点介绍重点SDK的热更新相关的内容，因此项目中的代码虽然是实现简单的功能，但是使用了SDK和demo等多个项目以及java和Native多层调用。**本项目中不会再介绍SDK相关的内容，建议可以先通过下面的链接了解这个项目的结构，然后再看热更新项目的内容，[点击了解Android-gradle-jni-so](https://github.com/bihe0832/Android-gradle-jni-so)。

## 二、体验Demo

个人博客的SDK热更之Demo体验方法：[http://blog.bihe0832.com/sdk_hotfix_demo.html](http://blog.bihe0832.com/sdk_hotfix_demo.html)中介绍了SDK的热更项目的详细体验方法，包括项目运行、补丁生成、热更效果验证等，大家可以前往博客查看。其中直接下载的方式，补丁包已经放在作者的服务器，体验时直接下载安装APK即可。这里仅附上直接体验方式的apk现在地址:
	
- [点击下载](http://blog.bihe0832.com/public/resource/Hotfix-debug.apk)
	
- 扫码下载APK：
	
![Demo下载二维码](http://blog.bihe0832.com/public/images/gradle-test-hotfix-apk-download.png)


## 三、代码介绍

- SDK热更之Demo工程介绍：[http://blog.bihe0832.com/sdk_hotfix_demo_project.html](http://blog.bihe0832.com/sdk_hotfix_demo_project.html)

	包括SDK的工程介绍以及一些核心文件和关键操作的介绍

- SDK热更之SDKHotfix待优化点：[http://blog.bihe0832.com/sdk_hotfix_need_to_do.html](http://blog.bihe0832.com/sdk_hotfix_need_to_do.html)

	主要从安全性、后续的代码维护两个方面介绍了SDKHotfix中没有增加的相关内容。

- SDK热更之如何在SDK代码中自动插桩及如何生成补丁包：[http://blog.bihe0832.com/sdk_hotfix_patch.html](http://blog.bihe0832.com/sdk_hotfix_patch.html)

	主要介绍SDK怎么插桩、怎么生成补丁的原理；文章主要是介绍原理，没有对具体实现的代码做详细解读。
	
- SDK热更之如何获取应用在当前设备上的so对应的指令集：[http://blog.bihe0832.com/SDK_hotfix_so_abi.html
](http://blog.bihe0832.com/sdk_hotfix_so_abi.html)

	目前的demo中只使用了arm的so，但是对于SDK的热更新，肯定要提供完整的so，当需要提供完整的so的时候，怎么下发正确的so主要在这边文章介绍


## 四、其他

### 参考及引用

该项目是基于QQ空间终端开发团队的技术文章实现的，然后补充了Native的so的动态加载相关的内容。文章地址：[安卓App热补丁动态修复技术介绍](http://zhuanlan.zhihu.com/magilu/20308548)

项目代码前期有参考dodola的HotFix项目，项目地址为：[https://github.com/dodola/HotFix](https://github.com/dodola/HotFix)

项目自动插桩脚本有参考jasonross的项目NuwaGradle，项目地址为：[https://github.com/jasonross/NuwaGradle](https://github.com/jasonross/NuwaGradle)

