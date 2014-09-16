#include "stdio.h"
#include "com_hongquan_jni_HelloJNI.h"


JNIEXPORT jstring JNICALL Java_com_hongquan_jni_HelloJNI_sayHi(JNIEnv *env, jclass jc, jstring name)
{
	return name;
}