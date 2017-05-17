package drrino.com.ssseasonnnrxjava2.model;

import drrino.com.ssseasonnnrxjava2.model.bean.DailyListBean;
import drrino.com.ssseasonnnrxjava2.model.bean.ThemeListBean;
import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by Dell1 on 2017/5/16.
 */

public interface Api {
    String HOST = "http://news-at.zhihu.com/api/4/";

    /**
     * 最新日报
     */
    @GET("news/latest")
    Observable<DailyListBean> getDailyList();

    /**
     * 主题日报
     */
    @GET("themes")
    Observable<ThemeListBean> getThemeList();
}
