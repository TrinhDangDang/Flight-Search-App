package com.example.flightsearchapp

import android.app.Application
import android.text.Editable
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.input.key.Key
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.flightsearchapp.data.Airport
import com.example.flightsearchapp.data.Favorite
import com.example.flightsearchapp.data.FlightSearchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FlightSearchViewModel(private val flightSearchRepository: FlightSearchRepository): ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    val allAirportsDataStream: StateFlow<List<Airport>> = flightSearchRepository.getAllAirportsStream().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = emptyList()
    )
    init {
        // Log the data when it updates
        viewModelScope.launch {
            allAirportsDataStream.collect { airports ->
                Log.d("FlightSearchViewModel", "All Airports stream: $airports")
            }
        }
    }

    val favoriteAirportsStream: StateFlow<List<Favorite>> = flightSearchRepository.getFavoriteFlightsStream().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed((TIMEOUT_MILLIS)),
        initialValue = emptyList()
    )



    fun setSearchKeyword(keyword: String){
        val filtered = if (keyword.isBlank()) {
            emptyList()
        } else {
            allAirportsDataStream.value.filter {
                it.name.contains(keyword, ignoreCase = true) ||
                        it.iata_code.contains(keyword, ignoreCase = true)
            }
        }
        _uiState.update { currentState ->
            currentState.copy(keyword = keyword, airportsMatch = filtered)
        }
    }


    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as FlightSearchApplication)
                FlightSearchViewModel(application.container.flightSearchRepository)

            }
        }
    }
}

data class UiState (
    val keyword: String = "",
    val airportsMatch: List<Airport> = emptyList(),
)