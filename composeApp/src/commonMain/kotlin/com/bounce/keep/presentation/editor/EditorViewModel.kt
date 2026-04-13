package com.bounce.keep.presentation.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bounce.keep.data.entity.NotepadEntity
import com.bounce.keep.domain.usecase.GetNoteByIdUseCase
import com.bounce.keep.domain.usecase.UpsertNoteUseCase
import com.bounce.keep.presentation.utils.UiState
import com.bounce.keep.presentation.utils.getCurrentDate
import com.bounce.keep.presentation.utils.getRandomColor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EditorViewModel(
    private val upsertNoteUseCase: UpsertNoteUseCase,
    private val getNoteByIdUseCase: GetNoteByIdUseCase
) : ViewModel() {

    private val _noteState = MutableStateFlow<UiState<NotepadEntity>>(UiState.Loading)
    val noteState = _noteState.asStateFlow()

    fun getNoteById(id: Int) {
        if (id == 0) {
            _noteState.value = UiState.Success(
                NotepadEntity(
                    title = "",
                    notes = "",
                    date = getCurrentDate(),
                    color = getRandomColor()
                )
            )
            return
        }

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

    fun upsertNote(id: Int, title: String, notes: String, color: Long) {
        viewModelScope.launch {
            val note = NotepadEntity(
                id = id,
                title = title,
                notes = notes,
                date = getCurrentDate(),
                color = color
            )
            upsertNoteUseCase(note)
        }
    }
}
