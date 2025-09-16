package com.example.threadclone.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil3.compose.rememberAsyncImagePainter
import com.example.threadclone.components.ThreadItem
import com.example.threadclone.model.UserModel
import com.example.threadclone.navigation.Routes
import com.example.threadclone.viewmodel.AuthViewModel
import com.example.threadclone.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun OtherUser(navHostController: NavHostController, uid: String?) {

    var authviewModel: AuthViewModel = viewModel()
    val firebaseUser by authviewModel.firebaseUser.observeAsState(null);

    var userViewModel: UserViewModel = viewModel()
    val threads by userViewModel.threads.observeAsState(null);
    val users by userViewModel.users.observeAsState(null)
    val followerList by userViewModel.followerList.observeAsState(null)
    val followingList by userViewModel.followingList.observeAsState(null)




    userViewModel.fetchThreads(uid!!)
    userViewModel.fetchUser(uid)
    userViewModel.getFollowers(uid)
    userViewModel.getFollowing(uid)

    var currentUserId="";
    if(FirebaseAuth.getInstance().currentUser!=null) {
        currentUserId= FirebaseAuth.getInstance().currentUser!!.uid
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
            UserCard(
                users=users,
//        name = "Papaya Coders",
                followers = followerList?.size ?: 0,
                following = followingList?.size ?: 0,
                website = "papayacoders.in",
                userViewModel=userViewModel,
                uid=uid,
                currentUserId=currentUserId,
                followerList=followerList
            )
        }
        if(threads!=null && users!=null) {
            items(threads ?: emptyList()) { pair ->
                ThreadItem(
                    thread = pair,
                    userModel = users!!,
                    navHostController = navHostController,
                    userId = FirebaseAuth.getInstance().currentUser!!.uid,
                )

            }
        }
    }
}


@Composable
fun UserCard(
    followers: Int,
    following: Int,
    website: String,
    users: UserModel?,
    userViewModel: UserViewModel,
    uid: String,
    currentUserId: String,
    followerList: List<String>?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Top Row -> Name, Username, Profile Image
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = users?.name ?: "",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = users?.userName ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }

                Image(
                    painter = rememberAsyncImagePainter(users?.imageUrl),
                    contentDescription = "Profile image",
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Bio text
            Text(
                text = users?.cio ?: "",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Followers | Website | Following
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("$followers Followers", style = MaterialTheme.typography.bodySmall)
                Text(website, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                Text("$following Following", style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Logout button
            Button(
                onClick = {
                    if(currentUserId!=null) {
                        userViewModel.followUsers(uid, currentUserId)
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(0.5f),
                shape = RoundedCornerShape(50)
            ) {
                Text(
                    if(followerList!=null && followerList.isNotEmpty() && followerList?.contains(currentUserId) == true) "Following" else "Follow"
                )
            }
        }
    }
}
