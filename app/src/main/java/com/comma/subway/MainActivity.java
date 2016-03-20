package com.comma.subway;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.comma.subway.network.SubwayApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;
import retrofit.client.Response;
import rx.Observable;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{

  private TextToSpeech mEngine;
  private SpeechRecognizer mRecognizer;
  private String mHeardText;

  private ArrayList<String> mRightQuestionList;

  @Bind(R.id.tts_edittext) EditText mTtsInput;
  @Bind(R.id.tts_button) Button mTtsBtn;
  @Bind(R.id.stt_button) Button mSttBtn;
  @Bind(R.id.stt_result_check_button) Button mSttResultCheckBtn;

  @Override
  protected void onDestroy() {
    super.onDestroy();

    ButterKnife.unbind(this);

  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    setRightQuestionList();

  //------------- for STT start

    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
    intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

    mSttBtn.setOnClickListener(
            view -> {

              mRecognizer.startListening(intent);

            }
    );

    //------------- for STT end
    mTtsBtn.setOnClickListener(
            view -> {
              final String inputText = mTtsInput.getText().toString();
              if (!TextUtils.isEmpty(inputText)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                  mEngine.speak(inputText, TextToSpeech.QUEUE_FLUSH, null, null);
                } else {
                  mEngine.speak(inputText, TextToSpeech.QUEUE_FLUSH, null);
                }
              }
            }
    );
  }

  private void recognizeQuestion(String heardText) {

    if(mRightQuestionList.contains(heardText)) {
      Log.d("kim", "다음 지하철은 3분후 입니다(예시)");
      // 자훈이형이 짠 코드 실행
      request();
    }
    else {
      Log.d("kim","인식 되지 않음");
    }

  }

  private void setRightQuestionList () {
    mRightQuestionList = new ArrayList<String>();

    mRightQuestionList.add("다음 지하철 언제 와");
    mRightQuestionList.add("다음 지하철 혼자");
    mRightQuestionList.add("다음 지하철 언제야");
    mRightQuestionList.add("다음 지하철 언제");
    mRightQuestionList.add("다음 지하철");
    mRightQuestionList.add("언제");
    mRightQuestionList.add("언제야");
    mRightQuestionList.add("언제 와");

  }

  private void request(){
    String uri = "api/subway/4170517367786b64343068584e6949/json/realtimeStationArrival/0/5/%EC%84%9C%EC%9A%B8";
    Observable.just(uri)
        .observeOn(Schedulers.newThread())
//        .map(url -> {
//          final SubwayApi r = SubwayApplication.mRestAdapterBuilder.build().create(SubwayApi.class);
//          //http://data.seoul.go.kr/openinf/openapiview.jsp?infId=OA-12764&tMenu=11
//          Response response = r.me(url, null);
//          //final SubwayApi r = SubwayApplication.mRestAdapterBuilder.build().create(SubwayApi.class);
//          ////http://data.seoul.go.kr/openinf/openapiview.jsp?infId=OA-12764&tMenu=11
//          //Response response = r.me(url, null);
//          //Log.d("koo", response.toString());
//          //Log.d("koo", "koo test::"+responseStr);
//          return response.toString();
//        })
        .subscribeOn(Schedulers.newThread())
        .subscribe(url -> {
          final SubwayApi r = SubwayApplication.mRestAdapterBuilder.build().create(SubwayApi.class);
          Response response = r.me(url, null);
          try{
            String text = fromStream(response.getBody().in());
            Log.d("kim", text);

            if (!TextUtils.isEmpty(text)) {
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mEngine.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
              } else {
                mEngine.speak(text, TextToSpeech.QUEUE_FLUSH, null);
              }
            }





          }catch(Exception e){

          }

        }, throwable -> {
          throwable.printStackTrace();
        });
  }

  private static String fromStream(InputStream in) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    StringBuilder out = new StringBuilder();
    String newLine = System.getProperty("line.separator");
    String line;
    while ((line = reader.readLine()) != null) {
      out.append(line);
      out.append(newLine);
    }
    return out.toString();
  }
  private RecognitionListener listener = new RecognitionListener() {
    @Override
    public void onReadyForSpeech(Bundle params) {

    }

    @Override
    public void onBeginningOfSpeech() {
      Log.d("kim","begin speech");
    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {
      Log.d("kim", "end speech");

    }

    @Override
    public void onError(int error) {

    }

    @Override
    public void onResults(Bundle results) {
      ArrayList<String> resultList = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

      Log.d("kim",resultList.get(0));
      mHeardText = resultList.get(0);
      mTtsInput.setText(mHeardText);

      recognizeQuestion(mHeardText);

    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }
  };

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

    mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
    mRecognizer.setRecognitionListener(listener);

  }

  @Override protected void onStop() {
    super.onStop();
    mEngine.shutdown();
  }
}
