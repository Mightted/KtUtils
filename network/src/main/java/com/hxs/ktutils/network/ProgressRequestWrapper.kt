package com.hxs.ktutils.network

import okhttp3.MediaType
import okhttp3.RequestBody
import okio.*

typealias ProgressListener = (bytesWritten: Long,contentLength: Long)->Unit

class ProgressRequestWrapper(private var delegate: RequestBody, var onRequestProgress: ProgressListener) :
    RequestBody() {
    private lateinit var countingSink: CountingSink
    override fun contentType(): MediaType? {
        return delegate.contentType()
    }


    override fun contentLength(): Long {
        return delegate.contentLength()
    }


    override fun writeTo(sink: BufferedSink) {
        countingSink = CountingSink(sink)
        val bufferedSink: BufferedSink = countingSink.buffer()
        delegate.writeTo(bufferedSink)
        bufferedSink.flush()
    }

    inner class CountingSink(delegate: Sink?) : ForwardingSink(delegate!!) {
        private var bytesWritten: Long = 0
        override fun write(source: Buffer, byteCount: Long) {
            super.write(source, byteCount)
            bytesWritten += byteCount
            onRequestProgress(bytesWritten, contentLength())
        }
    }

}