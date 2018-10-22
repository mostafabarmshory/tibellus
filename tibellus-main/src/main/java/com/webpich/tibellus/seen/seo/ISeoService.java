package com.webpich.tibellus.seen.seo;

import com.webpich.tibellus.seen.Collection;
import com.webpich.tibellus.seen.CollectionQuery;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface ISeoService {

	@GET("seo/contents")
	Call<Collection<Content>> getContents(@QueryMap(encoded = true) CollectionQuery query);
}
