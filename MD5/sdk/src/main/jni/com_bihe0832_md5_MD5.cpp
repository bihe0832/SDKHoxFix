#include "com_bihe0832_md5_MD5.h"
#include "md5.h"
#include <string>

void md5ToHex(const unsigned char* buf, bool lower, unsigned char* dbuf){
    static const unsigned char hex_table1[16] = {'0', '1', '2', '3', '4', '5', '6', '7',
        '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    static const unsigned char hex_table2[16] = {'0', '1', '2', '3', '4', '5', '6', '7',
        '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    if (!buf)
    {
        return;
    }

    const unsigned char* hex_table = lower ? hex_table1 : hex_table2;

    for (unsigned int i = 0; i < 16; i++)
    {
        dbuf[i * 2] = hex_table[buf[i] >> 4 & 0xF];
        dbuf[i * 2 + 1] = hex_table[buf[i] & 0xF];
    }
    dbuf[32] = '\0';
}

/*
 * Class:     com_bihe0832_md5_MD5
 * Method:    getMD5Result
 * Signature: (Ljava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_bihe0832_md5_MD5_getMD5Result
(JNIEnv *env, jclass, jstring jEncryptString) {

	LOGD("Java_com_bihe0832_md5_MD5_getMD5Result：1%s", "");
	jboolean isCopy;
	const char* cEncryptString = env->GetStringUTFChars(jEncryptString, &isCopy);
	int cEncryptStringLength = strlen(cEncryptString);

	MD5_CTX md5_ctx;
	BYTE md[16];
	md5_init(&md5_ctx);
	md5_update(&md5_ctx, (BYTE *)cEncryptString, cEncryptStringLength);
	md5_final(&md5_ctx,md);

	unsigned char mdstr[33];
	memset(mdstr, 0, sizeof(mdstr));
	md5ToHex((const unsigned char*)md,false,(unsigned char*)mdstr);

	jstring jMdResult = env->NewStringUTF((const char*) mdstr);
	env->ReleaseStringUTFChars(jEncryptString,cEncryptString);

	//转为jstring
	jclass strClass = env->FindClass("java/lang/String");
	jmethodID ctorID = env->GetMethodID(strClass, "<init>","([BLjava/lang/String;)V");
	jbyteArray bytes = env->NewByteArray(32);
	env->SetByteArrayRegion(bytes, 0, 32, (jbyte*) mdstr);
	jstring encoding = env->NewStringUTF("utf-8");

	return (jstring) env->NewObject(strClass, ctorID, bytes, encoding);
}
