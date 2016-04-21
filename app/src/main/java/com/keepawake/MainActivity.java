package com.keepawake;

import android.app.Activity;
import android.content.Context;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity implements Runnable {

    private static final String TAG = "KeepAwakeActivity";

    private PowerManager.WakeLock wl;

    private TextView tView;
    private StringBuilder text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setContentView(R.layout.activity_main);
        this.tView = (TextView)findViewById(R.id.textView);
        this.text = new StringBuilder("Coucou");

        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                synchronized (text) {
                                    tView.setText(text.toString());
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Thread mThread = new Thread(this);
        mThread.start();
    }

    @Override
    public void run() {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
        this.wl.acquire();
        try {
            while (true) {
                Thread.sleep(2000);
                synchronized (text) {
                    this.text.setLength(0);
                    this.text.append("I'm still alive !");
                }
                Thread.sleep(2000);
                synchronized (text) {
                    this.text.setLength(0);
                    this.text.append("Hello world !");
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        this.wl.release();
        super.onDestroy();
    }
}
