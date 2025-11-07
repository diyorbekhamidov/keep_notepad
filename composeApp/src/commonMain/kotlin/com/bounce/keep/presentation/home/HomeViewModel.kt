package com.bounce.keep.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bounce.keep.data.entity.NotepadEntity
import com.bounce.keep.domain.usecase.NotepadUseCase
import com.bounce.keep.presentation.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

class HomeViewModel(private val notepadUseCase: NotepadUseCase) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<NotepadEntity>>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        getAllNotes()
    }

    fun getAllNotes() {
        viewModelScope.launch {
            try {
                notepadUseCase.getAllNotes().collect { result ->
                    if (result.isNotEmpty()) _uiState.emit(UiState.Success(result))
                    else _uiState.emit(UiState.Empty)
                }
            } catch (e: Exception) {
                _uiState.emit(UiState.Error(e.message ?: ""))
            }
        }
    }

    fun getNoteByStr(str: String) {
        viewModelScope.launch {
            _uiState.emit(UiState.Loading)
            try {
                notepadUseCase.getNoteByStr(str)
                    .debounce(1000)
                    .collect { result ->
                        if (result.isNotEmpty()) _uiState.emit(UiState.Success(result))
                        else _uiState.emit(UiState.Empty)
                    }
            } catch (e: Exception) {
                _uiState.emit(UiState.Error(e.message ?: ""))
            }
        }
    }
}