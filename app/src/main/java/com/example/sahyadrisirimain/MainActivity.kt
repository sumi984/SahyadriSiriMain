package com.example.sahyadrisirimain

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.example.sahyadrisirimain.ui.theme.SahyadriSiriMainTheme
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.database.*
import com.google.maps.android.compose.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        enableEdgeToEdge()
        setContent {
            SahyadriSiriMainTheme {
                val viewModel: WaterQualityViewModel = viewModel()
                var currentAppState by remember { mutableStateOf("splash") }
                
                LaunchedEffect(Unit) {
                    delay(2000)
                    // If user is already logged in, go straight to main map
                    currentAppState = if (auth.currentUser != null) "main" else "welcome"
                }

                when (currentAppState) {
                    "splash" -> SplashScreen()
                    "welcome" -> WelcomeScreen { currentAppState = "language" }
                    "language" -> LanguageSelectionScreen(viewModel) { currentAppState = "login" }
                    "login" -> LoginScreen(auth, this) { currentAppState = "main" }
                    "main" -> MainScaffold(viewModel)
                }
            }
        }
    }
}

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Waves,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(100.dp)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = "Sahyadri-Siri",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun WelcomeScreen(onGetStarted: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Waves, null, tint = Color.White, modifier = Modifier.size(50.dp))
                }
                Spacer(Modifier.height(24.dp))
                Text(
                    "Sahyadri-Siri",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "ಸಹ್ಯಾದ್ರಿ-ಸಿರಿ",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    "'Guardian of the Ghats' Lifeblood'",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color.Green))
                    Spacer(Modifier.width(8.dp))
                    Text("Live Ecosystem Vitality: 88%", style = MaterialTheme.typography.bodySmall)
                }
                Spacer(Modifier.height(32.dp))
                Button(
                    onClick = onGetStarted,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Get Started", fontSize = 18.sp)
                    Spacer(Modifier.width(8.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, null)
                }
                Spacer(Modifier.height(16.dp))
                Text(
                    "Community-driven conservation initiative",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun LanguageSelectionScreen(viewModel: WaterQualityViewModel, onLanguageSelected: () -> Unit) {
    val languages = listOf(
        Triple("English", "International", "en"),
        Triple("ಕನ್ನಡ (Kannada)", "State Language", "kn"),
        Triple("हिन्दी (Hindi)", "National Language", "hi"),
        Triple("मರಾಠಿ (Marathi)", "Regional Language", "mr"),
        Triple("മലയാളം (Malayalam)", "Regional Language", "ml"),
        Triple("தமிழ் (Tamil)", "Regional Language", "ta")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Box(
            modifier = Modifier.size(60.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary).align(Alignment.CenterHorizontally),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Waves, null, tint = Color.White)
        }
        Spacer(Modifier.height(32.dp))
        Text(
            "Choose your language",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(
            "Select your preferred language to continue.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )
        Spacer(Modifier.height(32.dp))
        
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(languages) { (name, sub, code) ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { 
                            viewModel.selectedLanguage = code
                            onLanguageSelected()
                        },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = if (viewModel.selectedLanguage == code) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                            Text(sub, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                        }
                        RadioButton(
                            selected = viewModel.selectedLanguage == code,
                            onClick = { 
                                viewModel.selectedLanguage = code
                                onLanguageSelected()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LoginScreen(auth: FirebaseAuth, activity: android.app.Activity, onLogin: () -> Unit) {
    var phone by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var verificationId by remember { mutableStateOf("") }
    var isOtpSent by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    
    val context = LocalContext.current
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Text("Sahyadri-Siri", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(40.dp))
        
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Lock, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(60.dp))
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            if (!isOtpSent) "Login with Mobile Number" else "Verify OTP",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            if (!isOtpSent) "Access your environmental dashboard and contribute to water stewardship."
            else "Enter the 6-digit code sent to +91 $phone",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 12.dp)
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                if (!isOtpSent) {
                    Text("Phone Number", style = MaterialTheme.typography.labelMedium)
                    Spacer(Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("+91", fontWeight = FontWeight.Bold)
                        Spacer(Modifier.width(12.dp))
                        TextField(
                            value = phone,
                            onValueChange = { if (it.length <= 10) phone = it },
                            placeholder = { Text("Enter 10-digit number") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent
                            )
                        )
                    }
                } else {
                    Text("Enter OTP", style = MaterialTheme.typography.labelMedium)
                    Spacer(Modifier.height(8.dp))
                    TextField(
                        value = otp,
                        onValueChange = { if (it.length <= 6) otp = it },
                        placeholder = { Text("X X X X X X") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent
                        ),
                        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center, letterSpacing = 8.sp)
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        if (isLoading) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        } else {
            Button(
                onClick = {
                    if (!isOtpSent) {
                        if (phone.length == 10) {
                            isLoading = true
                            val options = PhoneAuthOptions.newBuilder(auth)
                                .setPhoneNumber("+91$phone")
                                .setTimeout(60L, TimeUnit.SECONDS)
                                .setActivity(activity)
                                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                                        auth.signInWithCredential(credential).addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                onLogin()
                                            } else {
                                                Toast.makeText(context, "Verification Failed", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    }

                                    override fun onVerificationFailed(e: FirebaseException) {
                                        isLoading = false
                                        Toast.makeText(context, "Failed: ${e.message}", Toast.LENGTH_LONG).show()
                                    }

                                    override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                                        isLoading = false
                                        verificationId = id
                                        isOtpSent = true
                                        Toast.makeText(context, "OTP Sent", Toast.LENGTH_SHORT).show()
                                    }
                                })
                                .build()
                            PhoneAuthProvider.verifyPhoneNumber(options)
                        } else {
                            Toast.makeText(context, "Enter a valid 10-digit number", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        if (otp.length == 6) {
                            isLoading = true
                            val credential = PhoneAuthProvider.getCredential(verificationId, otp)
                            auth.signInWithCredential(credential).addOnCompleteListener { task ->
                                isLoading = false
                                if (task.isSuccessful) {
                                    onLogin()
                                } else {
                                    Toast.makeText(context, "Incorrect OTP", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = if (!isOtpSent) phone.length == 10 else otp.length == 6
            ) {
                Text(if (!isOtpSent) "Send OTP" else "Verify & Login")
            }
            
            if (isOtpSent) {
                TextButton(onClick = { isOtpSent = false }) {
                    Text("Change Number", color = MaterialTheme.colorScheme.primary)
                }
            }
        }
        
        Spacer(Modifier.weight(1f))
        Text(
            "SAHYADRI-SIRI ECOSYSTEM MONITORING PROJECT \u00A9 2026",
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 24.dp)
        )
    }
}

data class WikiItem(
    val title: String,
    val description: String,
    val category: String,
    val imageUrl: String = ""
)

class WaterQualityViewModel : ViewModel() {
    var selectedLanguage by mutableStateOf("en")
    var wikiSearchQuery by mutableStateOf("")

    val wikiItems = listOf(
        WikiItem(
            "Sacred Springs",
            "Hidden freshwater springs considered sacred by local communities. They often host unique micro-ecosystems.",
            "Tradition"
        ),
        WikiItem(
            "Netravathi River",
            "A major river that originates in the Western Ghats and flows through the Dakshina Kannada district.",
            "Rivers"
        ),
        WikiItem(
            "Malabar Pit Viper",
            "A venomous pit viper species endemic to the Western Ghats of India. Usually seen near streams.",
            "Fauna"
        ),
        WikiItem(
            "Myristica Swamps",
            "Ancient freshwater swamps with prehistoric trees that have stilt roots. Extremely rare ecosystem.",
            "Flora"
        ),
        WikiItem(
            "Agumbe Rainforest",
            "Known as the 'Cherrapunji of the South', it is home to many rare medicinal plants and the King Cobra.",
            "Forests"
        ),
        WikiItem(
            "Sharavathi Valley",
            "Houses the famous Jog Falls and a wildlife sanctuary known for the Lion-tailed Macaque.",
            "Rivers"
        )
    )

    fun t(en: String, kn: String = "", mr: String = "", hi: String = "", ml: String = "", ta: String = ""): String {
        return when (selectedLanguage) {
            "kn" -> if (kn.isEmpty()) en else kn
            "mr" -> if (mr.isEmpty()) en else mr
            "hi" -> if (hi.isEmpty()) en else hi
            "ml" -> if (ml.isEmpty()) en else ml
            "ta" -> if (ta.isEmpty()) en else ta
            else -> en
        }
    }

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference.child("streams")

    var streamScores = mutableStateMapOf("stream1" to 85, "stream2" to 70, "stream3" to 50, "stream4" to 30, "stream5" to 95, "stream6" to 90)
    
    val streamLocations = mapOf(
        "Sharavathi River" to LatLng(13.20, 75.15),
        "Netravathi River" to LatLng(12.94, 75.32),
        "Tunga River" to LatLng(13.15, 75.00),
        "Bhadra River" to LatLng(13.10, 75.39),
        "KSIT Demo Spring" to LatLng(12.87, 77.54),
        "Home Demo Point" to LatLng(12.866, 77.5761)
    )

    fun saveReport(streamId: String, clarity: Int, flow: String, smell: String, pollution: String) {
        val score = calculateHealthScore(clarity, flow, smell, pollution)
        updateStreamScoreById(streamId, score)
    }

    fun calculateHealthScore(clarityIndex: Int, flowRate: String, smell: String, pollution: String): Int {
        // Clarity (Max 40 pts): 0=Blue(40), 1=Cyan(30), 2=Gray(20), 3=Yellow(10), 4=Brown(0)
        val clarityScore = (4 - clarityIndex) * 10
        
        // Flow (Max 20 pts)
        val flowScore = when(flowRate) {
            "High", "ಹೆಚ್ಚು" -> 20
            "Normal", "ಸಾಮಾನ್ಯ" -> 15
            else -> 5 // Stagnant is bad for health score usually
        }
        
        // Smell (Max 20 pts)
        val smellScore = when(smell) {
            "Fresh", "ತಾಜಾ" -> 20
            "No Smell", "ವಾಸನೆ ಇಲ್ಲ" -> 15
            "Earthy", "ಮಣ್ಣಿನ ವಾಸನೆ" -> 10
            else -> 0 // Pungent/Chemical
        }
        
        // Pollution (Max 20 pts)
        val pollutionScore = when(pollution) {
            "None", "ಯಾವುದೂ ಇಲ್ಲ" -> 20
            "Natural Debris", "ನೈಸರ್ಗಿಕ ಕಸ" -> 15
            "Plastic", "ಪ್ಲಾಸ್ಟಿಕ್" -> 5
            else -> 0 // Sewage/Industrial
        }
        
        return clarityScore + flowScore + smellScore + pollutionScore
    }

    init {
        try {
            database.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        snapshot.children.forEach { child ->
                            val key = child.key ?: return@forEach
                            val score = child.value.toString().toIntOrNull() ?: return@forEach
                            streamScores[key] = score
                        }
                    } else {
                        streamScores.forEach { (key, value) -> database.child(key).setValue(value) }
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
        } catch (e: Exception) {
            // Log or handle initial Firebase failure
        }
    }

    fun updateStreamScoreById(streamId: String, newScore: Int) {
        database.child(streamId).setValue(newScore)
    }

    fun isWithinRange(currentLat: Double, currentLng: Double, streamName: String): Boolean {
        val streamLatLng = streamLocations[streamName] ?: return false
        val results = FloatArray(1)
        Location.distanceBetween(currentLat, currentLng, streamLatLng.latitude, streamLatLng.longitude, results)
        return results[0] <= 500 // 500 meters
    }
}

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Map : Screen("map", "Map", Icons.Default.Map)
    object Report : Screen("report", "Report", Icons.Default.EditNote)
    object Alerts : Screen("alerts", "Alerts", Icons.Default.Notifications)
    object Wiki : Screen("wiki", "Wiki", Icons.Default.Info)
}

val navItems = listOf(Screen.Map, Screen.Report, Screen.Alerts, Screen.Wiki)

@Composable
fun MainScaffold(waterViewModel: WaterQualityViewModel) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                navItems.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { 
                            Text(
                                waterViewModel.t(
                                    en = screen.label,
                                    kn = when(screen.route) {
                                        "map" -> "ನಕ್ಷೆ"
                                        "report" -> "ವರದಿ"
                                        "alerts" -> "ಎಚ್ಚರಿಕೆ"
                                        "wiki" -> "ವಿಕಿ"
                                        else -> screen.label
                                    },
                                    hi = when(screen.route) {
                                        "map" -> "मानचित्र"
                                        "report" -> "रिपोर्ट"
                                        "alerts" -> "अलर्ट"
                                        "wiki" -> "विकी"
                                        else -> screen.label
                                    },
                                    mr = when(screen.route) {
                                        "map" -> "नकाशा"
                                        "report" -> "अहवाल"
                                        "alerts" -> "सूचना"
                                        "wiki" -> "विकी"
                                        else -> screen.label
                                    },
                                    ml = when(screen.route) {
                                        "map" -> "മാപ്പ്"
                                        "report" -> "റിപ്പോർട്ട്"
                                        "alerts" -> "അലേർട്ടുകൾ"
                                        "wiki" -> "വിക്കി"
                                        else -> screen.label
                                    },
                                    ta = when(screen.route) {
                                        "map" -> "வரைபடம்"
                                        "report" -> "அறிக்கை"
                                        "alerts" -> "எச்சரிக்கைகள்"
                                        "wiki" -> "விக்கி"
                                        else -> screen.label
                                    }
                                )
                            ) 
                        },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController, startDestination = Screen.Map.route, modifier = Modifier.padding(innerPadding)) {
            composable(Screen.Map.route) { MapScreen(waterViewModel) }
            composable(Screen.Report.route) { ReportScreen(waterViewModel) }
            composable(Screen.Alerts.route) { AlertsScreen(waterViewModel) }
            composable(Screen.Wiki.route) { WikiScreen(waterViewModel) }
        }
    }
}

@Composable
fun MapScreen(viewModel: WaterQualityViewModel) {
    val netravathiCenter = LatLng(12.95, 75.30)
    // Initial camera zoom out to show both Western Ghats and Bangalore demo points
    val cameraPositionState = rememberCameraPositionState { position = CameraPosition.fromLatLngZoom(netravathiCenter, 7f) }
    val context = LocalContext.current
    val scores = viewModel.streamScores

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(zoomControlsEnabled = true)
    ) {
        // 1. Sharavathi River (stream1)
        val sharavathiPath = listOf(LatLng(13.15, 75.10), LatLng(13.20, 75.15), LatLng(13.25, 75.20))
        StreamPolyline(sharavathiPath, scores["stream1"] ?: 0) { score ->
            Toast.makeText(context, "${viewModel.t(en = "Sharavathi River Health", kn = "ಶರಾವತಿ ನದಿ ಆರೋಗ್ಯ")}: $score", Toast.LENGTH_SHORT).show()
        }
        Marker(state = MarkerState(position = LatLng(13.20, 75.15)), title = viewModel.t(en = "Sharavathi River", kn = "ಶರಾವತಿ ನದಿ"))
        
        // 2. Netravathi River (stream2)
        val netravathiPath = listOf(LatLng(12.85, 75.35), LatLng(12.91, 75.31), LatLng(12.97, 75.29), LatLng(13.03, 75.24))
        StreamPolyline(netravathiPath, scores["stream2"] ?: 0) { score ->
            Toast.makeText(context, "${viewModel.t(en = "Netravathi River Health", kn = "ನೇತ್ರಾವತಿ ನದಿ ಆರೋಗ್ಯ")}: $score", Toast.LENGTH_SHORT).show()
        }
        Marker(state = MarkerState(position = LatLng(12.94, 75.32)), title = viewModel.t(en = "Netravathi River", kn = "ನೇತ್ರಾವತಿ ನದಿ"))

        // 3. Tunga River (stream3)
        val tungaPath = listOf(LatLng(13.10, 74.95), LatLng(13.15, 75.00), LatLng(13.20, 75.05))
        StreamPolyline(tungaPath, scores["stream3"] ?: 0) { score ->
            Toast.makeText(context, "${viewModel.t(en = "Tunga River Health", kn = "ತುಂಗಾ ನದಿ ಆರೋಗ್ಯ")}: $score", Toast.LENGTH_SHORT).show()
        }
        Marker(state = MarkerState(position = LatLng(13.15, 75.00)), title = viewModel.t(en = "Tunga River", kn = "ತುಂಗಾ ನದಿ"))

        // 4. Bhadra River (stream4)
        val bhadraPath = listOf(LatLng(13.05, 75.35), LatLng(13.10, 75.39), LatLng(13.14, 75.43))
        StreamPolyline(bhadraPath, scores["stream4"] ?: 0) { score ->
            Toast.makeText(context, "${viewModel.t(en = "Bhadra River Health", kn = "ಭದ್ರಾ ನದಿ ಆರೋಗ್ಯ")}: $score", Toast.LENGTH_SHORT).show()
        }
        Marker(state = MarkerState(position = LatLng(13.10, 75.39)), title = viewModel.t(en = "Bhadra River", kn = "ಭದ್ರಾ ನದಿ"))

        // 5. KSIT Demo Spring (stream5)
        val demoPath = listOf(LatLng(12.87, 77.54), LatLng(12.872, 77.542))
        StreamPolyline(demoPath, scores["stream5"] ?: 0) { score ->
            Toast.makeText(context, "KSIT Demo Spring Health: $score", Toast.LENGTH_SHORT).show()
        }
        Marker(state = MarkerState(position = LatLng(12.87, 77.54)), title = "KSIT Demo Spring")

        // 6. Home Demo Point (stream6)
        val homePath = listOf(LatLng(12.866, 77.5761), LatLng(12.8665, 77.5766))
        StreamPolyline(homePath, scores["stream6"] ?: 0) { score ->
            Toast.makeText(context, "Home Demo Point Health: $score", Toast.LENGTH_SHORT).show()
        }
        Marker(state = MarkerState(position = LatLng(12.866, 77.5761)), title = "Home Demo Point")
    }
}

@Composable
fun StreamPolyline(points: List<LatLng>, score: Int, onClick: (Int) -> Unit) {
    val color = when { 
        score >= 80 -> Color.Blue 
        score >= 60 -> Color.Green 
        score >= 40 -> Color(0xFFFBC02D) 
        else -> Color(0xFF795548) 
    }
    Polyline(points = points, color = color, width = 12f, clickable = true, onClick = { onClick(score) })
}

@Composable
fun ReportScreen(viewModel: WaterQualityViewModel) {
    val isKN = viewModel.selectedLanguage == "kn"
    var expanded by remember { mutableStateOf(false) }
    var selectedStream by remember { mutableStateOf("Sharavathi River") }
    var clarity by remember { mutableIntStateOf(2) }
    var flowRate by remember { mutableStateOf(viewModel.t(en = "Normal", kn = "ಸಾಮಾನ್ಯ")) }
    var smell by remember { mutableStateOf(viewModel.t(en = "No Smell", kn = "ವಾಸನೆ ಇಲ್ಲ")) }
    var pollution by remember { mutableStateOf(viewModel.t(en = "None", kn = "ಯಾವುದೂ ಇಲ್ಲ")) }
    var description by remember { mutableStateOf("") }
    var isAnonymous by remember { mutableStateOf(false) }
    var liveLocationStatus by remember { mutableStateOf(viewModel.t(en = "GPS Idle", kn = "ಜಪಿಎಸ್ ಸ್ಥಿತಿ")) }
    var locationVerified by remember { mutableStateOf(false) }
    var currentCoords by remember { mutableStateOf("0.0° N, 0.0° E") }
    
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        ) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    val clarities = listOf(Color.Blue, Color.Cyan, Color.LightGray, Color(0xFFFBC02D), Color(0xFF795548))
    val streams = listOf("Sharavathi River", "Netravathi River", "Tunga River", "Bhadra River", "KSIT Demo Spring", "Home Demo Point")

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(24.dp)) {
        item {
            Text(
                text = viewModel.t(en = "Stream Report", kn = "ತೊರೆ ವರದಿ"),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        
        // Stream Selection
        item {
            Column {
                Text(viewModel.t(en = "1. Select Stream", kn = "೧. ನದಿಯನ್ನು ಆರಿಸಿ"), fontWeight = FontWeight.Bold)
                Box(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                    OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                        Text(selectedStream)
                        Icon(Icons.Default.ArrowDropDown, null)
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        streams.forEach { streamName ->
                            DropdownMenuItem(
                                text = { Text(streamName) },
                                onClick = { selectedStream = streamName; expanded = false; locationVerified = false; liveLocationStatus = viewModel.t(en = "GPS Idle", kn = "ಜಪಿಎಸ್ ಸ್ಥಿತಿ") }
                            )
                        }
                    }
                }
            }
        }

        // Water Clarity
        item {
            Column {
                Text(viewModel.t(en = "2. Water Clarity", kn = "೨. ನೀರಿನ ಸ್ಪಷ್ಟತೆ"), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text(viewModel.t(en = "How clear does the water look today?", kn = "ಇಂದು ನೀರು ಎಷ್ಟು ಸ್ಪಷ್ಟವಾಗಿ ಕಾಣುತ್ತದೆ?"), style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                Spacer(Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    clarities.forEachIndexed { index, color ->
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(color.copy(alpha = if (clarity == index) 1f else 0.3f))
                                .border(if (clarity == index) 2.dp else 0.dp, MaterialTheme.colorScheme.primary, CircleShape)
                                .clickable { clarity = index }
                        )
                    }
                }
            }
        }
        
        // Flow Rate
        item {
            Column {
                Text(viewModel.t(en = "3. Flow Rate", kn = "೩. ಹರಿವಿನ ಪ್ರಮಾಣ"), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    val flowOptions = listOf(
                        viewModel.t(en = "High", kn = "ಹೆಚ್ಚು"), 
                        viewModel.t(en = "Normal", kn = "ಸಾಮಾನ್ಯ"), 
                        viewModel.t(en = "Stagnant", kn = "ಸ್ಥಗಿತ")
                    )
                    flowOptions.forEach { rate ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(60.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (flowRate == rate) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.White)
                                .border(1.dp, if (flowRate == rate) MaterialTheme.colorScheme.primary else Color.LightGray, RoundedCornerShape(12.dp))
                                .clickable { flowRate = rate },
                            contentAlignment = Alignment.Center
                        ) { Text(rate, color = if (flowRate == rate) MaterialTheme.colorScheme.primary else Color.Black) }
                    }
                }
            }
        }

        // Water Smell
        item {
            Column {
                Text(viewModel.t(en = "4. Water Smell", kn = "೪. ನೀರಿನ ವಾಸನೆ"), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(12.dp))
                val smellOptions = listOf(
                    viewModel.t(en = "Fresh", kn = "ತಾಜಾ"), 
                    viewModel.t(en = "No Smell", kn = "ವಾಸನೆ ಇಲ್ಲ"), 
                    viewModel.t(en = "Pungent", kn = "ತೀವ್ರ")
                )
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    smellOptions.forEach { option ->
                        FilterChip(
                            selected = smell == option,
                            onClick = { smell = option },
                            label = { Text(option) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // Visible Pollution
        item {
            Column {
                Text(viewModel.t(en = "5. Visible Pollution", kn = "೫. ಕಣ್ಣಿಗೆ ಕಾಣುವ ಮಾಲಿನ್ಯ"), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(12.dp))
                val pollutionOptions = listOf(
                    viewModel.t(en = "None", kn = "ಯಾವುದೂ ಇಲ್ಲ"), 
                    viewModel.t(en = "Plastic", kn = "ಪ್ಲಾಸ್ಟಿಕ್"), 
                    viewModel.t(en = "Sewage", kn = "ಚರಂಡಿ ನೀರು")
                )
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    pollutionOptions.forEach { option ->
                        FilterChip(
                            selected = pollution == option,
                            onClick = { pollution = option },
                            label = { Text(option) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // GPS Verification
        item {
            Column {
                Text(viewModel.t(en = "6. GPS Verification", kn = "೬. ಸ್ಥಳ ಪರಿಶೀಲನೆ"), fontWeight = FontWeight.Bold)
                Card(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(liveLocationStatus, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                            Text(currentCoords, style = MaterialTheme.typography.labelSmall)
                        }
                        Button(onClick = { 
                            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                locationPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
                            } else {
                                liveLocationStatus = viewModel.t(en = "Fetching...", kn = "ಪಡೆಯಲಾಗುತ್ತಿದೆ...")
                                fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, CancellationTokenSource().token)
                                    .addOnSuccessListener { location ->
                                        if (location != null) {
                                            currentCoords = "${String.format("%.2f", location.latitude)}° N, ${String.format("%.2f", location.longitude)}° E"
                                            if (viewModel.isWithinRange(location.latitude, location.longitude, selectedStream)) {
                                                locationVerified = true
                                                liveLocationStatus = viewModel.t(en = "Location Verified ✓", kn = "ಸ್ಥಳ ದೃಢೀಕರಿಸಲಾಗಿದೆ ✓")
                                            } else {
                                                locationVerified = false
                                                liveLocationStatus = viewModel.t(en = "Too far from stream!", kn = "ತೊರೆಯಿಂದ ಬಹಳ ದೂರವಿದೆ!")
                                                Toast.makeText(context, "Please be within 500m of the stream", Toast.LENGTH_LONG).show()
                                            }
                                        } else {
                                            liveLocationStatus = viewModel.t(en = "GPS Error", kn = "ಜಪಿಎಸ್ ದೋಷ")
                                        }
                                    }
                            }
                        }) {
                            Text(viewModel.t(en = "Fetch", kn = "ಪಡೆಯಿರಿ"))
                        }
                    }
                }
            }
        }

        item {
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text(viewModel.t(en = "Observations", kn = "ವೀಕ್ಷಣೆಗಳು")) },
                placeholder = { Text(viewModel.t(en = "Describe any unusual smells...", kn = "ಯಾವುದೇ ಅಸಾಮಾನ್ಯ ವಾಸನೆಯನ್ನು ವಿವರಿಸಿ...")) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
        }

        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Switch(checked = isAnonymous, onCheckedChange = { isAnonymous = it })
                Spacer(Modifier.width(12.dp))
                Text(viewModel.t(en = "Report Anonymously", kn = "ಅನಾಮಧೇಯವಾಗಿ ವರದಿ ಮಾಡಿ"))
            }
        }

        item {
            Button(
                onClick = { 
                    if (!locationVerified) {
                        Toast.makeText(context, "Please verify GPS location first", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    val streamId = when(selectedStream) {
                        "Sharavathi River" -> "stream1"
                        "Netravathi River" -> "stream2"
                        "Tunga River" -> "stream3"
                        "Bhadra River" -> "stream4"
                        "KSIT Demo Spring" -> "stream5"
                        else -> "stream6"
                    }
                    viewModel.saveReport(streamId, clarity, flowRate, smell, pollution)
                    Toast.makeText(context, viewModel.t(en = "SYNCING TO CLOUD...", kn = "ಕ್ಲೌಡ್‌ಗೆ ಉಳಿಸಲಾಗುತ್ತಿದೆ..."), Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = locationVerified
            ) {
                Text(viewModel.t(en = "SYNC TO CLOUD", kn = "ಕ್ಲೌಡ್‌ಗೆ ಉಳಿಸಿ"))
                Spacer(Modifier.width(8.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, null)
            }
        }
        item { Spacer(modifier = Modifier.height(32.dp)) }
    }
}

@Composable
fun AlertsScreen(viewModel: WaterQualityViewModel) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(viewModel.t(en = "Alert Feed", kn = "ಎಚ್ಚರಿಕೆ ಫೀಡ್"), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text(viewModel.t(en = "Real-time ecological pulse of the Western Ghats river systems.", kn = "ಪಶ್ಚಿಮ ಘಟ್ಟಗಳ ನದಿ ವ್ಯವಸ್ಥೆಗಳ ನೈಜ-ಸಮಯದ ಪರಿಸರ ನಾಡಿಮಿಡಿತ."), style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        Spacer(Modifier.height(24.dp))
        
        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Warning, null, tint = Color(0xFF92400E))
                            Spacer(Modifier.width(8.dp))
                            Text(viewModel.t(en = "URGENT ADVISORY", kn = "ತುರ್ತು ಸಲಹೆ"), fontWeight = FontWeight.Bold, color = Color(0xFF92400E))
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(viewModel.t(en = "Sudden turbidity seen in Netravathi River tributary - Please check before using.", kn = "ನೇತ್ರಾವತಿ ನದಿಯ ಉಪನದಿಯಲ್ಲಿ ಹಠಾತ್ ಮಣ್ಣಿನಂಶ ಕಂಡುಬಂದಿದೆ - ದಯವಿಟ್ಟು ಬಳಸುವ ಮೊದಲು ಪರಿಶೀಲಿಸಿ."), fontWeight = FontWeight.Bold)
                        Text(viewModel.t(en = "Upper Stream 2, Sakleshpur Range", kn = "ಮೇಲಿನ ಹರಿವು ೨, ಸಕಲೇಶಪುರ ಶ್ರೇಣಿ"), style = MaterialTheme.typography.labelSmall)
                        Spacer(Modifier.height(12.dp))
                        Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black)) { 
                            Text(viewModel.t(en = "Map It", kn = "ನಕ್ಷೆಯಲ್ಲಿ ನೋಡಿ")) 
                        }
                    }
                }
            }
            
            item {
                Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(viewModel.t(en = "WATER QUALITY UPDATE", kn = "ನೀರಿನ ಗುಣಮಟ್ಟ ನವೀಕರಣ"), color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                        Text(viewModel.t(en = "Nitrate levels returning to baseline near Agumbe foothills.", kn = "ಆಗುಂಬೆ ಪಾದದ ಬಳಿ ನೈಟ್ರೇಟ್ ಮಟ್ಟಗಳು ಸಹಜ ಸ್ಥಿತಿಗೆ ಮರಳುತ್ತಿವೆ."), fontWeight = FontWeight.Bold)
                        Text(viewModel.t(en = "Someshwara Wildlife Sanctuary Perimeter", kn = "ಸೋಮೇಶ್ವರ ವನ್ಯಜีವಿ ಅಭಯಾರಣ್ಯದ ಸುತ್ತಳತೆ"), style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
    }
}

@Composable
fun WikiScreen(viewModel: WaterQualityViewModel) {
    val filteredItems = viewModel.wikiItems.filter {
        it.title.contains(viewModel.wikiSearchQuery, ignoreCase = true) ||
        it.description.contains(viewModel.wikiSearchQuery, ignoreCase = true) ||
        it.category.contains(viewModel.wikiSearchQuery, ignoreCase = true)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text(viewModel.t(en = "Ecosystem Wiki", kn = "ಪರಿಸರ ವಿಕಿ"), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text(viewModel.t(en = "Discover why the crystalline streams of the Ghats are the lifeblood of our peninsula.", kn = "ಘಟ್ಟಗಳ ಸ್ಫಟಿಕ ಸ್ಪಷ್ಟ ತೊರೆಗಳು ನಮ್ಮ ದ್ವೀಪಕಲ್ಪದ ಜೀವನನಾಡಿ ಏಕೆ ಎಂದು ಅನ್ವೇಷಿಸಿ."), style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                Spacer(Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = viewModel.wikiSearchQuery,
                    onValueChange = { viewModel.wikiSearchQuery = it },
                    placeholder = { Text(viewModel.t(en = "Search for species, water courses...", kn = "ಜಾತಿಗಳು, ಜಲಮೂಲಗಳಿಗಾಗಿ ಹುಡುಕಿ...")) },
                    leadingIcon = { Icon(Icons.Default.Search, null) },
                    trailingIcon = {
                        if (viewModel.wikiSearchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.wikiSearchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear")
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }
        }
        
        if (viewModel.wikiSearchQuery.isEmpty()) {
            item {
                Box(modifier = Modifier.fillMaxWidth().height(200.dp).clip(RoundedCornerShape(16.dp))) {
                    AsyncImage(
                        model = "https://images.unsplash.com/photo-1542332213-9b5a5a3fad35?auto=format&fit=crop&q=80&w=1000",
                        contentDescription = "Sacred Springs",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                                    startY = 300f
                                )
                            )
                    )
                    Column(modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)) {
                        Box(modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(Color.Green.copy(alpha = 0.5f)).padding(horizontal = 8.dp, vertical = 4.dp)) {
                            Text(viewModel.t(en = "Flora & Tradition", kn = "ಸಸ್ಯಸಂಕುಲ ಮತ್ತು ಸಂಪ್ರದಾಯ"), color = Color.White, fontSize = 10.sp)
                        }
                        Text(viewModel.t(en = "Sacred Springs", kn = "ಪವಿತ್ರ ಬುಗ್ಗೆಗಳು"), color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineSmall)
                    }
                }
            }

            item {
                Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                                Text(viewModel.t(en = "Health Trend (Last 7 Days)", kn = "ಆರೋಗ್ಯ ಪ್ರವೃತ್ತಿ (ಕಳೆದ ೭ ದಿನಗಳು)"), fontWeight = FontWeight.Bold)
                                Spacer(Modifier.height(16.dp))
                                
                                // Simplified Custom Chart (Stable for Demo)
                                Row(
                                    modifier = Modifier.fillMaxWidth().height(100.dp),
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    verticalAlignment = Alignment.Bottom
                                ) {
                                    val data = listOf(0.8f, 0.75f, 0.88f, 0.85f, 0.9f, 0.82f, 0.95f)
                                    data.forEach { value ->
                                        Box(
                                            modifier = Modifier
                                                .width(20.dp)
                                                .fillMaxHeight(value)
                                                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                                .background(MaterialTheme.colorScheme.primary)
                                        )
                                    }
                                }
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                                    listOf("M", "T", "W", "T", "F", "S", "S").forEach { day ->
                                        Text(day, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                                    }
                                }
                    }
                }
            }

            item {
                Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(viewModel.t(en = "Vitality Index", kn = "ಜೀವಂತಿಕೆ ಸೂಚ್ಯಂಕ"), fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(12.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(viewModel.t(en = "Stream Clarity", kn = "ತೊರೆಯ ಸ್ಪಷ್ಟತೆ"), modifier = Modifier.weight(1f))
                            Text("88%", fontWeight = FontWeight.Bold)
                        }
                        LinearProgressIndicator(progress = { 0.88f }, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp))
                    }
                }
            }
        }

        items(filteredItems) { item ->
            WikiCard(item.title, item.description, item.category)
        }
        
        item { Spacer(modifier = Modifier.height(32.dp)) }
    }
}


@Composable
fun WikiCard(title: String, desc: String, category: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(category, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
            }
            Spacer(Modifier.height(8.dp))
            Text(title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            Text(desc, style = MaterialTheme.typography.bodyMedium, color = Color.DarkGray)
        }
    }
}
