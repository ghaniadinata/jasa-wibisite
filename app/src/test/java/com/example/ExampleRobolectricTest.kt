package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.WebCraftRepository
import com.example.model.InvoiceStatus
import com.example.model.OrderStatus
import com.example.model.TermType
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("WebCraft DevOps", appName)
    }

    @Test
    fun `test split billing 50 percent and order creation`() {
        val repo = WebCraftRepository()
        val pkg = repo.packages[0] // Landing page Rp 2.500.000
        val addon = repo.availableAddOns[0] // Domain .com Rp 175.000
        val total = pkg.basePrice + addon.price // 2.675.000

        val order = repo.createOrder(pkg, listOf(addon), "Budi Santoso", "CV Sukses Mandiri")
        assertEquals(total, order.totalAmount)
        assertEquals(OrderStatus.WAITING_DP, order.status)

        // Verify split invoices created (DP 50% and Settlement 50%)
        val invs = repo.invoices.value.filter { it.orderId == order.id }
        assertEquals(2, invs.size)

        val dpInv = invs.find { it.termType == TermType.TERM_1_DP }
        assertNotNull(dpInv)
        assertEquals(total / 2, dpInv!!.amount)
    }

    @Test
    fun `test credential vault gated until term 2 settlement paid`() {
        val repo = WebCraftRepository()
        val defaultOrderId = "ORD-001"

        // Initially Term 2 is unpaid, so vault must be locked
        assertFalse(repo.isVaultUnlocked(defaultOrderId))

        // Pay Term 2
        val term2Inv = repo.invoices.value.find { it.orderId == defaultOrderId && it.termType == TermType.TERM_2_SETTLEMENT }
        assertNotNull(term2Inv)

        repo.processPaymentWebhook(term2Inv!!.id, "QRIS Dinamis")

        // Now vault must be unlocked!
        assertTrue(repo.isVaultUnlocked(defaultOrderId))
    }

    @Test
    fun `test revision quota deduction and enforcement`() {
        val repo = WebCraftRepository()
        val defaultOrderId = "ORD-001"
        val initialOrder = repo.orders.value.find { it.id == defaultOrderId }!!
        val initialRemaining = initialOrder.remainingRevisionQuota

        // Submit revision
        val success = repo.submitRevisionTicket(defaultOrderId, "Perubahan Header", "Ubah font header", "mock.png")
        assertTrue(success)

        val updatedOrder = repo.orders.value.find { it.id == defaultOrderId }!!
        assertEquals(initialRemaining - 1, updatedOrder.remainingRevisionQuota)
    }

    @Test
    fun `test responsive server autoscaling`() {
        val repo = WebCraftRepository()
        repo.setReplicas(4)
        assertEquals(4, repo.serverReplicas.value)

        // Clamping check
        repo.setReplicas(10)
        assertEquals(5, repo.serverReplicas.value)
    }
}
