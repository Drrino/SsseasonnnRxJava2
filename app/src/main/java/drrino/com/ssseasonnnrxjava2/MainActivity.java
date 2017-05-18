package drrino.com.ssseasonnnrxjava2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import drrino.com.ssseasonnnrxjava2.model.bean.DailyListBean;
import drrino.com.ssseasonnnrxjava2.model.bean.DailyThemeListBean;
import drrino.com.ssseasonnnrxjava2.model.bean.ThemeListBean;
import drrino.com.ssseasonnnrxjava2.model.http.RetrofitHelper;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    private TextView tv;
    private Button start, request;
    private RetrofitHelper retrofitHelper = new RetrofitHelper();
    public static Subscription mSubscription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.tv);
        start = (Button) findViewById(R.id.start);
        request = (Button) findViewById(R.id.request);

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
        //demo12();
        //demo13();
        //demo14();
        //demo15();
        //demo16();
        //demo17();
        //demo18();
        //demo19();
        //demo20();
        //demo21();
        //demo22();
        //demo23();
        //demo24();
        //demo25();
        //demo26();
        //demo27();
        //demo28();
        //demo29();
        demo30();
    }


    public static void request(long n) {
        mSubscription.request(n);
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


    /**
     * zip的使用
     */
    public void demo13() {
        Observable.zip(Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override public void subscribe(@NonNull ObservableEmitter<Integer> e)
                throws Exception {
                Log.e(TAG, "e 1");
                e.onNext(1);
                Thread.sleep(1000);

                Log.e(TAG, "e 2");
                e.onNext(2);
                Thread.sleep(1000);

                Log.e(TAG, "e 3");
                e.onNext(3);
                Thread.sleep(1000);

                Log.e(TAG, "e 4");
                e.onNext(4);
                Thread.sleep(1000);

                Log.e(TAG, "e complete1");
                e.onComplete();
            }
        }), Observable.create(new ObservableOnSubscribe<String>() {
            @Override public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                Log.e(TAG, "e A");
                e.onNext("A");
                Thread.sleep(1000);

                Log.e(TAG, "e B");
                e.onNext("B");
                Thread.sleep(1000);

                Log.e(TAG, "e C");
                e.onNext("C");
                Thread.sleep(1000);

                Log.e(TAG, "e D");
                e.onNext("D");
                Thread.sleep(1000);

                Log.e(TAG, "e complete2");
                e.onComplete();
            }
        }), new BiFunction<Integer, String, String>() {
            @Override
            public String apply(
                @NonNull Integer integer, @NonNull String s)
                throws Exception {
                return integer + s;
            }
        }).subscribe(new Observer<String>() {
            @Override public void onSubscribe(@NonNull Disposable d) {
                Log.e(TAG, "onSubscribe");
            }


            @Override public void onNext(@NonNull String s) {
                Log.e(TAG, "onNext: " + s);
            }


            @Override public void onError(@NonNull Throwable e) {
                Log.e(TAG, "onError");
            }


            @Override public void onComplete() {
                Log.e(TAG, "onComplete");
            }
        });
    }


    /**
     * io线程使用zip
     * zip发送的事件数量跟上游中发送事件最少的那一根水管的事件数量是有关
     */
    public void demo14() {
        Observable.zip(Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override public void subscribe(@NonNull ObservableEmitter<Integer> e)
                throws Exception {
                Log.e(TAG, "e 1");
                e.onNext(1);
                Thread.sleep(1000);

                Log.e(TAG, "e 2");
                e.onNext(2);
                Thread.sleep(1000);

                Log.e(TAG, "e 3");
                e.onNext(3);
                Thread.sleep(1000);

                Log.e(TAG, "e 4");
                e.onNext(4);
                Thread.sleep(1000);

                Log.e(TAG, "e complete1");
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()), Observable.create(new ObservableOnSubscribe<String>() {
            @Override public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                Log.e(TAG, "e A");
                e.onNext("A");
                Thread.sleep(1000);

                Log.e(TAG, "e B");
                e.onNext("B");
                Thread.sleep(1000);

                Log.e(TAG, "e C");
                e.onNext("C");
                Thread.sleep(1000);

                Log.e(TAG, "e complete2");
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()), new BiFunction<Integer, String, String>() {
            @Override
            public String apply(
                @NonNull Integer integer, @NonNull String s)
                throws Exception {
                return integer + s;
            }
        }).subscribe(new Observer<String>() {
            @Override public void onSubscribe(@NonNull Disposable d) {
                Log.e(TAG, "onSubscribe");
            }


            @Override public void onNext(@NonNull String s) {
                Log.e(TAG, "onNext: " + s);
            }


            @Override public void onError(@NonNull Throwable e) {
                Log.e(TAG, "onError");
            }


            @Override public void onComplete() {
                Log.e(TAG, "onComplete");
            }
        });
    }


    /**
     * zip操作和retrofit进行相结合
     */
    public void demo15() {
        Observable.zip(retrofitHelper.fetchDailyListInfo().subscribeOn(Schedulers.io()),
            retrofitHelper.fetchThemesListInfo().subscribeOn(Schedulers.io()),
            new BiFunction<DailyListBean, ThemeListBean, DailyThemeListBean>() {
                @Override
                public DailyThemeListBean apply(
                    @NonNull DailyListBean dailyListBean, @NonNull ThemeListBean themeListBean)
                    throws Exception {
                    return new DailyThemeListBean(dailyListBean, themeListBean);
                }
            }).observeOn(AndroidSchedulers.mainThread()).subscribe(
            new Consumer<DailyThemeListBean>() {
                @Override public void accept(@NonNull DailyThemeListBean dailyThemeListBean)
                    throws Exception {
                    tv.setText(
                        dailyThemeListBean.getDailyListBean().getStories().get(0).getTitle() +
                            "&&" +
                            dailyThemeListBean.getThemeListBean().getOthers().get(0).getName());
                }
            });
    }


    /**
     * Backpressure出现
     */
    public void demo16() {
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; ; i++) {
                    emitter.onNext(i);
                }
            }
        }).subscribeOn(Schedulers.io());

        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("A");
            }
        }).subscribeOn(Schedulers.io());

        Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer integer, String s) throws Exception {
                return integer + s;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, s);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.w(TAG, throwable);
            }
        });
    }


    /**
     * Backpressure出现,通过filter减少内存
     */
    public void demo17() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override public void subscribe(@NonNull ObservableEmitter<Integer> e)
                throws Exception {
                for (int i = 0; ; i++) {
                    e.onNext(i);
                }
            }
        }).subscribeOn(Schedulers.io()).filter(new Predicate<Integer>() {
            @Override public boolean test(@NonNull Integer integer) throws Exception {
                return integer % 10 == 0;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Integer>() {
            @Override public void accept(@NonNull Integer integer) throws Exception {
                Log.e(TAG, String.valueOf(integer));
            }
        });
    }


    /**
     * Backpressure出现,通过sample减少内存
     */
    public void demo18() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override public void subscribe(@NonNull ObservableEmitter<Integer> e)
                throws Exception {
                for (int i = 0; ; i++) {
                    e.onNext(i);
                }
            }
        })
            .subscribeOn(Schedulers.io())
            .sample(2, TimeUnit.SECONDS)        //隔两秒取样
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                new Consumer<Integer>() {
                    @Override public void accept(@NonNull Integer integer) throws Exception {
                        Log.e(TAG, String.valueOf(integer));
                    }
                });
    }


    /**
     * Backpressure出现,通过延时上游速度减少内存
     */
    public void demo19() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override public void subscribe(@NonNull ObservableEmitter<Integer> e)
                throws Exception {
                for (int i = 0; ; i++) {
                    e.onNext(i);
                    Thread.sleep(2000);
                }
            }
        })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                new Consumer<Integer>() {
                    @Override public void accept(@NonNull Integer integer) throws Exception {
                        Log.e(TAG, String.valueOf(integer));
                    }
                });
    }


    /**
     * zip操作Backpressure出现,使用sample采样减少内存
     */
    public void demo20() {
        Observable.zip(Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override public void subscribe(@NonNull ObservableEmitter<Integer> e)
                throws Exception {
                for (int i = 0; ; i++) {
                    e.onNext(i);
                }
            }
        }).subscribeOn(Schedulers.io()).sample(2, TimeUnit.SECONDS), Observable.create(
            new ObservableOnSubscribe<String>() {
                @Override public void subscribe(@NonNull ObservableEmitter<String> e)
                    throws Exception {
                    e.onNext("A");
                }
            }).subscribeOn(Schedulers.io()), new BiFunction<Integer, String, String>() {
            @Override public String apply(@NonNull Integer integer, @NonNull String s)
                throws Exception {
                return integer + s;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override public void accept(@NonNull String s) throws Exception {
                Log.e(TAG, s);
            }
        }, new Consumer<Throwable>() {
            @Override public void accept(@NonNull Throwable throwable) throws Exception {
                Log.w(TAG, throwable);
            }
        });
    }


    /**
     * zip操作Backpressure出现,延时上游减少内存
     */
    public void demo21() {
        Observable.zip(Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override public void subscribe(@NonNull ObservableEmitter<Integer> e)
                throws Exception {
                for (int i = 0; ; i++) {
                    e.onNext(i);
                    Thread.sleep(2000);
                }
            }
        }).subscribeOn(Schedulers.io()), Observable.create(
            new ObservableOnSubscribe<String>() {
                @Override public void subscribe(@NonNull ObservableEmitter<String> e)
                    throws Exception {
                    e.onNext("A");
                }
            }).subscribeOn(Schedulers.io()), new BiFunction<Integer, String, String>() {
            @Override public String apply(@NonNull Integer integer, @NonNull String s)
                throws Exception {
                return integer + s;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override public void accept(@NonNull String s) throws Exception {
                Log.e(TAG, s);
            }
        }, new Consumer<Throwable>() {
            @Override public void accept(@NonNull Throwable throwable) throws Exception {
                Log.w(TAG, throwable);
            }
        });
    }


    /**
     * Flowable的使用
     * 添加BackpressureStrategy.ERROR参数,上下游不均衡时直接抛出异常
     * 下游onSubscribe传的是Subscription不再是Disposable
     * 不添加s.request(Long.MAX_VALUE)会抛MissingBackpressureException异常
     */
    public void demo22() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override public void subscribe(@NonNull FlowableEmitter<Integer> e) throws Exception {
                Log.e(TAG, "e 1");
                e.onNext(1);
                Log.e(TAG, "e 2");
                e.onNext(2);
                Log.e(TAG, "e 3");
                e.onNext(3);
                Log.e(TAG, "e complete");
                e.onComplete();
            }
        }, BackpressureStrategy.ERROR).subscribe(new Subscriber<Integer>() {
            @Override public void onSubscribe(Subscription s) {
                Log.e(TAG, "onSubscribe");
                s.request(Long.MAX_VALUE);
            }


            @Override public void onNext(Integer integer) {
                Log.e(TAG, "onNext: " + integer);
            }


            @Override public void onError(Throwable t) {
                Log.w(TAG, "onError: ", t);
            }


            @Override public void onComplete() {
                Log.e(TAG, "onComplete");
            }
        });
    }


    /**
     * 不添加s.request(Long.MAX_VALUE)并使上游处于io线程,下游接收不到事件
     * s.request()控制下游处理上游多少事件
     */
    public void demo23() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override public void subscribe(@NonNull FlowableEmitter<Integer> e) throws Exception {
                Log.e(TAG, "e 1");
                e.onNext(1);
                Log.e(TAG, "e 2");
                e.onNext(2);
                Log.e(TAG, "e 3");
                e.onNext(3);
                Log.e(TAG, "e complete");
                e.onComplete();
            }
        }, BackpressureStrategy.ERROR)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                new Subscriber<Integer>() {
                    @Override public void onSubscribe(Subscription s) {
                        Log.e(TAG, "onSubscribe");
                    }


                    @Override public void onNext(Integer integer) {
                        Log.e(TAG, "onNext: " + integer);
                    }


                    @Override public void onError(Throwable t) {
                        Log.w(TAG, "onError: ", t);
                    }


                    @Override public void onComplete() {
                        Log.e(TAG, "onComplete");
                    }
                });
    }


    /**
     * 增加start和request按钮验证s.request()
     */
    public void demo24() {
        start.setVisibility(View.VISIBLE);
        request.setVisibility(View.VISIBLE);
        start.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Flowable.create(new FlowableOnSubscribe<Integer>() {
                    @Override public void subscribe(@NonNull FlowableEmitter<Integer> e)
                        throws Exception {
                        Log.e(TAG, "e 1");
                        e.onNext(1);
                        Log.e(TAG, "e 2");
                        e.onNext(2);
                        Log.e(TAG, "e 3");
                        e.onNext(3);
                        Log.e(TAG, "e complete");
                        e.onComplete();
                    }
                }, BackpressureStrategy.ERROR)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        new Subscriber<Integer>() {
                            @Override public void onSubscribe(Subscription s) {
                                Log.e(TAG, "onSubscribe");
                                mSubscription = s;  //把Subscription保存起来
                            }


                            @Override public void onNext(Integer integer) {
                                Log.e(TAG, "onNext: " + integer);
                            }


                            @Override public void onError(Throwable t) {
                                Log.w(TAG, "onError: ", t);
                            }


                            @Override public void onComplete() {
                                Log.e(TAG, "onComplete");
                            }
                        });
            }
        });
        request.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                request(1);
            }
        });
    }


    /**
     * 当上游的i超过128时会报MissingBackpressureException异常
     * 源码中默认定义了buffersize为128
     */
    public void demo25() {
        start.setVisibility(View.VISIBLE);
        request.setVisibility(View.VISIBLE);
        start.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Flowable.create(new FlowableOnSubscribe<Integer>() {
                    @Override public void subscribe(@NonNull FlowableEmitter<Integer> e)
                        throws Exception {
                        for (int i = 0; i < 129; i++) {
                            Log.e(TAG, "e " + i);
                            e.onNext(i);
                        }
                    }
                }, BackpressureStrategy.ERROR)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        new Subscriber<Integer>() {
                            @Override public void onSubscribe(Subscription s) {
                                Log.e(TAG, "onSubscribe");
                                mSubscription = s;
                            }


                            @Override public void onNext(Integer integer) {
                                Log.e(TAG, "onNext: " + integer);
                            }


                            @Override public void onError(Throwable t) {
                                Log.w(TAG, "onError: ", t);
                            }


                            @Override public void onComplete() {
                                Log.e(TAG, "onComplete");
                            }
                        });
            }
        });
        request.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                request(128);
            }
        });
    }


    /**
     * 自定义水缸大小
     * 使用BackpressureStrategy.BUFFER
     */
    public void demo26() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override public void subscribe(@NonNull FlowableEmitter<Integer> e)
                throws Exception {
                for (int i = 0; i < 1000; i++) {
                    Log.e(TAG, "e " + i);
                    e.onNext(i);
                }
            }
        }, BackpressureStrategy.BUFFER)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Integer>() {
            @Override public void onSubscribe(Subscription s) {
                Log.e(TAG, "onSubscribe");
                mSubscription = s;
            }


            @Override public void onNext(Integer integer) {
                Log.e(TAG, "onNext: " + integer);
            }


            @Override public void onError(Throwable t) {
                Log.w(TAG, "onError: ", t);
            }


            @Override public void onComplete() {
                Log.e(TAG, "onComplete");
            }
        });
    }


    /**
     * BackpressureStrategy.DROP将存不下的事件丢弃
     */
    public void demo27() {
        start.setVisibility(View.VISIBLE);
        request.setVisibility(View.VISIBLE);
        start.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Flowable.create(new FlowableOnSubscribe<Integer>() {
                    @Override public void subscribe(@NonNull FlowableEmitter<Integer> e)
                        throws Exception {
                        for (int i = 0; ; i++) {
                            e.onNext(i);
                        }
                    }
                }, BackpressureStrategy.DROP)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        new Subscriber<Integer>() {
                            @Override public void onSubscribe(Subscription s) {
                                Log.e(TAG, "onSubscribe");
                                mSubscription = s;
                            }


                            @Override public void onNext(Integer integer) {
                                Log.e(TAG, "onNext: " + integer);
                            }


                            @Override public void onError(Throwable t) {
                                Log.w(TAG, "onError: ", t);
                            }


                            @Override public void onComplete() {
                                Log.e(TAG, "onComplete");
                            }
                        });
            }
        });
        request.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                request(128);
            }
        });
    }


    /**
     * BackpressureStrategy.LATEST点击处理只保留最新的事件
     */
    public void demo28() {
        start.setVisibility(View.VISIBLE);
        request.setVisibility(View.VISIBLE);
        start.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Flowable.create(new FlowableOnSubscribe<Integer>() {
                    @Override public void subscribe(@NonNull FlowableEmitter<Integer> e)
                        throws Exception {
                        for (int i = 0; ; i++) {
                            e.onNext(i);
                        }
                    }
                }, BackpressureStrategy.LATEST)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        new Subscriber<Integer>() {
                            @Override public void onSubscribe(Subscription s) {
                                Log.e(TAG, "onSubscribe");
                                mSubscription = s;
                            }


                            @Override public void onNext(Integer integer) {
                                Log.e(TAG, "onNext: " + integer);
                            }


                            @Override public void onError(Throwable t) {
                                Log.w(TAG, "onError: ", t);
                            }


                            @Override public void onComplete() {
                                Log.e(TAG, "onComplete");
                            }
                        });
            }
        });
        request.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                request(128);
            }
        });
    }


    /**
     * DROP改良版
     */
    public void demo29() {
        start.setVisibility(View.VISIBLE);
        request.setVisibility(View.VISIBLE);
        start.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Flowable.create(new FlowableOnSubscribe<Integer>() {
                    @Override public void subscribe(@NonNull FlowableEmitter<Integer> e)
                        throws Exception {
                        for (int i = 0; i < 10000; i++) {
                            e.onNext(i);
                        }
                    }
                }, BackpressureStrategy.DROP)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        new Subscriber<Integer>() {
                            @Override public void onSubscribe(Subscription s) {
                                Log.e(TAG, "onSubscribe");
                                mSubscription = s;
                                s.request(128);  //一开始就处理掉128个事件
                            }


                            @Override public void onNext(Integer integer) {
                                Log.e(TAG, "onNext: " + integer);
                            }


                            @Override public void onError(Throwable t) {
                                Log.w(TAG, "onError: ", t);
                            }


                            @Override public void onComplete() {
                                Log.e(TAG, "onComplete");
                            }
                        });
            }
        });
        request.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                request(128);
            }
        });
    }


    /**
     * LATEST改良版
     */
    public void demo30() {
        start.setVisibility(View.VISIBLE);
        request.setVisibility(View.VISIBLE);
        start.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Flowable.create(new FlowableOnSubscribe<Integer>() {
                    @Override public void subscribe(@NonNull FlowableEmitter<Integer> e)
                        throws Exception {
                        for (int i = 0; i < 10000; i++) {
                            e.onNext(i);
                        }
                    }
                }, BackpressureStrategy.LATEST)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        new Subscriber<Integer>() {
                            @Override public void onSubscribe(Subscription s) {
                                Log.e(TAG, "onSubscribe");
                                mSubscription = s;
                                s.request(128);  //一开始就处理掉128个事件
                            }


                            @Override public void onNext(Integer integer) {
                                Log.e(TAG, "onNext: " + integer);
                            }


                            @Override public void onError(Throwable t) {
                                Log.w(TAG, "onError: ", t);
                            }


                            @Override public void onComplete() {
                                Log.e(TAG, "onComplete");
                            }
                        });
            }
        });
        request.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                request(128);
            }
        });
    }
}
