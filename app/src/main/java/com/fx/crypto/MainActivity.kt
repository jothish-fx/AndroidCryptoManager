package com.fx.crypto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fx.crypto.ui.theme.AndroidCryptoSampleTheme
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class MainActivity : ComponentActivity() {
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
                    var messageToEncrypt by remember {
                        mutableStateOf("")
                    }

                    var messageToDecrypt by remember {
                        mutableStateOf("")
                    }

                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(32.dp)
                    ) {
                        TextField(
                            value = messageToEncrypt,
                            onValueChange = { messageToEncrypt = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text(text = "Encrypt String") }
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row {
                            Button(onClick = {
                                val bytes = messageToEncrypt.encodeToByteArray()
                                val file = File(filesDir, "secret.txt")
                                if (!file.exists()) {
                                    file.createNewFile()
                                }
                                val fos = FileOutputStream(file)

                                messageToDecrypt = cryptoManager.encrypt(
                                    bytes, fos
                                ).decodeToString()
                            }) {
                                Text(text = "Encrypt")
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Button(onClick = {
                                val file = File(filesDir, "secret.txt")
                                messageToEncrypt = cryptoManager.decrypt(
                                    inputStream = FileInputStream(file)
                                ).decodeToString()
                            }) {
                                Text(text = "Decrypt")
                            }

                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(text = "Encrypted Message $messageToDecrypt")

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