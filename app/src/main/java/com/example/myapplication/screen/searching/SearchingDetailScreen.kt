package com.example.myapplication.screen.searching

import android.graphics.Color
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun SearchingDetailScreen(uri: String) {
    AndroidView(
        modifier =
            Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.safeDrawing),
        factory = { context ->
            WebView(context).apply {
                webViewClient = WebViewClient()
                webChromeClient =
                    object : WebChromeClient() {
                        var videoCustomView: View? = null
                        var customView: FrameLayout? = null

                        override fun onShowCustomView(
                            view: View?,
                            callback: CustomViewCallback?,
                        ) {
                            if (videoCustomView != null) {
                                callback?.onCustomViewHidden()
                                return
                            }

                            val frame: FrameLayout = view as FrameLayout
                            val v1: View = frame.getChildAt(0)

                            view.setLayoutParams(
                                FrameLayout.LayoutParams(
                                    FrameLayout.LayoutParams.MATCH_PARENT,
                                    FrameLayout.LayoutParams.MATCH_PARENT,
                                    Gravity.CENTER,
                                ),
                            )
                            v1.setOnKeyListener(
                                object : View.OnKeyListener {
                                    override fun onKey(
                                        v: View?,
                                        keyCode: Int,
                                        event: KeyEvent?,
                                    ): Boolean {
                                        if (keyCode == KeyEvent.KEYCODE_BACK && event?.action == KeyEvent.ACTION_DOWN) {
                                            onHideCustomView()
                                            return true
                                        }
                                        return false
                                    }
                                },
                            )

                            videoCustomView = view
                            customView?.visibility = View.VISIBLE
                            customView?.setBackgroundColor(Color.BLACK)
                            customView?.bringToFront()

                            customView?.addView(videoCustomView)
                        }

                        override fun onHideCustomView() {
                            super.onHideCustomView()

                            customView?.removeView(videoCustomView)
                            videoCustomView = null
                            customView?.visibility = View.INVISIBLE
                        }
                    }
                settings.setDefaultSetting()
                clearHistory()
                clearCache(true)
                loadUrl(uri)
            }
        },
    )
}

private fun WebSettings.setDefaultSetting() {
    javaScriptEnabled = true
    setSupportMultipleWindows(true)
    javaScriptCanOpenWindowsAutomatically = true
    useWideViewPort = true
    loadWithOverviewMode = true
    allowFileAccess = true
    domStorageEnabled = true
    setSupportZoom(false)
    cacheMode = WebSettings.LOAD_DEFAULT
    safeBrowsingEnabled = true
    textZoom = 100
}
