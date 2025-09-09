package chiogros.cost.ui.ui.screens.about

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.createBitmap
import chiogros.cost.BuildConfig
import chiogros.cost.R

@Composable
fun About(
    onBack: () -> Unit,
    onProjectLicenseClick: () -> Unit,
    onThirdPartyLicensesClick: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { AboutTopBar(onBack) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AboutContent(onProjectLicenseClick, onThirdPartyLicensesClick)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutTopBar(onBack: () -> Unit) {
    TopAppBar(
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
        ),
        title = {
            Text(stringResource(R.string.about))
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

@Composable
fun AboutContent(
    onLicenceClick: () -> Unit,
    onThirdPartyLicencesClick: () -> Unit
) {
    val uriHandler = LocalUriHandler.current
    val repositoryUrl = stringResource(R.string.repository_url)
    val changelogUrl =
        stringResource(R.string.repository_url) + "/releases/tag/" + BuildConfig.VERSION_NAME

    val logo = ResourcesCompat.getDrawable(LocalResources.current, R.mipmap.ic_launcher_round, null)
    val bitmap: Bitmap

    Spacer(Modifier.height(64.dp))

    if (logo != null) {
        bitmap = createBitmap(width = logo.intrinsicWidth, height = logo.intrinsicHeight)
        val canva = Canvas(bitmap)
        logo.setBounds(0, 0, canva.width, canva.height)
        logo.draw(canva)

        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = stringResource(R.string.app_logo)
        )
    }

    Text(
        text = BuildConfig.APP_NAME,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 24.sp
    )

    Spacer(Modifier.height(64.dp))

    AboutItem(
        onClick = {},
        icon = Icons.Outlined.Info,
        iconDescription = stringResource(R.string.version),
        title = stringResource(R.string.version),
        subtitle = BuildConfig.VERSION_NAME
    )

    AboutItem(
        onClick = { uriHandler.openUri(changelogUrl) },
        icon = Icons.Outlined.History,
        iconDescription = stringResource(R.string.changelog),
        title = stringResource(R.string.changelog),
        subtitle = stringResource(R.string.what_s_new)
    )

    AboutItem(
        onClick = { uriHandler.openUri(repositoryUrl) },
        icon = Icons.Outlined.Code,
        iconDescription = stringResource(R.string.repository),
        title = stringResource(R.string.repository),
        subtitle = stringResource(R.string.repository_description)
    )

    AboutItem(
        onClick = onLicenceClick,
        icon = Icons.Outlined.Description,
        iconDescription = stringResource(R.string.license),
        title = stringResource(R.string.license),
        subtitle = stringResource(R.string.license_description)
    )

    AboutItem(
        onClick = onThirdPartyLicencesClick,
        icon = Icons.Outlined.Description,
        iconDescription = stringResource(R.string.third_party_licenses),
        title = stringResource(R.string.third_party_licenses),
        subtitle = stringResource(R.string.third_party_licenses_description)
    )
}

@Composable
fun AboutItem(
    onClick: () -> Unit,
    icon: ImageVector,
    iconDescription: String?,
    title: String,
    subtitle: String? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = iconDescription
        )

        Spacer(Modifier.width(24.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Normal
            )

            if (subtitle != null) {
                Text(
                    text = subtitle,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 14.sp,
                    lineHeight = 18.sp
                )
            }
        }
    }
}
