package com.example.myapplication;

import android.app.Activity;
import android.content.Context;

public interface ContextProvider {
    Context getContext();
    Activity getActivity();
}
