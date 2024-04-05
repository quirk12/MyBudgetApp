package com.example.mybudgetapp.ui.widgets

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mybudgetapp.R
import com.example.mybudgetapp.data.SpendingCategoryDisplayData
import com.example.mybudgetapp.data.SpendingCategoryDisplayObject
import com.example.mybudgetapp.ui.theme.dmSans
import com.example.mybudgetapp.ui.theme.inter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryCard(
    modifier: Modifier = Modifier,
    totalSpending: String,
    item: SpendingCategoryDisplayData,
    navigateToSpendingOnCategory: () -> Unit
){
    Card (
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, Color.Black) ,
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondaryContainer),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .then(modifier),
        onClick = navigateToSpendingOnCategory
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            Row (
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ){

                Card (
                    elevation = CardDefaults.cardElevation(2.dp),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.size(52.dp),
                    colors = CardDefaults.cardColors(colorResource(id = item.cardColor))
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Image(
                            painter = painterResource(id = item.spendingIcon),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                        )
                    }

                }

                Text(
                    text = stringResource(id = item.title),
                    fontFamily = dmSans,
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp
                )
            }

            Text(
                text = totalSpending,
                fontFamily = dmSans,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TotalIncomeSpending(
    modifier: Modifier = Modifier,
   @DrawableRes icon: Int,
   @StringRes incomeOrSpending: Int,
    navigateToTotalIncome: () -> Unit,
    total: String
){

    Card (
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .then(modifier),
        onClick = navigateToTotalIncome
    ) {
        Box (
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.primaryContainer)
                .fillMaxSize()
                .padding(18.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth().align(Alignment.TopStart)
            ){
                Card (
                    elevation = CardDefaults.cardElevation(2.dp),
                    shape = RoundedCornerShape(percent = 50),
                    modifier = Modifier
                        .size(44.dp),
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.tertiaryContainer)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Image(
                            painter = painterResource(id = icon),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                        )
                    }

                }

                Text(
                    text = stringResource(id = incomeOrSpending),
                    fontFamily = inter,
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )


            }



            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth()
            ) {

                Text(
                    text = total,
                    fontFamily = inter,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,

                )


            }


        }
    }

}



@Preview
@Composable
fun IncomeCardPreview() {


}