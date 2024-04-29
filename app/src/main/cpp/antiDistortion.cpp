#include <jni.h>
#include <string>
#include <vector>
#include "Cardboard/CardboardPostRender.h"

extern "C"
JNIEXPORT jstring JNICALL
Java_com_ken_cpp_antidistortion_utils_DistortionDrawer_getAntiDistortionParams(JNIEnv *env,
                                                                           jobject thiz,
                                                                           jfloat k1,
                                                                           jfloat k2,
                                                                           jfloat k3,
                                                                           jfloatArray vertices,
                                                                           jfloatArray tex,
                                                                           jshortArray indices,
                                                                           jint width,
                                                                           jint height,
                                                                           jfloat size) {
    std::string hello = "Hello from C++";

    float buffer_vertices[width * height * 2];
    float buffer_tex[width * height * 2];
    short buffer_indices[(width - 1) * (height - 1) * 6];

    android::CardboardPostRender cpr(width, height);

    cpr.setDistortionParams(k1, k2, k3);
    cpr.rebuildDistortionMesh(buffer_vertices, buffer_tex, buffer_indices, size);

    (*env).SetFloatArrayRegion(vertices, 0, width * height * 2, buffer_vertices);
    (*env).SetFloatArrayRegion(tex, 0, width * height * 2, buffer_tex);
    (*env).SetShortArrayRegion(indices, 0, (width - 1) * (height - 1) * 6, buffer_indices);

    return env->NewStringUTF(hello.c_str());
}