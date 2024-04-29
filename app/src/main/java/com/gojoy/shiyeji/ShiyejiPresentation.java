package com.gojoy.shiyeji;

import android.app.Presentation;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;

import com.gojoy.shiyeji.PresentationView;
import com.gojoy.shiyeji.R;

public class ShiyejiPresentation extends Presentation {
  private Context mContext;
  private PresentationView mPresentationView;
  
  public ShiyejiPresentation(Context paramContext, Display paramDisplay) {
    super(paramContext, paramDisplay);
    this.mContext = paramContext;
  }

  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(R.layout.presentation_layout);
    mPresentationView = findViewById(R.id.presentation_view);
  }

  protected void onStart() {
    super.onStart();
  }
  
  protected void onStop() {
    super.onStop();
  }

  public void showAsb(int asb) {
    mPresentationView.showAsb(asb);
  }

  public void loopPlay() {
    mPresentationView.loopPlay();
  }
}