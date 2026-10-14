package com.example.model

import java.text.NumberFormat
import java.util.Locale

enum class UserRole(val label: String, val badgeColor: Long) {
    SUPER_ADMIN("Super Admin", 0xFF8B5CF6),
    PROJECT_MANAGER("Project Manager", 0xFF3B82F6),
    DEVELOPER("Developer / DevOps", 0xFF00D2FF),
    FINANCE("Finance Officer", 0xFF10B981),
    CLIENT("Klien UMKM / Bisnis", 0xFFF59E0B)
}

data class UserProfile(
    val id: String,
    val name: String,
    val email: String,
    val role: UserRole,
    val companyName: String = "",
    val phone: String = "0812-3456-7890",
    val twoFactorEnabled: Boolean = true
)

data class ServicePackage(
    val id: String,
    val name: String,
    val slug: String,
    val basePrice: Long,
    val revisionQuota: Int,
    val estimatedDays: Int,
    val description: String,
    val features: List<String>,
    val recommended: Boolean = false
) {
    fun formattedPrice(): String {
        val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        return format.format(basePrice).replace(",00", "")
    }
}

data class AddOnItem(
    val id: String,
    val name: String,
    val price: Long,
    val extraDays: Int,
    val description: String,
    val category: String
) {
    fun formattedPrice(): String {
        val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        return format.format(price).replace(",00", "")
    }
}

enum class OrderStatus(val label: String, val stepIndex: Int) {
    WAITING_DP("Menunggu Pembayaran DP", 0),
    BRIEF_PHASE("Pengisian Brief Proyek", 1),
    IN_PROGRESS("Pengembangan & Slicing", 2),
    STAGING_REVIEW("Uji Coba Staging & UAT", 3),
    WAITING_SETTLEMENT("Menunggu Pelunasan", 4),
    COMPLETED("Selesai & Handover BAST", 5),
    CANCELLED("Dibatalkan", -1)
}

data class BriefSubmission(
    val brandName: String = "",
    val taglineDescription: String = "",
    val colorPalette: String = "Navy & Cyan Neon",
    val targetAudience: String = "B2B & Korporasi Digital",
    val competitorUrls: String = "https://agency-contoh.com, https://techstartup.id",
    val logoFileName: String = "logo_vektor_resmi.svg",
    val supportingAssetsUrl: String = "https://drive.google.com/drive/folders/webcraft-assets",
    val status: String = "SUBMITTED",
    val submittedAt: String = "14 Okt 2026, 09:15 WIB"
)

enum class MilestoneStatus {
    LOCKED, ACTIVE, IN_REVIEW, APPROVED
}

data class ProjectMilestone(
    val id: String,
    val stageNumber: Int,
    val title: String,
    val description: String,
    val status: MilestoneStatus,
    val previewUrl: String? = null,
    val approvedAt: String? = null,
    val approvedByIp: String? = null
)

data class RevisionTicket(
    val id: String,
    val milestoneId: String,
    val title: String,
    val description: String,
    val screenshotNote: String,
    val status: String, // "OPEN", "IN_PROGRESS", "RESOLVED"
    val createdAt: String
)

enum class TermType(val label: String) {
    TERM_1_DP("Termin 1 (DP 50%)"),
    TERM_2_SETTLEMENT("Termin 2 (Pelunasan 50%)"),
    EXTRA_REVISION("Add-on Kuota Revisi")
}

enum class InvoiceStatus {
    UNPAID, PAID, EXPIRED
}

data class Invoice(
    val id: String,
    val invoiceNumber: String,
    val orderId: String,
    val termType: TermType,
    val amount: Long,
    val dueDate: String,
    val status: InvoiceStatus,
    val paidAt: String? = null,
    val paymentChannel: String? = null,
    val transactionRef: String? = null
) {
    fun formattedAmount(): String {
        val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        return format.format(amount).replace(",00", "")
    }
}

data class CredentialItem(
    val id: String,
    val serviceLabel: String,
    val loginUrl: String,
    val username: String,
    val encryptedPasswordCipher: String,
    val plaintextPassword: String,
    val notes: String = ""
)

data class Order(
    val id: String,
    val orderNumber: String,
    val clientId: String,
    val clientName: String,
    val companyName: String,
    val servicePackage: ServicePackage,
    val selectedAddOns: List<AddOnItem>,
    val totalAmount: Long,
    val status: OrderStatus,
    val startDate: String,
    val targetCompletionDate: String,
    val revisionQuotaTotal: Int,
    val revisionQuotaUsed: Int,
    val brief: BriefSubmission,
    val stagingDemoUrl: String = "https://staging.webcraft.io/client-preview"
) {
    fun formattedTotalAmount(): String {
        val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        return format.format(totalAmount).replace(",00", "")
    }

    val remainingRevisionQuota: Int
        get() = (revisionQuotaTotal - revisionQuotaUsed).coerceAtLeast(0)
}

// Docker & AntiGravity DevOps Monitoring Models
enum class ContainerState(val label: String, val colorHex: Long) {
    RUNNING("Running / Healthy", 0xFF10B981),
    HIGH_LOAD("High CPU / Auto-Scaling", 0xFFF59E0B),
    RESTARTING("Restarting...", 0xFF3B82F6),
    STOPPED("Stopped", 0xFFEF4444)
}

data class ContainerInfo(
    val id: String,
    val name: String,
    val serviceType: String, // "php_laravel", "postgres", "nginx", "redis", "worker"
    val image: String,
    val state: ContainerState,
    val cpuPercent: Float,
    val memoryMb: Float,
    val maxMemoryMb: Float,
    val networkIo: String,
    val uptime: String,
    val port: String,
    val restartPolicy: String = "always"
)

enum class LogLevel(val colorHex: Long) {
    EMERGENCY(0xFFDC2626),
    ERROR(0xFFEF4444),
    WARNING(0xFFF59E0B),
    INFO(0xFF3B82F6),
    DEBUG(0xFF6B7280)
}

data class LogEntry(
    val id: String,
    val timestamp: String,
    val level: LogLevel,
    val service: String,
    val message: String,
    val stackTrace: String? = null
)

data class BackupRecord(
    val id: String,
    val fileName: String,
    val timestamp: String,
    val sizeMb: Double,
    val status: String, // "COMPLETED", "IN_PROGRESS", "SCHEDULED"
    val type: String, // "AUTO_DAILY_PGDUMP", "MANUAL_FULL"
    val retentionDays: Int = 30
)

data class TelegramAlertConfig(
    val botToken: String = "6198421098:AAHxy7W9e4K-Z...",
    val chatId: String = "-1001849203112 (DevOps-Alerts)",
    val enabled: Boolean = true,
    val notifyOnError: Boolean = true,
    val notifyOnDeployment: Boolean = true,
    val notifyOnPayment: Boolean = true,
    val lastDispatchedAlert: String = "14 Okt 2026 11:24:02 WIB: [RESOLVED] Container laravel_app Auto-Scaled to 3 Replicas"
)

data class DeploymentStep(
    val title: String,
    val command: String,
    val status: String // "PENDING", "RUNNING", "SUCCESS", "FAILED"
)

data class PlatformAnalytics(
    val activeVisitors: Int,
    val todayRequests: Int,
    val avgTtfbMs: Int,
    val uptimePercent: Double,
    val errorRatePercent: Double,
    val totalGrossRevenue: Long,
    val totalCogs: Long
) {
    val netProfit: Long
        get() = totalGrossRevenue - totalCogs

    fun formattedGross(): String = formatRupiah(totalGrossRevenue)
    fun formattedCogs(): String = formatRupiah(totalCogs)
    fun formattedNetProfit(): String = formatRupiah(netProfit)

    companion object {
        fun formatRupiah(value: Long): String {
            val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
            return format.format(value).replace(",00", "")
        }
    }
}
