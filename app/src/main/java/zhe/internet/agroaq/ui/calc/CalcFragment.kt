package zhe.internet.agroaq.ui.calc

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import zhe.internet.agroaq.R

class CalcFragment : Fragment() {



    private lateinit var homeViewModel: CalcViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        homeViewModel =
//            ViewModelProvider(this).get(CalcViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_calc, container, false)

        val myWebView: WebView = root.findViewById(R.id.wv)



        //Прогрессбар
        val pbar : ProgressBar = root.findViewById(R.id.prgBar)

        myWebView.setWebChromeClient(object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, progress: Int) {
                if (progress < 100 && pbar.getVisibility() === ProgressBar.GONE) {
                    pbar.setVisibility(ProgressBar.VISIBLE)
                }
                pbar.setProgress(progress)
                if (progress == 100) {
                    pbar.setVisibility(ProgressBar.GONE)
                }
            }
        })


        val webSettings = myWebView.settings
        webSettings.javaScriptEnabled = true
        myWebView.webViewClient = MyWebViewClient()
        myWebView.webChromeClient = MyWebChromeClient()


        myWebView.loadUrl("https://agronaut.by/android/")
        return root
    }


    private class MyWebViewClient : WebViewClient() {
        override fun onPageFinished(view: WebView, url: String) {
            //Calling a javascript function in html page
            //view.loadUrl("javascript:document.getElementsByName('login')[0].value = 444;document.getElementsByName('password')[0].value = 555;")

        }
    }

    private class MyWebChromeClient : WebChromeClient() {
        override fun onJsAlert(view: WebView, url: String, message: String, result: JsResult): Boolean {
            Log.d("LogTag", message)
            result.confirm()
            return true
        }
    }


}