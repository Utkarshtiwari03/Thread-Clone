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
            Home(modifier = modifier)
        }
        composable(Routes.Notification.routes) {
            Notification(modifier = modifier)
        }
        composable(Routes.Search.routes) {
            Search(modifier = modifier)
        }
        composable(Routes.AddThread.routes) {
            AddThreads(modifier = modifier)
        }
        composable(Routes.Profile.routes) {
            Profile(modifier = modifier)
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
    }
}