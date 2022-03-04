package com.example.hw12.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.gson.JsonObject
import java.io.ByteArrayOutputStream
import java.io.Serializable


data class User(var name: String, var family: String, val email: String) : Serializable {
    var userName = "$name $family"
    var phone: String? = null
    var birthday: String? = null
    var image: Bitmap? = null

    class SerializableUser(user: User, favorites: List<Int>?) : Serializable {
        private val name = user.name
        private val email = user.email
        private val phone = user.phone
        private val family = user.family
        private val userName = user.userName
        private val birthday = user.birthday
        private var bytes: ByteArray
        val favorites: List<Int>? = favorites

        init {
            val stream = ByteArrayOutputStream()
            user.image?.compress(Bitmap.CompressFormat.PNG, 100, stream)
            bytes = stream.toByteArray()
        }

        fun toUser() = User(name, family, email).apply {
            this.phone = this@SerializableUser.phone
            this.userName = this@SerializableUser.userName
            this.birthday = this@SerializableUser.birthday
            this.image = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }
    }

    fun toUserInfo() = UserInfo(
        firstName = name,
        lastName = family,
        nationalCode = email,
        hobbies = listOf(userName, phone.toString(), birthday.toString())
    )

    fun save(favorites: List<Int>? = null) = SerializableUser(this, favorites)

    companion object {
        fun fromUserInfo(userInfo: UserInfo): User {
            with(userInfo) {
                return User(firstName, lastName, nationalCode).apply {
                    userName = hobbies[0]
                    phone = hobbies[1]
                    birthday = hobbies[2]
                }
            }
        }

        fun fromJsonObject(jsonObject: JsonObject): User {
            val name = jsonObject["firstName"].asString
            val family = jsonObject["lastName"].asString
            val email = jsonObject["nationalCode"].asString
            val hobbies = jsonObject["hobbies"].asJsonArray
            val user = User(name, family, email).apply {
                userName = hobbies[0].asString
                phone = hobbies[0].asString
                birthday = hobbies[0].asString
            }
            return user
        }
    }
}
