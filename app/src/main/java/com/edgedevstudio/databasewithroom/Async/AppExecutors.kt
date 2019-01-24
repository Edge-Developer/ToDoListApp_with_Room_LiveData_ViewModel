package com.edgedevstudio.databasewithroom.Async

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Created by Olorunleke Opeyemi on 23/01/2019.
 **/
class AppExecutors {

    val diskIO: Executor
    val mainThread: Executor
    val networkIO: Executor

    private constructor(diskIO: Executor, mainThread: Executor, networkIO: Executor) {
        this.diskIO = diskIO
        this.mainThread = mainThread
        this.networkIO = networkIO
    }

    companion object {
        private val LOCK = Any()
        private var sInstance: AppExecutors? = null

        fun getsInstance(): AppExecutors {
            if (sInstance == null) {
                synchronized(LOCK) {
                    sInstance = AppExecutors(
                        Executors.newSingleThreadExecutor(),
                        Executors.newFixedThreadPool(3),
                        MainThreadExecutor()
                    )
                }
            }
            return sInstance!!
        }


        private class MainThreadExecutor : Executor {
            private val mainThreadHandler = Handler(Looper.getMainLooper())
            override fun execute(command: Runnable?) {
                mainThreadHandler.post(command)
            }
        }
    }
}