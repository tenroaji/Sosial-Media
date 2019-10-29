package com.lagecong.sosialmediasignin.ui.pdf

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.webkit.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lagecong.sosialmediasignin.R
import com.lagecong.sosialmediasignin.utils.WebViewJSResizerUtil
import kotlinx.android.synthetic.main.activity_pdf.*
import java.io.UnsupportedEncodingException
import java.net.URLDecoder

/**
 * Created by Andi Tenroaji Ahmad on 10/11/2019.
 */
class ActivtyPdf : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf)

        webView.settings.javaScriptEnabled = true
        webView.addJavascriptInterface(
            WebViewJSResizerUtil(this, webView),
            WebViewJSResizerUtil.JS_RESIZER_NAME)
        val webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                Log.e("lapar", "$newProgress")
                swipeRefresh.isRefreshing = newProgress < 100

            }
        }

        val webClient = object : WebViewClient(){
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                Log.e("lapar", "$request")
                super.onReceivedError(view, request, error)
            }
        }


//        val webViewClient = object : WebViewClient() {
//            @SuppressWarnings("deprecation")
//            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
//                Log.e("lapar", "$url")
//                return overrideUrl(view, url)
//            }
//
//            fun overrideUrl(view: WebView?, urlRequested: String?): Boolean {
////                view?.loadUrl("http://facebook.com")
//                return true
//            }
//
//            @TargetApi(Build.VERSION_CODES.N)
//            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
//                return overrideUrl(webView, request.url.toString())
//            }
//
//            override fun onPageFinished(view: WebView?, url: String?) {
//                view!!.loadUrl("javascript:window.onload = function(){" + WebViewJSResizerUtil.JS_RESIZER_NAME + ".changeHeight(document.querySelector('body').offsetHeight);};")
//                swipeRefresh.isRefreshing = false
//                super.onPageFinished(view, url)
//            }
//        }

//        webView.webChromeClient = webChromeClient
        webView.webViewClient = webClient
        webView.loadUrl("http://docs.google.com/gview?embedded=true&url=http://staffnew.uny.ac.id/upload/132310817/pengabdian/MODUL_CORELDRAW.pdf")

        swipeRefresh.setOnRefreshListener {
            webView.loadUrl("http://facebook.com")
            Log.e("lapar", "webview tampil")
        }
    }
}




