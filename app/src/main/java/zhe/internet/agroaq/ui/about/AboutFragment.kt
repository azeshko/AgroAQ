package zhe.internet.agroaq.ui.about

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import zhe.internet.agroaq.R
import zhe.internet.agroaq.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {

    private lateinit var wv: WebView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val root = inflater.inflate(R.layout.fragment_about, container, false)

        wv = root.findViewById(R.id.wvAbout)

        wv.loadUrl("file:///android_asset/index.html")

        return root
    }


}