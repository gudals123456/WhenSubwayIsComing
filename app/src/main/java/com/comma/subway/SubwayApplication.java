package com.comma.subway;

import android.app.Application;
import android.os.AsyncTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import java.io.File;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Created by jahun on 2016. 3. 20..
 */
public class SubwayApplication extends Application{

  //http://swopenapi.seoul.go.kr/api/subway/4170517367786b64343068584e6949/json/realtimeStationArrival/0/5/%ED%9A%8C%EA%B8%B0서울
  public static String DOMAIN = "http://swopenapi.seoul.go.kr";

  public static RestAdapter.Builder mRestAdapterBuilder;

  @Override public void onCreate() {
    super.onCreate();
    SynchronousQueue<Runnable> queue = new SynchronousQueue<>();
    ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 30, 15, TimeUnit.SECONDS, queue);
    mRestAdapterBuilder = new RestAdapter.Builder().setEndpoint(DOMAIN)
        .setClient(new OkClient(getHttpClient()))
        .setExecutors(AsyncTask.THREAD_POOL_EXECUTOR, executor)
        .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
        .setConverter(new GsonConverter(getGson()));
  }

  private OkHttpClient getHttpClient() {
    OkHttpClient client = new OkHttpClient();
    try {
      int cacheSize = 10 * 1024 * 1024; // 10 MB
      File cacheDirectory = new File(this.getCacheDir().getAbsolutePath(), "HttpCache");
      Cache cache = new Cache(cacheDirectory, cacheSize);
      client.setCache(cache);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return client;
  }

  public Gson getGson() {
    return new GsonBuilder().serializeNulls()
        //.registerTypeAdapter(CardItem.class, new CardItemDeserializer())
        //.registerTypeAdapter(UserAction.class, new UserActionDeserializer())
        //.registerTypeAdapter(PredictedRatings.class, new PredictedRatingsDeserializer())
        //.registerTypeAdapter(UserBase.class, new UserBaseDeserializer())
        //.registerTypeAdapter(UserSmall.class, new SmallUserDeserializer())
        //.registerTypeAdapter(DeckBase.class, new DeckDeserializer())
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss+09:00")
        .create();
  }

}
