package com.bihe0832

import com.bihe0832.util.CommonUtils
import com.bihe0832.util.MapUtils
import com.bihe0832.util.Processor
import org.apache.commons.codec.digest.DigestUtils
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.FileTree

class BuildPatchPlugin implements Plugin<Project> {

    //SDK包的包名前缀，所以以改包名前缀开头的类，在生成补丁包的时候都回去校验是否有改动，如果没有改动，会被删除
    private static final String PACKAGE_HEADER = "com"
    //生成补丁SDK或者已插桩SDK的临时目录
    private static final String EXTEND_BUILD_DIR = "/../../bin/temp/jar"
    //生成补丁SDK读取已插桩SDK的文件的md5的保存目录
    private static final String EXTEND_HASH_DIR = "/../../libs/hash"
    //生成已插桩的SDK时记录所有文件md5的文件名
    private static final String HASH_TXT = "hash.txt"
    //生成插桩SDK时的命令
    private static final String TASK_PATCH_HACKJAR_JAR2HASH = "processJarAndGetJarHash"
    //生成补丁SDK时的命令
    private static final String TASK_PATCH_BUILD_PATCH = "buildPatch"
    //引用插件时项目build.gradle配置的内容
    private static final String PatchConfig = "PatchConfig";

    @Override
    void apply(Project project) {
        project.extensions.create(PatchConfig, BuildPatchPluginExtension,project)

        project.task(TASK_PATCH_BUILD_PATCH) << {
            println(TASK_PATCH_BUILD_PATCH)
            def extension = project.extensions.findByName(PatchConfig) as BuildPatchPluginExtension
            def excludeClass = extension.excludeClass
            String hashFileDir = project.buildDir.absolutePath + EXTEND_HASH_DIR
            println("hashFileDir:" + hashFileDir)
            def hashFile = new File(hashFileDir, getHashFileName(extension.oldSDKVersion))
            println(hashFile.absolutePath)
            Map hashMap = MapUtils.parseMap(hashFile)
            println(hashMap.size())

            String inputFileDir = project.buildDir.absolutePath + EXTEND_BUILD_DIR
            println("inputFileDir:" + inputFileDir)
            FileTree tree = project.fileTree(dir: inputFileDir)
            Set<File> inputFiles = tree.files
            inputFiles.each { currentFile ->
                def path = currentFile.absolutePath
                //目前因为个人项目仅需要class文件，因此仅保留了class文件，其余文件一律不打入补丁包
                if (path.endsWith(".class") && !path.contains("/R\$") && !path.endsWith("/R.class") && !path.endsWith("/BuildConfig.class")) {
                    path = path.replace("/",".")
                    if (!CommonUtils.isExcluded(path, excludeClass)) {
                        //核心类,不能删除
                        if(path.endsWith(extension.patchCoreClass)){
                            println(path + " is include")
                        }else {
                            int start = -1
                            if(path.indexOf(PACKAGE_HEADER) > 0) {
                                start = path.indexOf(PACKAGE_HEADER)
                            }
                            if(start > -1){
                                path = path.substring(start)
                                if(path.equals(extension.patchPileClass)){
                                    currentFile.delete()
//                                    println(path + " is isExcluded 1")
                                }else{
                                    def hash = DigestUtils.shaHex(currentFile.bytes)
                                    if (MapUtils.isSame(hashMap, path, hash)) {
                                        currentFile.delete()
//                                        println(path + " is isExcluded 2")
                                    } else {
                                        println "inputFile" + currentFile.absolutePath + "hash is differet, will be include "
                                    }
                                }
                            }
                        }
                    } else {
                        println(path + " is isExcluded 3")
                        currentFile.delete()
                    }
                } else {
                    currentFile.delete()
                }
            }
        }


        project.task('testBihe0832') << {
            println "hello, world! good job !!!"
        }

        project.task(TASK_PATCH_HACKJAR_JAR2HASH) << {
            println(TASK_PATCH_HACKJAR_JAR2HASH)
            //获取插件相关变量的定义
            def extension = project.extensions.findByName(PatchConfig) as BuildPatchPluginExtension
            def excludeClass = extension.excludeClass
            //获取构建目录的绝对路径
            String inputFileDir = project.buildDir.absolutePath + EXTEND_BUILD_DIR
            def hashFile = new File(inputFileDir +"/../", getHashFileName(extension.newSDKVersion))
            if(hashFile.exists()){
                hashFile.delete()
            }
            hashFile.createNewFile();
            //获取构建目录下的文件列表
            FileTree tree = project.fileTree(dir: inputFileDir);
            Set<File> inputFiles = tree.files;
            println tree.size();
            println(inputFiles.properties);
            println(extension.patchPileClass);
            //生成插桩类的签名，用于文件插桩
            String hackClassName = extension.patchPileClass.substring(0,extension.patchPileClass.length()-6);
            String sigClassName = hackClassName.replace(".","/");
            sigClassName = "L" + sigClassName + ";";
            println(sigClassName);
            inputFiles.each { inputFile ->
                def path = inputFile.absolutePath
                //目前因为个人项目仅需要class文件，因此仅保留了class文件，其余文件一律不打入补丁包，因此没有计算其余文件的md5
                if (path.endsWith(".class") && !path.contains("/R\$") && !path.endsWith("/R.class") && !path.endsWith("/BuildConfig.class")) {
                    path = path.replace("/",".")
                    if (!CommonUtils.isExcluded(path, excludeClass)) {
                        int start = -1
                        if(path.indexOf(PACKAGE_HEADER) > 0) {
                            start = path.indexOf(PACKAGE_HEADER)
                        }
                        if(start > -1){
                            path = path.substring(start)
                            if(path.equals(extension.patchPileClass)){
                                inputFile.delete()
                            }else{
                                def bytes = Processor.processClass(inputFile,sigClassName)
                                def hash = DigestUtils.shaHex(bytes)
                                hashFile.append(MapUtils.format(path, hash))
                                println(path + ":" + hash)
                            }
                        }
                    } else {
                        println(path + " is isExcluded")
                    }
                } else {
                    println(path + " is bad")
                }
            }
        }
    }

    //根据SDK版本获取对应版本的SDK文件的hash列表
    public String getHashFileName(int tag){
        if(tag > 0){
            return tag + "_" + HASH_TXT
        }else{
            return HASH_TXT
        }
    }

}


