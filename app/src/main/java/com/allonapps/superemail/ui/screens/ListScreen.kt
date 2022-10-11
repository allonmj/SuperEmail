package com.allonapps.superemail.ui.screens

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.SnackbarHostState
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.allonapps.superemail.R
import com.allonapps.superemail.model.Email
import com.allonapps.superemail.ui.theme.SuperDark
import com.allonapps.superemail.ui.theme.SuperGrey
import com.allonapps.superemail.ui.theme.SuperWhite
import com.allonapps.superemail.ui.theme.Typography
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun EmailScreen(
    uiState: ListViewModel.ListUiState,
    acceptIntent: (ListViewModel.ListIntent) -> Unit
) {

    val bottomBarHeight = 176.dp
    val topBarHeight = 64.dp
    val bottomBarHeightPx = with(LocalDensity.current) { bottomBarHeight.roundToPx().toFloat() }
    val topBarHeightPx = with(LocalDensity.current) { topBarHeight.roundToPx().toFloat() }


    val bottomBarOffsetHeightPx = remember { mutableStateOf(0f) }
    val topBarrOffsetHeightPx = remember { mutableStateOf(0f) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {

                val delta = available.y

                val newBottomOffset = bottomBarOffsetHeightPx.value + delta
                bottomBarOffsetHeightPx.value = newBottomOffset.coerceIn(-bottomBarHeightPx, 0f)

                val newTopOffset = topBarrOffsetHeightPx.value + (delta / 2)
                topBarrOffsetHeightPx.value = newTopOffset.coerceIn(-topBarHeightPx, 0f)

                return Offset.Zero
            }
        }
    }

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded }
    )
    val coroutineScope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = { ShortCutSheet() },
        modifier = Modifier.fillMaxSize()
    ) {
        TapDetector(listener = {
            coroutineScope.launch {
                if (sheetState.isVisible) sheetState.hide()
                else sheetState.show()
            }
        }, modifier = Modifier.fillMaxSize()) {
            BackdropScaffold(modifier = Modifier.nestedScroll(nestedScrollConnection),
                appBar = {
                }, backLayerContent = {
                    SearchHeader()
                },
                scaffoldState = rememberBackdropScaffoldState(
                    initialValue = BackdropValue.Concealed,
                    animationSpec = SpringSpec(
                        stiffness = Spring.StiffnessHigh,
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        visibilityThreshold = .25f
                    ),
                    confirmStateChange = {
                        println("state changed $it")
                        true
                    },
                    snackbarHostState = SnackbarHostState()
                ),
                backLayerBackgroundColor = SuperDark,
                peekHeight = 0.dp,
                persistentAppBar = false,
                frontLayerShape = RectangleShape,
                frontLayerScrimColor = Color.Unspecified,
                frontLayerContent = {
                    when (uiState) {
                        is ListViewModel.ListUiState.DataUiState -> ListData(
                            uiState = uiState,
                            bottomBarHeight = bottomBarHeight,
                            bottomBarOffset = IntOffset(
                                x = 0,
                                y = -bottomBarOffsetHeightPx.value.roundToInt()
                            ),
                            topBarOffset = IntOffset(
                                x = 0,
                                y = topBarrOffsetHeightPx.value.roundToInt()
                            )
                        )
                        is ListViewModel.ListUiState.ErrorUiState -> Error(uiState)
                        is ListViewModel.ListUiState.LoadingUiState -> Loading(uiState)
                    }
                }
            ) {

            }
        }
    }
}

@Composable
fun SearchHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = SuperDark)
            .padding(vertical = 32.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            painter = painterResource(id = R.drawable.ic_search),
            contentDescription = "questions",
            tint = SuperWhite
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            stringResource(id = R.string.search),
            style = Typography.titleMedium.copy(color = SuperWhite)
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListAppBar(scrollBehavior: TopAppBarScrollBehavior? = null, offset: IntOffset) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .offset { offset },
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {}) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_question),
                    contentDescription = "questions"
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = {}) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_write),
                    contentDescription = "questions"
                )
            }
        }
    }
}

@Composable
fun ListBottomBar(offset: IntOffset, modifier: Modifier = Modifier) {
    Column(modifier = modifier.offset { offset }) {
        Row {
            Spacer(modifier = Modifier.weight(1f))
            FloatingActionButton(
                modifier = Modifier.size(64.dp),
                onClick = { /*TODO*/ },
                shape = CircleShape, containerColor = MaterialTheme.colorScheme.primary
            ) {
            }
            Spacer(modifier = Modifier.width(16.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))
        BottomAppBar(
            tonalElevation = 16.dp,
            modifier = modifier
                .height(96.dp)
        ) {
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .background(color = MaterialTheme.colorScheme.background)
            ) {
                Text("Inbox", style = Typography.headlineSmall)
            }
        }
    }

}

@Composable
fun Loading(uiState: ListViewModel.ListUiState.LoadingUiState) {

}

@Composable
fun Error(uiState: ListViewModel.ListUiState.ErrorUiState) {

}

@OptIn(
    ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class
)
@Composable
fun ListData(
    uiState: ListViewModel.ListUiState.DataUiState,
    bottomBarHeight: Dp,
    bottomBarOffset: IntOffset,
    topBarOffset: IntOffset,
    modifier: Modifier = Modifier
) {


    Box(modifier = modifier) {

        val topPadding = LocalDensity.current.run { abs(topBarOffset.y).toDp() }
        val topDifference = 64.dp - topPadding

        LazyColumn {
            item {
                Spacer(modifier = Modifier.height(topDifference))
            }

            items(uiState.emails) {
                EmailRow(email = it)
            }
        }
        ListAppBar(offset = topBarOffset)

        ListBottomBar(
            offset = bottomBarOffset,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun EmailRow(email: Email, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Row {
                Text(text = email.sender, style = Typography.titleSmall)
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = email.timeDisplay,
                    style = Typography.titleSmall.copy(color = SuperGrey)
                )
            }
            Text(text = email.subject, style = Typography.bodyMedium)
            Text(text = email.message, style = Typography.bodyMedium.copy(color = SuperGrey))
        }
        Spacer(modifier = Modifier.height(4.dp))
        Divider(
            modifier = Modifier.padding(start = 16.dp),
            color = SuperWhite,
            thickness = 2.dp
        )
    }
}

@Composable
fun ShortCutSheet() {
    Column(
        modifier = Modifier
            .height(300.dp)
            .background(color = MaterialTheme.colorScheme.primary)
    ) {

    }
}

@Preview
@Composable
fun PreviewEmail() {
    val email = Email(
        sender = "Mike",
        subject = "My cool subject",
        message = "Here's the message",
        timeDisplay = "9:40PM"
    )
    EmailRow(email = email)
}