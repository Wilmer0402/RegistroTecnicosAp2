package edu.ucne.registrotecnicos.presentation.Home

import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.ucne.registrotecnicos.presentation.navigation.Screen

@Composable
fun HomeScreen(
    navController: NavController
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Bienvenidos",
                style = MaterialTheme.typography.h4,
                color = MaterialTheme.colors.primary,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            MenuButton(
                title = "Ir a Técnicos",
                icon = Icons.Default.Build,
                onClick = {navController.navigate(Screen.TecnicoList)},
                backgroundColor = Color(0xFF81C784) // Verde suave
            )

            Spacer(modifier = Modifier.height(16.dp))

            MenuButton(
                title = "Ir a Tickets",
                icon = Icons.Default.Assignment,
                onClick ={navController.navigate(Screen.TicketList)},
                backgroundColor = Color(0xFF64B5F6) // Azul
            )

            Spacer(modifier = Modifier.height(16.dp))

            MenuButton(
                title = "Ir a Prioridades",
                icon = Icons.Default.PriorityHigh,
                onClick = {navController.navigate(Screen.PrioridadList)},
                backgroundColor = Color(0xFFFFB74D) // Naranja
            )
        }
    }
}

@Composable
fun MenuButton(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    backgroundColor: Color
) {
    // Animación infinita
    val infiniteTransition = rememberInfiniteTransition()

    // Flotación (arriba y abajo)
    val floatOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Zoom suave
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Card(
        shape = RoundedCornerShape(16.dp),
        backgroundColor = backgroundColor,
        elevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .offset(y = floatOffset.dp)
            .scale(scale)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .clickable(onClick = onClick),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                color = Color.White,
                fontSize = 18.sp
            )
        }
    }
}
