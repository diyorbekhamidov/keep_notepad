package com.bounce.keep.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bounce.keep.data.entity.NotepadEntity
import com.bounce.keep.domain.usecase.DeleteNoteUseCase
import com.bounce.keep.domain.usecase.GetNoteByIdUseCase
import com.bounce.keep.presentation.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val getNoteByIdUseCase: GetNoteByIdUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase
) : ViewModel() {

    private val _noteState = MutableStateFlow<UiState<NotepadEntity>>(UiState.Loading)
    val noteState = _noteState.asStateFlow()

    fun getNoteById(id: Int) {
        viewModelScope.launch {
            _noteState.value = UiState.Loading
            try {
                val note = getNoteByIdUseCase(id)
                if (note != null) {
                    _noteState.value = UiState.Success(note)
                } else {
                    _noteState.value = UiState.Empty
                }
            } catch (e: Exception) {
                _noteState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun deleteNoteById(id: Int) {
        viewModelScope.launch {
            deleteNoteUseCase(id)
        }
    }
}
