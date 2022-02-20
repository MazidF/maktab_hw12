package com.example.hw12

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.resource.bitmap.BitmapEncoder
import com.example.hw12.NetworkManager.Companion.service
import com.example.hw12.databinding.FragmentProfileBinding
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Multipart
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import javax.security.auth.callback.Callback

class FragmentProfile : Fragment(R.layout.fragment_profile) {
    private lateinit var launcher: ActivityResultLauncher<Void>
    private val model: NetflixViewModel by activityViewModels()
    lateinit var binding: FragmentProfileBinding
    var imageBitmap: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        with(binding) {
            model.bitmap?.let {
                profileImage.setImageBitmap(it)
            }
            profileImage.setOnClickListener {
                launcher.launch(null)
            }
            profileImage.setOnLongClickListener {
                binding.profileImage.setImageBitmap(null)
                true
            }
            profileGet.setOnClickListener {
                get(profileUsername.text?.toString() ?: return@setOnClickListener)
            }
            profilePost.setOnClickListener {
                val imageName = profileUsername.text?.toString() ?: return@setOnClickListener
                post(imageName)
            }
        }
    }

    fun get(name: String) {
        val call = service.getImage(name.trim())
        call.enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                binding.profileUsername.error = response.code().toString()
                val body = response.body() ?: return
                val byteArray = body.byteStream()
                val file = File(requireContext().filesDir, "image.png")
                val writer = FileOutputStream(file)
                writer.write(byteArray.readBytes())
                while (!file.canRead()) {}
                val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                model.bitmap = bitmap
                binding.profileImage.setImageBitmap(bitmap)
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(context, "Problem!!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun post(imageName: String) {
        val body = MultipartBody.create(MediaType.parse("image/*"), getByteArray() ?: return)
        val image = MultipartBody.Part.createFormData("image", imageName, body)
        val call = service.putImage(imageName, image)
        call.enqueue(object : retrofit2.Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                binding.profileUsername.error = response.code().toString()
                Toast.makeText(context, "uploaded", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun getByteArray(): ByteArray? {
        return if(imageBitmap != null) {
            val output = ByteArrayOutputStream()
            imageBitmap!!.compress(Bitmap.CompressFormat.PNG, 100, output)
            output.toByteArray()
        } else {
            null
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        launcher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) {
            imageBitmap = it
            binding.profileImage.setImageBitmap(it)
        }
    }
}
