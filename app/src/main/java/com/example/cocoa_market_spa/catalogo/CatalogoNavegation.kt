package com.example.cocoa_market_spa.catalogo

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.cocoa_market_spa.catalogo.viewmodel.CatalogoViewModel
import com.example.cocoa_market_spa.data.local.AppDatabase
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cocoa_market_spa.viewmodel.CarritoViewModel
import com.example.cocoa_market_spa.model.CarritoModel
import com.example.cocoa_market_spa.carrito.CarritoScreen
import com.example.cocoa_market_spa.carrito.EntregaPagoScreen
import com.example.cocoa_market_spa.carrito.PedidoConfirmadoScreen
import com.example.cocoa_market_spa.viewmodel.PedidoViewModel
import com.example.cocoa_market_spa.carrito.PedidoScreen
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import android.media.RingtoneManager


@Composable
fun CatalogoNavigation(nombreUsuario: String) {

    val navController = rememberNavController()
    val context = LocalContext.current
    val carritoViewModel: CarritoViewModel = viewModel()

    val pedidoDao = AppDatabase.getDatabase(context).PedidoDao()

    val pedidoViewModel: PedidoViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return PedidoViewModel(pedidoDao) as T
            }
        }
    )
    var totalPedido by remember { mutableIntStateOf(0) }



    val database = remember {
        AppDatabase.getDatabase(context)
    }

    val catalogoViewModel = remember {
        CatalogoViewModel(database.ProductoDao())
    }

    LaunchedEffect(Unit) {
        catalogoViewModel.cargarProductos()
    }

    NavHost(
        navController = navController,
        startDestination = "catalogo"
    ) {
        //composable para la barra inferior

        composable("catalogo") {

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    BarraInferior(
                        onInicio = {
                            navController.navigate("catalogo")
                        },
                        onCarrito = {
                            navController.navigate("carrito")
                        },
                        onPedidos = {
                            navController.navigate("pedidos")
                        },
                        onCuenta = {
                            navController.navigate("cuenta")
                        }
                    )
                }
            ) { innerPadding ->

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {

                    CatalogoScreen(
                        productos = catalogoViewModel.productos.value,
                        onProductoClick = { id ->
                            navController.navigate("detalle/$id")
                        }
                    )
                }
            }
        }


        composable("carrito") {
            Scaffold(
                bottomBar = {
                    BarraInferior(
                        onInicio = {
                            navController.navigate("catalogo")
                        },
                        onCarrito = {
                            navController.navigate("carrito")
                        },
                        onPedidos = {
                            navController.navigate("pedidos")
                        },
                        onCuenta = {
                            navController.navigate("cuenta")
                        }
                    )
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    CarritoScreen(
                        onNavigateToPedidos = {
                            navController.navigate("entrega_pago")
                        },
                        onVolverCatalogo = {
                            navController.navigate("catalogo")
                        },
                        carritoViewModel = carritoViewModel
                    )
                }
            }
        }



        composable("entrega_pago") {
            EntregaPagoScreen(
                onConfirmarPedido = {
                    totalPedido = carritoViewModel.calcularTotal()

                    if (carritoViewModel.productos.value.isNotEmpty()) {

                        pedidoViewModel.agregarPedido(
                            total = totalPedido,
                            productos = carritoViewModel.productos.value
                        )

                        carritoViewModel.vaciarCarrito()

                        // Para el sonido al confirmar pedido
                        val sonido = RingtoneManager.getRingtone(
                            context,
                            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                        )
                        sonido?.play()

                        navController.navigate("pedido_confirmado")
                    }
                },
                onVolverCarrito = {
                    navController.popBackStack()
                }
            )
        }

        composable("pedido_confirmado") {
            PedidoConfirmadoScreen(
                total = totalPedido,
                onVerPedidos = {
                    navController.navigate("pedidos")
                },
                onVolverCatalogo = {
                    navController.navigate("carrito")

                }
            )
        }


        composable("pedidos") {
            Scaffold(
                bottomBar = {
                    BarraInferior(
                        onInicio = {
                            navController.navigate("catalogo")
                        },
                        onCarrito = {
                            navController.navigate("carrito")
                        },
                        onPedidos = {
                            navController.navigate("pedidos")
                        },
                        onCuenta = {
                            navController.navigate("cuenta")
                        }
                    )
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    PedidoScreen(
                        onVolvercarrito = {
                            navController.navigate("carrito")
                        },
                        pedidoViewModel = pedidoViewModel
                    )
                }
            }
        }

        composable("cuenta") {
            Scaffold(
                bottomBar = {
                    BarraInferior(
                        onInicio = {
                            navController.navigate("catalogo")
                        },
                        onCarrito = {
                            navController.navigate("carrito")
                        },
                        onPedidos = {
                            navController.navigate("pedidos")
                        },
                        onCuenta = {
                            navController.navigate("cuenta")
                        }
                    )
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    MiCuentaScreen(
                        nombreUsuario = nombreUsuario
                    )
                }
            }
        }






        //Para ver el detalle de los productos
        composable("detalle/{id}") { backStackEntry ->

            val id = backStackEntry.arguments
                ?.getString("id")
                ?.toIntOrNull()

            LaunchedEffect(id) {
                if (id != null) {
                    catalogoViewModel.cargarProductoPorId(id)
                }
            }

            val producto = catalogoViewModel.productoSeleccionado.value

            if (producto != null && producto.id == id) {

                DetalleProductoScreen(
                    nombre = producto.nombre,
                    precio = producto.precio,
                    descripcion = producto.descripcion,
                    stock = producto.stock,
                    imagen = producto.imagen,
                    onVolver = {
                        navController.popBackStack()
                    },
                    onAgregarCarrito = { cantidad ->

                        val productoCarrito = CarritoModel(
                            productoId = producto.id,
                            nombre = producto.nombre,
                            precio = producto.precio,
                            cantidad = cantidad,
                            imagen = producto.imagen
                        )

                        carritoViewModel.agregarProducto(productoCarrito)
                    }
                )


            } else {
                Text("Cargando producto...")
            }
        }
    }
}
