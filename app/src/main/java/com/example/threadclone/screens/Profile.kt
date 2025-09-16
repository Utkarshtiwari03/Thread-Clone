package com.example.threadclone.screens


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.threadclone.viewmodel.AuthViewModel
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import com.example.threadclone.navigation.Routes
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.platform.LocalContext
import com.example.threadclone.components.ProfileCard
import com.example.threadclone.components.ThreadItem
import com.example.threadclone.model.UserModel
import com.example.threadclone.utils.SharedPref
import com.example.threadclone.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

@Composable
fun Profile(navHostController: NavHostController) {

    var authviewModel: AuthViewModel = viewModel()
    val firebaseUser by authviewModel.firebaseUser.observeAsState();

    var userViewModel: UserViewModel = viewModel()
    val threads by userViewModel.threads.observeAsState(null);


    val followerList by userViewModel.followerList.observeAsState(null)
    val followingList by userViewModel.followingList.observeAsState(null)

    var currentUserId="";
    if(FirebaseAuth.getInstance().currentUser!=null) {
        currentUserId= FirebaseAuth.getInstance().currentUser!!.uid
    }

    if(currentUserId!=null) {
        userViewModel.getFollowers(currentUserId)
        userViewModel.getFollowing(currentUserId)
    }



    val context= LocalContext.current

    val user= UserModel(
        name=SharedPref.getName(LocalContext.current),
        userName = SharedPref.getUserName(context),
        imageUrl=SharedPref.getImage(LocalContext.current),

    )

    if(firebaseUser!=null) {
        userViewModel.fetchThreads(firebaseUser!!.uid)
    }

    LaunchedEffect(firebaseUser == null) {
        if (firebaseUser == null) {
            navHostController.navigate(Routes.Login.routes) {
                popUpTo(navHostController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
    }


    LazyColumn {
        item {
            ProfileCard(
                context = context,
//        name = "Papaya Coders",
                followers = followerList!!.size,
                following = followingList!!.size,
                website = "papayacoders.in",
                onLogoutClick = { authviewModel.logout() }
            )
        }
        if(firebaseUser!=null){
        items(threads ?: emptyList()) { pair ->
            ThreadItem(
                thread = pair,
                userModel = user,
                navHostController = navHostController,
                userId = FirebaseAuth.getInstance().currentUser!!.uid
            )
        }
        }
    }

}
