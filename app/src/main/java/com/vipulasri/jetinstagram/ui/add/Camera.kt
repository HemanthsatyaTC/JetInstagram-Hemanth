package com.vipulasri.jetinstagram.ui.add

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat


@Composable
fun RequestCameraPermission(onPermissionGranted: () -> Unit) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            onPermissionGranted()
        } else {
            Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) -> {
                onPermissionGranted() // Permission already granted
            }
            else -> {
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }
}

@Composable
fun OpenCameraWithPermission() {
    var isPermissionGranted by remember { mutableStateOf(false) }

    // Check and request camera permission
    RequestCameraPermission(onPermissionGranted = {
        isPermissionGranted = true
    })

    // Once permission is granted, open the camera
    if (isPermissionGranted) {
        OpenCamera()
    }
}

@Composable
fun OpenCamera() {
    val context = LocalContext.current
    val cameraIntent = remember { Intent(MediaStore.ACTION_IMAGE_CAPTURE) }

    // Start the camera intent
    if (cameraIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(cameraIntent)
    }
}