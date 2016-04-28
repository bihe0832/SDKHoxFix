#!/bin/sh
#函数定义，检测执行结果
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

if [ "$1"x = ""x ];then
  echo "***********************************************"
  echo "  Please Enter a Para for build.sh :"
  echo "    '/bin/bash ./build.sh md5' for build MD5 project"
  echo "    '/bin/bash ./build.sh GradleTestPre' for build MD5 and copy result to GradleTest"
  echo "    '/bin/bash ./build.sh all' for build MD5 and GradleTest project both"
  echo "***********************************************"
  exit;
fi 

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
#创建打包目录
if [ ! -d "./bin" ]; then
  mkdir $localPath/bin
fi

#进入打包目录并清空目录
cd $localPath/bin && rm -rf  * && cd $localPath

#构建md5
echo "********build md5 *******"
chmod +x $localPath/MD5/gradlew
cd $localPath/MD5 && ./gradlew clean
cd $localPath/MD5 && ./gradlew build
checkResult


#拷贝最新资源到bin
echo "********copy md5 so and jar to bin *******"
mkdir $localPath/bin/MD5
cp -r $localPath/MD5/sdk/build/intermediates/ndk/all/release/lib/* $localPath/bin/MD5/
checkResult
cp -r $localPath/MD5/sdk/build/intermediates/bundles/all/release/classes.jar $localPath/bin/MD5/bihe0832MD5_old.jar
checkResult

#制作多dex包
echo "******** create new jar *******"
#临时文件
mkdir $localPath/bin/temp
unzip -oqbC $localPath/bin/MD5/bihe0832MD5_old.jar -d $localPath/bin/temp/jar
# 删除class中的Fix.class
echo "********SDK build build hackdex *******"
rm -f $localPath/bin/temp/jar/com/bihe0832/hotfix/Fix.class
checkResult
# 将默认的hackdex更新到assets
cd $localPath/bin/temp/jar
if [ ! -d "./assets" ]; then
  mkdir $localPath/bin/temp/jar/assets
fi
cp -r $localPath/MD5/bihe0832_hackdex.jar $localPath/bin/temp/jar/assets/
checkResult
#重新打包jar
cd $localPath/bin/temp/jar
jar cvf $localPath/bin/temp/jar/classes.jar *
cp -r $localPath/bin/temp/jar/classes.jar $localPath/bin/MD5/bihe0832MD5.jar
checkResult

rm -fr $localPath/bin/temp
if [ "$1"x = "md5"x ];then
  echo "********md5 build succ, will exit *******"
  exit;
fi 

#拷贝最新包到GradleTest
echo "********copy md5 so and jar to GradleTest*******"
cp -r $localPath/MD5/sdk/build/intermediates/ndk/all/release/lib/* $localPath/GradleTest/app/src/main/jniLibs/
checkResult
cp -r $localPath/bin/MD5/bihe0832MD5.jar $localPath/GradleTest/app/libs/bihe0832MD5.jar
checkResult

if [ "$1"x = "GradleTestPre"x ];then
  echo "********copy md5 so and jar to GradleTest succ, will exit *******"
  exit;
fi 


echo "********build GradleTest*******"
chmod +x $localPath/GradleTest/gradlew
cd $localPath/GradleTest && ./gradlew clean
cd $localPath/GradleTest && ./gradlew build
checkResult

echo "********copy apk *******"
mkdir $localPath/bin/GradleTest
cp $localPath/GradleTest/app/build/outputs/apk/app-all-debug.apk $localPath/bin/GradleTest-debug.apk



