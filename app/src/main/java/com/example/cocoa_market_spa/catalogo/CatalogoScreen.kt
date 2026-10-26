
package com.example.cocoa_market_spa.catalogo

import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.cocoa_market_spa.R
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.Alignment
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.platform.LocalContext
import com.example.cocoa_market_spa.data.local.entity.ProductoEntity

@Composable
fun CatalogoScreen(
    productos: List<ProductoEntity>,
    onProductoClick: (Int)-> Unit
) {

    var categoriaSeleccionada by remember {
        mutableStateOf("Todos")
    }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon( // Aqui es para el entrar a la aplicacion de maps y ver la unicacion del local
                modifier = Modifier.clickable{
                    val direccion = "Av. Las Condes 9280, Local 4, Las Condes, Santiago, Chile"
                    val enlace = "https://www.google.com/maps/search/?api=1&query=" +
                            Uri.encode(direccion)

                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        enlace.toUri()
                    )

                    context.startActivity(intent)
                },
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Ubicanos :D"
            )

            Text(
                text = "Ubicanos :D",
                style = MaterialTheme.typography.bodySmall
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Cocoa Market",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text("Que te interesa?")
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_busqueda),
                    contentDescription = "Buscar",
                    modifier = Modifier.size(20.dp)
                )
            },
            singleLine = true,
            readOnly = true
        )

        Text(
            text = "Catálogo de productos",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Desplazamiento horizontal
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            items(
                listOf("Todos", "Abarrotes", "Bebidas", "Higiene", "Lacteos")
            ) { categoria ->

                Button(
                    onClick = {
                        categoriaSeleccionada = categoria
                    }
                ) {
                    Text(text = categoria)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // transición animada para los productos seleccionados como muestra
        AnimatedContent(
            targetState = categoriaSeleccionada,
            label = "AnimacionDeCatalogo",
            modifier = Modifier.weight(1f),
        ) { categoriaActual ->

          val productosFiltrados = if (categoriaActual == "Todos"){
              productos
          }else{
              productos.filter { producto->
                  producto.categoria == categoriaActual
              }
          }

            if (productosFiltrados.isEmpty()) {
                Text("No hay productos disponibles en esta categoría")
            }

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(productosFiltrados) { producto ->
                    Producto(
                        nombre = producto.nombre,
                        precio = "$${producto.precio}",
                        imagen = producto.imagen,
                        onClick = {
                            onProductoClick(producto.id)
                        }
                    )
                }
            }
        }
        }
    }


@Composable
fun Producto(
    nombre: String,
    precio: String,
    imagen: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    Card(
        modifier = modifier.clickable{
            onClick()
        },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {

            Image(
                painter = painterResource(id = imagen),
                contentDescription = nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = nombre,
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = precio,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
