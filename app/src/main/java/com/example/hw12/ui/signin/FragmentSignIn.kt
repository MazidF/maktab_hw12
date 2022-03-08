package com.example.hw12.ui.signin

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.graphics.Color.RED
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hw12.R
import com.example.hw12.databinding.DialogLoginBinding
import com.example.hw12.databinding.FragmentSignInBinding
import com.example.hw12.ui.profile.FragmentProfile
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.*

class FragmentSignIn : Fragment(R.layout.fragment_sign_in) {
    private lateinit var galleryLauncher: ActivityResultLauncher<String>
    private lateinit var cameraLauncher: ActivityResultLauncher<Void>
    private val model: ViewModelSignIn by activityViewModels()
    lateinit var binding: FragmentSignInBinding
    private val navController by lazy {
        findNavController()
    }
    lateinit var dialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
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

    }

    private fun post() {
        model.post(binding.profileImage.drawingCache).apply {
            observe(viewLifecycleOwner) {
                if (it.resource != null) {
                    goToProfile(it.resource!!)
                } else {
                    Toast.makeText(requireContext(), "Failed to SignIn", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun goToProfile(id: String) {
/*        parentFragmentManager.commit {
            replace<FragmentProfile>(R.id.container)
        }*/
        navController.navigate(FragmentSignInDirections.actionFragmentSignInToFragmentProfile(id))
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) {
            model.bitmap.value = it
        }
        galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
            val input =
                context.contentResolver.openInputStream(it) ?: return@registerForActivityResult
            val bytes = input.readBytes()
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun init() {
        createDialog()
        with(binding) {
            bitmap = model.bitmap
            lifecycleOwner = this@FragmentSignIn.viewLifecycleOwner
            profileImageEdit.setOnClickListener {
                dialog.show()
            }
            profileImageRemove.setOnClickListener {
                model.bitmap.value = null
            }
            profileBirthdayLayout.setEndIconOnClickListener {
                val datePicker = pikerMaker(profileBirthday)
                datePicker.show()
            }
            val simpleRegex = Regex("^(\\s?[a-zA-Z]*)+$")
            profileName.setRegex(simpleRegex)
            profileFamily.setRegex(simpleRegex)
            profileEmail.setRegex(Regex("^\\w+([\\.-]?\\w+)*$"))
            profilePhone.setRegex(Regex("^(0|\\+98)9(1[0-9]|3[1-9])[0-9]{7}$"))
            profileBirthday.setRegex(Regex("^\\d{4}/\\d{2}/\\d{2}$"))
            profileRegister.setOnClickListener {
                if (checker()) {
                    post()
                }
            }
            profileLogin.setOnClickListener {
                val context = requireContext()
                val dialogBinding = DialogLoginBinding.inflate(layoutInflater)
                val loginDialog = AlertDialog.Builder(context).setView(dialogBinding.root).create()
                val adapter = UserAdapter(arrayListOf()) {
                    loginDialog.cancel()
                    goToProfile(it._id)
                }
                dialogBinding.apply {
                    loginList.apply {
                        this.adapter = adapter
                        layoutManager = LinearLayoutManager(context)
                    }
                }
                model.getUsers().also { liveData ->
                    liveData.observeForever {
                        if (it != null && it.isSuccess && it.resource != null) {
                            adapter.addList(it.resource!!)
                            dialogBinding.apply {
                                loginList.visibility = VISIBLE
                                loginProgressbar.visibility = GONE
                            }
                        } else {
                            loginDialog.cancel()
                            Toast.makeText(context, "Failed to login!!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                loginDialog.show()
            }
            profileNameLayout.setHelperTextColor(ColorStateList.valueOf(RED))
            profileFamilyLayout.setHelperTextColor(ColorStateList.valueOf(RED))
            profileEmailLayout.setHelperTextColor(ColorStateList.valueOf(RED))
        }
        load()
    }

    private fun checker(): Boolean {

        fun checkIfNotEmpty(editText: TextInputEditText, parent: TextInputLayout): Boolean {
            var result = false
            parent.helperText = if (editText.text!!.isBlank()) {
                "*Required"
            } else if (editText.currentTextColor == RED) {
                "Wrong Input"
            } else {
                result = true
                ""
            }
            if (!result) {
                editText.requestFocus()
            }
            return result
        }

        fun checkIfValid(editText: TextInputEditText): Boolean {
            if (editText.text!!.isNotBlank() && editText.currentTextColor == RED) {
                editText.requestFocus()
                return false
            }
            return true
        }

        var result: Boolean
        with(binding) {
            result = checkIfValid(profileBirthday)
            result = result and checkIfValid(profilePhone)
            result = result and checkIfNotEmpty(profileEmail, profileEmailLayout)
            result = result and checkIfNotEmpty(profileFamily, profileFamilyLayout)
            result = result and checkIfNotEmpty(profileName, profileNameLayout)
        }
        return result
    }

    private fun pikerMaker(editText: TextInputEditText): DatePickerDialog {
        val cal = Calendar.getInstance()
        return DatePickerDialog(requireContext(), { _, year, month, day ->
            editText.setText(String.format("%4d/%02d/%02d", year, month + 1, day))
        }, cal.get(Calendar.YEAR), cal[Calendar.MONTH], cal[Calendar.DAY_OF_MONTH])
    }

    private fun TextInputEditText.setRegex(regex: Regex, task: ((CharSequence) -> Unit)? = null) {
        val color = currentTextColor
        doOnTextChanged { text, _, _, _ ->
            if (!regex.matches(text!!)) {
                this.setTextColor(RED)
            } else {
                this.setTextColor(color)
            }
            task?.invoke(text)
        }
    }

    private fun log(msg: String) {
        Log.d("tag-tag", msg)
    }

    override fun onPause() {
        binding.run {
            profileNameLayout.helperText = ""
            profileEmailLayout.helperText = ""
            profileFamilyLayout.helperText = ""
        }
        save()
        super.onPause()
    }

    private fun save() {
        log("saved")
        with(binding) {
            with(model) {
                userInfo[0] = profileName.text.toString()
                userInfo[1] = profileFamily.text.toString()
                userInfo[2] = profileEmail.text.toString()
                userInfo[3] = profileUsername.text.toString()
                userInfo[4] = profilePhone.text.toString()
                userInfo[5] = profileBirthday.text.toString()
            }
        }
    }

    private fun load() {
        log("loaded")
        with(binding) {
            with(model) {
                var i = 0
                profileName.setText(userInfo[i++])
                profileFamily.setText(userInfo[i++])
                profileEmail.setText(userInfo[i++])
                profileUsername.setText(userInfo[i++])
                profilePhone.setText(userInfo[i++])
                profileBirthday.setText(userInfo[i])
            }
        }
    }
}
