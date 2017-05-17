package drrino.com.ssseasonnnrxjava2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import drrino.com.ssseasonnnrxjava2.model.bean.DailyListBean;
import drrino.com.ssseasonnnrxjava2.model.bean.ThemeListBean;
import drrino.com.ssseasonnnrxjava2.model.http.RetrofitHelper;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "TAG";

    private TextView tv;

    private RetrofitHelper retrofitHelper = new RetrofitHelper();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.tv);

        //demo1();
        //demo2();
        //demo3();
        //demo4();
        //demo5();
        //demo6();
        //demo7();
        //demo8();
        //demo9();
        //demo10();
        //demo11();
        demo12();
    }


    /**
     * onComplete和onError唯一并且互斥
     * 当上游发送了一个onError后, 上游onError之后的事件将继续发送, 而下游收到onError事件之后将不再继续接收事件
     * 上游可以不发送onComplete或onError.
     **/
    public void demo1() {
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override public void subscribe(@NonNull ObservableEmitter<Integer> e)
                throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                e.onComplete();
            }
        });
        Observer<Integer> observer = new Observer<Integer>() {
            @Override public void onSubscribe(@NonNull Disposable d) {
                Log.e(TAG, "subscribe");
            }


            @Override public void onNext(@NonNull Integer integer) {
                Log.e(TAG, String.valueOf(integer));
            }


            @Override public void onError(@NonNull Throwable e) {
                Log.e(TAG, "error");
            }


            @Override public void onComplete() {
                Log.e(TAG, "complete");
            }
        };
        observable.subscribe(observer);
    }


    /**
     * 简化写法
     **/
    public void demo2() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override public void subscribe(@NonNull ObservableEmitter<Integer> e)
                throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                e.onComplete();
            }
        }).subscribe(new Observer<Integer>() {
            @Override public void onSubscribe(@NonNull Disposable d) {
                Log.e(TAG, "subscribe");
            }


            @Override public void onNext(@NonNull Integer integer) {
                Log.e(TAG, String.valueOf(integer));
            }


            @Override public void onError(@NonNull Throwable e) {
                Log.e(TAG, "error");
            }


            @Override public void onComplete() {
                Log.e(TAG, "complete");
            }
        });
    }


    /**
     * 当Disposable调用dispose()并不会导致上游不再继续发送事件, 上游会继续发送剩余的事件.下游收不到事件
     */
    public void demo3() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override public void subscribe(@NonNull ObservableEmitter<Integer> emitter)
                throws Exception {
                Log.e(TAG, "emit 1");
                emitter.onNext(1);
                Log.e(TAG, "emit 2");
                emitter.onNext(2);
                Log.e(TAG, "emit 3");
                emitter.onNext(3);
                Log.e(TAG, "emit complete");
                emitter.onComplete();
                Log.e(TAG, "emit 4");
                emitter.onNext(4);
            }
        }).subscribe(new Observer<Integer>() {
            private Disposable mDisposable;
            private int i;


            @Override public void onSubscribe(@NonNull Disposable d) {
                Log.e(TAG, "subscribe");
                mDisposable = d;
            }


            @Override public void onNext(@NonNull Integer integer) {
                Log.e(TAG, "onNext: " + integer);
                i++;
                if (i == 2) {
                    Log.e(TAG, "dispose");
                    mDisposable.dispose();
                    Log.e(TAG, "isDisposed : " + mDisposable.isDisposed());
                }
            }


            @Override public void onError(@NonNull Throwable e) {
                Log.e(TAG, "error");
            }


            @Override public void onComplete() {
                Log.e(TAG, "complete");
            }
        });
    }


    /**
     * Consumer表示下游只关心onNext事件,其他事件忽略
     */
    public void demo4() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.e(TAG, "emit 1");
                emitter.onNext(1);
                Log.e(TAG, "emit 2");
                emitter.onNext(2);
                Log.e(TAG, "emit 3");
                emitter.onNext(3);
                Log.e(TAG, "emit complete");
                emitter.onComplete();
                Log.e(TAG, "emit 4");
                emitter.onNext(4);
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.e(TAG, "onNext: " + integer);
            }
        });
    }


    /**
     * 上下游默认在统一线程工作
     */
    public void demo5() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override public void subscribe(@NonNull ObservableEmitter<Integer> e)
                throws Exception {
                Log.e(TAG, "Observable thread is:" + Thread.currentThread().getName());
                Log.e(TAG, "e 1");
                e.onNext(1);
            }
        }).subscribe(new Consumer<Integer>() {
            @Override public void accept(@NonNull Integer integer) throws Exception {
                Log.e(TAG, "Observer  thread is:" + Thread.currentThread().getName());
                Log.e(TAG, "onNext: " + integer);
            }
        });
    }


    /**
     * subscribeOn() 指定的是上游发送事件的线程,多次指定上游的线程只有第一次指定的有效
     * observeOn() 指定的是下游接收事件的线程,多次指定下游的线程是可以的
     */
    public void demo6() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override public void subscribe(@NonNull ObservableEmitter<Integer> e)
                throws Exception {
                Log.e(TAG, "Observable thread is:" + Thread.currentThread().getName());
                Log.e(TAG, "e 1");
                e.onNext(1);
            }
        }).subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<Integer>() {
                @Override public void accept(@NonNull Integer integer) throws Exception {
                    Log.e(TAG, "Observer  thread is:" + Thread.currentThread().getName());
                    Log.e(TAG, "onNext: " + integer);
                }
            });
    }


    public void demo7() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override public void subscribe(@NonNull ObservableEmitter<Integer> e)
                throws Exception {
                Log.e(TAG, "Observable thread is:" + Thread.currentThread().getName());
                Log.e(TAG, "e 1");
                e.onNext(1);
            }
        }).subscribeOn(Schedulers.newThread())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext(
                new Consumer<Integer>() {
                    @Override public void accept(@NonNull Integer integer) throws Exception {
                        Log.e(TAG,
                            "After observeOn(mainThread), current thread is: " +
                                Thread.currentThread()
                                    .getName());
                    }
                })
            .observeOn(Schedulers.io())
            .doOnNext(new Consumer<Integer>() {
                @Override public void accept(@NonNull Integer integer) throws Exception {
                    Log.e(TAG,
                        "After observeOn(io), current thread is : " +
                            Thread.currentThread().getName());
                }
            })
            .subscribe(new Consumer<Integer>() {
                @Override public void accept(@NonNull Integer integer) throws Exception {
                    Log.e(TAG, "Observer thread is :" + Thread.currentThread().getName());
                    Log.e(TAG, "onNext: " + integer);
                }
            });
    }


    /**
     * 与retrofit相结合
     */
    public void demo8() {
        retrofitHelper.fetchDailyListInfo()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<DailyListBean>() {
                @Override public void onSubscribe(@NonNull Disposable d) {

                }


                @Override public void onNext(@NonNull DailyListBean dailyListBean) {
                    tv.setText(dailyListBean.toString());
                }


                @Override public void onError(@NonNull Throwable e) {
                    Toast.makeText(MainActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
                }


                @Override public void onComplete() {
                    Toast.makeText(MainActivity.this, "获取成功", Toast.LENGTH_SHORT).show();
                }
            });
    }


    /**
     * 将Integer转化成String
     */
    public void demo9() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override public void subscribe(@NonNull ObservableEmitter<Integer> e)
                throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
            }
        }).map(new Function<Integer, String>() {
            @Override public String apply(@NonNull Integer integer) throws Exception {
                return "This is result " + integer;
            }
        }).subscribe(new Consumer<String>() {
            @Override public void accept(@NonNull String s) throws Exception {
                Log.e(TAG, s);
            }
        });
    }


    /**
     * 上游每发送一个事件, flatMap都将创建一个新的水管, 然后发送转换之后的新的事件, 下游接收到的就是这些新的水管发送的数据
     * flatMap并不保证事件的顺序
     */
    public void demo10() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override public void subscribe(@NonNull ObservableEmitter<Integer> e)
                throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
            }
        }).flatMap(new Function<Integer, ObservableSource<String>>() {
            @Override public ObservableSource<String> apply(@NonNull Integer integer)
                throws Exception {
                List<String> list = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    list.add("value is " + integer);
                }
                return Observable.fromIterable(list).delay(1, TimeUnit.SECONDS);
            }
        }).subscribe(new Consumer<String>() {
            @Override public void accept(@NonNull String s) throws Exception {
                Log.e(TAG, s);
            }
        });
    }


    /**
     * concatMap和flatMap的作用几乎一模一样,只是它的结果是严格按照上游发送的顺序来发送的
     */
    public void demo11() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override public void subscribe(@NonNull ObservableEmitter<Integer> e)
                throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
            }
        }).concatMap(new Function<Integer, ObservableSource<String>>() {
            @Override public ObservableSource<String> apply(@NonNull Integer integer)
                throws Exception {
                List<String> list = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    list.add("value is " + integer);
                }
                return Observable.fromIterable(list).delay(1, TimeUnit.SECONDS);
            }
        }).subscribe(new Consumer<String>() {
            @Override public void accept(@NonNull String s) throws Exception {
                Log.e(TAG, s);
            }
        });
    }


    /**
     * 先获取最新日报设个标题,再获取主题日报设置个名称
     */
    public void demo12() {
        retrofitHelper.fetchDailyListInfo()         //获取最新日报
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext(new Consumer<DailyListBean>() {
                @Override public void accept(@NonNull DailyListBean dailyListBean)
                    throws Exception {
                    tv.setText(dailyListBean.getStories().get(0).getTitle());
                }
            }).delay(2, TimeUnit.SECONDS)
            .observeOn(Schedulers.io())             //获取主题日报
            .flatMap(new Function<DailyListBean, ObservableSource<ThemeListBean>>() {
                @Override
                public ObservableSource<ThemeListBean> apply(@NonNull DailyListBean dailyListBean)
                    throws Exception {
                    return retrofitHelper.fetchThemesListInfo();
                }
            })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<ThemeListBean>() {
                @Override public void accept(@NonNull ThemeListBean themeListBean)
                    throws Exception {
                    tv.setText(themeListBean.getOthers().get(0).getName());
                    Toast.makeText(MainActivity.this, "获取主题日报成功", Toast.LENGTH_SHORT).show();
                }
            }, new Consumer<Throwable>() {
                @Override public void accept(@NonNull Throwable throwable) throws Exception {
                    Toast.makeText(MainActivity.this, "获取主题日报失败", Toast.LENGTH_SHORT).show();
                }
            });
    }
}
