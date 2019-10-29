package com.lagecong.sosialmediasignin.utils

import android.app.Activity
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebView

/**
 * Created by Anwar on 10/25/2018.
 */
class WebViewJSResizerUtil(private val mActivity: Activity, private val mWebView: WebView) {

    @JavascriptInterface
    fun changeHeight(height: String) {
        mActivity.runOnUiThread {
            val layoutParams = mWebView.layoutParams
            layoutParams.height = Integer.valueOf(height)
            mWebView.layoutParams = layoutParams
        }
    }

    companion object {

        val JS_RESIZER_NAME = "WebViewJSResizer"
    }
}
