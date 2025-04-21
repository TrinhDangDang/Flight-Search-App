package com.example.flightsearchapp

import android.content.ClipData
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.flightsearchapp.data.Airport

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
        Column(modifier = Modifier.padding(innerPadding)) {
            FlightSearchBar(
                onMicClicked = {},
                updateKeyword = { keyword -> viewModel.setSearchKeyword(keyword) },
                uiState = uiState,
                modifier = Modifier.fillMaxWidth()
            )
            Box(modifier = Modifier.weight(1f)) {
                FlightSearchAutoFill(uiState = uiState, modifier = Modifier.fillMaxWidth())
                FlightSearchResult()
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
        modifier = modifier
    )
}

@Composable
fun FlightSearchAutoFill(uiState: UiState, modifier: Modifier = Modifier){
    if (uiState.airportsMatch.isEmpty()) {
        Text("No matches found", modifier = modifier.padding(8.dp))
    } else {
        LazyColumn(modifier = modifier.fillMaxWidth()) {
            items(uiState.airportsMatch) { airport ->
                FlightSearchItem(airport.iata_code, airport.name)
            }
        }
    }
}

@Composable
fun FlightSearchResult(){
    LazyColumn() {  }
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
fun FlightSearchItem(iataCode: String, airportName: String, modifier: Modifier = Modifier){
    Row (modifier = modifier){
        Text(text = iataCode,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )
        Text(text = airportName, modifier = Modifier.weight(7f))
    }
}

@Preview(showBackground = true)
@Composable
fun FlightSearchTopAppBarPreview(){
    FlightSearchAppTheme {
        FlightSearchTopAppBar()
    }
}
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

@Preview(showBackground = true)
@Composable
fun FlightSearchItemPreview(){
    FlightSearchAppTheme {
        FlightSearchItem("MUC", "Munich International Airport", modifier = Modifier.fillMaxWidth())
    }
}


