package com.example.cocoa_market_spa.catalogo

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35, 37], qualifiers = "ar-rSA")
class AccountNavigationTest {
    @get:Rule
    val compose = createComposeRule()

    @Before
    fun openCatalog() {
        compose.setContent {
            MaterialTheme {
                CatalogoNavigation(nombreUsuario = "Ana")
            }
        }
        compose.onNodeWithText("Cocoa Market").assertIsDisplayed()
    }

    @Test
    fun homeOpensAccountWithLoggedInUser() {
        openAccountAndCheckUser()
    }

    @Test
    fun cartOpensAccountWithLoggedInUser() {
        compose.onNodeWithText("Carrito").performClick()
        openAccountAndCheckUser()
    }

    @Test
    fun ordersOpenAccountWithLoggedInUser() {
        compose.onNodeWithText("Pedidos").performClick()
        openAccountAndCheckUser()
    }

    private fun openAccountAndCheckUser() {
        compose.onNodeWithText("Cuenta").performClick()
        compose.onNodeWithText("Mi cuenta").assertIsDisplayed()
        compose.onNodeWithText("Hola, Ana").assertIsDisplayed()
    }
}
