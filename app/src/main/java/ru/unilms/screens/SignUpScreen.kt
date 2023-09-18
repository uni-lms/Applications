package ru.unilms.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ch.benlu.composeform.formatters.dateShort
import ru.unilms.R
import ru.unilms.components.form.ImagePickerField
import ru.unilms.components.form.M3DateField
import ru.unilms.components.form.M3PickerField
import ru.unilms.components.form.M3TextField
import ru.unilms.viewmodels.SignUpViewModel

@Composable
fun SignUpScreen(goToFeedScreen: () -> Unit) {

    val viewModel = hiltViewModel<SignUpViewModel>()

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            ImagePickerField(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                lastSelectedImage = viewModel.pickedImage.value,
                onSelection = {
                    viewModel.pickedImage.value = it
                }
            )
            M3TextField(
                label = stringResource(id = R.string.field_last_name),
                form = viewModel.form,
                fieldState = viewModel.form.lastName,
                keyboardType = KeyboardType.Text,
                keyboardCapitalization = KeyboardCapitalization.Sentences,
                modifier = Modifier.background(MaterialTheme.colorScheme.background)
            ).Field()
            M3TextField(
                label = stringResource(id = R.string.field_first_name),
                form = viewModel.form,
                fieldState = viewModel.form.firstName,
                keyboardType = KeyboardType.Text,
                keyboardCapitalization = KeyboardCapitalization.Sentences,
                modifier = Modifier.background(MaterialTheme.colorScheme.background)
            ).Field()
            M3TextField(
                label = stringResource(id = R.string.field_patronymic),
                form = viewModel.form,
                fieldState = viewModel.form.patronymic,
                keyboardType = KeyboardType.Text,
                keyboardCapitalization = KeyboardCapitalization.Sentences,
                modifier = Modifier.background(MaterialTheme.colorScheme.background)
            ).Field()
            M3TextField(
                label = stringResource(id = R.string.field_email),
                form = viewModel.form,
                fieldState = viewModel.form.email,
                keyboardType = KeyboardType.Email,
                modifier = Modifier.background(MaterialTheme.colorScheme.background)
            ).Field()
            M3DateField(
                label = stringResource(id = R.string.field_birth_date),
                form = viewModel.form,
                fieldState = viewModel.form.dateOfBirth,
                formatter = ::dateShort,
                modifier = Modifier.background(MaterialTheme.colorScheme.background)
            ).Field()
            M3PickerField(
                label = stringResource(R.string.field_gender),
                form = viewModel.form,
                fieldState = viewModel.form.gender,
                isSearchable = false
            ).Field()
            M3PickerField(
                label = stringResource(R.string.field_role),
                form = viewModel.form,
                fieldState = viewModel.form.role,
                isSearchable = false
            ).Field()

            Column(horizontalAlignment = Alignment.End, modifier = Modifier.fillMaxWidth()) {
                Button(onClick = { viewModel.submit { goToFeedScreen() } }) {
                    Text(text = stringResource(R.string.button_register))
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewSignUpScreen() {
    SignUpScreen {}
}