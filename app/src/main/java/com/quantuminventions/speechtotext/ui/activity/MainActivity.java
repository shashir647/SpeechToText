package com.quantuminventions.speechtotext.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.quantuminventions.speechtotext.R;
import com.quantuminventions.speechtotext.app.AppConstant;
import com.quantuminventions.speechtotext.dictionary.Dictionary;
import com.quantuminventions.speechtotext.model.DictionaryModel;
import com.quantuminventions.speechtotext.ui.adapter.DictionaryListAdapter;
import com.quantuminventions.speechtotext.ui.base.BaseActivity;
import com.quantuminventions.speechtotext.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import static com.quantuminventions.speechtotext.app.AppConstant.INTENT_KEY_SPEECH_INPUT_RESULT;
import static com.quantuminventions.speechtotext.app.AppConstant.REQ_SPEECH_INPUT_RESULT;

public class MainActivity extends BaseActivity implements View.OnClickListener {


    private LinearLayoutCompat llSpeak;
    private RecyclerView rvDictionaryList;
    private DictionaryListAdapter dictionaryListAdapter;
    private ArrayList<DictionaryModel> dictionaryArrayData;
    private long back_pressed;
    private ProgressDialog progressDialog;

    private Dictionary dictionary;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onActivityCreate() {
        AndroidNetworking.initialize(getApplicationContext());
        dictionaryArrayData= new ArrayList<>();
        initView();
        initClickListener();
        createDictionaryNew();

    }



    @Override
    protected void onResume() {
        super.onResume();
        setToolbarText(getString(R.string.app_name));
    }

    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()) {
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            super.onBackPressed();
        } else {
            AppUtils.showToast(this, getString(R.string.press_again_to_exit));
        }
        back_pressed = System.currentTimeMillis();

    }

    /**
     * Initialize Views with their IDs
     */
    private void initView() {
        llSpeak =  findViewById(R.id.ll_speak);
        rvDictionaryList =  findViewById(R.id.rv_dictionary_list);
        rvDictionaryList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    /**
     * Initialize Click Listeners for UI Views
     */
    private void initClickListener() {
        llSpeak.setOnClickListener(this);
    }

    /**
     * Create the dictionary with predefined word set
     */
    private void createDictionaryNew() {
        AndroidNetworking.get(AppConstant.getDictionaryUrl(MainActivity.this))
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            JSONArray jsonArray = response.getJSONArray("dictionary");
                            for (int i =0 ;i<jsonArray.length();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                DictionaryModel dictionaryModel = new DictionaryModel
                                        (jsonObject.getString("word"),jsonObject.getInt("frequency"));
                                dictionaryModel.setDictionaryWord(jsonObject.getString("word"));
                                dictionaryModel.setSpeechFrequency(jsonObject.getInt("frequency"));
                                dictionaryArrayData.add(dictionaryModel);

                            }
                            dictionary = new Dictionary();
                            dictionaryArrayData = dictionary.createDictionaryNew(dictionaryArrayData);
                            dictionaryListAdapter = new DictionaryListAdapter(MainActivity.this,dictionaryArrayData);
                            rvDictionaryList.setAdapter(dictionaryListAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Toast.makeText(MainActivity.this,error.getResponse().message(),Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ll_speak) {
            if (AppUtils.checkNetworkConnection(this)) {
                Intent intent = new Intent(this, SpeechActivity.class);
                startActivityForResult(intent, REQ_SPEECH_INPUT_RESULT);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            } else
                AppUtils.showToast(this, getString(R.string.error_network_connection));
        }
    }

    /**
     * Handle Activity result from Speech Activity
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQ_SPEECH_INPUT_RESULT) {
                showProgress();
                new Handler().postDelayed(new Runnable() { //Used just for showing a UI Progress dialog, in order to better user experience
                    @Override
                    public void run() {
                        if (data != null)
                            checkWordInDictionary(data.getStringExtra(INTENT_KEY_SPEECH_INPUT_RESULT));
                    }
                }, 1000);
            }
        }
    }

    /**
     * Method used to check whether the spoken word(s) is present in the dictionary or not
     *
     * @param speechText
     */
    private void checkWordInDictionary(String speechText) {
        if (dictionaryArrayData != null) {
            HashMap<Integer, Integer> updatedPositions = dictionary.getDictionaryMatchPosition(speechText);

            //Clear All status Update Value in the list
            for (int i = 0; i < dictionaryArrayData.size(); i++) {
                dictionaryArrayData.get(i).setUpdated(false);
            }

            //Push the status whether the item is updated or not
            for (Integer integer : updatedPositions.keySet()) {
                int key = (int) integer;
                int value = updatedPositions.get(key);
                dictionaryArrayData.get(value).setUpdated(true);
            }
            dictionaryListAdapter.notifyDataSetChanged();
            hideProgress();
            //Sort the after item check in Descending order
            Collections.sort(dictionaryArrayData, new Comparator< DictionaryModel >() {
                @Override public int compare(DictionaryModel p1, DictionaryModel p2) {
                    return p2.getSpeechFrequency()- p1.getSpeechFrequency(); // Descending
                }
            });
            //Check if the word(s) is not present in the dictionary
            if (updatedPositions.isEmpty())
                AppUtils.showToast(this, getString(R.string.error_no_matching_word));
        } else
            AppUtils.showToast(this, getString(R.string.error_something_went_wrong));
    }

    /**
     * To Show Progress Dialog
     */
    private void showProgress() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.speech_progress_msg));
        progressDialog.setCancelable(false);
        if (!isFinishing())
            progressDialog.show();
    }

    /**
     * To hide progress dialog
     */
    private void hideProgress() {
        if (progressDialog != null) {
            if (progressDialog.isShowing())
                if (!isFinishing())
                    progressDialog.hide();
        }
    }
}