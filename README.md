Cocoa Market SPA 🛒
Prototipo académico de aplicación móvil Android para un minimarket.

Cocoa Market SPA es una aplicación desarrollada en Kotlin con Jetpack Compose para explorar productos de un minimarket, consultar su disponibilidad, gestionar un carrito y registrar pedidos de prueba. El proyecto busca ofrecer una alternativa digital a la compra exclusivamente presencial, con vistas a facilitar pedidos para retiro en tienda o despacho.
Estado del proyecto: MVP / prototipo académico en desarrollo. La implementación descrita corresponde a la rama develop. No es una plataforma comercial ni está conectada al sistema de caja del minimarket.

Contexto del proyecto
Cocoa Market SPA es un minimarket ubicado en la comuna de Las Condes, Chile. El caso plantea que las ventas dependen de la atención presencial y que los clientes no disponen de un canal móvil para revisar productos y preparar sus compras a distancia.

La propuesta es una aplicación Android orientada principalmente al cliente/comprador. El proyecto se desarrolla como parte de la asignatura Desarrollo de Aplicaciones Móviles (DSY1105) de Duoc UC.
Funcionalidades implementadas
Autenticación y usuarios (E1)
- Pantallas de registro e inicio de sesión.
- Validación de campos obligatorios, formato de correo y longitud mínima de contraseña durante el registro.
- Comprobación de correo ya registrado y de credenciales de acceso.
- Almacenamiento local de usuarios mediante Room/SQLite.
- Navegación desde el inicio de sesión al catálogo.
Catálogo y productos (E2)
- Visualización de productos con imagen, nombre y precio.
- Productos iniciales almacenados en la tabla productos de SQLite.
- Filtros por categorías: Todos, Abarrotes, Bebidas, Higiene y Lacteos.
- Mensaje cuando una categoría no tiene productos registrados.
- Pantalla de detalle que consulta cada producto por su ID y muestra descripción, precio y stock desde Room.
- Selección de cantidad limitada en la pantalla de detalle según el stock mostrado.
- Acceso a la ubicación del negocio mediante un Intent que abre una aplicación de mapas.
- Vibración como respuesta táctil al presionar «Agregar al carrito».
Carrito y pedidos (E3)
- Agregar un producto y su cantidad desde el detalle al carrito.
- Aumentar, disminuir y eliminar productos; calcular subtotales y total del carrito.
- Pantalla de carrito vacío.
- Selección de retiro en tienda o despacho a domicilio (con campo de dirección).
- Selección de efectivo o tarjeta como método de pago dentro del prototipo.
- Pantalla de confirmación y sección Mis pedidos.
- Código para guardar pedidos y sus detalles en Room mediante PedidoDao y PedidoViewModel.
Alcances y advertencias: seleccionar «Tarjeta» no procesa pagos reales. En el código revisado no existe una operación que descuente automáticamente el stock de productos al confirmar un pedido. El carrito mantiene su lista en el estado de su ViewModel; que exista una entidad carrito no implica que la lista se esté guardando mediante un DAO. La persistencia y recuperación completa de pedidos, así como las migraciones de Room, deben validarse con pruebas adicionales. La interfaz de búsqueda del catálogo se encuentra en modo de solo lectura y los accesos Inicio/Perfil de la barra inferior no tienen todavía toda su navegación funcional.

Tecnologías utilizadas
Tecnología	Uso en el proyecto
Kotlin	Lenguaje de programación
Android Studio	Entorno de desarrollo y ejecución
Jetpack Compose	Construcción de pantallas declarativas
Material Design 3	Componentes y estilos visuales
Navigation Compose	Navegación entre pantallas
ViewModel y estado de Compose	Gestión de datos y actualización de la interfaz
Room / SQLite	Persistencia local de usuarios, productos y estructura de pedidos
Corrutinas de Kotlin	Ejecución de operaciones de base de datos
KSP	Procesamiento de anotaciones de Room
Git y GitHub	Control de versiones y colaboración por ramas


Arquitectura y estructura de packages
El proyecto emplea una organización basada en MVVM (Model–View–ViewModel):
- Model / datos: entidades y modelos que representan usuarios, productos, carrito y pedidos.
- View: composables que muestran formularios, catálogo, detalle, carrito y pedidos.
- ViewModel: clases que mantienen el estado y coordinan las consultas o acciones de la interfaz.
app/src/main/java/com/example/cocoa_market_spa/
├── MainActivity.kt                # Punto de entrada de la aplicación
├── catalogo/                      # Catálogo, detalle, barra inferior y navegación de E2/E3
│   └── viewmodel/                 # CatalogoViewModel
├── carrito/                       # Carrito, entrega/pago, confirmación y pedidos
├── data/
│   └── local/                     # AppDatabase, User y UserDao
│       └── entity/                # Entidades y DAO de productos y pedidos
├── model/                         # CarritoModel y PedidoModel
├── ui/
│   ├── auth/                      # LoginScreen, RegisterScreen y AuthViewModel
│   ├── navigation/                # AppNavigation (rutas de inicio de sesión)
│   └── theme/                     # Colores, tipografía y tema Material 3
└── viewmodel/                     # CarritoViewModel y PedidoViewModel
¿Cómo se relacionan las capas?
1. MainActivity aplica el tema e inicia AppNavigation.
2. AppNavigation presenta el login y el registro y, tras autenticar, abre CatalogoNavigation.
3. AppDatabase proporciona acceso a las tablas mediante los DAO.
4. CatalogoViewModel consulta ProductoDao y mantiene la lista y el producto seleccionado.
5. CatalogoScreen muestra productos y filtros; DetalleProductoScreen recibe los datos consultados por ID.
6. CarritoViewModel mantiene los productos agregados y calcula el total.
7. PedidoViewModel contiene la lógica para registrar pedidos mediante PedidoDao.
Almacenamiento local
La base de datos se llama cocoa_market_db. En el código actual, AppDatabase declara las entidades de usuarios, productos, carrito, pedidos y detalles de pedidos.
Tabla	Propósito
users	Datos de usuarios registrados
productos	Nombre, precio, categoría, descripción, imagen y stock
carrito	Estructura de datos de los artículos del carrito
pedidos	Número, total y estado de pedidos
detalle_pedidos	Productos y cantidades asociados a cada pedido


Nota técnica: el esquema actual declara la versión 3. La migración 1 → 2 incorpora productos. En la migración 2 → 3 revisada se crean carrito y detalle_pedidos, pero no se observa la creación de pedidos; por eso esa migración debe revisarse antes de asegurar que todas las instalaciones existentes actualizan correctamente. La aplicación no utiliza una base de datos remota en esta etapa.
Requisitos para ejecutar el proyecto
- Android Studio con el SDK de Android instalado.
- Un dispositivo Android o emulador compatible (minSdk 24).
- JDK 21 para Gradle y las pruebas Robolectric de Android 17.
- Android SDK Platform 37.2 y Build Tools 37.0.0.
- Conexión a Internet para descargar dependencias durante la configuración inicial.
Instalación
1. Clonar el repositorio y entrar a la carpeta:
   git clone https://github.com/asvasltx/Cocoa-Market-SPA.git
   cd Cocoa-Market-SPA
2. Como el trabajo integrado se encuentra en develop, seleccionar esa rama:
   git switch develop
3. Abrir la carpeta del proyecto en Android Studio y sincronizar Gradle.
4. Iniciar un emulador o conectar un dispositivo Android.
5. Ejecutar la configuración app con Run ▶.
Para comprobar la compilación desde Windows, se puede ejecutar:
.\gradlew.bat assembleDebug
La base de datos local se puede inspeccionar con App Inspection → Database Inspector mientras la aplicación esté ejecutándose en modo depuración.
Flujo de uso del prototipo
Registro / Inicio de sesión
            ↓
         Catálogo
            ↓
     Detalle del producto
            ↓
      Agregar al carrito
            ↓
          Carrito
            ↓
       Entrega y pago
            ↓
     Confirmación de pedido
            ↓
        Mis pedidos
Organización del trabajo
El desarrollo se divide en tres épicas principales:
- E1: autenticación y usuarios.
- E2: catálogo, categorías, detalle y productos en Room.
- E3: carrito de compras y gestión de pedidos.
Se utiliza un flujo de trabajo colaborativo con ramas feature/*, Pull Requests y la rama develop para integrar funcionalidades. La rama main es la rama predeterminada del repositorio, pero puede no contener las funcionalidades más recientes de develop.
Mejoras y verificaciones pendientes
- Probar y corregir la migración de Room de la versión 2 a la 3, especialmente la creación de la tabla pedidos.
- Implementar una validación de stock y su descuento transaccional cuando se confirme un pedido.
- Validar que los pedidos almacenados se recuperen correctamente al volver a abrir la aplicación.
- Mejorar las funciones de búsqueda y las rutas aún no conectadas de la barra inferior.
- Revisar la seguridad de las credenciales: el código actual almacena contraseñas como texto en la base local, por lo que no corresponde utilizarlo como solución de autenticación de producción.
- Integrar sistemas remotos o pagos reales solo si se incorporan al alcance de una etapa posterior.
Repositorio
GitHub: https://github.com/asvasltx/Cocoa-Market-SPA

Rama integrada documentada: develop


Verificación automática
La configuración actual usa Gradle 9.8.2, Android Gradle Plugin 9.4.1,
Kotlin 2.4.21 y las versiones estables de las dependencias declaradas en
gradle/libs.versions.toml. compileSdk es 37.2, targetSdk es 37 y minSdk sigue en 24.

Con JAVA_HOME y ANDROID_HOME configurados, ejecutar desde la raíz:

```sh
sh ./gradlew :app:assembleDebug :app:assembleRelease :app:testDebugUnitTest :app:lintDebug :app:assembleDebugAndroidTest
```

Las pruebas locales incluyen el acceso a Mi cuenta desde Inicio, Carrito y
Pedidos, comprobando el nombre del usuario en Android 15 y Android 17 con
Robolectric. No requieren un dispositivo o emulador. El test de instrumentación
existente se compila con assembleDebugAndroidTest; su ejecución en un dispositivo
se realiza por separado con connectedDebugAndroidTest.

La generación de código de Room usa una configuración regional fija para las
JVM de Gradle y Kotlin, evitando literales numéricos árabes en el código generado.
Esto no cambia el idioma ni la configuración regional de la aplicación.

.github/workflows/android.yml define estas comprobaciones para cada push y pull
request, usando JDK 21 y dos configuraciones regionales del host: árabe e inglés.
