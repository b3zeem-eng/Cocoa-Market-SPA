package com.example.cocoa_market_spa.catalogo

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import android.util.Log
import android.widget.Toast

@Composable
fun DetalleProductoScreen(
    nombre: String,
    precio: Int,
    descripcion: String,
    stock: Int,
    imagen: Int,
    onVolver: () -> Unit,
    onAgregarCarrito: (Int) -> Unit

)   { var cantidad by remember{
    mutableIntStateOf(1)
    }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Detalles del producto",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = imagen),
            contentDescription = nombre,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)

        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = nombre,
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = "$$precio",
            style = MaterialTheme.typography.titleMedium

        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Descripcion",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = descripcion,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Stock disponible $stock unidades",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = " $cantidad ",
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = "Cantidad",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Button(
                onClick = {
                    if (cantidad > 1) {
                        cantidad--
                    }
                },
                enabled = cantidad > 1 && stock > 0
            ) {
                Text(text = "−")
            }

            Text(
                text = "   $cantidad   ",
                style = MaterialTheme.typography.titleLarge
            )

            Button(
                onClick = {
                    if (cantidad < stock) {
                        cantidad++
                    }
                },
                enabled = cantidad < stock
            ) {
                Text(text = "+")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                vibrar(context)
                onAgregarCarrito(cantidad)
            },
                modifier = Modifier.fillMaxWidth(),
                enabled = stock > 0
        ) {
            Text(text = "Agregar al carrito")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Volver al catalogo"
            )
        }

    }
}

fun vibrar(context: Context){
    val vibrator: Vibrator

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){

        val manager = context.getSystemService(
            Context.VIBRATOR_MANAGER_SERVICE
        )as VibratorManager

        vibrator = manager.defaultVibrator
    }else{
        @Suppress("DEPRECATION")
        vibrator = context.getSystemService(
            Context.VIBRATOR_SERVICE
        ) as Vibrator
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
        vibrator.vibrate(
            VibrationEffect.createOneShot(
                150,
                VibrationEffect.DEFAULT_AMPLITUDE
            )
        )
    } else  {
        @Suppress("DEPRECATION")
        vibrator.vibrate(150)
    }
}