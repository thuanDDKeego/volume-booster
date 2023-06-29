package dev.keego.volume.booster.ui.component

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun _text_field(
    modifier: Modifier = Modifier,
    onTextFieldFocused: () -> Unit = {},
    onTextChanged: (String) -> Unit = {},
    isError: Boolean = false,
    text: String = "",
    cursorColor: Color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    hint: String = "",
    strokeColor: Color = MaterialTheme.colorScheme.outline,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    shape: Shape = CircleShape,
    isEnabled: Boolean,
    maxLines: Int = 1,
    onFocusChanged: (Boolean) -> Unit = {}

) {
    var isFocus by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = text,
        textStyle = textStyle,
        singleLine = maxLines == 1,
        enabled = isEnabled,
        onValueChange = onTextChanged,
        placeholder = {
            if (!isFocus) {
                Text(
                    text = hint,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onBackground.copy(
                            0.8f
                        )
                    )
                )
            }
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
//            cursorColor = cursorColor,
            containerColor = backgroundColor,
            focusedBorderColor = strokeColor,
            unfocusedBorderColor = strokeColor,
            textColor = MaterialTheme.colorScheme.onBackground
        ),
        shape = shape,
        modifier = modifier
//            .border(
//                width = 3.dp,
//                shape = CircleShape,
//                color = strokeColor
//            )
            .onFocusChanged {
                onFocusChanged(it.isFocused)
                isFocus = it.isFocused
                if (it.isFocused) {
                    onTextFieldFocused()
                } else {
                    // hide keyboard
                }
            },
        maxLines = maxLines
    )
}
