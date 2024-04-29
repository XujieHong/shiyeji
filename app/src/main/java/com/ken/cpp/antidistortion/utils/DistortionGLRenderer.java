package com.ken.cpp.antidistortion.utils;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class DistortionGLRenderer implements GLSurfaceView.Renderer {
  private int glSurfaceTex;
  
  private DistortionDrawer mDistortionDrawer;
  
//  private SurfaceTexture mSurfaceTexture;
  
  public DistortionGLRenderer(Context paramContext) {
    this.mDistortionDrawer = new DistortionDrawer(paramContext, this.glSurfaceTex);
  }
  
//  private int createSurfaceTexture(int paramInt1, int paramInt2) {
//    GLES20.glGenTextures(1, new int[1], 0);
//    if (this.glSurfaceTex > 0) {
//      GLES20.glBindTexture(3553, this.glSurfaceTex);
//      GLES20.glTexImage2D(3553, 0, 6407, paramInt1, paramInt2, 0, 6407, 5121, null);
//      GLES20.glTexParameteri(3553, 10241, 9728);
//      GLES20.glTexParameteri(3553, 10240, 9728);
//      GLES20.glTexParameteri(3553, 10242, 33071);
//      GLES20.glTexParameteri(3553, 10243, 33071);
//    }
//    return this.glSurfaceTex;
//  }
  
  public void computeDistortion(float k1, float k2, float k3) {
    this.mDistortionDrawer.computeAntiDistortion(k1, k2, k3);
  }
  
  public void onDrawFrame(GL10 paramGL10) {
    GLES20.glClearColor(1.0F, 0.0F, 0.0F, 1.0F);
    GLES20.glClear(16640);
    GLES20.glEnable(3042);
    GLES20.glBlendFunc(1, 771);
    this.mDistortionDrawer.draw();
  }
  
  public void onSurfaceChanged(GL10 paramGL10, int paramInt1, int paramInt2) {}
  
  public void onSurfaceCreated(GL10 paramGL10, EGLConfig paramEGLConfig) {
    this.mDistortionDrawer.initGL();
    Log.d("Kenhong", "onSurfaceCreated");
  }
  
  public void selectImage(int paramInt) {
    this.mDistortionDrawer.selectImage(paramInt);
  }
  
  public void setSize(int paramInt) {
    this.mDistortionDrawer.setSize(paramInt);
  }
}


/* Location:              /Users/Ken/tools/bin/decompile-apk/output/base/base-dex2jar.jar!/com/ken/cpp/antidistortion/utils/DistortionGLRenderer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */