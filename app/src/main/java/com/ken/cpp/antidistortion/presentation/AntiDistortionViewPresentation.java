package com.ken.cpp.antidistortion.presentation;

import android.app.ActivityManager;
import android.app.Presentation;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Toast;

import com.ken.cpp.antidistortion.R;
import com.ken.cpp.antidistortion.utils.DistortionGLRenderer;

public class AntiDistortionViewPresentation extends Presentation {
  private Context mContext;
  
  private DistortionGLRenderer mDistortionRenderer;
  
  private GLSurfaceView mGlSurfaceView;
  
  private int mIndex = 0;
  
  private boolean supportsEs2 = false;
  
  public AntiDistortionViewPresentation(Context paramContext, Display paramDisplay) {
    super(paramContext, paramDisplay);
    this.mContext = paramContext;
  }

  private void initIsGLSurfaceView() {
    boolean bool2;
    int i = (((ActivityManager)this.mContext.getSystemService("activity")).getDeviceConfigurationInfo()).reqGlEsVersion;
    boolean bool1 = false;
    if (i >= 8192) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    this.supportsEs2 = bool2;
    if (Build.VERSION.SDK_INT > 15 && (Build.FINGERPRINT.startsWith("generic") || Build.FINGERPRINT.startsWith("unknown") || Build.MODEL.contains("google_sdk") || Build.MODEL.contains("Emulator") || Build.MODEL.contains("Android SDK built for x86"))) {
      i = 1;
    } else {
      i = 0;
    } 
    if (this.supportsEs2 || i != 0) {
      bool2 = true;
    } else {
      bool2 = bool1;
    } 
    this.supportsEs2 = bool2;
  }
  
  private void initOpenGl() {
    if (this.supportsEs2) {
      setContentView(R.layout.tinyxr_presentation_layout);
      mGlSurfaceView = findViewById(R.id.tinyxr_gl_view);
      //this.mGlSurfaceView = new GLSurfaceView(this.mContext);
      this.mDistortionRenderer = new DistortionGLRenderer(this.mContext);
      this.mGlSurfaceView.setEGLContextClientVersion(2);
      this.mGlSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
      this.mGlSurfaceView.setRenderer((GLSurfaceView.Renderer)this.mDistortionRenderer);
      this.mGlSurfaceView.setZOrderOnTop(true);
      //setContentView((View)this.mGlSurfaceView);
    } else {
      setContentView(R.layout.activity_main);
      Toast.makeText(this.mContext, "设备不支持OpenGL", 0).show();
    } 
  }
  
  public void computeDistortion(float k1, float k2, float k3) {
    this.mDistortionRenderer.computeDistortion(k1, k2, k3);
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    initIsGLSurfaceView();
    initOpenGl();
  }
  
  protected void onStart() {
    super.onStart();
  }
  
  protected void onStop() {
    super.onStop();
  }
  
  public void selectImage(int paramInt) {
    this.mIndex = paramInt;
    this.mGlSurfaceView.queueEvent(new Runnable() {
          public void run() {
            AntiDistortionViewPresentation.this.mDistortionRenderer.selectImage(AntiDistortionViewPresentation.this.mIndex);
          }
        });
  }
  
  public void setSize(int paramInt) {
    this.mDistortionRenderer.setSize(paramInt);
  }
}


/* Location:              /Users/Ken/tools/bin/decompile-apk/output/base/base-dex2jar.jar!/com/ken/cpp/antidistortion/presentation/AntiDistortionViewPresentation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */