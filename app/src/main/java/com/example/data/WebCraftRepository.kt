package com.example.data

import com.example.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.*

class WebCraftRepository {

    // Current logged in user (switchable role for testing multi-role workflows)
    private val _currentUser = MutableStateFlow(
        UserProfile(
            id = "USR-001",
            name = "Rian Pratama",
            email = "rian.client@perusahaan.co.id",
            role = UserRole.CLIENT,
            companyName = "PT Nusa Kreatif Mandiri",
            phone = "0812-8899-7711",
            twoFactorEnabled = true
        )
    )
    val currentUser = _currentUser.asStateFlow()

    // Catalog: Packages
    val packages = listOf(
        ServicePackage(
            id = "pkg_landing",
            name = "Landing Page UMKM",
            slug = "landing-page-umkm",
            basePrice = 2500000L,
            revisionQuota = 2,
            estimatedDays = 5,
            description = "Website 1 halaman cepat, responsif, dan konversi tinggi untuk promosi produk atau jasa UMKM.",
            features = listOf(
                "Desain Responsif Mobile & Desktop",
                "Integrasi Tombol WhatsApp Direct",
                "Setup Domain .com (1 Tahun)",
                "SSL Keamanan HTTPS Terpasang",
                "2x Siklus Revisi Minor"
            )
        ),
        ServicePackage(
            id = "pkg_compro",
            name = "Company Profile Pro",
            slug = "company-profile-pro",
            basePrice = 4800000L,
            revisionQuota = 4,
            estimatedDays = 10,
            recommended = true,
            description = "Website korporat elegan 5-8 halaman dengan arsitektur modern, formulir interaktif, dan SEO siap pakai.",
            features = listOf(
                "Hingga 8 Halaman Khusus",
                "CMS Admin Mudah Dikelola (Laravel)",
                "Desain UI/UX Figma Eksklusif",
                "Staging Server Preview Live",
                "Hosting NVMe Berkecepatan Tinggi",
                "4x Siklus Revisi Terjadwal"
            )
        ),
        ServicePackage(
            id = "pkg_ecommerce",
            name = "Toko Online / E-Commerce",
            slug = "e-commerce-store",
            basePrice = 8500000L,
            revisionQuota = 5,
            estimatedDays = 18,
            description = "Platform penjualan online lengkap dengan katalog produk, keranjang belanja, dan pembayaran otomatis.",
            features = listOf(
                "Katalog Produk & Manajemen Stok",
                "Payment Gateway VA & QRIS Dinamis",
                "Hitung Ongkos Kirim Otomatis",
                "Notifikasi WhatsApp Transaksi",
                "5x Siklus Revisi"
            )
        ),
        ServicePackage(
            id = "pkg_custom_app",
            name = "Custom Web Application",
            slug = "custom-web-application",
            basePrice = 14500000L,
            revisionQuota = 6,
            estimatedDays = 30,
            description = "Aplikasi web kustom full-stack berbasis Laravel & Docker dengan AntiGravity container orchestration.",
            features = listOf(
                "Arsitektur Docker Containerized Terisolasi",
                "Backend Laravel 11 + PostgreSQL 15",
                "AntiGravity CI/CD Deployment Otomatis",
                "Dasbor Pemantauan Metrik Real-time",
                "Enkripsi Kredensial AES-256 & BAST Resmi",
                "6x Siklus Revisi Berita Acara"
            )
        )
    )

    // Catalog: Add-ons
    val availableAddOns = listOf(
        AddOnItem(
            id = "addon_domain_com",
            name = "Domain .com (1 Tahun)",
            price = 175000L,
            extraDays = 0,
            description = "Registrasi domain internasional .com resmi atas nama klien",
            category = "Domain & Jaringan"
        ),
        AddOnItem(
            id = "addon_domain_id",
            name = "Domain Identitas .id Resmi",
            price = 250000L,
            extraDays = 1,
            description = "Domain resmi Indonesia kredibel dengan verifikasi KTP/Akta",
            category = "Domain & Jaringan"
        ),
        AddOnItem(
            id = "addon_hosting_nvme",
            name = "Hosting NVMe 5GB Cloud Server",
            price = 450000L,
            extraDays = 0,
            description = "Penyimpanan solid ultra cepat dengan LiteSpeed & PHP 8.2",
            category = "Infrastruktur"
        ),
        AddOnItem(
            id = "addon_payment_gateway",
            name = "Payment Gateway Integrasi (Midtrans/Xendit)",
            price = 850000L,
            extraDays = 2,
            description = "Terima pembayaran via VA Mandiri, BCA, BRI, BNI dan QRIS instan",
            category = "Fitur Sistem"
        ),
        AddOnItem(
            id = "addon_multilingual",
            name = "Multi-Bahasa (ID & English)",
            price = 600000L,
            extraDays = 2,
            description = "Fitur alih bahasa otomatis dan terstruktur untuk pasar global",
            category = "Konten & Desain"
        ),
        AddOnItem(
            id = "addon_mass_upload",
            name = "Input Konten & Produk Massal (20 Item)",
            price = 350000L,
            extraDays = 1,
            description = "Bantu entri teks profil usaha, produk, dan penataan gambar awal",
            category = "Konten & Desain"
        ),
        AddOnItem(
            id = "addon_seo_setup",
            name = "SEO Setup & Google Search Console",
            price = 500000L,
            extraDays = 1,
            description = "Optimasi meta tags, XML sitemap, schema markup, dan pendaftaran indexing Google",
            category = "Pemasaran"
        ),
        AddOnItem(
            id = "addon_docker_antigravity",
            name = "Dedicated AntiGravity Docker Stack",
            price = 1200000L,
            extraDays = 3,
            description = "Lingkungan kontainer mandiri dengan auto-healing, auto-restart, dan live metrics",
            category = "DevOps"
        )
    )

    // Active Orders
    private val defaultOrder = Order(
        id = "ORD-001",
        orderNumber = "ORD-20261014-0012",
        clientId = "USR-001",
        clientName = "Rian Pratama",
        companyName = "PT Nusa Kreatif Mandiri",
        servicePackage = packages[1], // Company Profile Pro
        selectedAddOns = listOf(availableAddOns[0], availableAddOns[3], availableAddOns[7]),
        totalAmount = 4800000L + 175000L + 850000L + 1200000L, // Rp 7.025.000
        status = OrderStatus.STAGING_REVIEW,
        startDate = "10 Okt 2026",
        targetCompletionDate = "25 Okt 2026",
        revisionQuotaTotal = 4,
        revisionQuotaUsed = 1,
        brief = BriefSubmission(
            brandName = "PT Nusa Kreatif Mandiri",
            taglineDescription = "Solusi Fabrikasi Presisi & Inovasi Manufaktur Berstandar ISO",
            colorPalette = "Deep Navy (#0A0F1D) & Cyan Modern (#00D2FF)",
            targetAudience = "Klien Industri B2B, Manajer Pabrik, dan Pengadaan Proyek",
            competitorUrls = "https://indomanufaktur.com, https://presisikarya.co.id",
            logoFileName = "logo_nusa_kreatif.svg",
            supportingAssetsUrl = "https://drive.google.com/drive/folders/nusa-kreatif-assets",
            status = "ACCEPTED",
            submittedAt = "11 Okt 2026, 14:20 WIB"
        ),
        stagingDemoUrl = "https://staging.webcraft.io/client-preview/nusa-kreatif"
    )

    private val _orders = MutableStateFlow(listOf(defaultOrder))
    val orders = _orders.asStateFlow()

    private val _selectedOrderId = MutableStateFlow("ORD-001")
    val selectedOrderId = _selectedOrderId.asStateFlow()

    // Milestones for current order
    private val _milestones = MutableStateFlow(
        listOf(
            ProjectMilestone(
                id = "m1",
                stageNumber = 1,
                title = "Fase 1: Review Brief & Konsep Sitemap",
                description = "Validasi dokumen brief, struktur navigasi halaman, dan sitemap arsitektur sistem.",
                status = MilestoneStatus.APPROVED,
                previewUrl = "https://figma.com/@webcraft/nusa-kreatif-sitemap",
                approvedAt = "12 Okt 2026, 10:15 WIB",
                approvedByIp = "182.253.110.42"
            ),
            ProjectMilestone(
                id = "m2",
                stageNumber = 2,
                title = "Fase 2: Desain UI/UX & Wireframing (Figma)",
                description = "Desain antarmuka responsif high-fidelity dengan skema warna Navy & Cyan.",
                status = MilestoneStatus.APPROVED,
                previewUrl = "https://figma.com/@webcraft/nusa-kreatif-ui-mockup",
                approvedAt = "13 Okt 2026, 16:45 WIB",
                approvedByIp = "182.253.110.42"
            ),
            ProjectMilestone(
                id = "m3",
                stageNumber = 3,
                title = "Fase 3: Slicing Kode & Backend Laravel 11",
                description = "Implementasi frontend TailwindCSS, controller RESTful, integrasi database PostgreSQL.",
                status = MilestoneStatus.APPROVED,
                previewUrl = "https://github.com/webcraft-agency/nusa-kreatif-web",
                approvedAt = "14 Okt 2026, 08:30 WIB",
                approvedByIp = "182.253.110.42"
            ),
            ProjectMilestone(
                id = "m4",
                stageNumber = 4,
                title = "Fase 4: Pengujian Staging & Evaluasi UAT",
                description = "Website aktif di kontainer Docker staging terisolasi siap uji coba oleh klien.",
                status = MilestoneStatus.IN_REVIEW,
                previewUrl = "https://staging.webcraft.io/client-preview/nusa-kreatif"
            ),
            ProjectMilestone(
                id = "m5",
                stageNumber = 5,
                title = "Fase 5: Pelunasan, Migrasi Live & Serah Terima (BAST)",
                description = "Handover kredensial brankas server terenkripsi dan aktivasi masa garansi pemeliharaan 60 hari.",
                status = MilestoneStatus.LOCKED
            )
        )
    )
    val milestones = _milestones.asStateFlow()

    // Revision Tickets
    private val _revisionTickets = MutableStateFlow(
        listOf(
            RevisionTicket(
                id = "TKT-001",
                milestoneId = "m4",
                title = "Penyesuaian teks tombol CTA di bagian Hero",
                description = "Teks 'Mulai Sekarang' diubah menjadi 'Konsultasikan Proyek Gratis' dan arahkan ke form kontak.",
                screenshotNote = "Lampiran: screenshot-hero-cta.png",
                status = "RESOLVED",
                createdAt = "14 Okt 2026, 09:00 WIB"
            )
        )
    )
    val revisionTickets = _revisionTickets.asStateFlow()

    // Invoices for default order
    private val _invoices = MutableStateFlow(
        listOf(
            Invoice(
                id = "INV-001",
                invoiceNumber = "INV-202610-0042",
                orderId = "ORD-001",
                termType = TermType.TERM_1_DP,
                amount = 3512500L, // 50% of 7.025.000
                dueDate = "12 Okt 2026",
                status = InvoiceStatus.PAID,
                paidAt = "11 Okt 2026, 11:32 WIB",
                paymentChannel = "QRIS Dinamis (BCA)",
                transactionRef = "TXN-BCA-889102374"
            ),
            Invoice(
                id = "INV-002",
                invoiceNumber = "INV-202610-0043",
                orderId = "ORD-001",
                termType = TermType.TERM_2_SETTLEMENT,
                amount = 3512500L, // remaining 50%
                dueDate = "25 Okt 2026",
                status = InvoiceStatus.UNPAID
            )
        )
    )
    val invoices = _invoices.asStateFlow()

    // Secure Credential Vault (Gated by Term 2 Paid status!)
    private val _credentials = MutableStateFlow(
        listOf(
            CredentialItem(
                id = "c1",
                serviceLabel = "cPanel & Web Hosting NVMe",
                loginUrl = "https://cpanel.nusakreatif.com:2083",
                username = "nusakrea",
                encryptedPasswordCipher = "U2FsdGVkX19zS0A09bZ9Xl1f+qT39aPj5Z1kLm8q...",
                plaintextPassword = "KreaTif#NVMe_2026!Secured",
                notes = "Port 2083, PHP Version 8.2 (Native)"
            ),
            CredentialItem(
                id = "c2",
                serviceLabel = "Domain DNS Cloudflare",
                loginUrl = "https://dash.cloudflare.com/login",
                username = "admin@nusakreatif.com",
                encryptedPasswordCipher = "U2FsdGVkX1+vGz772XQOqLm13zN8dOa49qWl0...",
                plaintextPassword = "Cloud#Flare_Dns!Nusa2026",
                notes = "Nameservers: nova.ns.cloudflare.com, paul.ns.cloudflare.com"
            ),
            CredentialItem(
                id = "c3",
                serviceLabel = "Laravel Administrator Panel",
                loginUrl = "https://nusakreatif.com/admin/login",
                username = "superadmin@nusakreatif.com",
                encryptedPasswordCipher = "U2FsdGVkX187hQ9qPlK281kLMn45pQqRe9982...",
                plaintextPassword = "Admin@Laravel_WebCraft2026$",
                notes = "Role: Super Administrator dengan akses manajemen konten & user"
            ),
            CredentialItem(
                id = "c4",
                serviceLabel = "Database PostgreSQL 15 Engine",
                loginUrl = "postgres://db.webcraft.io:5432/nusakreatif_db",
                username = "pg_nusakreatif",
                encryptedPasswordCipher = "U2FsdGVkX1928kLMpq90184bVn991pQaRz349...",
                plaintextPassword = "PgSql#Secured_99812_Nusa!",
                notes = "Database: nusakreatif_db, SSL Mode: require"
            )
        )
    )
    val credentials = _credentials.asStateFlow()

    // AntiGravity Docker Containers
    private val _containers = MutableStateFlow(
        listOf(
            ContainerInfo(
                id = "cnt-01",
                name = "laravel_app",
                serviceType = "php_laravel",
                image = "webcraft/laravel:11-php8.2-fpm",
                state = ContainerState.RUNNING,
                cpuPercent = 14.5f,
                memoryMb = 148f,
                maxMemoryMb = 512f,
                networkIo = "1.2 MB / 8.4 MB",
                uptime = "4 hari 18 jam",
                port = "8000:8000",
                restartPolicy = "always"
            ),
            ContainerInfo(
                id = "cnt-02",
                name = "postgres_db",
                serviceType = "postgres",
                image = "postgres:15-alpine",
                state = ContainerState.RUNNING,
                cpuPercent = 8.2f,
                memoryMb = 210f,
                maxMemoryMb = 1024f,
                networkIo = "12.8 MB / 42.1 MB",
                uptime = "12 hari 4 jam",
                port = "5432:5432",
                restartPolicy = "always"
            ),
            ContainerInfo(
                id = "cnt-03",
                name = "nginx_gateway",
                serviceType = "nginx",
                image = "nginx:1.25-alpine-ssl",
                state = ContainerState.RUNNING,
                cpuPercent = 4.1f,
                memoryMb = 32f,
                maxMemoryMb = 128f,
                networkIo = "45.2 MB / 112.5 MB",
                uptime = "12 hari 4 jam",
                port = "80:80, 443:443",
                restartPolicy = "always"
            ),
            ContainerInfo(
                id = "cnt-04",
                name = "redis_cache",
                serviceType = "redis",
                image = "redis:7.2-alpine",
                state = ContainerState.RUNNING,
                cpuPercent = 2.4f,
                memoryMb = 48f,
                maxMemoryMb = 256f,
                networkIo = "2.3 MB / 3.1 MB",
                uptime = "12 hari 4 jam",
                port = "6379:6379",
                restartPolicy = "always"
            ),
            ContainerInfo(
                id = "cnt-05",
                name = "antigravity_worker",
                serviceType = "worker",
                image = "webcraft/worker:queue-telemetry",
                state = ContainerState.RUNNING,
                cpuPercent = 11.8f,
                memoryMb = 92f,
                maxMemoryMb = 512f,
                networkIo = "540 KB / 2.1 MB",
                uptime = "4 hari 18 jam",
                port = "internal",
                restartPolicy = "always"
            )
        )
    )
    val containers = _containers.asStateFlow()

    // Server Scaling Replicas (Responsive server scalability)
    private val _serverReplicas = MutableStateFlow(2)
    val serverReplicas = _serverReplicas.asStateFlow()

    // Centralized Logs
    private val _systemLogs = MutableStateFlow(
        listOf(
            LogEntry(
                id = "l1",
                timestamp = "14-10-2026 11:29:45",
                level = LogLevel.INFO,
                service = "laravel_app",
                message = "HTTP GET /api/v1/orders/ORD-20261014-0012 200 OK (38ms)"
            ),
            LogEntry(
                id = "l2",
                timestamp = "14-10-2026 11:28:10",
                level = LogLevel.INFO,
                service = "nginx_gateway",
                message = "TLS Handshake 1.3 negotiated with client IP 182.253.110.42"
            ),
            LogEntry(
                id = "l3",
                timestamp = "14-10-2026 11:25:33",
                level = LogLevel.WARNING,
                service = "antigravity_worker",
                message = "High throughput detected: Job Queue latency spiked to 210ms. Auto-scaling worker pool."
            ),
            LogEntry(
                id = "l4",
                timestamp = "14-10-2026 11:24:02",
                level = LogLevel.INFO,
                service = "antigravity_core",
                message = "AntiGravity autoscaler dynamically spawned 1 additional laravel_app worker instance."
            ),
            LogEntry(
                id = "l5",
                timestamp = "14-10-2026 11:20:15",
                level = LogLevel.INFO,
                service = "postgres_db",
                message = "Checkpoint starting: time; checkpoint complete: wrote 42 buffers (0.1%); 0 WAL file(s) added"
            ),
            LogEntry(
                id = "l6",
                timestamp = "14-10-2026 11:15:00",
                level = LogLevel.INFO,
                service = "system_cron",
                message = "Automated daily pg_dump snapshot executed cleanly (size: 42.8 MB, compressed .sql.gz)"
            )
        )
    )
    val systemLogs = _systemLogs.asStateFlow()

    // Automated Backups
    private val _backupRecords = MutableStateFlow(
        listOf(
            BackupRecord(
                id = "bk-01",
                fileName = "backup_webcraft_db_20261014_0200.sql.gz",
                timestamp = "14 Okt 2026, 02:00 WIB",
                sizeMb = 42.8,
                status = "COMPLETED",
                type = "AUTO_DAILY_PGDUMP",
                retentionDays = 30
            ),
            BackupRecord(
                id = "bk-02",
                fileName = "backup_webcraft_db_20261013_0200.sql.gz",
                timestamp = "13 Okt 2026, 02:00 WIB",
                sizeMb = 41.5,
                status = "COMPLETED",
                type = "AUTO_DAILY_PGDUMP",
                retentionDays = 30
            ),
            BackupRecord(
                id = "bk-03",
                fileName = "backup_storage_assets_20261012_0400.tar.gz",
                timestamp = "12 Okt 2026, 04:00 WIB",
                sizeMb = 184.2,
                status = "COMPLETED",
                type = "MANUAL_FULL",
                retentionDays = 60
            )
        )
    )
    val backupRecords = _backupRecords.asStateFlow()

    // Telegram Alert Config
    private val _telegramConfig = MutableStateFlow(TelegramAlertConfig())
    val telegramConfig = _telegramConfig.asStateFlow()

    // Deployment Status
    private val _isDeploying = MutableStateFlow(false)
    val isDeploying = _isDeploying.asStateFlow()

    private val _deploymentLogs = MutableStateFlow<List<String>>(emptyList())
    val deploymentLogs = _deploymentLogs.asStateFlow()

    // Platform Analytics & Financials
    private val _analytics = MutableStateFlow(
        PlatformAnalytics(
            activeVisitors = 142,
            todayRequests = 18450,
            avgTtfbMs = 345, // < 600ms per NFR
            uptimePercent = 99.94,
            errorRatePercent = 0.02,
            totalGrossRevenue = 48500000L,
            totalCogs = 9400000L // Domains, hosting licenses, etc.
        )
    )
    val analytics = _analytics.asStateFlow()

    // Switch Role
    fun switchUserRole(newRole: UserRole) {
        _currentUser.update { current ->
            current.copy(
                role = newRole,
                name = when (newRole) {
                    UserRole.SUPER_ADMIN -> "Bagus Setiawan (Super Admin)"
                    UserRole.PROJECT_MANAGER -> "Dimas Saputra (Project Manager)"
                    UserRole.DEVELOPER -> "Fajar Ramadhan (DevOps Lead)"
                    UserRole.FINANCE -> "Siti Rahmawati (Finance Officer)"
                    UserRole.CLIENT -> "Rian Pratama (Klien UMKM)"
                }
            )
        }
    }

    // Create New Order from Calculator
    fun createOrder(
        pkg: ServicePackage,
        selectedAddOns: List<AddOnItem>,
        clientName: String,
        companyName: String
    ): Order {
        val totalAddOnCost = selectedAddOns.sumOf { it.price }
        val grandTotal = pkg.basePrice + totalAddOnCost
        val orderNo = "ORD-${SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())}-${(1000..9999).random()}"
        val orderId = "ORD-${UUID.randomUUID().toString().take(6).uppercase()}"

        val newOrder = Order(
            id = orderId,
            orderNumber = orderNo,
            clientId = currentUser.value.id,
            clientName = clientName.ifBlank { currentUser.value.name },
            companyName = companyName.ifBlank { "Bisnis Baru Klien" },
            servicePackage = pkg,
            selectedAddOns = selectedAddOns,
            totalAmount = grandTotal,
            status = OrderStatus.WAITING_DP,
            startDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date()),
            targetCompletionDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(
                Date(System.currentTimeMillis() + (pkg.estimatedDays + selectedAddOns.sumOf { it.extraDays }) * 86400000L)
            ),
            revisionQuotaTotal = pkg.revisionQuota,
            revisionQuotaUsed = 0,
            brief = BriefSubmission(brandName = companyName),
            stagingDemoUrl = "https://staging.webcraft.io/client-preview/${pkg.slug}"
        )

        // Generate Split Invoices (50% DP and 50% Settlement)
        val dpAmount = grandTotal / 2
        val settlementAmount = grandTotal - dpAmount

        val invDp = Invoice(
            id = "INV-${UUID.randomUUID().toString().take(6).uppercase()}",
            invoiceNumber = "INV-${SimpleDateFormat("yyyyMM", Locale.getDefault()).format(Date())}-${(100..999).random()}",
            orderId = orderId,
            termType = TermType.TERM_1_DP,
            amount = dpAmount,
            dueDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(System.currentTimeMillis() + 2 * 86400000L)),
            status = InvoiceStatus.UNPAID
        )

        val invSettlement = Invoice(
            id = "INV-${UUID.randomUUID().toString().take(6).uppercase()}",
            invoiceNumber = "INV-${SimpleDateFormat("yyyyMM", Locale.getDefault()).format(Date())}-${(100..999).random()}",
            orderId = orderId,
            termType = TermType.TERM_2_SETTLEMENT,
            amount = settlementAmount,
            dueDate = newOrder.targetCompletionDate,
            status = InvoiceStatus.UNPAID
        )

        _orders.update { listOf(newOrder) + it }
        _invoices.update { listOf(invDp, invSettlement) + it }
        _selectedOrderId.value = orderId

        addLog(
            LogLevel.INFO,
            "order_engine",
            "Order baru berhasil dibuat: $orderNo sebesar ${newOrder.formattedTotalAmount()} oleh $clientName"
        )

        return newOrder
    }

    // Submit / Update Brief
    fun submitBrief(
        orderId: String,
        brandName: String,
        tagline: String,
        colorPalette: String,
        targetAudience: String,
        competitorUrls: String,
        logoFileName: String,
        supportingAssetsUrl: String
    ) {
        val brief = BriefSubmission(
            brandName = brandName,
            taglineDescription = tagline,
            colorPalette = colorPalette,
            targetAudience = targetAudience,
            competitorUrls = competitorUrls,
            logoFileName = logoFileName,
            supportingAssetsUrl = supportingAssetsUrl,
            status = "SUBMITTED",
            submittedAt = SimpleDateFormat("dd MMM yyyy, HH:mm 'WIB'", Locale("id", "ID")).format(Date())
        )

        _orders.update { ordersList ->
            ordersList.map { order ->
                if (order.id == orderId) {
                    order.copy(brief = brief, status = OrderStatus.IN_PROGRESS)
                } else order
            }
        }

        addLog(
            LogLevel.INFO,
            "brief_intake",
            "Brief proyek untuk order $orderId berhasil dikirimkan oleh klien. Tim PM & Designer dinotifikasi."
        )
    }

    // Pay Invoice (Idempotent Webhook Simulator)
    fun processPaymentWebhook(invoiceId: String, channel: String = "QRIS Dinamis"): Boolean {
        var orderToUpdate: Order? = null
        var isSettlement = false

        _invoices.update { list ->
            list.map { inv ->
                if (inv.id == invoiceId) {
                    if (inv.status == InvoiceStatus.PAID) {
                        // Idempotency check: Already paid, no change
                        return@map inv
                    }
                    if (inv.termType == TermType.TERM_2_SETTLEMENT) {
                        isSettlement = true
                    }
                    val updated = inv.copy(
                        status = InvoiceStatus.PAID,
                        paidAt = SimpleDateFormat("dd MMM yyyy, HH:mm 'WIB'", Locale("id", "ID")).format(Date()),
                        paymentChannel = channel,
                        transactionRef = "TXN-${System.currentTimeMillis()}"
                    )
                    orderToUpdate = _orders.value.find { it.id == inv.orderId }
                    updated
                } else inv
            }
        }

        orderToUpdate?.let { ord ->
            val newStatus = if (isSettlement) {
                OrderStatus.COMPLETED
            } else {
                OrderStatus.BRIEF_PHASE
            }
            _orders.update { list ->
                list.map { if (it.id == ord.id) it.copy(status = newStatus) else it }
            }

            if (isSettlement) {
                // Also approve the final milestone
                _milestones.update { mList ->
                    mList.map { m ->
                        if (m.stageNumber == 5) {
                            m.copy(
                                status = MilestoneStatus.APPROVED,
                                approvedAt = SimpleDateFormat("dd MMM yyyy, HH:mm 'WIB'", Locale("id", "ID")).format(Date()),
                                approvedByIp = "182.253.110.42"
                            )
                        } else m
                    }
                }
            }
        }

        addLog(
            LogLevel.INFO,
            "payment_webhook",
            "Pembayaran sukses diverifikasi via webhook HMAC: Invoice $invoiceId senilai status [PAID]"
        )

        return true
    }

    // Milestone Digital Approval
    fun approveMilestone(milestoneId: String, clientIp: String = "182.253.110.42") {
        val now = SimpleDateFormat("dd MMM yyyy, HH:mm 'WIB'", Locale("id", "ID")).format(Date())
        _milestones.update { list ->
            var activateNext = false
            list.map { m ->
                if (m.id == milestoneId) {
                    activateNext = true
                    m.copy(status = MilestoneStatus.APPROVED, approvedAt = now, approvedByIp = clientIp)
                } else if (activateNext && m.status == MilestoneStatus.LOCKED) {
                    activateNext = false
                    m.copy(status = MilestoneStatus.ACTIVE)
                } else m
            }
        }

        addLog(
            LogLevel.INFO,
            "milestone_tracker",
            "Milestone $milestoneId disetujui secara digital oleh klien dari IP $clientIp"
        )
    }

    // Submit Revision Ticket with Quota Checking
    fun submitRevisionTicket(orderId: String, title: String, description: String, screenshot: String): Boolean {
        val order = _orders.value.find { it.id == orderId } ?: return false

        if (order.remainingRevisionQuota <= 0) {
            // Need add-on invoice
            val addOnInv = Invoice(
                id = "INV-${UUID.randomUUID().toString().take(6).uppercase()}",
                invoiceNumber = "INV-REV-${SimpleDateFormat("yyyyMM", Locale.getDefault()).format(Date())}-${(100..999).random()}",
                orderId = orderId,
                termType = TermType.EXTRA_REVISION,
                amount = 200000L, // Rp 200.000,- flat fee per cycle per PRD
                dueDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(System.currentTimeMillis() + 86400000L)),
                status = InvoiceStatus.UNPAID
            )
            _invoices.update { listOf(addOnInv) + it }
            addLog(
                LogLevel.WARNING,
                "revision_quota",
                "Kuota revisi untuk order $orderId telah habis (0 tersisa). Invoice add-on revisi Rp 200.000 diterbitkan."
            )
            return false
        }

        // Deduct 1 quota
        _orders.update { list ->
            list.map { ord ->
                if (ord.id == orderId) ord.copy(revisionQuotaUsed = ord.revisionQuotaUsed + 1) else ord
            }
        }

        val ticket = RevisionTicket(
            id = "TKT-${SimpleDateFormat("HHmm", Locale.getDefault()).format(Date())}-${(10..99).random()}",
            milestoneId = "m4",
            title = title,
            description = description,
            screenshotNote = screenshot.ifBlank { "Tangkapan layar terlampir" },
            status = "OPEN",
            createdAt = SimpleDateFormat("dd MMM yyyy, HH:mm 'WIB'", Locale("id", "ID")).format(Date())
        )

        _revisionTickets.update { listOf(ticket) + it }

        addLog(
            LogLevel.INFO,
            "revision_engine",
            "Tiket revisi #${ticket.id} diajukan oleh klien. Sisa kuota revisi: ${order.remainingRevisionQuota - 1}"
        )

        return true
    }

    // Auto-scale replicas
    fun setReplicas(count: Int) {
        val clamped = count.coerceIn(1, 5)
        _serverReplicas.value = clamped
        _containers.update { list ->
            list.map { c ->
                if (c.serviceType == "php_laravel") {
                    val newCpu = (30f / clamped).coerceAtLeast(6f)
                    c.copy(cpuPercent = newCpu)
                } else c
            }
        }
        addLog(
            LogLevel.INFO,
            "antigravity_autoscaler",
            "Server cluster responsif diskalakan ke $clamped replika. Load balancing disesuaikan."
        )
    }

    // Restart Container
    fun restartContainer(containerId: String) {
        _containers.update { list ->
            list.map { c ->
                if (c.id == containerId) c.copy(state = ContainerState.RESTARTING) else c
            }
        }
        addLog(
            LogLevel.WARNING,
            "docker_engine",
            "Perintah restart kontainer diterima untuk $containerId. Restarting..."
        )
    }

    fun completeRestartContainer(containerId: String) {
        _containers.update { list ->
            list.map { c ->
                if (c.id == containerId) c.copy(state = ContainerState.RUNNING) else c
            }
        }
        addLog(
            LogLevel.INFO,
            "docker_engine",
            "Kontainer $containerId pulih dan berstatus HEALTHY (auto-restart: always)"
        )
    }

    // Trigger CI/CD 1-Click Auto Deployment
    suspend fun runAutomatedDeployment(): Boolean {
        _isDeploying.value = true
        _deploymentLogs.value = listOf(
            "[AntiGravity CI/CD Pipeline v3.2 Initiated]",
            ">> Connecting to remote repository: git@github.com:webcraft-agency/client-app.git",
            ">> Branch: main | Commit: 8f3b190 (feat: optimize database queries & cache)"
        )

        delay(900)
        _deploymentLogs.update { it + listOf(">> Step 1/5: git pull origin main (Fast-forward, 14 files changed)") }

        delay(900)
        _deploymentLogs.update { it + listOf(">> Step 2/5: composer install --no-dev --optimize-autoloader (Generating optimized class loader: Success)") }

        delay(900)
        _deploymentLogs.update { it + listOf(">> Step 3/5: php artisan migrate --force (Nothing to migrate, schema up to date)") }

        delay(900)
        _deploymentLogs.update { it + listOf(">> Step 4/5: php artisan config:cache && php artisan route:cache (Configuration & routes cached successfully)") }

        delay(900)
        _deploymentLogs.update {
            it + listOf(
                ">> Step 5/5: docker compose up -d --no-deps --build laravel_app (Zero-downtime container swap)",
                "==> [SUCCESS] Deployment completed in 4.5s! HTTP 200 health check passed."
            )
        }

        _isDeploying.value = false
        addLog(
            LogLevel.INFO,
            "antigravity_deployment",
            "Pembaruan sistem otomatis selesai: Container laravel_app berhasil di-deploy tanpa downtime."
        )

        return true
    }

    // Trigger Manual Backup
    suspend fun runScheduledBackupNow(): BackupRecord {
        val fileName = "backup_webcraft_db_${SimpleDateFormat("yyyyMMdd_HHmm", Locale.getDefault()).format(Date())}.sql.gz"
        val record = BackupRecord(
            id = "bk-${System.currentTimeMillis()}",
            fileName = fileName,
            timestamp = SimpleDateFormat("dd MMM yyyy, HH:mm 'WIB'", Locale("id", "ID")).format(Date()),
            sizeMb = 43.2,
            status = "COMPLETED",
            type = "MANUAL_FULL",
            retentionDays = 30
        )
        _backupRecords.update { listOf(record) + it }
        addLog(
            LogLevel.INFO,
            "backup_scheduler",
            "Snapshot backup database otomatis $fileName tersimpan (Retensi: 30 hari, Status: Aman)"
        )
        return record
    }

    // Telegram Alert Trigger (Simulation + Live dispatch feedback)
    fun testTelegramAlert(): String {
        val msg = "⚠️ [WebCraft-DevOps Alert]\n" +
                "Server: production-cluster-01\n" +
                "Container: laravel_app\n" +
                "Event: Telemetry Health Check 200 OK\n" +
                "Autoscaler: 2 Replicas Active\n" +
                "Time: ${SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date())}"

        _telegramConfig.update { it.copy(lastDispatchedAlert = "Notifikasi terkirim ke Telegram: ${it.chatId}") }
        addLog(LogLevel.INFO, "telegram_bot", "Telegram error alert payload terkirim via Webhook API")
        return msg
    }

    // Centralized Log Management
    fun addLog(level: LogLevel, service: String, message: String, stackTrace: String? = null) {
        val entry = LogEntry(
            id = "log-${System.currentTimeMillis()}-${(10..99).random()}",
            timestamp = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(Date()),
            level = level,
            service = service,
            message = message,
            stackTrace = stackTrace
        )
        _systemLogs.update { listOf(entry) + it }
    }

    // Check if Credential Vault can be unlocked
    fun isVaultUnlocked(orderId: String): Boolean {
        val settlementInvoice = _invoices.value.find {
            it.orderId == orderId && it.termType == TermType.TERM_2_SETTLEMENT
        }
        val allRevisionsResolved = _revisionTickets.value.none { it.status == "OPEN" }
        return (settlementInvoice?.status == InvoiceStatus.PAID) && allRevisionsResolved
    }
}
