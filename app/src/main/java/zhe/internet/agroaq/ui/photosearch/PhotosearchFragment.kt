package zhe.internet.agroaq.ui.photosearch

import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import zhe.internet.agroaq.R
import zhe.internet.agroaq.ui.calc.CalcFragment


class PhotosearchFragment : CalcFragment() {

    private lateinit var imView: ImageView
    val REQUEST_IMAGE_CAPTURE = 1


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val root = inflater.inflate(R.layout.fragment_photosearch, container, false)

        val myButton: Button = root.findViewById(R.id.btn_camera)
            imView = root.findViewById(R.id.imgViewer)


            myButton.setOnClickListener(){
                dispatchTakePictureIntent()
            }
        val wvCamera: WebView = root.findViewById(R.id.wvCamera)
        val webSettings = wvCamera.settings
            webSettings.javaScriptEnabled = true

            wvCamera.webViewClient = MyWebViewClient()
            wvCamera.webChromeClient = MyWebChromeClient()
//            wvCamera.loadUrl("https://yandex.by/images/search?text=tractors&rdrnd=338774")
            wvCamera.loadUrl("https://yandex.by/images")
            return root
    }




    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
                imView.setImageBitmap(imageBitmap)
        }
    }

}