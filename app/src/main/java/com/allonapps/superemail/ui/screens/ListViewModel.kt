package com.allonapps.superemail.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.allonapps.superemail.model.Email
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ListViewModel : ViewModel() {

    sealed class ListIntent {
        data class EmailTap(val email: Email) : ListIntent()
        data class AccountTap(val accountId: String): ListIntent()
    }

    sealed class ListUiState {
        data class LoadingUiState(val loading: Boolean) : ListUiState()
        data class DataUiState(val emails: List<Email>) : ListUiState()
        data class ErrorUiState(val message: String) : ListUiState()
    }

    val uiState = createUiStateFlow()

    fun acceptIntent(intent: ListIntent) {

    }

    private fun createUiStateFlow(): StateFlow<ListUiState> {
        return flow<ListUiState> {
            val emailList = List(20) {
                Email(
                    sender = "Mike", subject = "heres something cool",
                    message = "no it really is cool you should actually check it out",
                    timeDisplay = "8:20AM"
                )
            }
            emit(
                ListUiState.DataUiState(emails = emailList)
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = ListUiState.LoadingUiState(true)
        )
    }

    private fun refresh() {
        viewModelScope.launch {
            uiState.collect()
        }
    }
}