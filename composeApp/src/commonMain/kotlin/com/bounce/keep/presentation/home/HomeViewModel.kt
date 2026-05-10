package com.bounce.keep.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bounce.keep.data.entity.NotepadEntity
import com.bounce.keep.domain.usecase.GetAllNotesUseCase
import com.bounce.keep.domain.usecase.SearchNotesUseCase
import com.bounce.keep.presentation.utils.UiState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class HomeViewModel(
    private val getAllNotesUseCase: GetAllNotesUseCase,
    private val searchNotesUseCase: SearchNotesUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _uiState = MutableStateFlow<UiState<List<NotepadEntity>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<NotepadEntity>>> = _uiState.asStateFlow()

    init {
        observeNotes()
    }

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    private fun observeNotes() {
        viewModelScope.launch {
            _searchQuery
                .debounce(300L)
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    if (query.isBlank()) {
                        getAllNotesUseCase()
                    } else {
                        searchNotesUseCase(query)
                    }
                }
                .catch { e ->
                    _uiState.value = UiState.Error(e.message ?: "Unknown error")
                }
                .collectLatest { notes ->
                    _uiState.value = if (notes.isEmpty()) {
                        if (_searchQuery.value.isBlank()) UiState.Empty else UiState.Empty // Maybe distinct Empty states for search vs general
                    } else {
                        UiState.Success(notes)
                    }
                }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }
}