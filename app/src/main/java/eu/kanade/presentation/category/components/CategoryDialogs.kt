package eu.kanade.presentation.category.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import eu.kanade.tachiyomi.R
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
fun CategoryCreateDialog(
    onDismissRequest: () -> Unit,
    onCreate: (String) -> Unit,
    // SY -->
    categories: List<String>,
    title: String,
    extraMessage: String? = null,
    alreadyExistsError: Int = R.string.error_category_exists,
    // SY <--
) {
    var name by remember { mutableStateOf("") }

    val focusRequester = remember { FocusRequester() }
    val nameAlreadyExists = remember(name) { categories.contains(name) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                enabled = name.isNotEmpty() && !nameAlreadyExists,
                onClick = {
                    onCreate(name)
                    onDismissRequest()
                },
            ) {
                Text(text = stringResource(R.string.action_add))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(R.string.action_cancel))
            }
        },
        title = {
            Text(text = title)
        },
        text = {
            // SY -->
            Column {
                if (extraMessage != null) {
                    Text(extraMessage)
                }
                // SY <--
                OutlinedTextField(
                    modifier = Modifier
                        .focusRequester(focusRequester),
                    value = name,
                    onValueChange = { name = it },
                    label = {
                        Text(text = stringResource(R.string.name))
                    },
                    supportingText = {
                        val msgRes = if (name.isNotEmpty() && nameAlreadyExists) alreadyExistsError else R.string.information_required_plain
                        Text(text = stringResource(msgRes))
                    },
                    isError = name.isNotEmpty() && nameAlreadyExists,
                    singleLine = true,
                )
                // SY -->
            }
            // SY <--
        },
    )

    LaunchedEffect(focusRequester) {
        // TODO: https://issuetracker.google.com/issues/204502668
        delay(0.1.seconds)
        focusRequester.requestFocus()
    }
}

@Composable
fun CategoryRenameDialog(
    onDismissRequest: () -> Unit,
    onRename: (String) -> Unit,
    // SY -->
    categories: List<String>,
    category: String,
    alreadyExistsError: Int = R.string.error_category_exists,
    // SY <--
) {
    var name by remember { mutableStateOf(category) }
    var valueHasChanged by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val nameAlreadyExists = remember(name) { categories.contains(name) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                enabled = valueHasChanged && !nameAlreadyExists,
                onClick = {
                    onRename(name)
                    onDismissRequest()
                },
            ) {
                Text(text = stringResource(android.R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(R.string.action_cancel))
            }
        },
        title = {
            Text(text = stringResource(R.string.action_rename_category))
        },
        text = {
            OutlinedTextField(
                modifier = Modifier.focusRequester(focusRequester),
                value = name,
                onValueChange = {
                    valueHasChanged = name != it
                    name = it
                },
                label = { Text(text = stringResource(R.string.name)) },
                supportingText = {
                    val msgRes = if (valueHasChanged && nameAlreadyExists) alreadyExistsError else R.string.information_required_plain
                    Text(text = stringResource(msgRes))
                },
                isError = valueHasChanged && nameAlreadyExists,
                singleLine = true,
            )
        },
    )

    LaunchedEffect(focusRequester) {
        // TODO: https://issuetracker.google.com/issues/204502668
        delay(0.1.seconds)
        focusRequester.requestFocus()
    }
}

@Composable
fun CategoryDeleteDialog(
    onDismissRequest: () -> Unit,
    onDelete: () -> Unit,
    // SY -->
    title: String,
    text: String,
    // SY <--
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = {
                onDelete()
                onDismissRequest()
            },) {
                Text(text = stringResource(android.R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(R.string.action_cancel))
            }
        },
        title = {
            Text(text = title)
        },
        text = {
            Text(text = text)
        },
    )
}
