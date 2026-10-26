package com.example.cocoa_market_spa.carrito


import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cocoa_market_spa.viewmodel.PedidoViewModel
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.cocoa_market_spa.viewmodel.CarritoViewModel
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cocoa_market_spa.data.local.AppDatabase

@Composable
fun CarritoNavigation() {

    val navController = rememberNavController()
    val context = LocalContext.current
    val pedidoDao = AppDatabase.getDatabase(context).PedidoDao()

    val pedidoViewModel: PedidoViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return PedidoViewModel(pedidoDao) as T
            }
        }
    )
    val carritoViewModel: CarritoViewModel = viewModel()

    var totalPedido by remember { mutableIntStateOf(0) }

        NavHost(
            navController = navController,
            startDestination = "carrito"
        ) {

            composable("carrito") {
                CarritoScreen(
                    onNavigateToPedidos = {
                        navController.navigate("entrega_pago")
                    },
                    onVolverCatalogo = {
                        navController.popBackStack()
                    },
                    carritoViewModel=carritoViewModel
                )
            }

            composable("entrega_pago") {
                EntregaPagoScreen(
                    onConfirmarPedido = {
                        totalPedido=carritoViewModel.calcularTotal()
                        pedidoViewModel.agregarPedido(
                            total = totalPedido,
                            productos = carritoViewModel.productos.value
                        )
                        navController.navigate("pedido_confirmado")
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
                PedidoScreen(
                    onVolvercarrito = {
                        navController.navigate("carrito")
                    },
                    pedidoViewModel = pedidoViewModel
                )
            }
        }


}
