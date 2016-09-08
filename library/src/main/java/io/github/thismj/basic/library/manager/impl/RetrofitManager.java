package io.github.thismj.basic.library.manager.impl;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.github.thismj.basic.library.BasicApplication;
import io.github.thismj.basic.library.BasicConstant;
import io.github.thismj.basic.library.manager.BasicManager;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * ╭══╮　┌═════┐
 * ╭╯上车║═║老司机专用║
 * └══⊙═⊙═~----╰⊙═⊙╯
 * ----------------
 * 此处写用途
 *
 * @author tangmingjian
 * @version v1.0
 * @date 2016-09-06 16:30
 */

public class RetrofitManager extends BasicManager {
    private Retrofit mRetrofit;

    /**
     * 连接超时30秒
     */
    private static final long TIME_OUT = 30;

    public static RetrofitManager get() {
        return BasicApplication.get().getManager(BasicConstant.RETROFIT_MANAGER);
    }

    @Override
    public void onCreate(BasicApplication appContext) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {

                        Request.Builder builder = chain.request().newBuilder();

                        builder.addHeader("token", "");

                        builder.addHeader("client_type", "");

                        builder.addHeader("uid", "");

                        return chain.proceed(builder.build());
                    }
                })
                .build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(BasicConstant.FEED_ENDPOINT)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public <T> T create(Class<T> service) {
        return mRetrofit.create(service);
    }
}
