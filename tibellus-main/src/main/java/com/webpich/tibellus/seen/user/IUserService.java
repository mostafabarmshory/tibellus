package com.webpich.tibellus.seen.user;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface IUserService {
	@FormUrlEncoded
	@POST("user/login")
	Call<Account> login(@Field("login") String login, @Field("password") String password);

	@GET("user/accounts/current")
	Call<Account> getCurrentAccount();
}
