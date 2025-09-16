package com.example.threadclone.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil3.compose.rememberAsyncImagePainter
import com.example.threadclone.model.ThreadModel
import com.example.threadclone.model.UserModel
import com.example.threadclone.viewmodel.HomeViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlin.concurrent.thread
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import com.example.threadclone.components.ThreadItem

@Composable
fun Home(navHostController: NavHostController) {

    val homeViewModel: HomeViewModel=viewModel()
    val threadsAndUsers by homeViewModel.threadsAndUsers.observeAsState(null)



    LazyColumn {
        items(threadsAndUsers.orEmpty()) {pairs->
            ThreadItem(
                thread=pairs.first,
                userModel=pairs.second,
                navHostController,
                FirebaseAuth.getInstance().currentUser?.uid?: ""
            )
        }
        }
}



