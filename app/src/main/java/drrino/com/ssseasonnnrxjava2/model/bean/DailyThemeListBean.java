package drrino.com.ssseasonnnrxjava2.model.bean;

/**
 * Created by Dell1 on 2017/5/17.
 */

public class DailyThemeListBean {
    DailyListBean dailyListBean;
    ThemeListBean themeListBean;


    public DailyThemeListBean(DailyListBean dailyListBean, ThemeListBean themeListBean) {
        this.dailyListBean = dailyListBean;
        this.themeListBean = themeListBean;
    }


    public DailyListBean getDailyListBean() {
        return dailyListBean;
    }


    public void setDailyListBean(DailyListBean dailyListBean) {
        this.dailyListBean = dailyListBean;
    }


    public ThemeListBean getThemeListBean() {
        return themeListBean;
    }


    public void setThemeListBean(ThemeListBean themeListBean) {
        this.themeListBean = themeListBean;
    }
}
