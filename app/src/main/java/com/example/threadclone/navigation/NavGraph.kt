package com.example.threadclone.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.threadclone.screens.AddThreads
import com.example.threadclone.screens.Home
import com.example.threadclone.screens.Notification
import com.example.threadclone.screens.Profile
import com.example.threadclone.screens.Search
import com.example.threadclone.screens.Splash
import com.example.threadclone.screens.ButtonNav
import com.example.threadclone.screens.Login
import com.example.threadclone.screens.OtherUser
import com.example.threadclone.screens.Register

@Composable
fun NavGraph(navController: NavHostController, modifier: Modifier) {

    NavHost(navController=navController, startDestination = Routes.Splash.routes){

        composable(Routes.Splash.routes) {
            Splash(
                modifier = modifier,
                navController = navController
            )
        }
        composable(Routes.Home.routes) {
            Home(navController)
        }
        composable(Routes.Notification.routes) {
            Notification()
        }
        composable(Routes.Search.routes) {
            Search(navController)
        }
        composable(Routes.AddThread.routes) {
            AddThreads(navController)
        }
        composable(Routes.Profile.routes) {
            Profile(
                navHostController = navController
            )
        }
        composable(Routes.BottomNav.routes) {
            ButtonNav(
                modifier = modifier,
                navController = navController
            )
        }
        composable(Routes.Login.routes) {
            Login(modifier = modifier,navController = navController)
        }
        composable(Routes.Register.routes) {
            Register(modifier= modifier,navController = navController)
        }
        composable(Routes.OtherUsers.routes) {
            val data=it.arguments!!.getString("data")
            OtherUser(navController,data!!)
        }
    }
}