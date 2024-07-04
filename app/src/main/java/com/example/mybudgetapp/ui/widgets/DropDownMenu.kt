package com.example.mybudgetapp.ui.widgets

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mybudgetapp.R
import com.example.mybudgetapp.ui.theme.dmSans
import com.example.mybudgetapp.ui.viewmodels.ScreenItemsUiState

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

@Composable
fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit, onDeleteCancel: () -> Unit, modifier: Modifier = Modifier
) {
    AlertDialog(onDismissRequest = onDeleteCancel,
        title = { Text(stringResource(R.string.attention)) },
        text = { Text(stringResource(R.string.delete_question)) },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(stringResource(R.string.no))
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(stringResource(R.string.yes))
            }
        })
}


