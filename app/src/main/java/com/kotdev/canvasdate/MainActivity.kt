package com.kotdev.canvasdate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kotdev.canvasdate.components.BottomContentWidget
import com.kotdev.canvasdate.components.ButtonSave
import com.kotdev.canvasdate.components.TopContentWidget
import com.kotdev.canvasdate.ui.theme.CanvasDateTheme
import com.kotdev.canvasdate.widget.ScaleStyle
import com.kotdev.canvasdate.widget.WeightScale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CanvasDateTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var weight by remember {
            mutableStateOf(80)
        }
        val haptic = LocalHapticFeedback.current
        WeightScale(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp) ,
            style = ScaleStyle(
                scaleWidth = 140.dp
            ),
            onWeightChange = {
                weight = it
                when {
                    it % 10 == 0 -> {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
                }
            }
        )
        Text(
            modifier = Modifier
                .wrapContentSize()
                .padding(16.dp),
            text = "$weight Kg",
            fontSize = 32.sp
        )
        ButtonSave()
        TopContentWidget()
        BottomContentWidget()
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CanvasDateTheme {
        Greeting("Android")
    }
}