package com.example.threadclone.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.threadclone.model.ThreadModel
import com.example.threadclone.model.UserModel
import com.example.threadclone.utils.SharedPref
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.storage
import java.util.UUID

class AddThreadViewModel: ViewModel() {

    private val db = FirebaseDatabase.getInstance()
    val userRef = db.getReference("threads")

    private val storageRef = Firebase.storage.reference
    private val imageRef = storageRef.child("threads/${UUID.randomUUID()}.jpg")

    private val _isPosted = MutableLiveData<Boolean>()
    val isPosted: LiveData<Boolean> = _isPosted








     fun saveImage(
        thread: String,
        imageUri: Uri,
        userId: String,

    ) {
        val uploadTask = imageRef.putFile(imageUri!!)
        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {
                saveData(thread,userId,it.toString())
                Log.d("TAG", "Save Image: functioin working")
            }.addOnFailureListener {
                Log.d("TAG", "save iamge: function not working")
            }
        }
    }

    fun saveData(
        thread: String,
        userId: String,
        imageUrl: String
    ){
        val threadData= ThreadModel(thread,imageUrl,userId, System.currentTimeMillis().toString())

        userRef.child(userRef.push().key!!).setValue(threadData)
            .addOnSuccessListener {
                _isPosted.postValue(true)
            }.addOnFailureListener {
                _isPosted.postValue(false)
            }
    }


}
