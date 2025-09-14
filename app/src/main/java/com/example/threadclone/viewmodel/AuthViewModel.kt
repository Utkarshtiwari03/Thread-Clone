package com.example.threadclone.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.threadclone.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.storage
import java.util.UUID

class AuthViewModel: ViewModel() {

    val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance()
    val userRef = db.getReference("users")

    private val storageRef = Firebase.storage.reference
    private val imageRef = storageRef.child("users/${UUID.randomUUID()}.jpg")

    private val _firebaseUser = MutableLiveData<FirebaseUser>()
    val firebaseUser: MutableLiveData<FirebaseUser> = _firebaseUser

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        _firebaseUser.value = auth.currentUser
    }

    fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                _firebaseUser.postValue(auth.currentUser);
            } else {
                _error.postValue("Something went wrong");
            }
        }
    }

    fun register(
        email: String,
        password: String,
        name: String,
        cio: String,
        userName: String,
        imageUri: Uri?
    ) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                _firebaseUser.postValue(auth.currentUser);
                saveImage(email, password, name, cio, userName, imageUri, auth.currentUser?.uid)
            } else {
                _error.postValue("Something went wrong");
            }
        }
    }

    private fun saveImage(
        email: String,
        password: String,
        name: String,
        cio: String,
        userName: String,
        imageUri: Uri?,
        uid: String?
    ) {
        val uploadTask = imageRef.putFile(imageUri!!)
        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {
                saveData(email,password,name,cio,userName,it.toString())
            }
        }
    }

    private fun saveData(
        email: String,
        password: String,
        name: String,
        cio: String,
        userName: String,
        uid: String?
    ){
        val userData= UserModel(email,password,name,cio,userName,toString())

        userRef.child(uid!!).setValue(userData)
            .addOnSuccessListener {

            }.addOnFailureListener {

            }
    }
}
