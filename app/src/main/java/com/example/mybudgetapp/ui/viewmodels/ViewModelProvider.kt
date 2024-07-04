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
                budgetApplication().container.itemRepository,
                this.createSavedStateHandle()
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
        initializer {
            TotalSpendingScreenViewModel(
                budgetApplication().container.itemRepository,
                this.createSavedStateHandle()
            )
        }
        initializer {
            ItemDatesViewModel(
                budgetApplication().container.itemRepository,
                this.createSavedStateHandle()
            )
        }
        initializer {
            ThisYearScreenViewModel(
                budgetApplication().container.itemRepository,
            )
        }
        initializer {
            SpendingOnCategoryForYearScreenViewModel(
                budgetApplication().container.itemRepository,
                this.createSavedStateHandle()
            )
        }
        initializer {
            TotalSpendingScreenForYearViewModel(
                budgetApplication().container.itemRepository,
                this.createSavedStateHandle()
            )
        }


    }
}

fun CreationExtras.budgetApplication(): BudgetApp =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BudgetApp)