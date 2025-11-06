package com.bounce.keep.presentation.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bounce.keep.data.entity.NotepadEntity
import com.bounce.keep.domain.usecase.NotepadUseCase
import com.bounce.keep.presentation.utils.UiState
import com.bounce.keep.presentation.utils.getCurrentDate
import com.bounce.keep.presentation.utils.getRandomColor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EditorViewModel(private val notepadUseCase: NotepadUseCase) : ViewModel() {

    private val _notepad = MutableStateFlow<UiState<NotepadEntity>>(UiState.Loading)
    val notepad = _notepad.asStateFlow()

    fun insertNote(title: String, notes: String) {
        val notepadEntity = NotepadEntity(
            title = title,
            notes = notes,
            date = getCurrentDate(),
            color = getRandomColor()
        )

        viewModelScope.launch {
            notepadUseCase.insertNote(notepadEntity)
        }
    }

    fun updateNote(id: Int, title: String, notes: String, color: Long) {
        viewModelScope.launch {
            val notepadEntity = NotepadEntity(
                id = id,
                title = title,
                notes = notes,
                date = getCurrentDate(),
                color = color
            )
            notepadUseCase.updateNote(notepadEntity)
        }
    }

    fun getNotepadById(id: Int) {
        viewModelScope.launch {
            val note = notepadUseCase.getNoteById(id)

            try {
                if (note != null) _notepad.emit(UiState.Success(note))
                else _notepad.emit(UiState.Empty)
            } catch (exception: Exception) {
                _notepad.emit(UiState.Error(exception.message ?: ""))
            }
        }
    }

    fun resetState() {
        viewModelScope.launch {
            _notepad.emit(
                UiState.Success(
                    NotepadEntity(
                        title = "",
                        notes = "",
                        date = "",
                        color = 0
                    )
                )
            )
        }
    }

}
