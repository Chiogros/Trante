package chiogros.trante.ui.ui.screens.license

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.mikepenz.aboutlibraries.entity.License

@Composable
fun LicenceDisplay(license: License, modifier: Modifier) {
    Text(
        text = license.licenseContent.toString(),
        modifier = modifier,
        fontSize = 9.sp
    )
}