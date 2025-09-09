package chiogros.cost.ui.ui.screens.license

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import chiogros.cost.R
import com.mikepenz.aboutlibraries.ui.compose.android.rememberLibraries
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer

@Composable
fun ThirdPartyLicenses(
    onBack: () -> Unit
) {
    val libraries by rememberLibraries(R.raw.aboutlibraries)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { ThirdPartyLicensesTopBar(onBack) }
    ) { innerPadding ->
        LibrariesContainer(
            libraries = libraries,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            showDescription = true,
            showFundingBadges = true
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThirdPartyLicensesTopBar(onBack: () -> Unit) {
    TopAppBar(
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
        ),
        title = {
            Text(stringResource(R.string.third_party_licenses))
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.cancel)
                )
            }
        }
    )
}