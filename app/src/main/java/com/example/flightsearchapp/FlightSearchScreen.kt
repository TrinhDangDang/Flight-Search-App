package com.example.flightsearchapp

import android.content.ClipData
import android.graphics.Paint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flightsearchapp.ui.theme.FlightSearchAppTheme
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.room.util.TableInfo
import com.example.flightsearchapp.data.Airport
import com.example.flightsearchapp.data.Favorite



//@Composable
//fun FlightSearchApp(flightSearchViewModel: FlightSearchViewModel = viewModel(factory = FlightSearchViewModel.Factory)){
//    Card {
//        IconButton(onClick = {flightSearchViewModel.insertTest(AirportFlight(depart = Airport(name = "houston international", iata_code = "HOU", passengers = 1000), destination = Airport(name = "los angeles inernational airport", iata_code = "LAX", passengers = 2000)))}) {
//            Icon(Icons.Default.Star, contentDescription = "just a button")
//        }
//        IconButton(onClick = {flightSearchViewModel.insertTest(AirportFlight(depart = Airport(name = "hawaii international", iata_code = "HAU", passengers = 1000), destination = Airport(name = "houston inernational airport", iata_code = "HOU", passengers = 2000)))}) {
//            Icon(Icons.Default.Star, contentDescription = "just a button")
//        }
//
//    }
//}
@Composable
fun FlightSearchApp(flightSearchViewModel: FlightSearchViewModel = viewModel(factory = FlightSearchViewModel.Factory)){
    FlightSearchScreen(
        uiState = flightSearchViewModel.uiState.collectAsState().value,
        viewModel = flightSearchViewModel
    )
}
@Composable
fun FlightSearchScreen(viewModel: FlightSearchViewModel, uiState: UiState){
    Scaffold(
        topBar = {
            FlightSearchTopAppBar()
        }
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding))
        {
            FlightSearchBar(
                onMicClicked = {},
                updateKeyword = { keyword -> viewModel.setSearchKeyword(keyword) },
                uiState = uiState,
                modifier = Modifier.fillMaxWidth()
                    .padding(16.dp)
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                if (uiState.keyword.isEmpty()) {
                    FavoriteFlights(uiState.listOfFavoriteFlights, onClicked = {
                            flight ->
                        if (!flight.isFavorite) {
                            viewModel.insertFavoriteFlight(
                                Favorite(
                                    departure_code = flight.depart.iata_code,
                                    destination_code = flight.destination.iata_code
                                )
                            )
                        } else {
                            viewModel.deleteFavoriteFlight(
                                Favorite(
                                    departure_code = flight.depart.iata_code,
                                    destination_code = flight.destination.iata_code
                                )
                            )
                        }
                    })
                }
                else{
                    if (uiState.currentAirport == null) {
                        FlightSearchAutoFill(onClicked = { airport ->
                            viewModel.setCurrentAirport(
                                airport
                            )
                        }, uiState = uiState, modifier = Modifier.fillMaxWidth())
                    } else {
                        //viewModel.deleteFavoriteFlight(Favorite(departure_code = flight.depart.iata_code, destination_code = flight.destination.iata_code)) else
//                    FlightSearchResult(uiState.airportsResult, starClick = { flight -> if (!flight.isFavorite)  viewModel.insertFavoriteFlight(Favorite(departure_code = flight.depart.iata_code, destination_code = flight.destination.iata_code)) }
//                    )
                        FlightSearchResult(
                            flights = uiState.airportsResult,
                            starClick = { flight ->
                                if (!flight.isFavorite) {
                                    viewModel.insertFavoriteFlight(
                                        Favorite(
                                            departure_code = flight.depart.iata_code,
                                            destination_code = flight.destination.iata_code
                                        )
                                    )
                                } else {
                                    viewModel.deleteFavoriteFlight(
                                        Favorite(
                                            departure_code = flight.depart.iata_code,
                                            destination_code = flight.destination.iata_code
                                        )
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun FlightSearchBar(
    onMicClicked: () -> Unit,
    updateKeyword: (String) -> Unit,
    uiState: UiState,
    modifier: Modifier = Modifier
){
    TextField(
        value = uiState.keyword,
        onValueChange = { updateKeyword(it)},
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = "Search Icon")
        },
        trailingIcon = {
            IconButton(onClick = onMicClicked) {
                Icon(Icons.Default.Mic, contentDescription = "Mic Icon")
            }
        },
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        modifier = modifier.clip(RoundedCornerShape(50.dp))
    )
}

@Composable
fun FlightSearchAutoFill(onClicked: (Airport) -> Unit, uiState: UiState, modifier: Modifier = Modifier){
    if (uiState.airportsMatch.isEmpty()) {
        Text("No matches found", modifier = modifier.padding(8.dp))
    } else {
        LazyColumn(modifier = modifier.fillMaxWidth()) {
            items(uiState.airportsMatch) { airport ->
                FlightSearchItem(airport = airport, onClicked = onClicked)
            }
        }
    }
}

@Composable
fun FlightSearchResult(flights: List<AirportFlight>, starClick: (AirportFlight) -> Unit, modifier: Modifier = Modifier){
    LazyColumn( verticalArrangement = Arrangement.spacedBy(8.dp),modifier = modifier){
        items(flights){
            flight -> ResultItem(flight, onClicked = starClick)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightSearchTopAppBar(
    modifier: Modifier = Modifier
){
    TopAppBar(
        title = {Text("Flight Search")},
        modifier = modifier
    )
}

@Composable
fun FlightSearchItem(airport: Airport, onClicked: (Airport) -> Unit, modifier: Modifier = Modifier){
    Row (
        modifier = modifier
            .clickable{onClicked(airport)}
    ){
        Text(text = airport.iata_code,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )
        Text(text = airport.name, modifier = Modifier.weight(7f))
    }
}

@Composable
fun ResultItem(flight: AirportFlight, onClicked: (AirportFlight) -> Unit, modifier: Modifier = Modifier){
    Card(modifier = modifier
        .clip(RoundedCornerShape(bottomStart = 16.dp, topEnd = 16.dp))){
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .weight(0.85f)
                    .padding(8.dp)
            ) {
                Text(text = "DEPART", fontWeight = FontWeight.Normal)
                Row {
                    Text(text = flight.depart.iata_code, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = flight.depart.name)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "ARRIVE", fontWeight = FontWeight.Normal)
                Row {
                    Text(text = flight.destination.iata_code, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = flight.destination.name)
                }
            }
            Column(
                modifier = Modifier
                    .weight(0.15f)
                    .align(Alignment.CenterVertically

                    )){
                IconButton(onClick = {
                    onClicked(flight)
                }) {
                    Icon(
                        imageVector =
                            if(flight.isFavorite) {
                                Icons.Default.Star
                            } else {
                                Icons.Default.StarBorder
                                   },
                        contentDescription = "Favorite Flight"
                    )
                }
            }
        }
    }

}

@Composable
fun FavoriteFlights(listOfFavoriteFlights: List<AirportFlight>, onClicked: (AirportFlight) -> Unit, modifier: Modifier = Modifier){
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp),modifier = modifier) {
        items(listOfFavoriteFlights){
            item -> ResultItem(item, onClicked = onClicked)
        }
    }
}





//@Preview(showBackground = true)
//@Composable
//fun ResultItemPreview() {
//    val sampleFlight = AirportFlight(
//        depart = Airport(
//            iata_code = "IAH",
//            name = "George Bush Intercontinental Airport",
//            passengers = 20,
//        ),
//        destination = Airport(
//            iata_code = "LAX",
//            name = "Los Angeles International Airport",
//            passengers = 50
//        )
//    )
//
//    ResultItem(flight = sampleFlight, onClicked = {})
//}
//
//@Preview(showBackground = true)
//@Composable
//fun FlightSearchTopAppBarPreview(){
//    FlightSearchAppTheme {
//        FlightSearchTopAppBar()
//    }
//}
//

//
//@Preview(showBackground = true)
//@Composable
//fun FlightSearchBarPreview(){
//    FlightSearchAppTheme {
//        FlightSearchBar(
//            onMicClicked = {},
//            modifier = Modifier.fillMaxWidth(),
//            uiState =
//        )
//    }
//
//}

//@Preview(showBackground = true)
//@Composable
//fun FlightSearchItemPreview(){
//    FlightSearchAppTheme {
//        FlightSearchItem("MUC", "Munich International Airport", modifier = Modifier.fillMaxWidth())
//    }
//}


