package com.quantuminventions.speechtotext.ui.activity;

import android.content.Intent;
import androidx.appcompat.widget.AppCompatTextView;
import com.quantuminventions.speechtotext.R;
import com.quantuminventions.speechtotext.ui.base.BaseActivity;
import com.quantuminventions.speechtotext.utils.AppUtils;
import java.text.MessageFormat;
import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends BaseActivity {

    private static final long SPLASH_TIMEOUT = 1500;
    private Timer thread;

    private AppCompatTextView tvAppVersion;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onActivityCreate() {
        initView();
        setAppVersion();
        startSplashTimer();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (thread != null) {
            thread.cancel();
            thread.purge();
        }
    }

    /**
     * Initialize Views with their IDs
     */
    private void initView() {
        tvAppVersion =  findViewById(R.id.tv_app_version);
    }

    /**
     * To Show the App Version at Splash Screen.
     */
    private void setAppVersion() {
        tvAppVersion.setText(MessageFormat.format("V {0}", AppUtils.getAppVersionName(this)));
    }

    /**
     * Start the Splash Timer to wait for few second to move to the new screen
     */
    private void startSplashTimer() {
        thread = new Timer();
        thread.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }, SPLASH_TIMEOUT);
    }
}
