package com.belatrixsf.events.di.module;

import com.belatrixsf.events.BuildConfig;
import com.belatrixsf.events.data.datasource.rest.retrofit.api.EventAPI;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by diegoveloper on 3/31/17.
 */

@Module
public class RetrofitModule {

    @Singleton
    @Provides
    public OkHttpClient providesOkHttpClient() {
        return new OkHttpClient();
    }

    @Singleton
    @Provides
    public Retrofit provideRetrofit(OkHttpClient okHttpClient) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        }
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        /*
        OkHttpClient.Builder builder = okHttpClient.newBuilder();
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {


                return chain.proceed(chain.request());
            }
        });*/
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BuildConfig.BASE_URL)
                .client(client)
                .build();
    }

    @Singleton
    @Provides
    public EventAPI provideEventAPI(Retrofit retrofit) {
        return retrofit.create(EventAPI.class);
    }

}