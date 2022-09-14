package com.fx.crypto.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fx.crypto.CryptoManager
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

@Composable
fun EncryptToFileView(cryptoManager: CryptoManager) {
    val filesDir = LocalContext.current.filesDir
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

@Preview(showBackground = true)
@Composable
fun EncryptToFileViewPreview() {
    val cryptoManager by remember {
        mutableStateOf(CryptoManager())
    }
    EncryptToFileView(cryptoManager = cryptoManager)
}