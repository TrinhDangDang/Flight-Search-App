package com.example.flightsearchapp

import android.content.ClipData
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.flightsearchapp.ui.theme.FlightSearchAppTheme

@Composable
fun FlightSearchApp(){
    FlightSearchScreen()
}

@Composable
fun FlightSearchScreen(){
    Scaffold(
        topBar = {
            FlightSearchTopAppBar()
        }
    ){ innerPadding ->
        FlightSearchBar(
            onMicClicked = {},
            modifier = Modifier.padding(innerPadding)
        )
        Box(){
            FlightSearchAutoFill(airports = listOf(""))
            FlightSearchResult()
        }
    }
}


@Composable
fun FlightSearchBar(
    onMicClicked: () -> Unit,
    modifier: Modifier = Modifier
){
    var text by remember { mutableStateOf("hello") }
    TextField(
        value = text,
        onValueChange = { text = it},
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
fun FlightSearchAutoFill(airports: List<Airport>){
    LazyColumn() {
        items(airports) { airport ->
            FlightSearchItem(airport.identifier, airport.name, modifier = Modifier)
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
fun FlightSearchItem(identifier: String, airportName: String, modifier: Modifier = Modifier){
    Row (modifier = modifier){
        Text(text = identifier,
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

@Preview(showBackground = true)
@Composable
fun FlightSearchBarPreview(){
    FlightSearchAppTheme {
        FlightSearchBar(
            onMicClicked = {},
            modifier = Modifier.fillMaxWidth()
        )
    }

}

@Preview(showBackground = true)
@Composable
fun FlightSearchItemPreview(){
    FlightSearchAppTheme {
        FlightSearchItem("MUC", "Munich International Airport", modifier = Modifier.fillMaxWidth())
    }
}
