package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView tv;
    private Button bStart;
    private Button      bCancel;
    private ProgressBar pBar;
    //persists accross config changes
    DataVM myVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.textView2);
        bStart= findViewById(R.id.bStart);
        bCancel = findViewById(R.id.bCancel);
        pBar = findViewById(R.id.progressBar1);

        // Create a ViewModel the first time the system calls an activity's
        // onCreate() method.  Re-created activities receive the same
        // MyViewModel instance created by the first activity.
        myVM = new ViewModelProvider(this).get(DataVM.class);

        // Create the observer which updates the UI.
        final Observer<Integer> cntrObserver = new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable final Integer newInt) {
                // Update the UI, in this case, a TextView.
                tv.setText("The new cnt=" +Integer.toString(newInt));
                pBar.setProgress(newInt);
            }
        };
        //now observe
        myVM.getCurrentProgress().observe(this,cntrObserver);

        final Observer<Boolean> isThreadRunningObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                bStart.setEnabled(!aBoolean);
                bCancel.setEnabled(aBoolean);
                pBar.setProgress(0);
            }
        };
        //now observe
        myVM.getThreadState().observe(this,isThreadRunningObserver);
    }

//    now flesh out do start and doStop to start and stop a thread
    public void doStart(View view) {
        myVM.startThread();
        pBar.setProgress(0);

    }

    public void doCancel(View view) {
        myVM.stopThread();
    }
}