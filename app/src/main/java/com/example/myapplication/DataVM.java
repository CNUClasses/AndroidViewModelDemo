package com.example.myapplication;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DataVM extends ViewModel {
    private MyThread mt;
    //lets add some livedata
    private MutableLiveData<Integer> progress ;
    public MutableLiveData<Integer> getCurrentProgress() {
        return progress;
    }

    //if any observer wants to react to a running thread
    // (for instance if an activity wants to configure its start and stop buttons)
    private MutableLiveData<Boolean> isThreadRunning;
    public MutableLiveData<Boolean> getThreadState() {
        return isThreadRunning;
    }
    public DataVM() {
        super();

        //initialize as appropriate
        progress = new MutableLiveData<Integer>();
        progress.setValue(0);    //initialize

        //a tough one is the thread running or not
        isThreadRunning = new MutableLiveData<Boolean>();
        isThreadRunning.setValue(false);
    }

     //    We want the DataVM to be able to run 1 thread at a time and be able to stop that thread as well, so add a couple of methods to DataVM
    public void startThread(){
        //if I have not started a thread
        //or if I have and its finished
        //then start another
        if(mt==null || !mt.isAlive()) {
            mt = new MyThread();
            mt.start();
            isThreadRunning.setValue(true);
        }
    }

    public void stopThread(){
        //if null we are done
        if(mt==null)
            return;

        //ask for it to stop
        mt.stopRequested=true;

        try {
            //wait until its done
            //this is risky, it may take a while
            //your thread has to be designed to
            //handle this
            mt.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mt=null;  //GC this thread

        isThreadRunning.setValue(false);
    }

    public class MyThread extends Thread {

        //used to ask thread to stop
        public boolean stopRequested=false;
        private int NUMBER_TICKS=100;

        public void run() {
            for (int i=0;i<NUMBER_TICKS;i++){
                //if MainACtivity has asked us to stop then break
                if(stopRequested)
                    break;

                //need to notify Mainactivity of progress
                //can only postValue from background thread cannot use setValue
                progress.postValue(i);
                //sleep for 1/2 sec
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //need to notify mainactivity that we are done
            //so it can enable/disable buttons
            //if we are done then say so
            isThreadRunning.postValue(false);
        }
    }

}



