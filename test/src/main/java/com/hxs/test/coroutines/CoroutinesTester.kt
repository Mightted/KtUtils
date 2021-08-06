package com.hxs.test.coroutines

import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

object CoroutinesTester {

    suspend fun mainTest1(): Boolean = suspendCancellableCoroutine {
        Thread.sleep(1000)
        println("1结束")
        it.resume(true)
    }

    suspend fun mainTest2(): Boolean = suspendCancellableCoroutine {
        Thread.sleep(2000)
        println("2结束")
        it.resume(true)
    }


}