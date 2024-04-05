package com.example.mybudgetapp.ui.widgets

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mybudgetapp.R
import com.example.mybudgetapp.data.NavigationItems


@Composable
fun BottomNavigationBar(){
    var selectedItemIndex by rememberSaveable {
            mutableIntStateOf(1)
    }
    NavigationBar (
        modifier = Modifier.wrapContentHeight(),
        tonalElevation = 2.dp,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        NavigationItems.items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItemIndex == index,
                onClick = { selectedItemIndex = index },
                label = {
                        Text(text = item.title)
                        },
                icon = {
                    Icon(
                        painter = if (index == selectedItemIndex) painterResource( id = item.selectedIcon ) else painterResource(id = item.unSelectedIcon)
                    ,
                        contentDescription = item.title
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetTopAppBar(
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    title: String
) {
    CenterAlignedTopAppBar(title = { Text(text = title) },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(MaterialTheme.colorScheme.surface),
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateBack) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            }
        })
}

@Preview
@Composable
fun BottomNavigationPreview() {
    BottomNavigationBar()
}