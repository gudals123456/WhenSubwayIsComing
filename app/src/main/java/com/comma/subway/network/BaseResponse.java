package com.comma.subway.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by jahun on 2016. 3. 20..
 */
public class BaseResponse {

  public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

  @SerializedName("realtimeArrivalList") private List<RealTimeArrival> realtimeArrivalList; //


  @Override public String toString() {
    return gson.toJson(this);
  }

}
