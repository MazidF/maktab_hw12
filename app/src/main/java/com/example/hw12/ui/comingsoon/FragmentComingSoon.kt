package com.example.hw12.ui.comingsoon

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.hw12.R
import com.example.hw12.databinding.RandomImageBinding
import kotlin.random.Random

class FragmentComingSoon : Fragment() {
    private lateinit var binding: RandomImageBinding
    val liveImage by lazy {
        MutableLiveData<Bitmap>()
    }
    val random by lazy {
        Random(System.currentTimeMillis())
    }
    val options by lazy {
        RequestOptions()
//            .centerCrop()
            .fitCenter()
            .placeholder(R.drawable.loading_animation)
//            .placeholder(R.drawable.icon_load)
            .error(R.drawable.icon_error)
    }
    val uri: String
        get() = "https://picsum.photos/200/300?random=${random.nextInt(1, 1000)}"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.random_image, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        binding.image = liveImage
//        binding.lifecycleOwner = this

        with(binding) {
            getButton.setOnClickListener {
                val uri = uri
                Glide.with(this@FragmentComingSoon)
                    .load(uri)
                    .apply(options)
                    .override(imageSize.text.toString().toInt())
                    .into(imageView)
            }
        }
    }
}
