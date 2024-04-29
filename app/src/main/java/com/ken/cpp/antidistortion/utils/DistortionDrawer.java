package com.ken.cpp.antidistortion.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.os.Environment;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

public class DistortionDrawer {
  private static final int COORDS_PER_VERTEX = 2;
  
  private short[] buffer_indices = new short[9126];
  
  private float[] buffer_tex = new float[3200];
  
  private float[] buffer_vertices = new float[3200];

  private final int width = 11;

  private final int height = 11;
  
  private Bitmap mBitmap = null;
  
  private Context mContext;
  
  private int mImageIndex = 0;
  
  private List<String> mImageList;
  
  private ShortBuffer mIndexBuffer;
  
  private int mPositionHandle;
  
  private int mProgram;
  
  private float mSize = 1.0F;
  
  private FloatBuffer mTexBuffer;
  
  private int mTextureCoordHandle;
  
  private int[] mTextures = new int[1];
  
  private FloatBuffer mVertexBuffer;

  private final int vertexStride = 8;
  
  public DistortionDrawer(Context paramContext, int paramInt) {
    this.mContext = paramContext;
    computeAntiDistortion(0.0F, 0.0F, 0.0F);
    try {
      this.mImageList = getImagePathFromSD();
      if (this.mImageList.size() <= 0) {
        this.mBitmap = BitmapFactory.decodeResource(this.mContext.getResources(), 2131099735);
      } else {
        this.mBitmap = BitmapFactory.decodeFile(this.mImageList.get(this.mImageIndex));
      } 
    } catch (Exception exception) {}
  }
  
  @SuppressLint({"DefaultLocale"})
  private boolean checkIsImageFile(String paramString) {
    paramString = paramString.substring(paramString.lastIndexOf(".") + 1).toLowerCase();
    return (paramString.equals("jpg") || paramString.equals("png") || paramString.equals("gif") || paramString.equals("jpeg") || paramString.equals("bmp"));
  }
  
  private native String getAntiDistortionParams(float k1, float k2, float k3, float[] paramArrayOffloat1, float[] paramArrayOffloat2, short[] paramArrayOfshort, int paramInt1, int paramInt2, float paramFloat3);
  
  private List<String> getImagePathFromSD() {
    ArrayList<String> arrayList = new ArrayList();
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(Environment.getExternalStorageDirectory().toString());
    stringBuilder.append(File.separator);
    stringBuilder.append("sampleImages");
    File[] arrayOfFile = (new File(stringBuilder.toString())).listFiles();
    if (arrayOfFile != null)
      for (byte b = 0; b < arrayOfFile.length; b++) {
        File file = arrayOfFile[b];
        if (checkIsImageFile(file.getPath()))
          arrayList.add(file.getPath()); 
      }  
    return arrayList;
  }
  
  private int loadShader(int paramInt, String paramString) {
    paramInt = GLES20.glCreateShader(paramInt);
    GLES20.glShaderSource(paramInt, paramString);
    GLES20.glCompileShader(paramInt);
    return paramInt;
  }
  
  public void computeAntiDistortion(float k1, float k2, float k3) {
//    getAntiDistortionParams(k1, k2, k3, this.buffer_vertices, this.buffer_tex, this.buffer_indices, width, height, this.mSize);



    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(this.buffer_vertices.length * 4);
    byteBuffer.order(ByteOrder.nativeOrder());
    this.mVertexBuffer = byteBuffer.asFloatBuffer();
    this.mVertexBuffer.put(this.buffer_vertices);
    this.mVertexBuffer.position(0);

    byteBuffer = ByteBuffer.allocateDirect(this.buffer_indices.length * 2);
    byteBuffer.order(ByteOrder.nativeOrder());
    this.mIndexBuffer = byteBuffer.asShortBuffer();
    this.mIndexBuffer.put(this.buffer_indices);
    this.mIndexBuffer.position(0);

    byteBuffer = ByteBuffer.allocateDirect(this.buffer_tex.length * 4);
    byteBuffer.order(ByteOrder.nativeOrder());
    this.mTexBuffer = byteBuffer.asFloatBuffer();
    this.mTexBuffer.put(this.buffer_tex);
    this.mTexBuffer.position(0);
  }
  
  public void draw() {
    GLES20.glUseProgram(this.mProgram);
    GLES20.glActiveTexture(33984);
    GLES20.glBindTexture(3553, this.mTextures[0]);

    this.mPositionHandle = GLES20.glGetAttribLocation(this.mProgram, "vPosition");
    GLES20.glEnableVertexAttribArray(this.mPositionHandle);
    GLES20.glVertexAttribPointer(this.mPositionHandle, 2, 5126, false, 8, this.mVertexBuffer);

    this.mTextureCoordHandle = GLES20.glGetAttribLocation(this.mProgram, "inputTextureCoordinate");
    GLES20.glEnableVertexAttribArray(this.mTextureCoordHandle);
    GLES20.glVertexAttribPointer(this.mTextureCoordHandle, 2, 5126, false, 8, this.mTexBuffer);

    GLES20.glDrawElements(4, 9126, 5123, this.mIndexBuffer);

    GLES20.glDisableVertexAttribArray(this.mPositionHandle);
    GLES20.glDisableVertexAttribArray(this.mTextureCoordHandle);
  }
  
  public void initGL() {
    if (this.mBitmap != null && !this.mBitmap.isRecycled()) {
      GLES20.glGenTextures(1, this.mTextures, 0);
      GLES20.glBindTexture(3553, this.mTextures[0]);
      GLES20.glTexParameteri(3553, 10241, 9728);
      GLES20.glTexParameteri(3553, 10240, 9728);
      GLES20.glTexParameteri(3553, 10242, 33071);
      GLES20.glTexParameteri(3553, 10243, 33071);
      GLUtils.texImage2D(3553, 0, this.mBitmap, 0);
    } 
    int i = loadShader(35633, "attribute vec4 vPosition;attribute vec2 inputTextureCoordinate;varying vec2 textureCoordinate;void main(){gl_Position = vPosition;textureCoordinate = inputTextureCoordinate;}");
    int j = loadShader(35632, "precision mediump float;varying vec2 textureCoordinate;\nuniform sampler2D s_texture;\nvoid main() {  gl_FragColor = texture2D( s_texture, textureCoordinate );\n}");
    this.mProgram = GLES20.glCreateProgram();
    GLES20.glAttachShader(this.mProgram, i);
    GLES20.glAttachShader(this.mProgram, j);
    GLES20.glLinkProgram(this.mProgram);
  }
  
  public void selectImage(int paramInt) {
    this.mImageIndex += paramInt;
    paramInt = this.mImageList.size();
    if (paramInt <= 0)
      return; 
    if (this.mImageIndex < 0) {
      this.mImageIndex = paramInt - 1;
    } else if (this.mImageIndex >= paramInt) {
      this.mImageIndex = 0;
    } 
    this.mBitmap.recycle();
    this.mBitmap = BitmapFactory.decodeFile(this.mImageList.get(this.mImageIndex));
    GLES20.glActiveTexture(33984);
    GLES20.glBindTexture(3553, this.mTextures[0]);
    GLES20.glTexParameteri(3553, 10242, 33071);
    GLES20.glTexParameteri(3553, 10243, 33071);
    GLES20.glTexParameteri(3553, 10241, 9985);
    GLES20.glTexParameteri(3553, 10240, 9729);
    GLUtils.texImage2D(3553, 0, this.mBitmap, 0);
    GLES20.glGenerateMipmap(3553);
    GLES20.glBindTexture(3553, 0);
  }
  
  public void setSize(int paramInt) {
    this.mSize = paramInt / 1600.0F;
  }
}


/* Location:              /Users/Ken/tools/bin/decompile-apk/output/base/base-dex2jar.jar!/com/ken/cpp/antidistortion/utils/DistortionDrawer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */