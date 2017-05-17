package drrino.com.ssseasonnnrxjava2.model.http;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import drrino.com.ssseasonnnrxjava2.App;
import drrino.com.ssseasonnnrxjava2.BuildConfig;
import drrino.com.ssseasonnnrxjava2.model.Api;
import drrino.com.ssseasonnnrxjava2.model.bean.DailyListBean;
import drrino.com.ssseasonnnrxjava2.model.bean.ThemeListBean;
import drrino.com.ssseasonnnrxjava2.utils.SystemUtils;
import io.reactivex.Observable;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Dell1 on 2017/5/16.
 */

public class RetrofitHelper {
    private static OkHttpClient okHttpClient = null;
    private static Api apiService = null;

    private static final String PATH_DATA = App.getInstance().getCacheDir().getAbsolutePath() +
        File.separator + "data";
    private static final String PATH_CACHE = PATH_DATA + "/NetCache";


    public RetrofitHelper() {
        init();
    }


    private void init() {
        initOkHttp();
        apiService = getApiService(Api.HOST, Api.class);
    }


    private void initOkHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
            builder.addInterceptor(loggingInterceptor);
        }
        // http://www.jianshu.com/p/93153b34310e
        File cacheFile = new File(PATH_CACHE);
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
        Interceptor cacheInterceptor = new Interceptor() {
            @Override public Response intercept(Chain chain) throws IOException {

                Request request = chain.request();
                if (!SystemUtils.isNetworkConnected()) {
                    request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
                }
                Response response = chain.proceed(request);
                if (SystemUtils.isNetworkConnected()) {
                    int maxAge = 0;
                    // 有网络时, 不缓存, 最大保存时长为0
                    response.newBuilder()
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .removeHeader("Pragma")
                        .build();
                } else {
                    // 无网络时，设置超时为4周
                    int maxStale = 60 * 60 * 24 * 28;
                    response.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .removeHeader("Pragma")
                        .build();
                }
                return response;
            }
        };
        //设置缓存
        builder.addNetworkInterceptor(cacheInterceptor);
        builder.addInterceptor(cacheInterceptor);
        builder.cache(cache);
        //设置超时
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
        okHttpClient = builder.build();
    }


    private <T> T getApiService(String baseUrl, Class<T> clz) {
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();
        return retrofit.create(clz);
    }


    public Observable<DailyListBean> fetchDailyListInfo() {
        return apiService.getDailyList();
    }

    public Observable<ThemeListBean> fetchThemesListInfo() {
        return apiService.getThemeList();
    }
}
