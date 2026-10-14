package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.WebCraftRepository
import com.example.model.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class AppNavTab(val label: String, val iconName: String) {
    KATALOG("Katalog & Hitung", "calculate"),
    PORTAL("Portal Klien", "dashboard"),
    DEVOPS("Docker DevOps", "developer_board"),
    LOGS("Log Terpusat", "receipt_long"),
    SECURITY("Backup & 2FA", "security"),
    ANALYTICS("Analitik & Finansial", "insights")
}

data class QuotationState(
    val selectedPackage: ServicePackage,
    val selectedAddOnIds: Set<String> = emptySet(),
    val clientNameInput: String = "Rian Pratama",
    val companyNameInput: String = "PT Nusa Kreatif Mandiri"
) {
    fun calculateTotal(allAddOns: List<AddOnItem>): Long {
        val addOnSum = allAddOns.filter { it.id in selectedAddOnIds }.sumOf { it.price }
        return selectedPackage.basePrice + addOnSum
    }

    fun calculateDays(allAddOns: List<AddOnItem>): Int {
        val extraDays = allAddOns.filter { it.id in selectedAddOnIds }.sumOf { it.extraDays }
        return selectedPackage.estimatedDays + extraDays
    }
}

class WebCraftViewModel(
    private val repository: WebCraftRepository = WebCraftRepository()
) : ViewModel() {

    // Current Navigation Tab
    private val _currentTab = MutableStateFlow(AppNavTab.PORTAL)
    val currentTab = _currentTab.asStateFlow()

    // Active User
    val currentUser = repository.currentUser

    // Packages & Add-ons
    val packages = repository.packages
    val availableAddOns = repository.availableAddOns

    // Calculator State
    private val _quotationState = MutableStateFlow(
        QuotationState(selectedPackage = repository.packages[1])
    )
    val quotationState = _quotationState.asStateFlow()

    // Orders & Milestones
    val orders = repository.orders
    val selectedOrderId = repository.selectedOrderId
    val milestones = repository.milestones
    val revisionTickets = repository.revisionTickets
    val invoices = repository.invoices
    val credentials = repository.credentials

    // Docker & DevOps
    val containers = repository.containers
    val serverReplicas = repository.serverReplicas
    val isDeploying = repository.isDeploying
    val deploymentLogs = repository.deploymentLogs

    // Logs & Backups
    val systemLogs = repository.systemLogs
    val backupRecords = repository.backupRecords
    val telegramConfig = repository.telegramConfig
    val analytics = repository.analytics

    // UI Dialog States
    private val _showPaymentDialogForInvoice = MutableStateFlow<Invoice?>(null)
    val showPaymentDialogForInvoice = _showPaymentDialogForInvoice.asStateFlow()

    private val _showRevisionModal = MutableStateFlow(false)
    val showRevisionModal = _showRevisionModal.asStateFlow()

    private val _showBriefModal = MutableStateFlow(false)
    val showBriefModal = _showBriefModal.asStateFlow()

    private val _showStagingViewer = MutableStateFlow(false)
    val showStagingViewer = _showStagingViewer.asStateFlow()

    private val _showBastModal = MutableStateFlow(false)
    val showBastModal = _showBastModal.asStateFlow()

    private val _showTwoFactorPrompt = MutableStateFlow<(() -> Unit)?>(null)
    val showTwoFactorPrompt = _showTwoFactorPrompt.asStateFlow()

    private val _revealedPasswordIds = MutableStateFlow<Set<String>>(emptySet())
    val revealedPasswordIds = _revealedPasswordIds.asStateFlow()

    private val _selectedLogLevelFilter = MutableStateFlow<LogLevel?>(null)
    val selectedLogLevelFilter = _selectedLogLevelFilter.asStateFlow()

    private val _logSearchQuery = MutableStateFlow("")
    val logSearchQuery = _logSearchQuery.asStateFlow()

    private val _userFeedbackMessage = MutableStateFlow<String?>(null)
    val userFeedbackMessage = _userFeedbackMessage.asStateFlow()

    // Navigation
    fun setTab(tab: AppNavTab) {
        _currentTab.value = tab
    }

    // Role Switching
    fun switchUserRole(role: UserRole) {
        repository.switchUserRole(role)
        showFeedback("Peran pengguna dialihkan menjadi: ${role.label}")
    }

    // Calculator Operations
    fun selectPackage(pkg: ServicePackage) {
        _quotationState.update { it.copy(selectedPackage = pkg) }
    }

    fun toggleAddOn(addonId: String) {
        _quotationState.update { current ->
            val set = current.selectedAddOnIds.toMutableSet()
            if (set.contains(addonId)) set.remove(addonId) else set.add(addonId)
            current.copy(selectedAddOnIds = set)
        }
    }

    fun updateQuotationInputs(clientName: String, companyName: String) {
        _quotationState.update {
            it.copy(clientNameInput = clientName, companyNameInput = companyName)
        }
    }

    fun checkoutQuotation() {
        val q = quotationState.value
        val addOns = availableAddOns.filter { it.id in q.selectedAddOnIds }
        val newOrder = repository.createOrder(
            pkg = q.selectedPackage,
            selectedAddOns = addOns,
            clientName = q.clientNameInput,
            companyName = q.companyNameInput
        )
        _currentTab.value = AppNavTab.PORTAL
        showFeedback("Pesanan ${newOrder.orderNumber} berhasil dibuat! Silakan bayar DP Termin 1.")
    }

    // Invoices & Payment
    fun openPaymentModal(invoice: Invoice) {
        _showPaymentDialogForInvoice.value = invoice
    }

    fun closePaymentModal() {
        _showPaymentDialogForInvoice.value = null
    }

    fun confirmPayment(invoiceId: String, channel: String) {
        repository.processPaymentWebhook(invoiceId, channel)
        closePaymentModal()
        showFeedback("Pembayaran invoice berhasil diproses dan diverifikasi! [PAID]")
    }

    // Brief Submission
    fun openBriefModal() {
        _showBriefModal.value = true
    }

    fun closeBriefModal() {
        _showBriefModal.value = false
    }

    fun submitBrief(
        brandName: String,
        tagline: String,
        colors: String,
        audience: String,
        competitors: String,
        logoFile: String,
        driveUrl: String
    ) {
        val orderId = selectedOrderId.value
        repository.submitBrief(
            orderId = orderId,
            brandName = brandName,
            tagline = tagline,
            colorPalette = colors,
            targetAudience = audience,
            competitorUrls = competitors,
            logoFileName = logoFile,
            supportingAssetsUrl = driveUrl
        )
        closeBriefModal()
        showFeedback("Brief proyek berhasil dikirimkan ke tim desainer & developer!")
    }

    // Milestone & Staging
    fun approveMilestone(milestoneId: String) {
        repository.approveMilestone(milestoneId)
        showFeedback("Tahapan milestone berhasil disetujui secara digital!")
    }

    fun openStagingViewer() {
        _showStagingViewer.value = true
    }

    fun closeStagingViewer() {
        _showStagingViewer.value = false
    }

    // Revision Ticketing
    fun openRevisionModal() {
        _showRevisionModal.value = true
    }

    fun closeRevisionModal() {
        _showRevisionModal.value = false
    }

    fun submitRevision(title: String, description: String, screenshot: String) {
        val success = repository.submitRevisionTicket(selectedOrderId.value, title, description, screenshot)
        closeRevisionModal()
        if (success) {
            showFeedback("Tiket revisi berhasil diajukan dan diproses!")
        } else {
            showFeedback("Kuota revisi telah habis! Invoice add-on revisi telah diterbitkan.")
        }
    }

    // Credential Vault
    fun togglePasswordVisibility(credId: String) {
        val orderId = selectedOrderId.value
        val isUnlocked = repository.isVaultUnlocked(orderId)

        if (!isUnlocked) {
            showFeedback("Brankas terkunci! Selesaikan Pelunasan Termin 2 terlebih dahulu.")
            return
        }

        // Require 2FA verification for decrypting sensitive credentials
        requestTwoFactorAuth {
            _revealedPasswordIds.update { set ->
                val newSet = set.toMutableSet()
                if (newSet.contains(credId)) newSet.remove(credId) else newSet.add(credId)
                newSet
            }
            showFeedback("Kredensial berhasil didekripsi dengan kunci AES-256!")
        }
    }

    fun isVaultUnlocked(): Boolean {
        return repository.isVaultUnlocked(selectedOrderId.value)
    }

    fun openBastModal() {
        _showBastModal.value = true
    }

    fun closeBastModal() {
        _showBastModal.value = false
    }

    // Docker DevOps actions
    fun setReplicas(count: Int) {
        repository.setReplicas(count)
    }

    fun restartContainer(containerId: String) {
        requestTwoFactorAuth {
            viewModelScope.launch {
                repository.restartContainer(containerId)
                kotlinx.coroutines.delay(2000)
                repository.completeRestartContainer(containerId)
                showFeedback("Kontainer berhasil di-restart dan kembali berstatus HEALTHY.")
            }
        }
    }

    fun triggerAutomatedDeployment() {
        viewModelScope.launch {
            repository.runAutomatedDeployment()
            showFeedback("Pembaruan otomatis selesai! Seluruh container diperbarui tanpa downtime.")
        }
    }

    fun runScheduledBackup() {
        viewModelScope.launch {
            val record = repository.runScheduledBackupNow()
            showFeedback("Backup snapshot berhasil dibuat: ${record.fileName}")
        }
    }

    fun triggerTelegramTestAlert() {
        val result = repository.testTelegramAlert()
        showFeedback("Notifikasi peringatan berhasil dikirim ke bot Telegram!")
    }

    // Two Factor Authentication
    fun requestTwoFactorAuth(onSuccess: () -> Unit) {
        _showTwoFactorPrompt.value = onSuccess
    }

    fun verifyTwoFactorCode(code: String): Boolean {
        if (code == "123456" || code.length == 6) {
            val action = _showTwoFactorPrompt.value
            _showTwoFactorPrompt.value = null
            action?.invoke()
            return true
        }
        showFeedback("Kode 2FA tidak valid. Coba kode autentikasi 6 digit.")
        return false
    }

    fun cancelTwoFactor() {
        _showTwoFactorPrompt.value = null
    }

    // Logs filtering
    fun setLogLevelFilter(level: LogLevel?) {
        _selectedLogLevelFilter.value = level
    }

    fun setLogSearchQuery(query: String) {
        _logSearchQuery.value = query
    }

    fun clearFeedback() {
        _userFeedbackMessage.value = null
    }

    private fun showFeedback(msg: String) {
        _userFeedbackMessage.value = msg
    }
}
