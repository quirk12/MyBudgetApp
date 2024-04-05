package com.example.mybudgetapp.ui.viewmodels

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.mybudgetapp.database.Item
import com.example.mybudgetapp.database.ItemRepository
import com.example.mybudgetapp.database.PurchaseDetails
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Month
import java.util.Date
import java.util.Locale


class AddingItemScreenViewModel (
    private val itemRepository: ItemRepository
): ViewModel() {

    var uiState by mutableStateOf(SpendingItemDetailsUiState())
    private set
    private var date: LocalDate? = null
    private var localIsUploadSuccessful = false



    init {
        date = LocalDate.now()
    }

    //updating the UiState
    fun updateUiState(itemDetails: ItemDetails) {
        uiState =
            SpendingItemDetailsUiState(itemDetails = itemDetails, isEntryValid = validateInput(itemDetails), isUploadSuccessful = localIsUploadSuccessful)
    }

    //when the user selects an image
    fun onImageSelected(context: Context, uri: Uri?) {
        val imagePath: String? = getImagePath(context, uri) { isSuccess ->
            localIsUploadSuccessful = isSuccess
        }

        updateUiState(uiState.itemDetails.copy(imagePath = imagePath))
        uiState = uiState.copy(isUploadSuccessful = true)
    }


    //saving the item to database
   suspend fun onSaveButtonClicked(context: Context) {

              try {

                  itemRepository.insertItem(uiState.itemDetails.toItem(""))
                  itemRepository.insertItem(uiState.itemDetails.toItem("random"))

              } catch (e: SQLiteConstraintException) {
                  val itemId = getElementId(uiState.itemDetails.name)

                  if(uiState.itemDetails.imagePath != null) {
                      itemRepository.updateItemImagePathWithId(
                          id = itemId,
                          imagePath = uiState.itemDetails.imagePath!!
                      )
                  }
                  itemRepository.updateItemDateWithId(date.toString(), itemId)
                  itemRepository.insetPurchaseDetails(uiState.itemDetails.toPurchaseDetails(
                      date = date.toString(),
                      year = date!!.year,
                      itemId = itemId,
                      month = date!!.monthValue
                  ))
              }

    }

    private suspend fun getElementId(name: String): Long {

        return itemRepository.getItemIdFromName(name)
    }



    //extracting the image path
    private fun getImagePath(context: Context, uri: Uri?, callback: (Boolean) -> Unit): String? {
        try {
            // Step 1: Obtain InputStream from URI
            val inputStream = uri?.let { context.contentResolver.openInputStream(it) }

            // Step 2: Create FileOutputStream in External Storage (App-specific directory)
            val externalStorageDirectory = context.getExternalFilesDir(null)
            val appSpecificDirectory = File(externalStorageDirectory, "YourAppImages")
            appSpecificDirectory.mkdirs() // Create the "YourAppImages" folder if it doesn't exist


            // Step 3: Generate a unique file name based on timestamp
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "image_$timeStamp.jpg"

            // Step 4: Create FileOutputStream for the new file
            val fileOutputStream = FileOutputStream(File(externalStorageDirectory, fileName))

            // Step 5: Copy Data from InputStream to FileOutputStream
            inputStream?.use { input ->
                fileOutputStream.use { output ->
                    input.copyTo(output)
                }
            }

            // Step 6: Close Streams
            inputStream?.close()
            fileOutputStream.close()

            //Step 8: update the upload success icon
            callback(true)

            // Step 7: Return the file path
            return File(externalStorageDirectory, fileName).absolutePath
        } catch (e: IOException) {
            // Handle exceptions, e.g., file not found, permissions, etc.
            e.printStackTrace()
            callback(false)

        }

        return null
    }

    //checking that the input fields are not blank
    private fun validateInput(itemDetails: ItemDetails = uiState.itemDetails): Boolean {
        return with(itemDetails) {
            name.isNotBlank() && cost.isNotBlank() && category.isNotBlank()
        }
    }


}

data class SpendingItemDetailsUiState(
    val itemDetails: ItemDetails = ItemDetails(),
    val isEntryValid: Boolean = false,
    val isUploadSuccessful: Boolean = false
)

data class ItemDetails(
    val id: Long = 0,
    val imagePath: String? = null,
    val name: String = "",
    val cost: String = "",
    val category: String = "",
)




fun ItemDetails.toItem(date: String): Item = Item(
    itemId = id,
    name = name,
    category = category,
    picturePath = imagePath,
    date = date
)

fun ItemDetails.toPurchaseDetails(month: Int, year:Int, date: String, itemId: Long) = PurchaseDetails (
    itemId = itemId,
    cost = cost.toDouble(),
    month = month,
    year = year,
    purchaseDate = date
)