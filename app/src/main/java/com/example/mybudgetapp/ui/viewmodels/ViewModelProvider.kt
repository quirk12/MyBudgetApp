package com.example.mybudgetapp.ui.viewmodels

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mybudgetapp.BudgetApp

object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for ItemEditViewModel
        initializer {
            AddingItemScreenViewModel(
                budgetApplication().container.itemRepository
            )
        }

        initializer {
            SpendingOnCategoryScreenViewModel(
                budgetApplication().container.itemRepository,
                this.createSavedStateHandle()
            )
        }

        initializer {
            ThisMonthScreenViewModel(
                budgetApplication().container.itemRepository
            )
        }


    }
}

fun CreationExtras.budgetApplication(): BudgetApp =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BudgetApp)