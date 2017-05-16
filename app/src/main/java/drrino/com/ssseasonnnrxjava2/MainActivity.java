package drrino.com.ssseasonnnrxjava2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "TAG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //demo1();
        //demo2();
        //demo3();
        //demo4();
        //demo5();
        //demo6();
        demo7();
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
}
