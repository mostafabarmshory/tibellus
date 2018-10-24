package com.webpich.tibellus.seen.seo;

import java.util.Map;

import com.webpich.tibellus.seen.Collection;
import com.webpich.tibellus.seen.CollectionQuery;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface ISeoService {

	@GET("seo/contents")
	Call<Collection<Content>> getContents(@QueryMap(encoded = true) CollectionQuery query);

	@FormUrlEncoded
	@POST("seo/contents/{id}")
	Call<Content> updateContent(@Path("id") Long id, //
			@FieldMap Map<String, Object> content);

	@POST("seo/contents/{id}/content")
	Call<Content> uploadContent(@Path("id") Long id, //
			@Header("Content-Encoding") String contentEncoding, //
			@Body RequestBody photo);
}
