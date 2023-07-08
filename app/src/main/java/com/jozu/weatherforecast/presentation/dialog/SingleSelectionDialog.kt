package com.jozu.weatherforecast.presentation.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

/**
 *
 * Created by jozuko on 2023/07/08.
 * Copyright (c) 2023 Studio Jozu. All rights reserved.
 */
@Composable
fun <T> SingleSelectionDialog(
    modifier: Modifier = Modifier,
    labels: List<String>,
    selection: List<T>,
    onConfirmRequest: (selected: T) -> Unit,
    onDismissRequest: () -> Unit,
    title: String? = null,
    message: String? = null,
    defaultSelectedIndex: Int = -1,
    confirmButtonLabel: String = stringResource(id = android.R.string.ok),
    dismissButtonLabel: String = stringResource(id = android.R.string.cancel),
) {
    var selectedIndex by remember { mutableStateOf(defaultSelectedIndex) }
    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Surface(
            modifier = modifier,
            shape = AlertDialogDefaults.shape,
            color = AlertDialogDefaults.containerColor,
            tonalElevation = AlertDialogDefaults.TonalElevation,
        ) {
            Column(
                modifier = Modifier
                    .sizeIn(minWidth = 280.dp, maxWidth = 560.dp)
                    .padding(8.dp),
            ) {
                title?.let {
                    Title(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.CenterHorizontally),
                        title = it,
                    )
                }
                message?.let {
                    Message(
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.Start),
                        message = it,
                    )
                }

                SelectionList(
                    modifier = Modifier.weight(weight = 1f, fill = false),
                    labels = labels,
                    selectedIndex = selectedIndex,
                    onSelect = {
                        selectedIndex = it
                    }
                )

                Buttons(
                    modifier = Modifier.align(Alignment.End),
                    selection = selection,
                    selectedIndex = selectedIndex,
                    dismissButtonLabel = dismissButtonLabel,
                    confirmButtonLabel = confirmButtonLabel,
                    onDismissRequest = onDismissRequest,
                    onConfirmRequest = onConfirmRequest,
                )
            }
        }
    }
}

@Composable
fun Title(modifier: Modifier, title: String) {
    Box(modifier = modifier) {
        Text(
            text = title,
            color = AlertDialogDefaults.titleContentColor,
            style = MaterialTheme.typography.headlineSmall,
        )
    }
}

@Composable
fun Message(modifier: Modifier, message: String) {
    Box(modifier = modifier) {
        Text(
            text = message,
            color = AlertDialogDefaults.textContentColor,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
fun <T> Buttons(
    modifier: Modifier,
    selection: List<T>,
    selectedIndex: Int,
    dismissButtonLabel: String,
    confirmButtonLabel: String,
    onDismissRequest: () -> Unit,
    onConfirmRequest: (T) -> Unit,
) {
    Box(modifier = modifier) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.End,
        ) {
            TextButton(
                onClick = {
                    onDismissRequest.invoke()
                },
            ) {
                Text(dismissButtonLabel)
            }

            TextButton(
                onClick = {
                    if (selectedIndex >= 0 && selectedIndex < selection.count()) {
                        onConfirmRequest.invoke(selection[selectedIndex])
                    }
                },
            ) {
                Text(confirmButtonLabel)
            }
        }
    }
}

@Composable
fun SelectionList(
    modifier: Modifier,
    labels: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
) {
    LazyColumn(modifier = modifier) {
        items(labels) { label ->
            val isSelected = (selectedIndex >= 0 && label == labels[selectedIndex])

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .selectable(
                        selected = isSelected,
                        onClick = {
                            onSelect.invoke(labels.indexOf(label))
                        }
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    selected = isSelected,
                    onClick = {
                        onSelect.invoke(labels.indexOf(label))
                    },
                )
                Text(
                    text = label,
                    color = AlertDialogDefaults.textContentColor,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}