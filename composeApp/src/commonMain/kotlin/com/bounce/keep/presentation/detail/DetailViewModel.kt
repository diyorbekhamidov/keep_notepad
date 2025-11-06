package com.bounce.keep.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bounce.keep.data.entity.NotepadEntity
import com.bounce.keep.domain.usecase.NotepadUseCase
import com.bounce.keep.presentation.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(private val notepadUseCase: NotepadUseCase) : ViewModel() {

    private val _note = MutableStateFlow<UiState<NotepadEntity>>(UiState.Loading)
    val note = _note.asStateFlow()

    fun getNoteById(id: Int) {
        viewModelScope.launch {
            try {
                val note = notepadUseCase.getNoteById(id)
                if (note != null) _note.emit(UiState.Success(note))
                else _note.emit(UiState.Empty)

            } catch (exception: Exception) {
                _note.emit(UiState.Error(exception.message ?: ""))
            }
        }
    }

    fun deleteNoteById(id: Int) {
        viewModelScope.launch {
            notepadUseCase.deleteNote(id)
        }
    }

}