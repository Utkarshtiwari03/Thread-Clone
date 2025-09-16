package com.example.threadclone.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.threadclone.navigation.Routes
import com.example.threadclone.utils.SharedPref
import com.example.threadclone.viewmodel.AddThreadViewModel
import com.google.firebase.auth.FirebaseAuth

//@Preview(showSystemUi = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddThreads(navController: NavHostController) {

    val threadViewModel: AddThreadViewModel= viewModel()

    val isPosted by threadViewModel.isPosted.observeAsState(false)

    var text by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }


    val context= LocalContext.current

    // Media picker launcher
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    //checking weather poster or not

    LaunchedEffect(isPosted) {
        if(isPosted!!){
            text=""
            selectedImageUri=null
            Toast.makeText(context, "Thread added", Toast.LENGTH_SHORT).show()
            navController.navigate(Routes.Home.routes){
                popUpTo(Routes.AddThread.routes){
                    inclusive=true
                }
                launchSingleTop = true
            }
        }
        else{
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        // Top Row -> Close button + Title
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                navController.navigate(Routes.Home.routes){
                    popUpTo(Routes.AddThread.routes){
                        inclusive=true
                    }
                    launchSingleTop = true
                }
            }) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("New thread", style = MaterialTheme.typography.titleMedium)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Profile + Thread input
        Row(verticalAlignment = Alignment.Top) {
            // Profile logo

            Image(painter=rememberAsyncImagePainter(model= SharedPref.getImage(context))
                ,contentDescription = null,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.Gray),
                contentScale = ContentScale.Crop
                )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = SharedPref.getUserName(context),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )

                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    placeholder = { Text("Start a thread...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 80.dp),
                    maxLines = 5,
                    shape = RoundedCornerShape(12.dp)
                )
                if(selectedImageUri==null) {
                    // Attachment icon
                    IconButton(
                        onClick = { launcher.launch("image/*") },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Attach file")
                    }
                }
            }
        }

        //Show  Selected Image and close button
        selectedImageUri?.let { uri ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = "Selected image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )

                IconButton(
                    onClick = { selectedImageUri = null },
                    modifier = Modifier
                        .padding(4.dp)
                        .size(28.dp)
                        .background(Color.White.copy(alpha = 0.7f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Remove image",
                        tint = Color.Black
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Bottom Row -> "Anyone can reply" + Post
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Anyone can reply",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            TextButton(onClick = {
                if(selectedImageUri==null){
                    threadViewModel.saveData(thread = text, userId = FirebaseAuth.getInstance().currentUser!!.uid,"")
                }
                else{
                    threadViewModel.saveImage(text,selectedImageUri!!,FirebaseAuth.getInstance().currentUser!!.uid)
                }
            }) {
                Text("Post")
            }
        }
    }
}
