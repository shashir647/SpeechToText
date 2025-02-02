package com.quantuminventions.speechtotext.ui.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Handler;
import android.speech.RecognizerIntent;
import androidx.appcompat.widget.AppCompatTextView;
import com.quantuminventions.speechtotext.R;
import com.quantuminventions.speechtotext.ui.base.BaseActivity;
import com.quantuminventions.speechtotext.utils.AppUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Locale;

import static com.quantuminventions.speechtotext.app.AppConstant.INTENT_KEY_SPEECH_INPUT_RESULT;
import static com.quantuminventions.speechtotext.app.AppConstant.REQ_CODE_SPEECH_INPUT;

public class SpeechActivity extends BaseActivity {

    private AppCompatTextView tvUserSpeaksData;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_speech;
    }

    @Override
    protected void onActivityCreate() {
        initView();
        startVoiceInput();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setToolbarText(getString(R.string.app_name));
    }

    /**
     * Initialize Views with their IDs
     */
    private void initView() {
        tvUserSpeaksData = findViewById(R.id.tv_user_speaks_data);
    }

    /**
     * Start the voice input process
     */
    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            a.printStackTrace();
            AppUtils.showToast(this, getString(R.string.error_speech_input));
        }
    }

    /**
     * Handle the result after Speech being detected
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && null != data) {
                final ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                assert result != null;
                final String speechInput = result.get(0);
                tvUserSpeaksData.setText(MessageFormat.format("''{0}''", speechInput));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra(INTENT_KEY_SPEECH_INPUT_RESULT, speechInput);
                        setResult(RESULT_OK, resultIntent);
                        finish();
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                }, 1200);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SpeechActivity.this,MainActivity.class));
        finish();
    }
}
