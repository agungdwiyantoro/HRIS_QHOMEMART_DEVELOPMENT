package com.app.mobiledev.apphris.test;

import android.util.Log;
import android.view.View;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

  private int mCurrentPosition;

  public BaseViewHolder(View itemView) {
    super(itemView);
  }

  protected abstract void clear();

  public void onBind(int position) {
    Log.d("CEK_ADD_ITEMS", "addItems: "+position);
    mCurrentPosition = position;
    clear();
  }

  public int getCurrentPosition() {
    return mCurrentPosition;
  }
}

