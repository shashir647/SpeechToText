package com.quantuminventions.speechtotext;

import android.test.ActivityInstrumentationTestCase2;
import androidx.appcompat.widget.LinearLayoutCompat;
import com.quantuminventions.speechtotext.ui.activity.MainActivity;

public class MainActivityTest extends
        ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mMainActivity;
    private LinearLayoutCompat llSpeak;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mMainActivity = getActivity();
        llSpeak =  mMainActivity.findViewById(R.id.ll_speak);
    }

    public void testPreconditions() {
        assertNotNull("HomeActivity is null", mMainActivity);
        assertNotNull("Tap to Speak View is null", llSpeak);
    }

    public void testTapToSpeakEvent() {
        mMainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                llSpeak.performClick();
            }
        });
    }
}
