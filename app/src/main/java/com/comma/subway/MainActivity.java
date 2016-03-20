package com.comma.subway;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import butterknife.Bind;
import com.comma.subway.network.SubwayApi;
import java.util.Locale;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{

  private TextToSpeech mEngine;
  @Bind(R.id.tts_edittext) EditText mTtsInput;
  @Bind(R.id.tts_button) Button mTtsBtn;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    new Handler(Looper.myLooper()).post(new Runnable() {
      @Override public void run() {
        final SubwayApi r = SubwayApplication.mRestAdapterBuilder.build().create(SubwayApi.class);
        String uri = "http://swopenapi.seoul.go.kr/api/subway/4170517367786b64343068584e6949/json/realtimeStationArrival/0/5/%ED%9A%8C%EA%B8%B0서울";
        Response response = r.me(uri, null);
        Log.d("koo", response.getBody().toString());
      }
    });

    mTtsBtn.setOnClickListener(
        view -> {
          final String inputText = mTtsInput.getText().toString();
          if(!TextUtils.isEmpty(inputText)){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
              mEngine.speak(inputText, TextToSpeech.QUEUE_FLUSH, null, null);
            }else{
              mEngine.speak(inputText, TextToSpeech.QUEUE_FLUSH, null);
            }
          }
        }
        );
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
