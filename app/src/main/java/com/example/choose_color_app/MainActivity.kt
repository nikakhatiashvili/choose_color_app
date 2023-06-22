package com.example.choose_color_app

import android.os.Bundle
import android.util.Log.d
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.choose_color_app.ui.theme.Choose_color_appTheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Choose_color_appTheme {
                // A surface container using the 'background' color from the theme
                val circleColor = remember { mutableStateOf(Color.Blue) }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = circleColor.value
                ) {
                    SwitchView(Modifier,circleColor)
                }
            }
        }
    }
}


@Composable
fun SwitchView(
    modifier: Modifier = Modifier,
    circleColor: MutableState<Color>,
) {

    var circleOffsetX by remember { mutableStateOf(0f) }
    var circleOffsetY by remember { mutableStateOf(0f) }
    val sz = LocalConfiguration.current.screenWidthDp
    var max by remember{ mutableStateOf(0f) }
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        Box(
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
        ) {
            Box(
                modifier = Modifier.height(25.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .fillMaxWidth()
                        .height(20.dp)
                        .background(color = Color.Gray)
                        .onSizeChanged { max = it.width.toFloat() }
                ) {
                }
                DragSample { x, y,size,parent ->
                    circleColor.value = getCircleColor(x,y,size,parent)
                }
            }
        }
    }
}

@Composable
private fun DragSample(
    onPositionChanged: (Float, Float,IntSize,Int) -> Unit
) {

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        var offsetX by remember { mutableStateOf(0f) }
        var offsetY by remember { mutableStateOf(0f) }

        val parentWidth = constraints.maxWidth
        val parentHeight = constraints.maxHeight
        Box(
            Modifier
                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                .background(Color.Blue, CircleShape)
                .clip(RoundedCornerShape(20))
                .width(30.dp)
                .height(25.dp)
                .align(Alignment.TopStart)
                .pointerInput(Unit) {
                    val boxSize = this.size
                    detectDragGestures { _, dragAmount ->
                        offsetX = (offsetX + dragAmount.x).coerceIn(
                            0f,
                            parentWidth - boxSize.width.toFloat()
                        )
                        offsetY = (offsetY + dragAmount.y).coerceIn(
                            0f,
                            parentHeight - boxSize.height.toFloat()
                        )
                        onPositionChanged(offsetX, offsetY,boxSize,parentWidth)
                    }
                }
        ) {
            // todo
        }
    }
}

private fun getCircleColor(offsetX: Float,y:Float,size: IntSize,parentWidth:Int): Color {
    val startHue = y
    val endHue = offsetX

    val normalizedOffsetX = offsetX / (parentWidth - size.width)
    val hue = normalizedOffsetX * 360f

    return Color.Companion.hsv(hue, saturation = 1f, value = 1f)
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Choose_color_appTheme {
//        SwitchView(circleColor = circleColor)
    }
}