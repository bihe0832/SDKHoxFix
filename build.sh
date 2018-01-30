#!/bin/sh
# ****************************************************
# * 为了开发者了解原理，我将SDK的构建脚本注释详细一点
# * 子勰（code@bihe0832.com）
# ****************************************************

# 函数定义，检测执行结果
function checkResult() {  
   result=$?
   echo "result : $result"
   if [ $result -eq 0 ];then
      echo "checkResult: execCommand succ"
   else
    echo "checkResult: execCommand failed"
    exit $result
   fi
}  

# 函数定义，删除当前目录及其子目录下的空文件夹
deleteempty() {
  find ${1:-.} -mindepth 1 -maxdepth 1 -type d | while read -r dir
  do
    if [[ -z "$(find "$dir" -mindepth 1 -type f)" ]] >/dev/null
    then
      echo "$dir"
      rm -rf ${dir} 2>&- && echo "Empty, Deleted!" || echo "Delete error"
    fi
    if [ -d ${dir} ]
    then
      deleteempty "$dir"
    fi
  done
}


if [ "$1"x = ""x ];then
  echo "***********************************************"
  echo "  Please Enter a Para for build.sh :"
  echo "    '/bin/bash ./build.sh md5' for build MD5 project"
  echo "    '/bin/bash ./build.sh GradleTestPre' for build MD5 and copy result to GradleTest"
  echo "    '/bin/bash ./build.sh patch' for build a patch"
  echo "    '/bin/bash ./build.sh all' for build MD5 and GradleTest project both"
  echo "***********************************************"
  exit;
fi 

# 环境变量设置和声明
export ANDROID_HOME=$ANDROID_SDK
export JAVA_HOME=`/usr/libexec/java_home -v 1.7`
export ANDROID_NDK_HOME=$ANDROIDNDK_LINUX_R10C
export PATH=$JAVA_HOME/bin:$GRADLE_HOME/bin:$PATH
echo $ANDROID_HOME
echo $JAVA_HOME
echo $ANDROID_NDK_HOME

echo "********build mkdir bin *******"

localPath=`pwd`
echo $localPath
# 创建打包目录
if [ ! -d "./bin" ]; then
  mkdir $localPath/bin
fi

# 进入打包目录并清空目录，最终的构建结果都会保存在这个目录，根目录下面的bin目录
cd $localPath/bin && rm -rf  * && cd $localPath

# 构建md5，也就是SDK项目
echo "********build md5 *******"
chmod +x $localPath/MD5/gradlew
cd $localPath/MD5 && ./gradlew clean
cd $localPath/MD5 && ./gradlew build
checkResult

# 进入SDK版本的预处理模块
echo "******** create new jar *******"
cd $localPath/MD5
if [ ! -d "./bin" ]; then
  mkdir $localPath/MD5/bin
fi
# 进入SDK项目的打包目录并清空目录
cd $localPath/MD5/bin && rm -rf * && mkdir $localPath/MD5/bin/temp
# 拷贝构建结果中的SDK项目jar到临时目录
cp -r $localPath/MD5/sdk/build/intermediates/bundles/release/classes.jar $localPath/MD5/bin/temp/bihe0832MD5_old.jar
# 解压已经生成的jar到指定目录
unzip -oqbC $localPath/MD5/bin/temp/bihe0832MD5_old.jar -d $localPath/MD5/bin/temp/jar
# 对SDK的所有代码进行插桩
cd $localPath/MD5 && ./gradlew processJarAndGetJarHash
checkResult

#如果是要生成补丁包，进入内部逻辑，如果不是跳过逻辑
if [ "$1"x = "patch"x ];then
  echo "********md5 build succ, will build patch *******"
  # 生成SDK对应的补丁，执行结束以后所以没有变化的文件会被删除
  cd $localPath/MD5 && ./gradlew buildPatch
  checkResult
  cd $localPath/MD5/bin/temp/jar
  # 删除所有空目录
  deleteempty
  # 将所有有变更的文件重新打包为jar
  jar cvf $localPath/MD5/bin/temp/jar/bihe0832_patch.jar *
  cp -r $localPath/MD5/bin/temp/jar/bihe0832_patch.jar $localPath/MD5/bin/bihe0832_patch.jar
  # 使用build-tools将jar生成为dex
  $ANDROID_HOME/build-tools/23.0.2/dx --dex --output=$localPath/MD5/bin/bihe0832_patch_dex.jar $localPath/MD5/bin/temp/jar/bihe0832_patch.jar

  mkdir $localPath/bin/Patch
  cp -r $localPath/MD5/bin/bihe0832_patch_dex.jar $localPath/bin/Patch/bihe0832_patch_dex.jar
  cp -r $localPath/MD5/bin/bihe0832_patch.jar $localPath/bin/Patch/bihe0832_patch.jar

  cp -r $localPath/MD5/sdk/build/intermediates/ndk/release/lib/armeabi $localPath/bin/Patch/
  checkResult
  # 删除临时文件夹
  rm -fr $localPath/MD5/bin/temp
  echo "********SDK build patch end*******"
  exit;
fi 

echo "********SDK build build jar *******"
cd $localPath/MD5/bin/temp/jar
# 将默认的hackdex更新到assets
if [ ! -d "./assets" ]; then
  mkdir $localPath/MD5/bin/temp/jar/assets
fi
cp -r $localPath/MD5/libs/bihe0832_hackdex.jar $localPath/MD5/bin/temp/jar/assets/
checkResult
# 重新打包jar，生成完整的SDK文件
cd $localPath/MD5/bin/temp/jar
jar cvf $localPath/MD5/bin/temp/jar/classes.jar *
cp -r $localPath/MD5/bin/temp/jar/classes.jar $localPath/MD5/bin/bihe0832MD5.jar
cp -r $localPath/MD5/bin/temp/bihe0832MD5_old.jar $localPath/MD5/bin/bihe0832MD5_old.jar

# 拷贝构建结果到bin，由于Demo仅使用了arm的so，因此这里只拷贝了arm
echo "********copy md5 so to bin *******"
mkdir $localPath/bin/MD5
cp -r $localPath/MD5/sdk/build/intermediates/ndk/release/lib/armeabi $localPath/bin/MD5/
checkResult

cp -r $localPath/MD5/bin/bihe0832MD5.jar $localPath/bin/MD5/bihe0832MD5.jar
cp -r $localPath/MD5/bin/bihe0832MD5_old.jar $localPath/bin/MD5/bihe0832MD5_old.jar
cp -r $localPath/MD5/bin/temp/*_hash.txt $localPath/MD5/bin/
cp -r $localPath/MD5/bin/*_hash.txt $localPath/bin/MD5/
# 保存新版本SDK中所有文件对应的md5
cp -r $localPath/MD5/bin/*_hash.txt $localPath/MD5/libs/hash/
checkResult

rm -fr $localPath/MD5/bin/temp
if [ "$1"x = "md5"x ];then
  echo "********md5 build succ, will exit *******"
  exit;
fi 

# 拷贝最新包到GradleTest
echo "********copy md5 so and jar to GradleTest*******"
rm -fr $localPath/GradleTest/app/src/main/jniLibs/*
cp -r $localPath/bin/MD5/armeabi $localPath/GradleTest/app/src/main/jniLibs/
checkResult
cp -r $localPath/bin/MD5/bihe0832MD5.jar $localPath/GradleTest/app/libs/bihe0832MD5.jar
checkResult

if [ "$1"x = "GradleTestPre"x ];then
  echo "********copy md5 so and jar to GradleTest succ, will exit *******"
  exit;
fi 
# 自动构建GradleTest
echo "********build GradleTest*******"
chmod +x $localPath/GradleTest/gradlew
cd $localPath/GradleTest && ./gradlew clean
cd $localPath/GradleTest && ./gradlew build
checkResult

# 拷贝自动构建生成的apk到构建目录
echo "********copy apk *******"
mkdir $localPath/bin/GradleTest
cp $localPath/GradleTest/app/build/outputs/apk/app-debug.apk $localPath/bin/GradleTest-debug.apk



