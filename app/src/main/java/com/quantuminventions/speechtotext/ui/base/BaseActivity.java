package com.quantuminventions.speechtotext.ui.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.quantuminventions.speechtotext.R;

public abstract class BaseActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private AppCompatTextView tvToolbarText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        setupToolbar();
        onActivityCreate();
    }


    /**
     * Its common use a toolbar within activity, if it exists in the
     * layout this will be configured
     */
    public void setupToolbar() {
        mToolbar =  findViewById(R.id.toolbar);
        tvToolbarText = findViewById(R.id.tv_toolbar_text);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }

    protected void setToolbarText(String toolbarTxt) {
        tvToolbarText.setText(toolbarTxt);
    }

    @Nullable
    public Toolbar getToolbar() {
        return mToolbar;
    }

    /**
     * @return The layout id that's gonna be the activity view.
     */
    protected abstract int getLayoutId();

    protected abstract void onActivityCreate();
}
