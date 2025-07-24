package com.example.myapplication.screen.searching

import android.content.Context
import android.view.View
import android.view.WindowManager
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.web.AccompanistWebChromeClient
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState

@Composable
fun SearchingDetailScreen(uri: String) {
    val state = rememberWebViewState(uri)
    val context = LocalContext.current
    val webViewChromeClient = remember { VideoWebChromeClient(context) }

    WebView(
        state = state,
        modifier = Modifier.fillMaxSize(),
        onCreated = { webView ->
            webView.settings.javaScriptEnabled = true
        },
        chromeClient = webViewChromeClient
    )
}

class VideoWebChromeClient(
    private val context: Context
) : AccompanistWebChromeClient() {
    private val windowManager: WindowManager by lazy {
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    private var customView: View? = null
    override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
        if (customView != null) {
            callback?.onCustomViewHidden()
            return
        }

        customView = view
        windowManager.addView(customView, WindowManager.LayoutParams())
    }

    override fun onHideCustomView() {
        super.onHideCustomView()
        windowManager.removeView(customView)
        customView = null
    }
}