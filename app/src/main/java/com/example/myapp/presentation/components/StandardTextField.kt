package com.example.myapp.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun StandardTextField(
    text: String = "",
    hint: String = "",
    onValueChange: (String) -> Unit,
    error: String? = null,
    singleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    leadingIcon: ImageVector? = null
){
    val isPasswordToggleDisplayed by remember {
        mutableStateOf(keyboardType == KeyboardType.Password)
    }
    var isPasswordVisible by remember {
        mutableStateOf(false)
    }
    TextField(
        value = text,
        onValueChange = onValueChange,
        textStyle = androidx.compose.ui.text.TextStyle(MaterialTheme.colors.onBackground),
        placeholder = {
            Text(text = hint,
                style = MaterialTheme.typography.body1
            )
        },
        singleLine = singleLine,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        visualTransformation = if(isPasswordToggleDisplayed && isPasswordVisible){
                                                                PasswordVisualTransformation()
                                                            } else {
                                                                   VisualTransformation.None
                                                                   },
        leadingIcon = if(leadingIcon != null) {
            {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colors.onBackground,
                    modifier = Modifier.size(25.dp)
                )
            }
        }else null,
        trailingIcon = {
            if(isPasswordToggleDisplayed) {
                IconButton(onClick = {
                    isPasswordVisible = !isPasswordVisible
                }) {
                    Icon(
                        imageVector = if (isPasswordVisible) {
                            Icons.Filled.VisibilityOff
                        } else {
                            Icons.Filled.Visibility
                        },
                        tint = MaterialTheme.colors.onBackground,
                    contentDescription = ""
                    )
                }
            }
        },
        isError = error != null,
        modifier = Modifier
            .fillMaxWidth(),
    )
    if (error != null) {
        Text(
            text = error,
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.error,
            textAlign = TextAlign.End,
            modifier = Modifier
                .fillMaxWidth()
        )
    }

}
