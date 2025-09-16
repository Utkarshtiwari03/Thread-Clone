package com.example.threadclone.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.threadclone.model.UserModel
import com.example.threadclone.utils.SharedPref
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import java.util.UUID

class AuthViewModel: ViewModel() {

    val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance()
    val userRef = db.getReference("users")

    private val storageRef = Firebase.storage.reference
    private val imageRef = storageRef.child("users/${UUID.randomUUID()}.jpg")

    private val _firebaseUser = MutableLiveData<FirebaseUser??>()
    val firebaseUser: MutableLiveData<FirebaseUser?> = _firebaseUser

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        _firebaseUser.value = auth.currentUser
    }

    fun login(email: String, password: String, context: Context) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                _firebaseUser.postValue(auth.currentUser);
                getData(auth.currentUser!!.uid,context)
            } else {
                _error.postValue(it.exception!!.message);
            }
        }
    }

    private fun getData(uid: String,context: Context) {



        userRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val userData=snapshot.getValue(UserModel::class.java)
                SharedPref.storeData(userData!!.name,userData!!.email,userData!!.cio,userData!!.userName,userData!!.imageUrl,context)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun register(
        email: String,
        password: String,
        name: String,
        cio: String,
        userName: String,
        imageUri: Uri?,
        context: Context
    ) {

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                _firebaseUser.postValue(auth.currentUser);
                saveImage(email, password, name, cio, userName, imageUri, auth.currentUser?.uid,context)
//                val defaultImageUrl = "https://i.stack.imgur.com/l60Hf.png"
//                val uid = auth.currentUser?.uid
//                saveData(email,password,name,cio,userName,defaultImageUrl,uid,context)
                Log.d("TAG", "register: functioin working")
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
        uid: String?,
        context: Context
    ) {
        val uploadTask = imageRef.putFile(imageUri!!)
        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {
                saveData(email,password,name,cio,userName,it.toString(),uid,context)
                Log.d("TAG", "Save Image: functioin working")
            }.addOnFailureListener {
                Log.d("TAG", "save iamge: function not working")
            }
        }
    }

    private fun saveData(
        email: String,
        password: String,
        name: String,
        cio: String,
        userName: String,
        toString: String,
        uid: String?,
        context: Context
    ){

        val fireStoreDb = Firebase.firestore
        val followersRef = fireStoreDb.collection("followers").document(uid!!)
        val followingRef= fireStoreDb.collection("following").document(uid)

        followingRef.set(mapOf("followingIds" to listOf<String>()))
        followersRef.set(mapOf("followerIds" to listOf<String>()))
        val userData= UserModel(email,password,name,cio,userName,toString,uid!!)

        userRef.child(uid!!).setValue(userData)
            .addOnSuccessListener {
                SharedPref.storeData(name,email,cio,userName,toString,context)
                Log.d("TAG", "save data: functio    n working")
            }.addOnFailureListener {
                Log.d("TAG", "save data: function not working")
            }
    }

    fun logout(){
        auth.signOut()
        _firebaseUser.postValue(null)
    }
}
