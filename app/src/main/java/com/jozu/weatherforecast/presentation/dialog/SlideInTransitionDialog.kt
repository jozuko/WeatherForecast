package com.jozu.weatherforecast.presentation.dialog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

/**
 *
 * Created by jozuko on 2023/07/09.
 * Copyright (c) 2023 Studio Jozu. All rights reserved.
 */
const val ANIMATION_TIME = 300L

@Composable
fun SlideInTransitionDialog(
    onDismissRequest: () -> Unit,
    dismissOnBackPress: Boolean = true,
    content: @Composable (SlideInTransitionDialogHelper) -> Unit,
) {
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val showContentState = remember { MutableTransitionState(false) }
    val mutex = remember { Mutex(true) }

    LaunchedEffect(key1 = Unit) {
        launch {
            mutex.withLock {
                showContentState.targetState = true
            }
        }
    }

    Dialog(
        onDismissRequest = {
            coroutineScope.launch {
                startDismissWithExitAnimation(showContentState, onDismissRequest)
            }
        },
        properties = DialogProperties(dismissOnBackPress = dismissOnBackPress),
    ) {
        LaunchedEffect(key1 = Unit) {
            if (mutex.isLocked) {
                mutex.unlock()
            }
        }

        SlideInTransition(visibleState = showContentState) {
            content(
                SlideInTransitionDialogHelper(
                    onStartDismissWithExitAnimation = { onCompletedAnimation ->
                        coroutineScope.launch {
                            startDismissWithExitAnimation(showContentState, onDismissRequest)
                            onCompletedAnimation?.invoke()
                        }
                    },
                ),
            )
        }
    }
}

@Composable
fun SlideInTransition(
    visibleState: MutableTransitionState<Boolean>,
    content: @Composable AnimatedVisibilityScope.() -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent),
        contentAlignment = Alignment.Center,
    ) {
        AnimatedVisibility(
            visibleState = visibleState,
            enter = slideInVertically(
                animationSpec = tween(
                    durationMillis = ANIMATION_TIME.toInt(),
                    easing = LinearEasing,
                ),
                initialOffsetY = { fullHeight -> fullHeight },
            ),
            exit = slideOutVertically(
                animationSpec = tween(
                    durationMillis = ANIMATION_TIME.toInt(),
                    easing = LinearEasing,
                ),
                targetOffsetY = { fullHeight -> fullHeight },
            ),
            content = content,
        )
    }
}

private suspend fun startDismissWithExitAnimation(
    showContentState: MutableTransitionState<Boolean>,
    onDismissRequest: () -> Unit,
) = withContext(Dispatchers.IO) {
    showContentState.targetState = false

    while (true) {
        delay(10L)
        if (!showContentState.targetState && showContentState.isIdle) {
            onDismissRequest.invoke()
            break
        }
    }
}

class SlideInTransitionDialogHelper(
    private val onStartDismissWithExitAnimation: ((() -> Unit)?) -> Unit,
) {
    fun triggerAnimatedClose(onCompletedAnimation: (() -> Unit)? = null) {
        onStartDismissWithExitAnimation.invoke(onCompletedAnimation)
    }
}