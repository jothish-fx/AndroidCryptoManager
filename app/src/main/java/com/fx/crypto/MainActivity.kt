package com.fx.crypto

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.dataStore
import com.fx.crypto.ui.theme.AndroidCryptoSampleTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val Context.dataStore by dataStore(
        fileName = "user-settings.json",
        serializer = UserSettingsSerializer(CryptoManager())
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cryptoManager = CryptoManager()
        setContent {
            AndroidCryptoSampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    //EncryptToFileView(cryptoManager = cryptoManager)

                    var username by remember {
                        mutableStateOf("")
                    }
                    var password by remember {
                        mutableStateOf("")
                    }

                    var settings by remember {
                        mutableStateOf(UserSettings())
                    }

                    val scope = rememberCoroutineScope()

                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(32.dp)
                    ) {
                        TextField(
                            value = username,
                            onValueChange = { username = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text(text = "Username") }
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        TextField(
                            value = password,
                            onValueChange = { password = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text(text = "Password") }
                        )

                        Row {
                            Button(onClick = {
                                scope.launch {
                                    dataStore.updateData {
                                        UserSettings(
                                            username = username,
                                            password = password
                                        )
                                    }
                                }
                            }) {
                                Text(text = "Save")
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Button(onClick = {
                                scope.launch {
                                    settings = dataStore.data.first()
                                }
                            }) {
                                Text(text = "Load")
                            }

                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(text = "Encrypted Message $settings")

                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AndroidCryptoSampleTheme {
        Greeting("Android")
    }
}