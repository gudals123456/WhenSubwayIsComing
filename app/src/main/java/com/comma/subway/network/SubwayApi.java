package com.comma.subway.network;

import java.util.Map;
import retrofit.client.Response;
import retrofit.http.EncodedPath;
import retrofit.http.GET;
import retrofit.http.QueryMap;

/**
 * Created by jahun on 2016. 3. 20..
 */
public interface SubwayApi {

  // GET My User Data
  @GET("/{path}") Response me(@EncodedPath("path") String path, @QueryMap Map<String, String> params);
}
