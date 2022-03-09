package com.example.hw12.ui.movie

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.hw12.R
import com.example.hw12.databinding.FragmentMovieBinding
import java.util.regex.Pattern


class FragmentMovie : Fragment(/*R.layout.fragment_movie*/) {
    val model: ViewModelMovie by viewModels()
    val args: FragmentMovieArgs by navArgs()
    var start = true


/*    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentMovieBinding.bind(view)
        with(binding.web) {
            model.uri.observe(viewLifecycleOwner) {
                if (it != null) {
                    loadUrl(it)
                }
            }
            model.getTrailerUri(args.item.id)
            with(settings) {
                javaScriptEnabled = true
            }
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    return true
                }

                override fun onPageStarted(
                    view: WebView?, url: String?,
                    favicon: Bitmap?
                ) {}

                override fun onPageFinished(view: WebView, url: String?) {
                    view.loadUrl("javascript:window.HTMLOUT.processHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');")
                }
            }
            addJavascriptInterface(LoadListener(), "HTMLOUT")
        }
    }*/

/*    inner class LoadListener {

        @android.webkit.JavascriptInterface
        fun processHTML(html: String?) {
            if (html == null) {
                return
            }
            val matcher = Pattern.compile("imdb-jw-video-1.*jw-media jw-reset.*src=\"").matcher(html)
            if (matcher.find()) {
                val start = matcher.end()
                matcher.usePattern(Pattern.compile("\"")).find(start)
                if (matcher.find()) {
                    val end = matcher.start()
                    val uri = html.substring(start, end)
                    findNavController().navigate(
                        FragmentMovieDirections.actionFragmentMovieToFragmentPlayer(
                            uri
                        )
                    )
                }
            }
        }
    }*/

    override fun onStart() {
        super.onStart()
        if (start) {
            findNavController().navigate(FragmentMovieDirections.actionFragmentMovieToFragmentPlayer(""))
        }
        start = false
    }

}
