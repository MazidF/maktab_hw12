package com.example.hw12.ui.profile

import android.app.AlertDialog
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.hw12.data.network.NetworkManager.imageService
import com.example.hw12.R
import com.example.hw12.databinding.FragmentProfileBinding
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class FragmentProfile : Fragment(R.layout.fragment_profile) {
    private lateinit var cameraLauncher: ActivityResultLauncher<Void>
    private lateinit var galleryLauncher: ActivityResultLauncher<String>
    private val model: ViewModelProfile by activityViewModels()
    lateinit var binding: FragmentProfileBinding
    lateinit var dialog: AlertDialog

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
        createDialog()
        with(binding) {
            model.bitmap?.let {
                profileImage.setImageBitmap(it)
            }
            profileImageEdit.setOnClickListener {
                dialog.show()
            }
            profileImage.setOnLongClickListener {
                binding.profileImage.setImageBitmap(null)
                true
            }
        }
    }

    private fun createDialog() {
        dialog = AlertDialog.Builder(requireContext())
            .setItems(arrayOf("Camera", "Gallery")) { _, itemIndex ->
                if (itemIndex == 0) { // camera
                    cameraLauncher.launch(null)
                } else {
                    galleryLauncher.launch("image/png")
                }
            }
            .create()
    }

    fun get(name: String) {
        val call = imageService.getImage(name.trim())
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

    private fun post(imageName: String, bytes: ByteArray) {
        model.post(imageName, bytes)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) {
            imageBitmap = it
            binding.profileImage.setImageBitmap(it)
        }
        galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
            val input = context.contentResolver.openInputStream(it) ?: return@registerForActivityResult
            input.readBytes()
        }
    }
}
