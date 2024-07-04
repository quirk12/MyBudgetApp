package com.example.mybudgetapp.ui.screens

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mybudgetapp.R
import com.example.mybudgetapp.ui.navigation.NavigationDestination
import com.example.mybudgetapp.ui.theme.dmSans
import com.example.mybudgetapp.ui.viewmodels.AddingItemScreenViewModel
import com.example.mybudgetapp.ui.viewmodels.AppViewModelProvider
import com.example.mybudgetapp.ui.viewmodels.ItemDetails
import com.example.mybudgetapp.ui.viewmodels.SpendingItemDetailsUiState
import com.example.mybudgetapp.ui.widgets.BudgetTopAppBar
import kotlinx.coroutines.launch

object AddingItemDestination: NavigationDestination {
    override val route = "AddingItem"
    override val titleRes = R.string.adding_item_screen
    const val category = "category"
    val routeWithArgs = "${AddingItemDestination.route}/{$category}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddingItem(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit
){
    val viewModel: AddingItemScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val uiState: SpendingItemDetailsUiState = viewModel.uiState
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    Scaffold (
        topBar =  {
            BudgetTopAppBar(
                canNavigateBack = true,
                title = stringResource(id = R.string.adding_item_screen_title,
                    when(uiState.previousCategory){
                        "food" -> "Food"
                        "transportation" -> "Transit"
                        "others" -> "Others"
                        "income" -> "Income"
                        "all" -> "Spending"
                        else -> ""
                    }
                    ),
                navigateBack = navigateBack
            )
        }
    ) {paddingValues ->
        AddingItemBody(
            Modifier.padding(paddingValues),
            onImageSelected = {context:Context, uri: Uri? ->viewModel.onImageSelected(context = context, uri = uri)},
            itemDetails = uiState.itemDetails,
            saveItem = {
                       coroutineScope.launch {
                           viewModel.onSaveButtonClicked(context = context)
                       }
                Toast.makeText(context, R.string.item_added, Toast.LENGTH_SHORT).show()
                navigateBack()
            },
            onItemValueChange = viewModel::updateUiState,
            isUploadSuccessful = uiState.isUploadSuccessful,
            isEntryValid = uiState.isEntryValid,
            previousCategory = uiState.previousCategory
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddingItemBody(
    modifier: Modifier = Modifier,
    onImageSelected: (context: Context, uri: Uri?) -> Unit,
    onItemValueChange: (ItemDetails) -> Unit,
    itemDetails: ItemDetails,
    isUploadSuccessful: Boolean,
    isEntryValid: Boolean,
    saveItem: () -> Unit,
    previousCategory: String,
) {
    var isExpanded by remember {
        mutableStateOf(false)
    }
    var category by remember {
        mutableStateOf(R.string.enter_category)
    }
    val context = LocalContext.current

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {uri: Uri? ->

            onImageSelected(context, uri)

        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 20.dp)
            .then(modifier),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = itemDetails.name,
            onValueChange = {
                onItemValueChange(itemDetails.copy(name = it))
                            },
            label = { Text(text = stringResource(id = R.string.enter_item_name))},
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)

        )
        OutlinedTextField(
            value = itemDetails.cost,
            onValueChange = { onItemValueChange(itemDetails.copy(cost = it.replace(regex = Regex("[ ,-]"), ""))) },
            label = { Text(text = stringResource(id = R.string.enter_cost))},
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Next)
        )

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {

            if(previousCategory == "all") {
                ExposedDropdownMenuBox(
                    expanded = isExpanded,
                    onExpandedChange = {newValue ->
                        isExpanded = newValue
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = stringResource(id = category) ,
                        onValueChange = {/* TODO */  },
                        readOnly = true,
                        trailingIcon = {ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)},
                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = isExpanded ,
                        onDismissRequest = { isExpanded = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        DropdownMenuItem(
                            text = { Text(text = stringResource(id = R.string.food_spending)) },
                            onClick = {
                                onItemValueChange(itemDetails.copy(category = "food"))
                                isExpanded = false
                                category = R.string.food_spending
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        DropdownMenuItem(
                            text = { Text(text = stringResource(id = R.string.transportation_spending)) },
                            onClick = {
                                onItemValueChange(itemDetails.copy(category = "transportation"))
                                isExpanded = false
                                category = R.string.transportation_spending
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        DropdownMenuItem(
                            text = { Text(text = stringResource(id = R.string.other_spending)) },
                            onClick = {
                                onItemValueChange(itemDetails.copy(category = "others"))
                                isExpanded = false
                                category = R.string.other_spending
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

        }

        if(previousCategory != "income") {
            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    imageVector = if(isUploadSuccessful) Icons.Filled.Check else Icons.Filled.Close,
                    contentDescription = null,
                    tint = if(isUploadSuccessful) Color.Green else Color.Red,
                )
                TextButton(onClick = {
                    photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }) {

                    Text(
                        text = stringResource(id = R.string.upload_picture),
                        fontSize = 26.sp,
                        fontFamily = dmSans,
                        fontWeight = FontWeight.Bold
                    )

                }

            }
        }


        Button(
            onClick = saveItem,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            enabled = isEntryValid
        ) {
            Text(text = stringResource(id = R.string.save_button))
        }


    }
}

@Preview
@Composable
fun AddingItemBodyPreview() {

}

