package com.bounce.keep.domain.usecase

import com.bounce.keep.data.entity.NotepadEntity
import com.bounce.keep.domain.repository.NotepadRepository
import kotlinx.coroutines.flow.Flow

class SearchNotesUseCase(private val repository: NotepadRepository) {
    suspend operator fun invoke(query: String): Flow<List<NotepadEntity>> = repository.getNoteByStr(query)
}
