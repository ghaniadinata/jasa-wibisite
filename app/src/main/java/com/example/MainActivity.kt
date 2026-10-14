package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AppNavTab
import com.example.ui.viewmodel.WebCraftViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme(darkTheme = true) {
                WebCraftApp()
            }
        }
    }
}

@Composable
fun WebCraftApp(viewModel: WebCraftViewModel = viewModel()) {
    val currentTab by viewModel.currentTab.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val orders by viewModel.orders.collectAsState()
    val selectedOrderId by viewModel.selectedOrderId.collectAsState()
    val activeOrder = orders.find { it.id == selectedOrderId } ?: orders.firstOrNull()

    val paymentInvoice by viewModel.showPaymentDialogForInvoice.collectAsState()
    val showBriefModal by viewModel.showBriefModal.collectAsState()
    val showStagingViewer by viewModel.showStagingViewer.collectAsState()
    val showRevisionModal by viewModel.showRevisionModal.collectAsState()
    val twoFactorAction by viewModel.showTwoFactorPrompt.collectAsState()
    val showBastModal by viewModel.showBastModal.collectAsState()
    val feedbackMessage by viewModel.userFeedbackMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Show snackbar feedback
    LaunchedEffect(feedbackMessage) {
        feedbackMessage?.let { msg ->
            snackbarHostState.showSnackbar(
                message = msg,
                duration = SnackbarDuration.Short
            )
            viewModel.clearFeedback()
        }
    }

    // Android Back Handler: return to PORTAL tab if on secondary tabs
    BackHandler(enabled = currentTab != AppNavTab.PORTAL) {
        viewModel.setTab(AppNavTab.PORTAL)
    }

    Scaffold(
        topBar = {
            WebCraftHeader(
                currentUser = currentUser,
                onRoleChange = { viewModel.switchUserRole(it) }
            )
        },
        bottomBar = {
            WebCraftBottomNav(
                currentTab = currentTab,
                onTabSelected = { viewModel.setTab(it) }
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(bottom = 60.dp)
            )
        },
        contentWindowInsets = WindowInsets.safeDrawing,
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                AppNavTab.PORTAL -> ClientPortalScreen(viewModel = viewModel)
                AppNavTab.KATALOG -> KatalogPricingScreen(viewModel = viewModel)
                AppNavTab.DEVOPS -> DockerMonitoringScreen(viewModel = viewModel)
                AppNavTab.LOGS -> CentralizedLogsScreen(viewModel = viewModel)
                AppNavTab.SECURITY -> BackupAndSecurityScreen(viewModel = viewModel)
                AppNavTab.ANALYTICS -> AnalyticsFinanceScreen(viewModel = viewModel)
            }
        }
    }

    // Active Modals & Dialogs
    paymentInvoice?.let { invoice ->
        PaymentModal(
            invoice = invoice,
            onConfirmPayment = { id, ch -> viewModel.confirmPayment(id, ch) },
            onDismiss = { viewModel.closePaymentModal() }
        )
    }

    if (showBriefModal && activeOrder != null) {
        BriefIntakeModal(
            order = activeOrder,
            onSubmit = { brand, tagline, colors, audience, comp, logo, drive ->
                viewModel.submitBrief(brand, tagline, colors, audience, comp, logo, drive)
            },
            onDismiss = { viewModel.closeBriefModal() }
        )
    }

    if (showStagingViewer && activeOrder != null) {
        StagingViewerModal(
            stagingUrl = activeOrder.stagingDemoUrl,
            onApproveStaging = {
                viewModel.closeStagingViewer()
                viewModel.approveMilestone("m4")
            },
            onDismiss = { viewModel.closeStagingViewer() }
        )
    }

    if (showRevisionModal && activeOrder != null) {
        RevisionModal(
            remainingQuota = activeOrder.remainingRevisionQuota,
            onSubmit = { title, desc, screenshot ->
                viewModel.submitRevision(title, desc, screenshot)
            },
            onDismiss = { viewModel.closeRevisionModal() }
        )
    }

    if (twoFactorAction != null) {
        TwoFactorAuthModal(
            onVerify = { code -> viewModel.verifyTwoFactorCode(code) },
            onCancel = { viewModel.cancelTwoFactor() }
        )
    }

    if (showBastModal && activeOrder != null) {
        BastModal(
            order = activeOrder,
            onDismiss = { viewModel.closeBastModal() }
        )
    }
}
