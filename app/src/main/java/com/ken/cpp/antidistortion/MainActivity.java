package com.ken.cpp.antidistortion;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.ken.cpp.antidistortion.presentation.AntiDistortionViewPresentation;
import com.ken.cpp.antidistortion.utils.FileUtils1;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_UP;

public class MainActivity extends AppCompatActivity {
  private float k1 = 0.0F;
  
  private float k2 = 0.0F;

  private float k3 = 0.0F;
  
  private AntiDistortionViewPresentation mAntiDistortionViewPresentation = null;
  
  private Button mBtnIncreaseK1;
  
  private Button mBtnIncreaseK2;

  private Button mBtnIncreaseK3;
  
  private Button mBtnReduceK1;
  
  private Button mBtnReduceK2;

  private Button mBtnReduceK3;
  
  private DisplayManager.DisplayListener mDisplayListener = new DisplayManager.DisplayListener() {
      public void onDisplayAdded(int param1Int) {
        Display display = MainActivity.this.mDisplayManager.getDisplay(param1Int);
        if (display != null)
          MainActivity.this.showPresentation(display); 
      }
      
      public void onDisplayChanged(int param1Int) {}
      
      public void onDisplayRemoved(int param1Int) {
        if (MainActivity.this.mAntiDistortionViewPresentation != null) {
          MainActivity.this.mAntiDistortionViewPresentation.dismiss();
          //MainActivity.access$1502(MainActivity.this, (AntiDistortionViewPresentation)null);
          mAntiDistortionViewPresentation = null;
        } 
      }
    };
  
  private DisplayManager mDisplayManager;
  
  private EditText mEditTextHeight;
  
  private EditText mEditTextK1;
  
  private EditText mEditTextK2;

  private EditText mEditTextK3;
  
  private EditText mEditTextWidth;
  
  private Handler mHandler;
  
  private boolean mHeightFocusChangeFlag = false; // 102
  
  private Boolean mIsUp = Boolean.valueOf(true);
  
  private SeekBar mSeekBarK1;
  
  private SeekBar mSeekBarK2;

  private SeekBar mSeekBarK3;
  
  private boolean mWidthFocusChangeFlag = false; // 002
  
  private int size = 1280;
  
  static {
    System.loadLibrary("native-lib");
  }
  
  private void checkExternalDisplay() {
    Display[] arrayOfDisplay = this.mDisplayManager.getDisplays();
    if (arrayOfDisplay.length > 1)
      showPresentation(arrayOfDisplay[1]); 
  }
  
  private void checkReadPermission() {
    if (ActivityCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE") == -1) {
      ActivityCompat.requestPermissions(this, new String[] { "android.permission.READ_EXTERNAL_STORAGE" }, 1);
    }
    else {
//      FileUtils1.getInstance(getApplicationContext()).copyAssetsToSD("samples", "samples").setFileOperateCallback(new FileUtils1.FileOperateCallback() {
//        @Override
//        public void onSuccess() {
//          checkExternalDisplay();
//        }
//
//        @Override
//        public void onFailed(String error) {
//        }
//      });
      checkExternalDisplay();
    }
  }
  
  private void showPresentation(Display paramDisplay) {
    this.mAntiDistortionViewPresentation = new AntiDistortionViewPresentation(this, paramDisplay);
    this.mAntiDistortionViewPresentation.show();
  }
  
  protected void onCreate(Bundle paramBundle) {

    Log.d("KenHong", "onCreate");
    super.onCreate(paramBundle);
    setContentView(R.layout.activity_main);
    this.mHandler = new Handler();
    this.mDisplayManager = (DisplayManager)getSystemService("display");
    this.mDisplayManager.registerDisplayListener(this.mDisplayListener, this.mHandler);

    checkReadPermission();
    this.mEditTextK1 = findViewById(R.id.edit_k1);
    this.mEditTextK2 = findViewById(R.id.edit_k2);
    this.mEditTextK3 = findViewById(R.id.edit_k3);

    this.mEditTextK1.setText("0.0");
    this.mEditTextK2.setText("0.0");
    this.mEditTextK3.setText("0.0");

    this.mEditTextWidth = findViewById(R.id.edit_width);
    this.mEditTextHeight = findViewById(R.id.edit_height);
    this.mEditTextWidth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
          public void onFocusChange(View param1View, boolean param1Boolean) {
            mWidthFocusChangeFlag = param1Boolean;
          }
        });
    this.mEditTextHeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
          public void onFocusChange(View param1View, boolean param1Boolean) {
            mHeightFocusChangeFlag = param1Boolean;
          }
        });
    this.mEditTextWidth.addTextChangedListener(new TextWatcher() {
          public void afterTextChanged(Editable param1Editable) {
            try {
              size = Integer.parseInt(param1Editable.toString());

              if (MainActivity.this.size < 1)
                size = 1;
            } catch (Exception exception) {
              size = 1;
            } 
            if (MainActivity.this.mWidthFocusChangeFlag)
              MainActivity.this.mEditTextHeight.setText(param1Editable.toString()); 
          }
          
          public void beforeTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {}
          
          public void onTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {}
        });
    this.mEditTextHeight.addTextChangedListener(new TextWatcher() {
          public void afterTextChanged(Editable param1Editable) {
            try {
              //MainActivity.access$202(MainActivity.this, Integer.parseInt(param1Editable.toString()));
              size = Integer.parseInt(param1Editable.toString());
              if (MainActivity.this.size < 1)
                //MainActivity.access$202(MainActivity.this, 1);
                size = 1;
            } catch (Exception exception) {
              //MainActivity.access$202(MainActivity.this, 1);
              size = 1;
            } 
            if (MainActivity.this.mHeightFocusChangeFlag)
              MainActivity.this.mEditTextWidth.setText(param1Editable.toString()); 
          }
          
          public void beforeTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {}
          
          public void onTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {}
        });

    this.mSeekBarK1 = findViewById(R.id.seekBar_k1);
    this.mSeekBarK2 = findViewById(R.id.seekBar_k2);
    this.mSeekBarK3 = findViewById(R.id.seekBar_k3);
    this.mSeekBarK1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
          public void onProgressChanged(SeekBar param1SeekBar, int param1Int, boolean param1Boolean) {
            Log.d("KenHong", "param1Boolean = " + param1Boolean);
            if (param1Boolean) {
              k1 = param1Int / 1000.0F;
              EditText editText = MainActivity.this.mEditTextK1;
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append("");
              stringBuilder.append(MainActivity.this.k1);
              editText.setText(stringBuilder.toString());
            } 
            MainActivity.this.setParams(null);
          }
          
          public void onStartTrackingTouch(SeekBar param1SeekBar) {}
          public void onStopTrackingTouch(SeekBar param1SeekBar) {}
        });
    this.mSeekBarK2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
          public void onProgressChanged(SeekBar param1SeekBar, int param1Int, boolean param1Boolean) {
            if (param1Boolean) {
              k2 = param1Int / 1000.0F;
              EditText editText = MainActivity.this.mEditTextK2;
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append("");
              stringBuilder.append(MainActivity.this.k2);
              editText.setText(stringBuilder.toString());
            } 
            MainActivity.this.setParams(null);
          }
          
          public void onStartTrackingTouch(SeekBar param1SeekBar) {}
          public void onStopTrackingTouch(SeekBar param1SeekBar) {}
        });
    this.mSeekBarK3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
        public void onProgressChanged(SeekBar param1SeekBar, int param1Int, boolean param1Boolean) {
          if (param1Boolean) {
            k3 = param1Int / 1000.0F;
            EditText editText = MainActivity.this.mEditTextK3;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("");
            stringBuilder.append(MainActivity.this.k3);
            editText.setText(stringBuilder.toString());
          }
          MainActivity.this.setParams(null);
        }

        public void onStartTrackingTouch(SeekBar param1SeekBar) {}
        public void onStopTrackingTouch(SeekBar param1SeekBar) {}
    });

    this.mEditTextK1.addTextChangedListener(new TextWatcher() {
          public void afterTextChanged(Editable param1Editable) {
            Log.d("KenHong", "mEditTextK1 afterTextChanged");
            try {
              //MainActivity.access$502(MainActivity.this, Float.parseFloat(param1Editable.toString()));
              k1 = Float.parseFloat(param1Editable.toString());
              if (MainActivity.this.k1 < 0.0F)
                //MainActivity.access$502(MainActivity.this, 0.0F);
                k1 = 0.0F;
              if (MainActivity.this.k1 > 1.0F)
                //MainActivity.access$502(MainActivity.this, 1.0F);
                k1 = 1.0F;
            } catch (Exception exception) {
              //MainActivity.access$502(MainActivity.this, 0.0F);
              k1 = 0.0F;
            } 
            MainActivity.this.mSeekBarK1.setProgress((int)(MainActivity.this.k1 * 1000.0F));
          }
          
          public void beforeTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {}
          
          public void onTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {}
        });
    this.mEditTextK2.addTextChangedListener(new TextWatcher() {
          public void afterTextChanged(Editable param1Editable) {
            try {
              //MainActivity.access$702(MainActivity.this, Float.parseFloat(param1Editable.toString()));
              k2 = Float.parseFloat(param1Editable.toString());
              if (MainActivity.this.k2 < 0.0F)
                //MainActivity.access$702(MainActivity.this, 0.0F);
                k2 = 0.0F;
              if (MainActivity.this.k2 > 1.0F)
                //MainActivity.access$702(MainActivity.this, 1.0F);
                k2 = 1.0F;
            } catch (Exception exception) {
              //MainActivity.access$702(MainActivity.this, 0.0F);
              k2 = 0.0F;
            } 
            MainActivity.this.mSeekBarK2.setProgress((int)(MainActivity.this.k2 * 1000.0F));
          }
          
          public void beforeTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {}
          
          public void onTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {}
        });
    this.mEditTextK3.addTextChangedListener(new TextWatcher() {
      public void afterTextChanged(Editable param1Editable) {
        try {
          //MainActivity.access$702(MainActivity.this, Float.parseFloat(param1Editable.toString()));
          k3 = Float.parseFloat(param1Editable.toString());
          if (MainActivity.this.k3 < 0.0F)
            //MainActivity.access$702(MainActivity.this, 0.0F);
            k3 = 0.0F;
          if (MainActivity.this.k3 > 1.0F)
            //MainActivity.access$702(MainActivity.this, 1.0F);
            k3 = 1.0F;
        } catch (Exception exception) {
          //MainActivity.access$702(MainActivity.this, 0.0F);
          k3 = 0.0F;
        }
        MainActivity.this.mSeekBarK3.setProgress((int)(MainActivity.this.k3 * 1000.0F));
      }

      public void beforeTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {}

      public void onTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {}
    });
    this.mBtnReduceK1 = findViewById(R.id.btn_reduceK1);
    this.mBtnIncreaseK1 = findViewById(R.id.btn_increaseK1);
    this.mBtnReduceK2 = findViewById(R.id.btn_reduceK2);
    this.mBtnIncreaseK2 = findViewById(R.id.btn_increaseK2);
    this.mBtnReduceK3 = findViewById(R.id.btn_reduceK3);
    this.mBtnIncreaseK3 = findViewById(R.id.btn_increaseK3);
    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        public boolean onTouch(final View v, MotionEvent param1MotionEvent) {
          Log.d("KenHong", "onTouch, param1MotionEvent.getAction() = " + param1MotionEvent.getAction());

          if(param1MotionEvent.getAction() == ACTION_DOWN){
            mIsUp = false;
            (new Thread(new Runnable() {
              public void run() {
                while (!MainActivity.this.mIsUp.booleanValue()) {
                  switch (v.getId()) {
                    case R.id.btn_reduceK1:
                      //MainActivity.access$502(MainActivity.this, MainActivity.this.k1 - 0.001F);
                      k1 = k1 - 0.001F;
                      if (MainActivity.this.k1 < 0.0F)
                        //MainActivity.access$502(MainActivity.this, 0.0F);
                        k1 = 0.0F;
                      //MainActivity.access$502(MainActivity.this, Math.round(MainActivity.this.k1 * 1000.0F) / 1000.0F);
                      k1 = Math.round(k1 * 1000.0F) / 1000.0F;
                      MainActivity.this.mHandler.post(new Runnable() {
                        public void run() {
                          MainActivity.this.mSeekBarK1.setProgress((int)(MainActivity.this.k1 * 1000.0F));
                          EditText editText = MainActivity.this.mEditTextK1;
                          StringBuilder stringBuilder = new StringBuilder();
                          stringBuilder.append("");
                          stringBuilder.append(MainActivity.this.k1);
                          editText.setText(stringBuilder.toString());
                        }
                      });
                      break;
                    case R.id.btn_reduceK2:
                      //MainActivity.access$702(MainActivity.this, MainActivity.this.k2 - 0.001F);
                      k2 = k2 - 0.001F;
                      if (MainActivity.this.k2 < 0.0F)
                        //MainActivity.access$702(MainActivity.this, 0.0F);
                        k2 = 0.0F;
                      //MainActivity.access$702(MainActivity.this, Math.round(MainActivity.this.k2 * 1000.0F) / 1000.0F);
                      k2 = Math.round(k2 * 1000.0F) / 1000.0F;
                      MainActivity.this.mHandler.post(new Runnable() {
                        public void run() {
                          MainActivity.this.mSeekBarK2.setProgress((int)(MainActivity.this.k2 * 1000.0F));
                          EditText editText = MainActivity.this.mEditTextK2;
                          StringBuilder stringBuilder = new StringBuilder();
                          stringBuilder.append("");
                          stringBuilder.append(MainActivity.this.k2);
                          editText.setText(stringBuilder.toString());
                        }
                      });
                      break;
                    case R.id.btn_reduceK3:
                      k3 = k3 - 0.001F;
                      if (MainActivity.this.k3 < 0.0F)
                        k3 = 0.0F;
                      k3 = Math.round(k3 * 1000.0F) / 1000.0F;
                      MainActivity.this.mHandler.post(new Runnable() {
                        public void run() {
                          MainActivity.this.mSeekBarK3.setProgress((int)(MainActivity.this.k3 * 1000.0F));
                          EditText editText = MainActivity.this.mEditTextK3;
                          StringBuilder stringBuilder = new StringBuilder();
                          stringBuilder.append("");
                          stringBuilder.append(MainActivity.this.k3);
                          editText.setText(stringBuilder.toString());
                        }
                      });
                      break;
                    case R.id.btn_increaseK1:
                      Log.d("KenHong", "btn_increaseK1");
                      //MainActivity.access$502(MainActivity.this, MainActivity.this.k1 + 0.001F);
                      k1 = k1 + 0.001F;
                      if (MainActivity.this.k1 > 1.0F)
                        //MainActivity.access$502(MainActivity.this, 1.0F);
                        k1 = 1.0F;
                      //MainActivity.access$502(MainActivity.this, Math.round(MainActivity.this.k1 * 1000.0F) / 1000.0F);
                      k1 = Math.round(k1 * 1000.0F) / 1000.0F;
                      MainActivity.this.mHandler.post(new Runnable() {
                        public void run() {
                          MainActivity.this.mSeekBarK1.setProgress((int)(MainActivity.this.k1 * 1000.0F));
                          EditText editText = MainActivity.this.mEditTextK1;
                          StringBuilder stringBuilder = new StringBuilder();
                          stringBuilder.append("");
                          stringBuilder.append(MainActivity.this.k1);
                          editText.setText(stringBuilder.toString());
                        }
                      });
                      break;
                    case R.id.btn_increaseK2:
                      //MainActivity.access$702(MainActivity.this, MainActivity.this.k2 + 0.001F);
                      k2 = k2 + 0.001F;
                      if (MainActivity.this.k2 > 1.0F)
                        //MainActivity.access$702(MainActivity.this, 1.0F);
                        k2 = 1.0F;
                      //MainActivity.access$702(MainActivity.this, Math.round(MainActivity.this.k2 * 1000.0F) / 1000.0F);
                      k2 = Math.round(k2 * 1000.0F) / 1000.0F;
                      MainActivity.this.mHandler.post(new Runnable() {
                        public void run() {
                          MainActivity.this.mSeekBarK2.setProgress((int)(MainActivity.this.k2 * 1000.0F));
                          EditText editText = MainActivity.this.mEditTextK2;
                          StringBuilder stringBuilder = new StringBuilder();
                          stringBuilder.append("");
                          stringBuilder.append(MainActivity.this.k2);
                          editText.setText(stringBuilder.toString());
                        }
                      });
                      break;
                    case R.id.btn_increaseK3:
                      k3 = k3 + 0.001F;
                      if (MainActivity.this.k3 > 1.0F)
                        k3 = 1.0F;
                      k3 = Math.round(k3 * 1000.0F) / 1000.0F;
                      MainActivity.this.mHandler.post(new Runnable() {
                        public void run() {
                          MainActivity.this.mSeekBarK3.setProgress((int)(MainActivity.this.k3 * 1000.0F));
                          EditText editText = MainActivity.this.mEditTextK3;
                          StringBuilder stringBuilder = new StringBuilder();
                          stringBuilder.append("");
                          stringBuilder.append(MainActivity.this.k3);
                          editText.setText(stringBuilder.toString());
                        }
                      });
                      break;
                  }
                  try {
                    Thread.sleep(50L);
                  } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                  }
                }
              }
            })).start();
          }
          else if(param1MotionEvent.getAction() == ACTION_UP){
            mIsUp = true;
          }

          return true;
        }
      };
    this.mBtnReduceK1.setOnTouchListener(onTouchListener);
    this.mBtnIncreaseK1.setOnTouchListener(onTouchListener);
    this.mBtnReduceK2.setOnTouchListener(onTouchListener);
    this.mBtnIncreaseK2.setOnTouchListener(onTouchListener);
    this.mBtnReduceK3.setOnTouchListener(onTouchListener);
    this.mBtnIncreaseK3.setOnTouchListener(onTouchListener);
    MainActivity.this.setParams(null);
  }
  
  protected void onPause() {
    super.onPause();
    Log.d("KenHong", "onPause");
  }
  
  protected void onResume() {
    Log.d("KenHong", "onResume");
    super.onResume();
  }

  protected void onStop(){
    Log.d("KenHong", "onDestroy");
    super.onStop();
  }

  protected void onDestroy(){
    Log.d("KenHong", "onDestroy");
    super.onDestroy();
  }
  
  public void selectNextImage(View paramView) {
    this.mAntiDistortionViewPresentation.selectImage(1);
  }
  
  public void selectPreviousImage(View paramView) {
    this.mAntiDistortionViewPresentation.selectImage(-1);
  }
  
  public void setDefault(View paramView) {
    this.k1 = 0.0F;
    this.k2 = 0.0F;
    this.k3 = 0.0F;
    EditText editTextk1 = this.mEditTextK1;
    StringBuilder stringBuilderk1 = new StringBuilder();
    stringBuilderk1.append("");
    stringBuilderk1.append(this.k1);
    editTextk1.setText(stringBuilderk1.toString());

    EditText editTextk2 = this.mEditTextK2;
    StringBuilder stringBuilderk2 = new StringBuilder();
    stringBuilderk2.append("");
    stringBuilderk2.append(this.k2);
    editTextk2.setText(stringBuilderk2.toString());

    EditText editTextk3 = this.mEditTextK3;
    StringBuilder stringBuilderk3 = new StringBuilder();
    stringBuilderk3.append("");
    stringBuilderk3.append(this.k3);
    editTextk3.setText(stringBuilderk3.toString());

    this.mSeekBarK1.setProgress((int)(this.k1 * 1000.0F));
    this.mSeekBarK2.setProgress((int)(this.k2 * 1000.0F));
    this.mSeekBarK3.setProgress((int)(this.k3 * 1000.0F));
    this.size = 1280;

    EditText editTextWidth = this.mEditTextWidth;
    StringBuilder stringBuilderWidth = new StringBuilder();
    stringBuilderWidth.append("");
    stringBuilderWidth.append(this.size);
    editTextWidth.setText(stringBuilderWidth.toString());

    EditText editTextHeight = this.mEditTextHeight;
    StringBuilder stringBuilderHeight = new StringBuilder();
    stringBuilderHeight.append("");
    stringBuilderHeight.append(this.size);
    editTextHeight.setText(stringBuilderHeight.toString());

    if (this.mAntiDistortionViewPresentation != null) {
      this.mAntiDistortionViewPresentation.setSize(this.size);
      this.mAntiDistortionViewPresentation.computeDistortion(this.k1, this.k2, this.k3);
    } 
  }
  
  public void setParams(View paramView) {
    if (this.mAntiDistortionViewPresentation != null) {
      this.mAntiDistortionViewPresentation.setSize(this.size);
      this.mAntiDistortionViewPresentation.computeDistortion(this.k1, this.k2, this.k3);
    } 
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//      FileUtils1.getInstance(getApplicationContext()).copyAssetsToSD("samples", "samples").setFileOperateCallback(new FileUtils1.FileOperateCallback() {
//        @Override
//        public void onSuccess() {
//          checkExternalDisplay();
//        }
//        @Override
//        public void onFailed(String error) {
//        }
//      });
      checkExternalDisplay();
    }
  }
}


/* Location:              /Users/Ken/tools/bin/decompile-apk/output/base/base-dex2jar.jar!/com/ken/cpp/antidistortion/MainActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */