package com.comma.subway;

import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{

  private TextToSpeech mEngine;
  private EditText mTtsInput;
  private Button mTtsBtn;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mTtsInput = (EditText) findViewById(R.id.tts_edittext);
    mTtsBtn = (Button) findViewById(R.id.tts_button);
    mTtsBtn.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        final String inputText = mTtsInput.getText().toString();
        if(!TextUtils.isEmpty(inputText)){
          if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            mEngine.speak(inputText, TextToSpeech.QUEUE_FLUSH, null, null);
          }else{
            mEngine.speak(inputText, TextToSpeech.QUEUE_FLUSH, null);
          }
        }
      }
    });
  }

  @Override public void onInit(int status) {
    Log.d("koo", "OnInit - Status ["+status+"]"+ "TextToSpeech.SUCCESS:"+TextToSpeech.SUCCESS);
    if(status == TextToSpeech.SUCCESS){
      mEngine.setLanguage(Locale.KOREAN);
      mEngine.setSpeechRate(1f);
    }
  }

  @Override protected void onStart() {
    super.onStart();
    mEngine = new TextToSpeech(this, this);
  }

  @Override protected void onStop() {
    super.onStop();
    mEngine.shutdown();
  }
}
