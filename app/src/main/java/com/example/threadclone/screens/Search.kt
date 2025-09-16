package com.example.threadclone.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.threadclone.components.ThreadItem
import com.example.threadclone.components.UserItem
import com.example.threadclone.viewmodel.HomeViewModel
import com.example.threadclone.viewmodel.SearchViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlin.collections.orEmpty

@Composable
fun Search(navHostController: NavHostController) {
    val searchViewModel: SearchViewModel=viewModel()
    val threadsAndUsers by searchViewModel.usersList.observeAsState(null)

    var search by remember {
        mutableStateOf("")
    }

    Column {

        Text(text="Search",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 24.sp,
            modifier = Modifier.padding(top=12.dp,start=16.dp))

        OutlinedTextField(value = search, onValueChange = {
            search=it
        },label={
            Text(text="Search User")
        }, keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password
        ), singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            leadingIcon = {
                Icon(imageVector = Icons.Rounded.Search, contentDescription = "Search")
            }
        )

        Box(modifier = Modifier.height(20.dp))

        LazyColumn {
            if(threadsAndUsers!=null && threadsAndUsers!!.isNotEmpty()) {
                val filterItems = threadsAndUsers!!.filter {
                    it.name.contains(search, ignoreCase = true)
                }
                items(filterItems) { pairs ->
                    UserItem(
                        userModel = pairs,
                        navHostController = navHostController
                    )
                }
            }
        }
    }
}