package com.duoc.clase6stateflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.duoc.clase6stateflow.ui.OrdenScreen
import com.duoc.clase6stateflow.ui.theme.Clase6StateFlowTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Clase6StateFlowTheme {
                OrdenScreen()
            }
        }
    }
}
