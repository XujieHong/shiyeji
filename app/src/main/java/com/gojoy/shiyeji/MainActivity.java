package com.gojoy.shiyeji;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private DisplayManager mDisplayManager;
    private Handler mHandler;
    private ShiyejiPresentation mPresentation = null;

    private DisplayManager.DisplayListener mDisplayListener = new DisplayManager.DisplayListener() {
        public void onDisplayAdded(int param1Int) {
            Display display = MainActivity.this.mDisplayManager.getDisplay(param1Int);
            if (display != null)
                MainActivity.this.showPresentation(display);
        }

        public void onDisplayChanged(int param1Int) {}

        public void onDisplayRemoved(int param1Int) {
            if (mPresentation != null) {
                mPresentation.dismiss();
                mPresentation = null;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHandler = new Handler();
        mDisplayManager = (DisplayManager)getSystemService("display");
        mDisplayManager.registerDisplayListener(this.mDisplayListener, this.mHandler);
        checkExternalDisplay();

//        findViewById(R.id.confirm_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                EditText editText = findViewById(R.id.asb_text);
//                int asb = Integer.parseInt(editText.getText().toString());
//                mPresentation.showAsb(asb);
//            }
//        });

        findViewById(R.id.loop_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresentation.loopPlay();
            }
        });

    }

    private void showPresentation(Display paramDisplay) {
        mPresentation = new ShiyejiPresentation(this, paramDisplay);
        mPresentation.show();
    }

    private void checkExternalDisplay() {
        Display[] arrayOfDisplay = this.mDisplayManager.getDisplays();
        if (arrayOfDisplay.length > 1)
            showPresentation(arrayOfDisplay[1]);
    }
}