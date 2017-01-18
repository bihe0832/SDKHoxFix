package com.bihe0832

import org.gradle.api.Project

class BuildPatchPluginExtension {
    //不可以被热更的类列表
    HashSet<String> excludeClass = []
    //被热更的SDK的版本号
    int oldSDKVersion = 0
    //热更后升级到的SDK的版本号
    int newSDKVersion = 0
    //SDK热更中保存SDK的版本、热更测试函数的核心类，这个类在生成补丁包时一定会保留
    String patchCoreClass = ""
    //SDK插桩使用的类的类名，这个类在生成SDK或者补丁的时候一定会被删除
    String patchPileClass = ""

    BuildPatchPluginExtension(Project project) {
    }
}
