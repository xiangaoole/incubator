package com.xiangaoole.android.incubator

import io.reactivex.*
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import org.junit.Test
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription

class RxJava2Test {

    @Test
    fun helloWorldSimple() {
        val consumer = Consumer<String> {
            println(it)
        }
        Observable.just("Hello", "World").subscribe(consumer)
    }

    @Test
    fun helloWorldComplex() {
        val observer = object : Observer<String> {
            override fun onSubscribe(d: Disposable) {
                println("onSubscribe: $d")
            }

            override fun onNext(t: String) {
                println("onNext: $t")
            }

            override fun onError(e: Throwable) {
                println("onError: $e")
            }

            override fun onComplete() {
                println("onComplete")
            }

        }

        Observable.just("Hello", "World").subscribe(observer)
    }

    @Test
    fun helloWorldEvil() {
        Observable
            // 创建 Observable
            .create<String> { emitter ->
                println("${Thread.currentThread()} ---")
                emitter.onNext("Hello")
                emitter.onNext("World")
                println("${Thread.currentThread()} ---")
                emitter.onComplete()
            }
            .subscribeOn(Schedulers.newThread())
            // 订阅
            .observeOn(Schedulers.newThread())
            .subscribe(object : Observer<String> {
                override fun onSubscribe(d: Disposable) {
                    println("${Thread.currentThread()} onSubscribe: $d")
                }

                override fun onNext(t: String) {
                    println("${Thread.currentThread()} onNext: $t")
                }

                override fun onError(e: Throwable) {
                    println("onError: $e")
                }

                override fun onComplete() {
                    println("${Thread.currentThread()} onComplete")
                }

            })

        Thread.sleep(2000)
    }

    @Test
    fun backpressure() {
        Flowable
            .create<String>({ emitter ->
                println("${Thread.currentThread()} ---")
                emitter.onNext("Hello")
                emitter.onNext("World")
                println("${Thread.currentThread()} ---")
                emitter.onComplete()
            }, BackpressureStrategy.ERROR)
            .subscribe(object : Subscriber<String> {
                override fun onSubscribe(s: Subscription?) {
                    println("${Thread.currentThread()} onSubscribe: $s")
                    s?.request(1)
                }

                override fun onNext(t: String?) {
                    println("${Thread.currentThread()} onNext: $t")
                }

                override fun onError(t: Throwable?) {
                    println("${Thread.currentThread()} onError: $t")
                }

                override fun onComplete() {
                    println("${Thread.currentThread()} onComplete")
                }

            })
    }
}