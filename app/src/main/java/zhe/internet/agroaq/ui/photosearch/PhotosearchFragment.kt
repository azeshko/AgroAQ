package zhe.internet.agroaq.ui.photosearch

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import zhe.internet.agroaq.R
import zhe.internet.agroaq.databinding.ActivityMainBinding
import zhe.internet.agroaq.ui.calc.CalcFragment
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class PhotosearchFragment : CalcFragment() {

    private lateinit var imView: ImageView
    private  lateinit var spinner: Spinner
    private lateinit var webView: WebView
    // LOAD ANY URL, I'VE TRIED THIS ONE BECAUSE IS WAS THE FIRST ONE ON SEARCH' RESULTS AND IT WORKS ON THE WEBVIEW. I'M NOT AFFILIATED WITH THEM
    private var URL: String = "https://yandex.by/images"

    // file upload
    private var uploadCallback: ValueCallback<Array<Uri>>? = null
    private var imageUri: Uri? = null
    lateinit var currentPhotoPath: String
    // imagechooser intent code
    private val UPLOAD_REQUEST_CODE = 1


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val root = inflater.inflate(R.layout.fragment_photosearch, container, false)

             imView = root.findViewById(R.id.imgViewer)


            spinner = root.findViewById(R.id.spinner)
            // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            root.context,
            R.array.urls_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

             webView = root.findViewById(R.id.wvCamera)




        spinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.d("fox","error")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

//                var url: String = "https://yandex.by/images"
                if (position == 0){
                    URL = getString(R.string.yand)

                }else if(position == 1){
                    URL = getString(R.string.labnol)

                }else if(position == 2){
                    URL = getString(R.string.rev)
                }

//                Log.d("pos", URL.toString())
//                Toast.makeText(view!!.context , URL, Toast.LENGTH_SHORT).show()
                setUpWebview()
            }
        }

        setUpWebview()


            return root
    }


    private fun setUpWebview() {

        webView.webViewClient = WebViewClient()

        val webViewSettings = webView.settings
        webViewSettings.javaScriptEnabled = true
        webViewSettings.domStorageEnabled = true
        webViewSettings.allowFileAccess = true
        webViewSettings.allowContentAccess = true
        webViewSettings.setSupportZoom(true)

        webView.loadUrl(URL)

        webView.webChromeClient = mWebChromeClient()

    }


    inner class mWebChromeClient() : WebChromeClient() {
        override fun onShowFileChooser(
            webView: WebView?,
            filePathCallback: ValueCallback<Array<Uri>>?, // TODO: make it backward compatible
            fileChooserParams: FileChooserParams?
        ): Boolean {
            uploadCallback = filePathCallback
            createChooserIntent()
            return true
        }
    }


    private fun createChooserIntent() {
        var photoFile: File? = null
        val authorities: String =  activity!!.applicationContext.packageName + ".fileprovider"
        var ac = activity?.applicationContext!!.applicationContext
        try {
            photoFile = createImageFile()
            imageUri = FileProvider.getUriForFile(activity?.applicationContext!!.applicationContext,authorities,
                photoFile!!
            )

        } catch (e: IOException) {
            e.printStackTrace()
        }

        // camera intent
        val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // camera intent includes handling the output file
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri)
        // gallery intent
        val photoIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        // picker intent, includes gallery intent
        val chooserIntent = Intent.createChooser(photoIntent, "File chooser")
        // we include the camera intent in the picker intent
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf<Parcelable>(captureIntent))
        // launch the intent
        resultLauncher.launch(chooserIntent)
    }



    // new activityResult handling
    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            if (uploadCallback != null) {
                // process image upload to the webview
                processImageUpload(result.data)

                val imgUri = Uri.parse(imageUri.toString())

               imView.setImageURI(imgUri)
            } else {
                val applicationContext = getActivity()!!.getApplicationContext()
                Toast.makeText(applicationContext, "An error occurred while uploading the file", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // creates the file in order to upload the camera photo to the webview
    @Throws(IOException::class)
    private fun createImageFile(): File? {
        // create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = activity!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}", // prefix
            ".jpg", // suffix
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    private fun processImageUpload(data: Intent?) {
        if (data != null) {
            val results: Array<Uri>
            val uriData: Uri? = data.data

            if (uriData != null) {
                arrayOf(uriData).also { results = it }
                // pass the data to the webview
                uploadCallback!!.onReceiveValue(results)
            } else {
                uploadCallback!!.onReceiveValue(null)
            }
        } else {
            if (imageUri != null) {
                uploadCallback!!.onReceiveValue(arrayOf(imageUri!!))
            }
        }
    }

//    override fun onBackPressed() {
//        if (webView.canGoBack()) {
//            webView.goBack()
//        } else {
//            super.onBackPressed()
//        }
//    }




}