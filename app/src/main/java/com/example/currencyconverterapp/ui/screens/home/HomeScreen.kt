package com.example.currencyconverterapp.ui.screens.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.currencyconverterapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier,
    uiState: HomeState,
    onAction: (HomeAction) -> Unit
) {
    BackHandler { onAction(HomeAction.CloseScreen) }

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {},
                navigationIcon = {
                    Box(modifier = Modifier.fillMaxSize()) {
                        IconButton(
                            modifier = Modifier.align(Alignment.CenterEnd),
                            onClick = { onAction(HomeAction.CloseScreen) }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = null,
                            )
                        }
                    }
                },
            )
        },
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            when (uiState.workState) {
                is WorkState.Error -> {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = uiState.workState.message,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }

                is WorkState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }

                is WorkState.Success -> {
                    Box {
                        Column(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(horizontal = 10.dp)
                                .clip(RoundedCornerShape(5))
                                .background(MaterialTheme.colorScheme.secondaryContainer)
                                .padding(horizontal = 10.dp),
                        ) {
                            Spacer(Modifier.height(40.dp))

                            DropDownMenuAndTextFieldContent(
                                acronyms = uiState.acronyms,
                                convertFrom = uiState.convertFrom,
                                amountToConvert = uiState.amountToConvert,
                                menuClick = { onAction(HomeAction.SetConvertFrom(it)) },
                                textFieldOnValueChanged = {onAction(HomeAction.SetValueInput(it))}
                            )

                            Spacer(Modifier.height(10.dp))

                            DropDownMenuAndTextFieldContent(
                                acronyms = uiState.acronyms,
                                convertFrom = uiState.convertTo,
                                amountToConvert = uiState.calculatedValue.toString(),
                                menuClick = { onAction(HomeAction.SetConvertTo(it)) },
                            )

                            Spacer(Modifier.height(50.dp))
                        }

                        Button(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .height(50.dp),
                            enabled = uiState.amountToConvert.isNotBlank(),
                            onClick = { onAction(HomeAction.SetValueToCovert) }
                        ) {
                            Text(
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                text = stringResource(R.string.convert),
                                fontSize = 20.sp,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ColumnScope.DropDownMenuAndTextFieldContent(
    acronyms: List<String>,
    convertFrom: String,
    amountToConvert: String,
    menuClick: (String) -> Unit,
    textFieldOnValueChanged: (String) -> Unit = {}
) = Row(
    modifier = Modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
) {

    DropDownMenu(
        acronyms = acronyms,
        displayValue = convertFrom,
        onClick = { acronym ->
            menuClick(acronym)
        }
    )

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = amountToConvert,
        onValueChange = { textFieldOnValueChanged(it) },
        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
    )
}

@Composable
private fun RowScope.DropDownMenu(
    acronyms: List<String>,
    displayValue: String,
    defaultItemPosition: Int = 0,
    onClick: (String) -> Unit,
) {
    val isDropDownExpanded = remember { mutableStateOf(false) }
    val itemPosition = remember { mutableIntStateOf(defaultItemPosition) }
    val columnHeight = LocalConfiguration.current.screenHeightDp.dp / 3

    Box(modifier = Modifier.padding(end = 20.dp)) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable {
                isDropDownExpanded.value = true
            }
        ) {
            Text(
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                text = displayValue
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
            )
        }
        DropdownMenu(
            modifier = Modifier.height(columnHeight),
            expanded = isDropDownExpanded.value,
            onDismissRequest = { isDropDownExpanded.value = false }
        ) {
            acronyms.forEachIndexed { index, currency ->
                DropdownMenuItem(
                    text = {
                        Text(
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            text = currency,
                        )
                    },
                    onClick = {
                        isDropDownExpanded.value = false
                        itemPosition.intValue = index
                        onClick(acronyms[itemPosition.intValue])
                    }
                )
            }
        }
    }
}