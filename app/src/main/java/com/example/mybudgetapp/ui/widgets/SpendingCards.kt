package com.example.mybudgetapp.ui.widgets

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.mybudgetapp.R
import com.example.mybudgetapp.data.SpendingCategoryDisplayData
import com.example.mybudgetapp.ui.theme.inter

@Composable
fun TotalSpendingText (
    @StringRes spendingOn: Int,
    category: String,
    totalSpending: String,
) {
        Column (
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = spendingOn, category),
                fontFamily = inter,
                fontWeight = FontWeight.Thin,
                fontSize = 16.sp
            )
            Text(
                text = totalSpending,
                fontFamily = inter,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
}

@Composable
fun SpendingProgress(
    totalSpending: String,
    totalSpendingOnCategory: String,
    category:String,
    spendingRatio: Float,
    modifier: Modifier = Modifier
) {
    Card (
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, Color.Black) ,
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .then(modifier)
    ) {
        Column (
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
            ) {
                TotalSpendingText(
                    spendingOn = R.string.total_spending_text,
                    totalSpending = totalSpendingOnCategory,
                    category = category
                    )
                TotalSpendingText(
                    spendingOn = R.string.total_spending_on_text,
                    totalSpending = totalSpending,
                    category = category
                )
            }
            LinearProgressIndicator(
                progress = spendingRatio,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f), // Semi-transparent
                trackColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f), // Light background track
                modifier = Modifier
                    .height(6.dp) // Slimmer bar
                    .clip(RoundedCornerShape(percent = 50)) // Smooth, rounded ends
                    .background(Color(0xFFE0E0E0)) // Light gray background
                    .fillMaxWidth(),
            )
        }
        }


}

@Composable
fun ItemCard(
    modifier: Modifier = Modifier,
    title: String,
    totalSpending: String,
    displayItem: SpendingCategoryDisplayData,
    date: String,
    imagePath: String?,
    deleteItem: () -> Unit,
    navigateToItemDates: () -> Unit,
){
    var isContextMenuVisible by rememberSaveable {
        mutableStateOf(false)
    }
    var isDeleteMenuVisible by rememberSaveable {
        mutableStateOf(false)
    }
    var pressOffset by remember {
        mutableStateOf(DpOffset.Zero)
    }
    var itemHeight by remember {
        mutableStateOf(0.dp)
    }
    val interactionSource = remember {
        MutableInteractionSource()
    }
    val density = LocalDensity.current

    Card (
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(24.dp),
       // border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSecondaryContainer) ,
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondaryContainer) ,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .onSizeChanged {
                itemHeight = with(density) { it.height.toDp() }
            }
            .then(modifier),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .indication(interactionSource, LocalIndication.current)
             .pointerInput(true) {
                 detectTapGestures(
                     onLongPress = {
                         isContextMenuVisible = true
                         pressOffset = DpOffset(it.x.toDp(), it.y.toDp())
                     },
                     onPress = {
                         val press = PressInteraction.Press(it)
                         interactionSource.emit(press)
                         tryAwaitRelease()
                         interactionSource.emit(PressInteraction.Release(press))
                     },
                     onTap = {
                         navigateToItemDates()
                     }
                 )
             }
        ) {
            Row (
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ){

                Card (
                    elevation = CardDefaults.cardElevation(2.dp),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.size(72.dp),
                    colors = CardDefaults.cardColors(colorResource(id = displayItem.cardColor))
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        AsyncImage(
                            model = imagePath,
                            contentDescription = null,
                            placeholder = painterResource(id = displayItem.spendingIcon),
                            error = painterResource(id = displayItem.spendingIcon),
                            contentScale = ContentScale.FillBounds
                        )
                    }

                }

                Column (
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = title,
                        fontFamily = inter,
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp
                    )
                    Text(
                        text = date,
                        fontFamily = inter,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp
                    )
                }

            }

            Text(
                text = totalSpending,
                fontFamily = inter,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
        DropdownMenu(
            expanded = isContextMenuVisible ,
            onDismissRequest = {isContextMenuVisible = false},
            offset = pressOffset.copy (
                y = pressOffset.y - itemHeight
            ),
            modifier = Modifier.padding(8.dp)
        ) {
           /* DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(id = R.string.edit_item),
                        fontFamily = inter,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp
                    )
                },
                onClick = { 
                    isContextMenuVisible = false
                }
            )*/
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(id = R.string.delete_item),
                        fontFamily = inter,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp
                        )
                },
                onClick = {
                    isDeleteMenuVisible = true
                    isContextMenuVisible = false
                }
            )
        }
    }
    if(isDeleteMenuVisible) {
        DeleteConfirmationDialog(
            onDeleteCancel = {isDeleteMenuVisible = false} ,
            onDeleteConfirm = {
                deleteItem()
                isDeleteMenuVisible = false
            } ,
        )
    }
}

@Composable
fun TotalIncomeCard(
    modifier: Modifier = Modifier,
    @StringRes title: Int,
    totalSpending: String,
    month: String,
) {
    Card(
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .then(modifier),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 52.dp)
        ) {
            Text(
                text = stringResource(id = title, month),
                fontFamily = inter,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp
            )
            Text(
                text = totalSpending,
                fontFamily = inter,
                fontWeight = FontWeight.Black,
                fontSize = 20.sp
            )
        }
    }
}

@Composable
fun DateCard(
    modifier: Modifier = Modifier,
    title: String,
    totalSpending: String,
){

    Card (
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSecondaryContainer) ,
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer) ,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .then(modifier),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row (
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ){

                Column (
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = title,
                        fontFamily = inter,
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp
                    )
                }

            }

            Text(
                text = totalSpending,
                fontFamily = inter,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }

    }
}

@Composable
fun ItemCardForDates(
    modifier: Modifier = Modifier,
    title: String,
    displayItem: SpendingCategoryDisplayData,
    imagePath: String?,
    ){

    Card (
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSecondaryContainer) ,
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer) ,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .then(modifier),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Row (
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ){

                Card (
                    elevation = CardDefaults.cardElevation(2.dp),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.size(72.dp),
                    colors = CardDefaults.cardColors(colorResource(id = displayItem.cardColor))
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        AsyncImage(
                            model = imagePath,
                            contentDescription = null,
                            placeholder = painterResource(id = displayItem.spendingIcon),
                            error = painterResource(id = displayItem.spendingIcon),
                            contentScale = ContentScale.FillBounds
                        )
                    }

                }

                Column (
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = title,
                        fontFamily = inter,
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp
                    )
                }

            }
        }
    }
}

@Preview
@Composable
fun TotalSpendingPreview(){

}
@Preview
@Composable
fun TotalSpendingCardPreview(){

}
@Preview
@Composable
fun itemCardPreview(){
   // ItemCard(title = R.string.other_spending, totalSpending = "4$", item = SpendingCategoryDisplayObject.items[0] )
}
@Preview
@Composable
fun DateCardPreview(){
    DateCard(title = "hello", totalSpending = "100" )
}

