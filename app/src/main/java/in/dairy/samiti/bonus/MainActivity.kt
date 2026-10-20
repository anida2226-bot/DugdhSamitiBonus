package in.dairy.samiti.bonus

import android.app.Activity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.WebChromeClient
import android.webkit.JavascriptInterface
import android.content.Context
import android.print.PrintAttributes
import android.print.PrintManager
import android.print.WebViewPrintDocumentAdapter
import android.widget.Toast

class MainActivity : Activity() {
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        webView = WebView(this)
        setContentView(webView)

        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.webViewClient = WebViewClient()
        webView.webChromeClient = WebChromeClient()

        webView.addJavascriptInterface(AppBridge(this, webView), "AndroidApp")
        webView.loadUrl("file:///android_asset/index.html")
    }

    class AppBridge(
        private val activity: Activity,
        private val webView: WebView
    ) {
        @JavascriptInterface
        fun printPage() {
            activity.runOnUiThread {
                try {
                    val printManager =
                        activity.getSystemService(Context.PRINT_SERVICE) as PrintManager

                    val adapter =
                        webView.createPrintDocumentAdapter("DugdhSamitiBonus")

                    printManager.print(
                        "Dugdh Samiti Bonus Report",
                        adapter,
                        PrintAttributes.Builder().build()
                    )
                } catch (e: Exception) {
                    Toast.makeText(
                        activity,
                        "प्रिंट सुविधा उपलब्ध नहीं है",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    @Suppress("DEPRECATION")
    override fun onBackPressed() {
        if (::webView.isInitialized && webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
