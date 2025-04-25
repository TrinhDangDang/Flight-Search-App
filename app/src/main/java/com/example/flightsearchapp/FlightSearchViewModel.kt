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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.flightsearchapp.data.Airport
import com.example.flightsearchapp.data.Favorite
import com.example.flightsearchapp.data.FlightSearchRepository
import com.example.flightsearchapp.data.KeywordRepository
import kotlinx.coroutines.coroutineScope
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

class FlightSearchViewModel(private val flightSearchRepository: FlightSearchRepository, private val keywordRepository: KeywordRepository): ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

//
//    fun insertTest(flight: AirportFlight){
//        viewModelScope.launch {
//            flightSearchRepository.insertFavorite(Favorite(departure_code = flight.depart.iata_code, destination_code = flight.destination.iata_code))
//        }
//
//    }



    val allAirportsDataStream: StateFlow<List<Airport>> = flightSearchRepository.getAllAirportsStream().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = emptyList()
    )

    val favoriteFlightsDataStream: StateFlow<List<Favorite>> = flightSearchRepository.getFavoriteFlightsStream().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = emptyList()
    )

    init {
        viewModelScope.launch {
            combine(
                keywordRepository.currentSearchKey,
                flightSearchRepository.getAllAirportsStream(),
                flightSearchRepository.getFavoriteFlightsStream()
            ) { keyword, airports , favorites->
                val currentAirport: Airport? = _uiState.value.currentAirport
                val filtered = if (keyword.isBlank()) {
                    emptyList()
                } else {
                    airports.filter {
                        it.name.contains(keyword, ignoreCase = true) ||
                                it.iata_code.contains(keyword, ignoreCase = true)
                    }
                }
                var result: List<AirportFlight> = emptyList()
                if (currentAirport != null){
                    result = airports.map { airport ->
                        val isFavorite = favorites.any{it.departure_code == currentAirport.iata_code && it.destination_code == airport.iata_code}
                        AirportFlight(depart = currentAirport, destination = airport, isFavorite = isFavorite) }
                }
                UiState(keyword = keyword, airportsMatch = filtered, airportsResult = result, currentAirport = currentAirport)
            }.collect { uiStateValue ->
                _uiState.value = uiStateValue
            }
        }
        // Log the data when it updates
        viewModelScope.launch {
            allAirportsDataStream.collect { airports ->
                Log.d("FlightSearchViewModel", "All Airports stream: $airports")
            }
        }
    }


    fun setCurrentAirport(airport: Airport){
        val favoriteFlights: List<Favorite> = favoriteFlightsDataStream.value
        val airportsResult: List<AirportFlight> = allAirportsDataStream.value.map { destination ->
            val isFavorite = favoriteFlights.any{it.departure_code == airport.iata_code && it.destination_code == destination.iata_code}
            AirportFlight(depart = airport, destination = destination, isFavorite = isFavorite) }

        _uiState.update { currentState ->
            currentState.copy(
                keyword = airport.iata_code,
                currentAirport = airport,
                airportsResult = airportsResult
            )
        }
        viewModelScope.launch {
            keywordRepository.saveCurrentSearchKey(airport.iata_code)
        }
    }


    fun setSearchKeyword(keyword: String){
//        val filtered = if (keyword.isBlank()) {
//            emptyList()
//        } else {
//            allAirportsDataStream.value.filter {
//                it.name.contains(keyword, ignoreCase = true) ||
//                        it.iata_code.contains(keyword, ignoreCase = true)
//            }
//        }
        _uiState.update { currentState ->
            currentState.copy(keyword = keyword, currentAirport = null)
        }
        viewModelScope.launch {
            keywordRepository.saveCurrentSearchKey(keyword)
        }

    }

    fun insertFavoriteFlight(favorite: Favorite){
        val updatedUiAirportsResult = uiState.value.airportsResult.map {
            airportFlight ->
                if (airportFlight.destination.iata_code == favorite.destination_code) {
                    AirportFlight(depart = airportFlight.depart, destination = airportFlight.destination, isFavorite = true)
                } else {
                    airportFlight
                }
        }
        _uiState.update { currentState ->
            currentState.copy(airportsResult = updatedUiAirportsResult)
        }
        viewModelScope.launch {
            flightSearchRepository.insertFavorite(favorite)
        }

    }

    fun deleteFavoriteFlight(favorite: Favorite){

        viewModelScope.launch {

        }
    }


    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as FlightSearchApplication)
                FlightSearchViewModel(application.container.flightSearchRepository, application.container.keywordRepository)

            }
        }
    }
}

data class UiState (
    val keyword: String = "",
    val airportsMatch: List<Airport> = emptyList(),
    val airportsResult: List<AirportFlight> = emptyList(),
    val currentAirport: Airport? = null
)

data class AirportFlight(
    val depart: Airport,
    val destination: Airport,
    val isFavorite: Boolean = false
)