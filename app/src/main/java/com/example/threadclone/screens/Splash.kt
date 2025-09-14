package com.example.threadclone.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.example.threadclone.navigation.Routes
import kotlinx.coroutines.delay
import com.example.threadclone.R
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Splash(modifier: Modifier = Modifier,navController: NavHostController) {

    Column(modifier = modifier.fillMaxSize()) {
//        Text(text = "Splash",
//            modifier = modifier)

        Image(painter = painterResource(id=R.drawable.logo), contentDescription = null,
            modifier = Modifier.fillMaxSize())
    }

    LaunchedEffect(true) {
        delay(2000)
        if(FirebaseAuth.getInstance().currentUser!=null){
            navController.navigate(Routes.BottomNav.routes){
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
        else{
            navController.navigate(Routes.Login.routes){
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
//        navController.navigate(Routes.BottomNav.routes)
    }
}