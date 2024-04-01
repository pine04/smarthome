package au.edu.swin.sdmd.smarthome.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import au.edu.swin.sdmd.smarthome.R

val Inter = FontFamily(
    Font(R.font.inter_regular),
    Font(R.font.inter_bold, FontWeight.Bold)
)

val Typography = Typography(
    displayMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    )
)