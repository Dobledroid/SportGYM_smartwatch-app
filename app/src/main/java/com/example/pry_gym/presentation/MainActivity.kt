package com.example.pry_gym.presentation

import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.wear.compose.material.Button
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.foundation.text.BasicTextField
import com.example.pry_gym.R
import com.example.pry_gym.presentation.theme.Pry_GYMTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.text.TextStyle
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.selection.toggleable
import androidx.wear.compose.material.Checkbox
import kotlin.random.Random
import kotlinx.coroutines.delay
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import androidx.compose.material.TextField

import okhttp3.OkHttpClient
import okhttp3.FormBody
import okhttp3.Request

import org.json.JSONObject
import java.io.IOException
import android.util.Log

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.RadioButton
import kotlinx.coroutines.CoroutineScope
import androidx.wear.compose.material.*

import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.gson.Gson
import android.net.Uri
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.ui.input.pointer.pointerInput


import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.PowerManager
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.IconButton
import androidx.compose.runtime.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import com.google.android.gms.location.LocationServices
import org.json.JSONArray
import java.time.LocalDate
import java.time.YearMonth


class MainActivity : ComponentActivity() {

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1
    }

    private lateinit var wakeLock: PowerManager.WakeLock

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "MyApp::WakeLock")

        setContent {
            Pry_GYMTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "splash") {
                    composable("splash") { SplashScreen(navController) }
                    composable("login") { LoginScreen(navController) }
                    composable("welcome/{userName}/{userId}") { backStackEntry ->
                        val userName = backStackEntry.arguments?.getString("userName") ?: ""
                        val userId = backStackEntry.arguments?.getString("userId")?.toInt() ?: -1
                        WelcomeScreen(userName = userName, userId = userId, navController)
                    }
                    composable("dataEntry/{userId}") { backStackEntry ->
                        val userId = backStackEntry.arguments?.getString("userId")?.toInt() ?: -1
                        DataEntryScreen(userId, navController)
                    }
                    composable("home/{userId}") { backStackEntry ->
                        val userId = backStackEntry.arguments?.getString("userId")?.toInt() ?: -1
                        HomeScreen(userId = userId, navController)
                    }
                    composable("profileView/{userId}") { backStackEntry ->
                        val userId = backStackEntry.arguments?.getString("userId")?.toInt() ?: -1
                        ProfileScreen(userId = userId, navController)
                    }
                    composable("editField/{userId}/{fieldLabel}/{fieldValue}") { backStackEntry ->
                        val userId = backStackEntry.arguments?.getString("userId")?.toInt() ?: -1
                        val fieldLabel = backStackEntry.arguments?.getString("fieldLabel") ?: ""
                        val fieldValue = backStackEntry.arguments?.getString("fieldValue") ?: ""
                        EditFieldScreen(userId, fieldLabel, fieldValue, navController)
                    }
                    composable("heartRateView/{userId}") { backStackEntry ->
                        val userId = backStackEntry.arguments?.getString("userId")?.toInt() ?: 0
                        HeartRateView(navController, userId)
                    }
                    composable("oxygenLevelView/{userId}") { backStackEntry ->
                        val userId = backStackEntry.arguments?.getString("userId")?.toInt() ?: 0
                        OxygenLevelView(navController, userId)
                    }
                    composable("caloriesBurnedView/{userId}") { backStackEntry ->
                        val userId = backStackEntry.arguments?.getString("userId")?.toInt() ?: 0
                        CaloriesBurnedView(navController, userId)
                    }
                    composable("stepsCounterView/{userId}") { backStackEntry ->
                        val userId = backStackEntry.arguments?.getString("userId")?.toInt() ?: 0
                        StepsCounterView(navController, userId)
                    }
                    composable("distanceView/{userId}") { backStackEntry ->
                        val userId = backStackEntry.arguments?.getString("userId")?.toInt() ?: 0
                        DistanceView(navController, userId)
                    }
                    composable("newView/{userId}/{mainText}/{secondaryText}") { backStackEntry ->
                        val userId = backStackEntry.arguments?.getString("userId")?.toInt() ?: 0
                        val mainText = backStackEntry.arguments?.getString("mainText") ?: ""
                        val secondaryText = backStackEntry.arguments?.getString("secondaryText") ?: ""
                        NewView(userId = userId, mainText = mainText, secondaryText = secondaryText, navController)
                    }
                    composable("oxygenInfo/{userId}/{mainText}/{secondaryText}") { backStackEntry ->
                        val userId = backStackEntry.arguments?.getString("userId")?.toInt() ?: -1
                        val mainText = backStackEntry.arguments?.getString("mainText") ?: ""
                        val secondaryText = backStackEntry.arguments?.getString("secondaryText") ?: ""
                        OxygenInfoView(userId = userId, mainText = mainText, secondaryText = secondaryText, navController = navController)
                    }
                }
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permissions = mutableListOf(
                Manifest.permission.BODY_SENSORS,
                Manifest.permission.ACTIVITY_RECOGNITION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.VIBRATE
            )

            permissions.forEach { permission ->
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, permissions.toTypedArray(), PERMISSION_REQUEST_CODE)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (!wakeLock.isHeld) {
            wakeLock.acquire()
        }
    }

    override fun onPause() {
        super.onPause()
        if (wakeLock.isHeld) {
            wakeLock.release()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            permissions.forEachIndexed { index, permission ->
                if (grantResults[index] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("MainActivity", "Permiso concedido: $permission")
                } else {
                    Log.d("MainActivity", "Permiso denegado: $permission")
                }
            }
        }
    }
}


@Composable
fun SplashScreen(navController: NavController) {

    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
    val userId = sharedPreferences.getInt("user_id", -1)
    val userName = sharedPreferences.getString("user_name", null)

    LaunchedEffect(Unit) {
        delay(3000) // Retraso de 3 segundos
        if (userId != -1 && !userName.isNullOrEmpty()) {
            navController.navigate("home/$userId")
        } else {
            navController.navigate("login")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo), // Reemplaza con tu ícono
            contentDescription = "App Icon",
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
        )
    }
}

@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    var emailTextFieldValue by remember { mutableStateOf(TextFieldValue("")) }
    var passwordTextFieldValue by remember { mutableStateOf(TextFieldValue("")) }
    var errorMessage by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.Black)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Iniciar Sesión",
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        BasicTextField(
            value = emailTextFieldValue,
            onValueChange = { emailTextFieldValue = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            singleLine = true,
            decorationBox = { innerTextField ->
                Box(
                    Modifier
                        .background(Color.Gray)
                        .padding(8.dp)
                ) {
                    if (emailTextFieldValue.text.isEmpty()) {
                        Text(
                            text = "Correo Electrónico",
                            style = TextStyle(color = Color.Black)
                        )
                    }
                    innerTextField()
                }
            }
        )

        BasicTextField(
            value = passwordTextFieldValue,
            onValueChange = { passwordTextFieldValue = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            singleLine = true,
            decorationBox = { innerTextField ->
                Box(
                    Modifier
                        .background(Color.Gray)
                        .padding(8.dp)
                ) {
                    if (passwordTextFieldValue.text.isEmpty()) {
                        Text(
                            text = "Contraseña",
                            style = TextStyle(color = Color.Black)
                        )
                    }
                    innerTextField()
                }
            },
            visualTransformation = PasswordVisualTransformation()
        )

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (emailTextFieldValue.text.isNotEmpty() && passwordTextFieldValue.text.isNotEmpty()) {
                    coroutineScope.launch {
                        val result = loginUser(emailTextFieldValue.text, passwordTextFieldValue.text)
                        if (result.first) {
                            saveUserSession(context, result.second.second, result.second.first)
                            navController.navigate("welcome/${result.second.first}/${result.second.second}")
                        } else {
                            errorMessage = "Error de inicio de sesión: ${result.second.first}"
                        }
                    }
                } else {
                    errorMessage = "Por favor, complete todos los campos"
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            Text(
                text = "Ingresar",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


fun saveUserSession(context: Context, userId: Int, userName: String) {
    val sharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
    with(sharedPreferences.edit()) {
        putInt("user_id", userId)
        putString("user_name", userName)
        apply()
    }
}

suspend fun loginUser(email: String, password: String): Pair<Boolean, Pair<String, Int>> {
    return withContext(Dispatchers.IO) {
        val client = OkHttpClient()
        val requestBody = FormBody.Builder()
            .add("correoElectronico", email)
            .add("contraseña", password)
            .build()

        val request = Request.Builder()
            .url("https://api44.vercel.app/api/users/login-skill")
            .post(requestBody)
            .build()

        try {
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val responseData = response.body?.string()
                val json = JSONObject(responseData)
                val userName = json.getString("nombre")
                val userId = json.getInt("ID_usuario")
                Pair(true, Pair(userName, userId))
            } else {
                Pair(false, Pair(response.message, -1))
            }
        } catch (e: IOException) {
            Pair(false, Pair(e.message ?: "Error desconocido", -1))
        }
    }
}


data class UserData(
    var name: String = "",
    var nacido: Int = 0,
    var gender: String = "",
    var height: Float = 0f,
    var weight: Float = 0f,
    var restingHeartRate: Int = 0,
    var heartRate: Int = 0,
    var caloriesBurned: Float = 0f,
    var steps: Int = 0,
    var distance: Float = 0f
)

@Composable
fun HomeScreen(userId: Int, navController: NavController) {
    // Get the shared userName
    Log.d("HomeScreen", "userId: $userId")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(start = 10.dp)
            .swipeToNavigateBack(navController)
    ) {
        Column {
            // Header with logo and user icon
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp, bottom = 1.dp, end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.navigate("home/$userId") }
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Inicio",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    painter = painterResource(id = R.drawable.logo), // Cambia 'logo' por el nombre de tu archivo sin extensión
                    contentDescription = "Logo",
                    tint = Color.Unspecified, // Usamos Color.Unspecified para que la imagen no cambie de color
                    modifier = Modifier.size(40.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                IconButton(
                    onClick = { navController.navigate("profileView/$userId") }
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Mi perfil",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            // Main Content
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top
            ) {
                item {
                    Text(
                        text = "Funcionalidades",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        style = TextStyle(
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }

                item {
                    MonthlyActivityView(userId)
                }

                item {
                    ClickableTextWithSecondary(
                        primaryText = "Frecuencia Cardíaca",
                        secondaryText = "",
                        onClick = { navController.navigate("heartRateView/$userId") },
                        textAlign = TextAlign.Left
                    )
                }

                item {
                    ClickableTextWithSecondary(
                        primaryText = "Nivel de oxígeno en la sangre (SpO2)",
                        secondaryText = "",
                        onClick = { navController.navigate("oxygenLevelView/$userId") },
                        textAlign = TextAlign.Left
                    )
                }

                item {
                    ClickableTextWithSecondary(
                        primaryText = "Quema de Calorías",
                        secondaryText = "",
                        onClick = { navController.navigate("caloriesBurnedView/$userId") },
                        textAlign = TextAlign.Left
                    )
                }

                item {
                    ClickableTextWithSecondary(
                        primaryText = "Marcador de Pasos",
                        secondaryText = "",
                        onClick = { navController.navigate("stepsCounterView/$userId") },
                        textAlign = TextAlign.Left
                    )
                }

                item {
                    Box(modifier = Modifier.padding(bottom = 30.dp)) {
                        ClickableTextWithSecondary(
                            primaryText = "Distancias",
                            secondaryText = "",
                            onClick = { navController.navigate("distanceView/$userId") },
                            textAlign = TextAlign.Left
                        )
                    }
                }
            }
        }
    }
}


suspend fun fetchUserEntries(userId: Int): List<LocalDate> {
    return withContext(Dispatchers.IO) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api44.vercel.app/api/entradas-miembros/usuario/$userId")
            .build()

        try {
            val response = client.newCall(request).execute()
            val responseData = response.body?.string()

            if (response.isSuccessful && !responseData.isNullOrEmpty()) {
                val jsonArray = JSONArray(responseData)
                val entries = mutableListOf<LocalDate>()
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val date = jsonObject.optString("fechaEntrada").substring(0, 10)
                    entries.add(LocalDate.parse(date))
                }
                entries
            } else {
                emptyList()
            }
        } catch (e: IOException) {
            emptyList()
        }
    }
}

@Composable
fun MonthlyActivityView(userId: Int) {
    val daysOfWeek = listOf("D", "L", "M", "M", "J", "V", "S")
    val currentMonth = YearMonth.now()
    val firstDayOfMonth = currentMonth.atDay(1)
    val lastDayOfMonth = currentMonth.atEndOfMonth()
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7 // Ensure Sunday is 0
    val totalDays = lastDayOfMonth.dayOfMonth
    val totalCells = firstDayOfWeek + totalDays

    // State to hold the entries data
    var entries by remember { mutableStateOf<List<LocalDate>>(emptyList()) }

    // Fetch the data
    LaunchedEffect(Unit) {
        entries = fetchUserEntries(userId) // Fetch entries for user with the provided ID
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.DarkGray, RoundedCornerShape(8.dp))
    ) {
        Text(
            text = "Datos de este mes",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            ),
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        // Days of the week
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            daysOfWeek.forEach { day ->
                Text(
                    text = day,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }
        }

        // Days of the month
        for (i in 0 until totalCells step 7) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (j in 0..6) {
                    val day = i + j - firstDayOfWeek + 1
                    if (day > 0 && day <= totalDays) {
                        val date = currentMonth.atDay(day)
                        val isEntryDay = entries.contains(date)
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(if (isEntryDay) Color.Green else Color.Gray, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = day.toString(),
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                        ) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                // Empty space for days not in the current month
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileScreen(userId: Int, navController: NavController) {
    val context = LocalContext.current
    var userProfile by remember { mutableStateOf<UserData?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(userId) {
        userProfile = fetchUserProfile(userId)
        isLoading = false
    }

    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        userProfile?.let { profile ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .pointerInput(Unit){
                        detectHorizontalDragGestures { change, dragAmount ->
                            if(dragAmount > 0){
                                navController.navigate("home/$userId")
                            }
                        }
                    }
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = { navController.navigate("home/$userId") },
                    modifier = Modifier.align(Alignment.Start)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Regresar",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Cuenta",
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                Text(
                    text = profile.name,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp)
                )

                ProfileItem(icon = Icons.Default.Person, label = "Género", value = profile.gender) {
                    Log.d("ProfileScreen", "Edit Género: ${profile.gender}")
                    navController.navigate("editField/${userId}/Género/${profile.gender}")
                }
                ProfileItem(icon = Icons.Default.Cake, label = "Año de Nacimiento", value = profile.nacido.toString()) {
                    Log.d("ProfileScreen", "Edit Año de Nacimiento: ${profile.nacido}")
                    navController.navigate("editField/${userId}/Año de Nacimiento/${profile.nacido}")
                }
                ProfileItem(icon = Icons.Default.Height, label = "Altura (cm)", value = "${profile.height}") {
                    Log.d("ProfileScreen", "Edit Altura (cm): ${profile.height}")
                    navController.navigate("editField/${userId}/Altura (cm)/${profile.height}")
                }
                ProfileItem(icon = Icons.Default.FitnessCenter, label = "Peso (kg)", value = "${profile.weight}") {
                    Log.d("ProfileScreen", "Edit Peso (kg): ${profile.weight}")
                    navController.navigate("editField/${userId}/Peso (kg)/${profile.weight}")
                }


                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        clearUserSession(context)
                        navController.navigate("login") {
                            popUpTo("login") { inclusive = true }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Cerrar Sesión",
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        } ?: run {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No se pudo obtener la información del perfil",
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = {
                            clearUserSession(context)
                            navController.navigate("login") {
                                popUpTo("login") { inclusive = true }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Cerrar Sesión",
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileItem(icon: ImageVector, label: String, value: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color.White,
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = label,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray
                )
            )
            Text(
                text = value,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
        }
    }
}

fun clearUserSession(context: Context) {
    val sharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
    sharedPreferences.edit().clear().apply()
}

suspend fun fetchUserProfile(userId: Int): UserData? {
    return withContext(Dispatchers.IO) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api44.vercel.app/api/smartwatch-users/$userId")
            .build()

        try {
            val response = client.newCall(request).execute()
            val responseData = response.body?.string()

            if (response.isSuccessful && !responseData.isNullOrEmpty()) {
                val json = JSONObject(responseData)
                UserData(
                    name = json.optString("nombre", ""),
                    gender = json.optString("genero", ""),
                    nacido = json.optInt("nacido", 0),
                    height = json.optDouble("altura", 0.0).toFloat(),
                    weight = json.optDouble("peso", 0.0).toFloat()
                )
            } else {
                null
            }
        } catch (e: IOException) {
            null
        }
    }
}




@Composable
fun EditFieldScreen(userId: Int, fieldLabel: String, fieldValue: String, navController: NavController) {
    var selectedValue by remember { mutableStateOf(fieldValue) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(8.dp), // Reduce el padding para minimizar el espacio
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        EditValueSelector(
            title = fieldLabel,
            options = when (fieldLabel) {
                "Género" -> listOf("Masculino", "Femenino")
                "Año de Nacimiento" -> (1900..2024).map { it.toString() }
                "Altura (cm)" -> (90..220).map { it.toString() }
                "Peso (kg)" -> (20..220).map { it.toString() }
                else -> listOf()
            },
            selectedIndex = when (fieldLabel) {
                "Género" -> listOf("Masculino", "Femenino").indexOf(fieldValue).coerceAtLeast(0)
                "Año de Nacimiento" -> (1900..2024).map { it.toString() }.indexOf(fieldValue).coerceAtLeast(0)
                "Altura (cm)" -> (90..220).map { it.toString() }.indexOf(fieldValue).coerceAtLeast(0)
                "Peso (kg)" -> (20..220).map { it.toString() }.indexOf(fieldValue).coerceAtLeast(0)
                else -> 0
            },
            onValueChange = { value ->
                selectedValue = value
            },
            onSave = {
                Log.d("EditFieldScreen", "Saving field: $fieldLabel with value: $selectedValue for userId: $userId")
                coroutineScope.launch {
                    saveUserProfileField(userId, fieldLabel, selectedValue)
                    navController.navigateUp()
                }
            }
        )
    }
}


@Composable
fun EditValueSelector(
    title: String,
    options: List<String>,
    selectedIndex: Int,
    onValueChange: (String) -> Unit,
    onSave: () -> Unit
) {
    // Crear la lista de opciones sin elementos adicionales
    val extendedOptions = listOf("") + options + listOf("")
    val scrollState = rememberLazyListState(initialFirstVisibleItemIndex = selectedIndex)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(top = 16.dp)
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            LazyColumn(
                state = scrollState,
                verticalArrangement = Arrangement.spacedBy(4.dp),
                contentPadding = PaddingValues(vertical = 32.dp) // Espaciado para mostrar 3 elementos
            ) {
                itemsIndexed(extendedOptions) { index, item ->
                    if (item.isNotEmpty()) {
                        Text(
                            text = item,
                            fontSize = 16.sp,
                            color = if (index == scrollState.firstVisibleItemIndex + 1) Color(0xFF60FDFF) else Color.Gray,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp), // Espaciado mínimo posible
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
        Button(
            onClick = {
                val adjustedIndex = scrollState.firstVisibleItemIndex
                val selectedValue = options[adjustedIndex]
                onValueChange(selectedValue)
                onSave()
            },
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
            modifier = Modifier
                .size(48.dp)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Check, contentDescription = "Check", tint = Color.Black)
        }
    }
}


suspend fun saveUserProfileField(userId: Int, fieldLabel: String, fieldValue: String) {
    withContext(Dispatchers.IO) {
        val client = OkHttpClient()

        // Mapa para traducir fieldLabel a los nombres de campo correctos en la base de datos
        val fieldMap = mapOf(
            "Género" to "genero",
            "Año de Nacimiento" to "nacido",
            "Altura (cm)" to "altura",
            "Peso (kg)" to "peso"
        )

        // Obtener el nombre de campo correcto
        val databaseField = fieldMap[fieldLabel] ?: fieldLabel.lowercase()

        // Obtener los datos actuales del usuario
        val getRequest = Request.Builder()
            .url("https://api44.vercel.app/api/smartwatch-users/$userId")
            .build()

        try {
            val getResponse = client.newCall(getRequest).execute()
            if (getResponse.isSuccessful) {
                val responseData = getResponse.body?.string()
                Log.d("saveUserProfileField", "Datos obtenidos del usuario: $responseData")
                val json = JSONObject(responseData)
                val idUsuarioSmartWatch = json.getInt("ID_usuarioSmartWatch")

                // Actualizar el campo específico con el nuevo valor
                json.put(databaseField, fieldValue)

                Log.d("saveUserProfileField", "Nuevo JSON a actualizar: $json")

                // Crear el cuerpo de la solicitud PUT con los datos actualizados
                val requestBody = FormBody.Builder()
                    .add("ID_usuarioSmartWatch", idUsuarioSmartWatch.toString())
                    .add("genero", json.getString("genero"))
                    .add("nacido", json.getInt("nacido").toString())
                    .add("altura", json.getDouble("altura").toString())
                    .add("peso", json.getDouble("peso").toString())
                    .build()

                val putRequest = Request.Builder()
                    .url("https://api44.vercel.app/api/smartwatch-users/$idUsuarioSmartWatch")
                    .put(requestBody)
                    .build()

                val putResponse = client.newCall(putRequest).execute()
                if (putResponse.isSuccessful) {
                    Log.d("saveUserProfileField", "Datos guardados exitosamente: $databaseField = $fieldValue")
                } else {
                    Log.e("saveUserProfileField", "Error al guardar los datos: ${putResponse.message}")
                }
            } else {
                Log.e("saveUserProfileField", "Error al obtener los datos del usuario: ${getResponse.message}")
            }
        } catch (e: IOException) {
            Log.e("saveUserProfileField", "Excepción al obtener o guardar los datos: ${e.message}")
        }
    }
}


@Composable
fun ClickableTextWithSecondary(
    primaryText: String,
    secondaryText: String,
    onClick: () -> Unit,
    textAlign: TextAlign = TextAlign.Left
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Text(
            text = primaryText,
            textAlign = textAlign,
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        )
        Text(
            text = secondaryText,
            textAlign = textAlign,
            style = TextStyle(
                fontSize = 14.sp,
                color = Color.White
            )
        )
    }
}

fun Modifier.swipeToNavigateBack(navController: NavController): Modifier = this.then(
    Modifier.pointerInput(Unit) {
        detectHorizontalDragGestures { change, dragAmount ->
            if (dragAmount > 0) {
                navController.navigate("home") {
                    popUpTo("home") { inclusive = true }
                }
            }
        }
    }
)



@Composable
fun HeartRateView(navController: NavController, userId: Int) {
    val context = LocalContext.current
    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)
    var heartRate by remember { mutableStateOf(0) }
    var sensorStatus by remember { mutableStateOf("No iniciado") }
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val wakeLock: PowerManager.WakeLock = (context.getSystemService(Context.POWER_SERVICE) as PowerManager)
        .newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "HeartRateView::WakeLock")

    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    val sensorEventListener = remember {
        object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                if (event.sensor.type == Sensor.TYPE_HEART_RATE) {
                    heartRate = event.values[0].toInt()
                }
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
        }
    }

    DisposableEffect(Unit) {
        if (heartRateSensor != null) {
            sensorManager.registerListener(sensorEventListener, heartRateSensor, SensorManager.SENSOR_DELAY_NORMAL)
            wakeLock.acquire()
            sensorStatus = "Midiendo..."
            coroutineScope.launch {
                delay(30000) // Medir durante 30 segundos
                sensorManager.unregisterListener(sensorEventListener)
                if (wakeLock.isHeld) {
                    wakeLock.release()
                }
                sensorStatus = when {
                    heartRate < 60 -> "Bradicardia"
                    heartRate > 100 -> "Taquicardia"
                    else -> "Normal"
                }
                Toast.makeText(context, "Medición ${sensorStatus.toLowerCase()}", Toast.LENGTH_SHORT).show()
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED) {
                    vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    Log.d("HeartRateView", "Permiso de vibración no concedido")
                }
            }
        } else {
            sensorStatus = "Sensor no disponible"
        }

        onDispose {
            sensorManager.unregisterListener(sensorEventListener)
            if (wakeLock.isHeld) {
                wakeLock.release()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(start = 10.dp)
            .swipeToNavigateBack(navController)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                // Header with logo and user icon
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp, bottom = 1.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { navController.navigate("home/$userId") }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Inicio",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        painter = painterResource(id = R.drawable.logo), // Cambia 'logo' por el nombre de tu archivo sin extensión
                        contentDescription = "Logo",
                        tint = Color.Unspecified, // Usamos Color.Unspecified para que la imagen no cambie de color
                        modifier = Modifier.size(40.dp)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(
                        onClick = { navController.navigate("profileView/$userId") }
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Mi perfil",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            item {
                Text(
                    text = "Frecuencia Cardíaca",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 8.dp)
                )
            }

            item {
                Text(
                    text = "ppm",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
            }

            item {
                Text(
                    text = heartRate.toString(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        navController.navigate("newView/$userId/Frecuencia Cardíaca: $heartRate ppm/Información relevante sobre la frecuencia cardíaca")
                    }
                ) {
                    Text(
                        text = sensorStatus,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(end = 8.dp)
                    )
                    if (sensorStatus in listOf("Normal", "Bradicardia", "Taquicardia")) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Ir a detalles",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}


@Composable
fun NewView(userId: Int, mainText: String, secondaryText: String, navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(start = 10.dp, top = 30.dp, bottom = 5.dp)
            .swipeToNavigateBack(navController)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = mainText,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
            Text(
                text = secondaryText,
                fontSize = 16.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
            Text(
                text = "Frecuencia Cardíaca Normal:",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            Text(
                text = "60-100 ppm (latidos por minuto) para adultos.",
                fontSize = 16.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            Text(
                text = "Frecuencia Cardíaca Baja (Bradicardia):",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            Text(
                text = "Menos de 60 ppm en adultos.",
                fontSize = 16.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            Text(
                text = "Frecuencia Cardíaca Alta (Taquicardia):",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            Text(
                text = "Más de 100 ppm en adultos.",
                fontSize = 16.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            IconButton(
                onClick = { navController.navigate("home/$userId") },
                modifier = Modifier
                    .padding(top = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Regresar",
                    tint = Color.White
                )
            }
        }
    }
}


@Composable
fun OxygenLevelView(navController: NavController, userId: Int) {
    val context = LocalContext.current
    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val deviceSensors: List<Sensor> = sensorManager.getSensorList(Sensor.TYPE_ALL)
    val spO2Sensor = deviceSensors.find { it.name.contains("SpO2", ignoreCase = true) }
    var oxygenLevel by remember { mutableStateOf(0) }
    var sensorStatus by remember { mutableStateOf("No iniciado") }
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    val sensorEventListener = remember {
        object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                if (event.sensor == spO2Sensor) {
                    oxygenLevel = event.values[0].toInt()
                }
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
                // Aquí puedes manejar los cambios en la precisión del sensor si es necesario
            }
        }
    }

    DisposableEffect(Unit) {
        if (spO2Sensor != null) {
            Log.d("OxygenLevelView", "Sensor de SpO2 encontrado: ${spO2Sensor.name}")
            sensorManager.registerListener(sensorEventListener, spO2Sensor, SensorManager.SENSOR_DELAY_NORMAL)
            sensorStatus = "Midiendo nivel de oxígeno..."
            coroutineScope.launch {
                delay(30000) // Medir durante 30 segundos
                sensorManager.unregisterListener(sensorEventListener)
                sensorStatus = when {
                    oxygenLevel < 90 -> "Hipoxemia grave"
                    oxygenLevel < 94 -> "Hipoxemia moderada"
                    oxygenLevel < 95 -> "Hipoxemia leve"
                    else -> "Normal"
                }
                Toast.makeText(context, "Medición ${sensorStatus.toLowerCase()}", Toast.LENGTH_SHORT).show()
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED) {
                    vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    Log.d("OxygenLevelView", "Permiso de vibración no concedido")
                }
            }
        } else {
            Log.d("OxygenLevelView", "Sensor de SpO2 no disponible")
            sensorStatus = "Sensor de SpO2 no disponible"
        }

        onDispose {
            sensorManager.unregisterListener(sensorEventListener)
            sensorStatus = "Medición detenida"
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(start = 10.dp)
            .swipeToNavigateBack(navController)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                // Header with logo and user icon
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp, bottom = 1.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { navController.navigate("home/$userId") }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Inicio",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        painter = painterResource(id = R.drawable.logo), // Cambia 'logo' por el nombre de tu archivo sin extensión
                        contentDescription = "Logo",
                        tint = Color.Unspecified, // Usamos Color.Unspecified para que la imagen no cambie de color
                        modifier = Modifier.size(40.dp)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(
                        onClick = { navController.navigate("profileView/$userId") }
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Mi perfil",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            item {
                Text(
                    text = "Nivel de Oxígeno en la Sangre",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 8.dp)
                )
            }

            item {
                Text(
                    text = "SpO2",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
            }

            item {
                Text(
                    text = oxygenLevel.toString(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        navController.navigate("oxygenInfo/$userId/Nivel de Oxígeno: $oxygenLevel/Información relevante sobre el nivel de oxígeno en la sangre")
                    }
                ) {
                    Text(
                        text = sensorStatus,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(end = 8.dp)
                    )
                    if (sensorStatus in listOf("Normal", "Hipoxemia leve", "Hipoxemia moderada", "Hipoxemia grave")) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Ir a detalles",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}


@Composable
fun OxygenInfoView(userId: Int, mainText: String, secondaryText: String, navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(start = 10.dp, top = 40.dp, bottom = 5.dp)
            .swipeToNavigateBack(navController)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = mainText,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
            Text(
                text = secondaryText,
                fontSize = 16.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
            Text(
                text = "Nivel de Oxígeno Normal:",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            Text(
                text = "95-100% de saturación.",
                fontSize = 16.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            Text(
                text = "Hipoxemia Leve:",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            Text(
                text = "90-94% de saturación.",
                fontSize = 16.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            Text(
                text = "Hipoxemia Moderada:",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            Text(
                text = "80-89% de saturación.",
                fontSize = 16.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            Text(
                text = "Hipoxemia Grave:",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            Text(
                text = "< 80% de saturación.",
                fontSize = 16.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            IconButton(
                onClick = { navController.navigate("home/$userId") },
                modifier = Modifier
                    .padding(top = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Regresar",
                    tint = Color.White
                )
            }
        }
    }
}


@Composable
fun CaloriesBurnedView(navController: NavController, userId: Int) {
    val context = LocalContext.current
    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    var caloriesBurned by remember { mutableStateOf(0) }
    var steps by remember { mutableStateOf(0) }
    var sensorStatus by remember { mutableStateOf("No iniciado") }

    val sensorEventListener = remember {
        object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                if (event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
                    steps = event.values[0].toInt()
                    caloriesBurned = calculateCaloriesBurned(steps)
                    Log.d("CaloriesBurnedView", "Pasos: $steps, Calorías quemadas: $caloriesBurned")
                }
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
                Log.d("CaloriesBurnedView", "Precisión del sensor cambiada: $accuracy")
            }
        }
    }

    DisposableEffect(Unit) {
        if (stepCounterSensor != null) {
            sensorManager.registerListener(sensorEventListener, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL)
            sensorStatus = "Midiendo quema de calorías..."
            Log.d("CaloriesBurnedView", "Sensor registrado, estado: $sensorStatus")
        } else {
            sensorStatus = "Sensor no disponible"
            Log.d("CaloriesBurnedView", "Sensor no disponible, estado: $sensorStatus")
        }

        onDispose {
            sensorManager.unregisterListener(sensorEventListener)
            sensorStatus = "Medición detenida"
            Log.d("CaloriesBurnedView", "Sensor no registrado, estado: $sensorStatus")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(start = 10.dp)
            .swipeToNavigateBack(navController)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                // Header with logo and user icon
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp, bottom = 1.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { navController.navigate("home/$userId") }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Inicio",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        painter = painterResource(id = R.drawable.logo), // Cambia 'logo' por el nombre de tu archivo sin extensión
                        contentDescription = "Logo",
                        tint = Color.Unspecified, // Usamos Color.Unspecified para que la imagen no cambie de color
                        modifier = Modifier.size(40.dp)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(
                        onClick = { navController.navigate("profileView/$userId") }
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Mi perfil",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
            item {
                Text(
                    text = "Quema de Calorías",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 8.dp)
                )
            }
            item {
                Text(
                    text = "Kcal",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
            }
            item {
                Text(
                    text = caloriesBurned.toString(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                )
            }
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                Text(
                    text = sensorStatus,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                )
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

fun calculateCaloriesBurned(steps: Int): Int {
    val caloriesPerStep = 0.04 // Aproximadamente 0.04 calorías por paso
    return (steps * caloriesPerStep).toInt()
}


@Composable
fun StepsCounterView(navController: NavController, userId: Int) {
    val context = LocalContext.current
    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    var steps by remember { mutableStateOf(0) }
    var sensorStatus by remember { mutableStateOf("No iniciado") }
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    var location by remember { mutableStateOf<Location?>(null) }

    // Function to check location permissions
    fun checkLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    val sensorEventListener = remember {
        object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                if (event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
                    steps = event.values[0].toInt()
                    Log.d("StepsCounterView", "Pasos actualizados: $steps")
                }
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
                Log.d("StepsCounterView", "Precisión del sensor cambiada: $accuracy")
            }
        }
    }

    DisposableEffect(Unit) {
        if (stepCounterSensor != null) {
            sensorManager.registerListener(sensorEventListener, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL)
            sensorStatus = "Midiendo pasos..."
            Log.d("StepsCounterView", "Sensor registrado, estado: $sensorStatus")
        } else {
            sensorStatus = "Sensor no disponible"
            Log.d("StepsCounterView", "Sensor no disponible, estado: $sensorStatus")
        }

        if (checkLocationPermission()) {
            fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                if (loc != null) {
                    location = loc
                    Log.d("StepsCounterView", "Última ubicación: $location")
                } else {
                    Log.d("StepsCounterView", "No se pudo obtener la última ubicación.")
                }
            }.addOnFailureListener {
                Log.d("StepsCounterView", "Error al obtener la última ubicación: ${it.message}")
            }
        } else {
            Log.d("StepsCounterView", "Permiso de ubicación no concedido. Solicitando permiso...")
            ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }

        onDispose {
            sensorManager.unregisterListener(sensorEventListener)
            sensorStatus = "Medición detenida"
            Log.d("StepsCounterView", "Sensor no registrado, estado: $sensorStatus")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(start = 10.dp)
            .swipeToNavigateBack(navController)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                // Header with logo and user icon
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp, bottom = 1.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { navController.navigate("home/$userId") }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Inicio",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        painter = painterResource(id = R.drawable.logo), // Cambia 'logo' por el nombre de tu archivo sin extensión
                        contentDescription = "Logo",
                        tint = Color.Unspecified, // Usamos Color.Unspecified para que la imagen no cambie de color
                        modifier = Modifier.size(40.dp)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(
                        onClick = { navController.navigate("profileView/$userId") }
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Mi perfil",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
            item {
                Text(
                    text = "Marcador de Pasos",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 8.dp)
                )
            }
            item {
                Text(
                    text = "Pasos",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
            }
            item {
                Text(
                    text = steps.toString(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                )
            }
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                Text(
                    text = sensorStatus,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                )
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}



@Composable
fun DistanceView(navController: NavController, userId: Int) {
    val context = LocalContext.current
    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    var steps by remember { mutableStateOf(0) }
    var distance by remember { mutableStateOf(0.0) }
    var sensorStatus by remember { mutableStateOf("No iniciado") }

    val sensorEventListener = remember {
        object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                if (event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
                    steps = event.values[0].toInt()
                    distance = steps * 0.762 // Calcula la distancia en metros (0.762 metros por paso)
                    Log.d("DistanceView", "Pasos: $steps, Distancia: $distance metros")
                }
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
                Log.d("DistanceView", "Precisión del sensor cambiada: $accuracy")
            }
        }
    }

    DisposableEffect(Unit) {
        if (stepCounterSensor != null) {
            sensorManager.registerListener(sensorEventListener, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL)
            sensorStatus = "Midiendo distancia..."
            Log.d("DistanceView", "Sensor registrado, estado: $sensorStatus")
        } else {
            sensorStatus = "Sensor no disponible"
            Log.d("DistanceView", "Sensor no disponible, estado: $sensorStatus")
        }

        onDispose {
            sensorManager.unregisterListener(sensorEventListener)
            sensorStatus = "Medición detenida"
            Log.d("DistanceView", "Sensor no registrado, estado: $sensorStatus")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(start = 10.dp)
            .swipeToNavigateBack(navController)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                // Header with logo and user icon
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp, bottom = 1.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { navController.navigate("home/$userId") }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Inicio",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        painter = painterResource(id = R.drawable.logo), // Cambia 'logo' por el nombre de tu archivo sin extensión
                        contentDescription = "Logo",
                        tint = Color.Unspecified, // Usamos Color.Unspecified para que la imagen no cambie de color
                        modifier = Modifier.size(40.dp)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(
                        onClick = { navController.navigate("profileView/$userId") }
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Mi perfil",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
            item {
                Text(
                    text = "Distancias",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 8.dp)
                )
            }
            item {
                Text(
                    text = "Km",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
            }

            item {
                Text(
                    text = String.format("%.2f", distance / 1000), // Convertir a kilómetros
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Text(
                    text = sensorStatus,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }


        }
    }
}




@Composable
fun WelcomeScreen(userName: String, userId: Int, navController: NavController) {

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Bienvenido, $userName",
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                coroutineScope.launch {
                    val userData = fetchUserData(userId)
                    if (userData != null) {
                        navController.navigate("home/$userId")
                    } else {
                        navController.navigate("dataEntry/$userId")
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            Text(
                text = "Continuar",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}



@Composable
fun ValueSelector(
    title: String,
    options: List<String>,
    selectedIndex: Int,
    onValueChange: (String) -> Unit
) {
    // Crear una lista con elementos vacíos al principio y al final
    val extendedOptions = listOf("") + options + listOf("")
    val scrollState = rememberLazyListState(initialFirstVisibleItemIndex = selectedIndex + 1)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(top = 16.dp)
        )
        LazyColumn(
            state = scrollState,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp) // Espaciado mínimo posible
        ) {
            itemsIndexed(extendedOptions) { index, item ->
                if (item.isNotEmpty()) {
                    Text(
                        text = item,
                        fontSize = 16.sp,
                        color = if (index == scrollState.firstVisibleItemIndex + 1) Color(0xFF60FDFF) else Color.Gray,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp), // Espaciado mínimo posible
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        Button(
            onClick = {
                val adjustedIndex = scrollState.firstVisibleItemIndex
                val selectedValue = extendedOptions[adjustedIndex + 1]
                onValueChange(selectedValue)
            },
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
            modifier = Modifier
                .size(48.dp)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Check, contentDescription = "Check", tint = Color.Black)
        }
    }
}

@Composable
fun DataEntryScreen(userId: Int, navController: NavController) {
    var currentStep by remember { mutableStateOf(0) }
    val userData = remember { mutableStateOf(UserData()) }
    val fieldsToShow = listOf("Género", "Año de Nacimiento", "Altura (cm)", "Peso (kg)")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (currentStep < fieldsToShow.size) {
            val label = fieldsToShow[currentStep]

            ValueSelector(
                title = label,
                options = when (label) {
                    "Género" -> listOf("Masculino", "Femenino")
                    "Año de Nacimiento" -> (1900..2024).map { it.toString() }
                    "Altura (cm)" -> (90..220).map { it.toString() }
                    "Peso (kg)" -> (20..220).map { it.toString() }
                    else -> listOf()
                },
                selectedIndex = when (label) {
                    "Género" -> if (userData.value.gender == "Masculino") 0 else 1
                    "Año de Nacimiento" -> (userData.value.nacido - 1900).coerceAtLeast(0)
                    "Altura (cm)" -> (userData.value.height.toInt() - 90).coerceAtLeast(0)
                    "Peso (kg)" -> (userData.value.weight.toInt() - 20).coerceAtLeast(0)
                    else -> 0
                },
                onValueChange = { selectedValue ->
                    when (label) {
                        "Género" -> userData.value = userData.value.copy(gender = selectedValue)
                        "Año de Nacimiento" -> userData.value = userData.value.copy(nacido = selectedValue.toInt())
                        "Altura (cm)" -> userData.value = userData.value.copy(height = selectedValue.toFloat())
                        "Peso (kg)" -> userData.value = userData.value.copy(weight = selectedValue.toFloat())
                    }
                    currentStep++
                    if (currentStep >= fieldsToShow.size) {
                        saveUserData(userId, userData.value) {
                            navController.navigate("home/$userId")
                        }
                    }
                }
            )
        }
    }
}

suspend fun fetchUserData(userId: Int): UserData? {
    return withContext(Dispatchers.IO) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api44.vercel.app/api/smartwatch-users/$userId")
            .build()

        try {
            val response = client.newCall(request).execute()
            val responseData = response.body?.string()
            Log.d("fetchUserData", "Response: $responseData")

            if (response.isSuccessful && !responseData.isNullOrEmpty()) {
                val json = JSONObject(responseData)
                UserData(
                    name = json.optString("nombre", ""),
                    gender = json.optString("genero", ""),
                    nacido = json.optInt("nacido", 0),
                    height = json.optDouble("altura", 0.0).toFloat(),
                    weight = json.optDouble("peso", 0.0).toFloat()
                )
            } else {
                null
            }
        } catch (e: IOException) {
            Log.e("fetchUserData", "Error fetching user data: ${e.message}")
            null
        }
    }
}

fun saveUserData(userId: Int, userData: UserData, onSaveSuccess: () -> Unit) {
    GlobalScope.launch(Dispatchers.IO) {
        val client = OkHttpClient()
        val requestBody = FormBody.Builder()
            .add("ID_usuario", userId.toString())
            .add("genero", userData.gender)
            .add("nacido", userData.nacido.toString())
            .add("altura", userData.height.toString())
            .add("peso", userData.weight.toString())
            .build()

        val request = Request.Builder()
            .url("https://api44.vercel.app/api/smartwatch-users")
            .post(requestBody)
            .build()

        try {
            val response = client.newCall(request).execute()
            val responseBodyString = response.body?.string()
            Log.d("saveUserData", "Response: $responseBodyString")

            if (response.isSuccessful) {
                Log.d("saveUserData", "Datos guardados exitosamente: ${userData}")
                withContext(Dispatchers.Main) {
                    onSaveSuccess()
                }
            } else {
                Log.e("saveUserData", "Error al guardar los datos: ${response.message}")
            }
        } catch (e: IOException) {
            Log.e("saveUserData", "Excepción al guardar los datos: ${e.message}")
        }
    }
}

@Preview
@Composable
fun PreviewGymApp() {
    Pry_GYMTheme {
        MainActivity()
    }
}
