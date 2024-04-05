package com.example.mybudgetapp.ui.widgets

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mybudgetapp.ui.theme.dmSans
import com.example.mybudgetapp.ui.viewmodels.ScreenItemsUiState
import com.example.mybudgetapp.ui.viewmodels.ThisMonthScreenUiState

data class DropDownItem (
    val title: String,
    val number: Int,
)

@Composable
fun DropDownMenu(
    dropDownItem: List<DropDownItem>,
    isContextMenuVisible: Boolean,
    onDismissRequest: (ScreenItemsUiState) -> Unit,
    uiState: ScreenItemsUiState,
    modifier: Modifier = Modifier,
    onSelected: (Int) -> Unit
){


    val density = LocalDensity.current

   DropdownMenu(
       expanded = isContextMenuVisible ,
       onDismissRequest = {
           onDismissRequest(
               uiState.copy(
                   isDropDownMenuVisible = !uiState.isDropDownMenuVisible
               )
           )
       },
       offset = uiState.offSet.copy(
           y = uiState.offSet.y
       )
   ) {

       dropDownItem.forEach { item ->

           DropdownMenuItem(
               text = { Text(
                   text = item.title,
                   fontSize = 20.sp,
                   fontWeight = FontWeight.Bold,
                   fontFamily = dmSans
               ) },
               onClick ={onSelected(item.number)} ,
               modifier = Modifier.padding(horizontal = 8.dp)
           )

       }
   }

}


