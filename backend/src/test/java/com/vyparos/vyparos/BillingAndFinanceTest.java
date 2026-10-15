package com.vyparos.vyparos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vyparos.vyparos.billing.dto.CreateInvoiceRequest;
import com.vyparos.vyparos.billing.dto.InvoiceItemRequest;
import com.vyparos.vyparos.billing.dto.RecordPaymentRequest;
import com.vyparos.vyparos.billing.entity.InvoiceItemType;
import com.vyparos.vyparos.billing.entity.PaymentMethod;
import com.vyparos.vyparos.business.dto.BusinessSettingsDto;
import com.vyparos.vyparos.business.dto.CreateBusinessRequest;
import com.vyparos.vyparos.customers.dto.CustomerRequest;
import com.vyparos.vyparos.finance.dto.ExpenseRequest;
import com.vyparos.vyparos.identity.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BillingAndFinanceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateInvoiceCalculateTotalsRecordPaymentsAndGenerateReport() throws Exception {
        // 1. Setup user & business
        RegisterRequest userReq = new RegisterRequest("financeowner@test.com", "pass12345", "Suresh Finance", "9876543210");
        String userRes = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        String token = objectMapper.readTree(userRes).path("data").path("token").asText();

        CreateBusinessRequest bizReq = new CreateBusinessRequest("Suresh Studio", "Studio", "INR", "Asia/Kolkata", "Indiranagar", "9876543210", "suresh@studio.com");
        String bizRes = mockMvc.perform(post("/api/businesses")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bizReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        long bizId = objectMapper.readTree(bizRes).path("data").path("id").asLong();

        // 2. Configure 18% GST tax rate
        BusinessSettingsDto settingsReq = new BusinessSettingsDto();
        settingsReq.setTaxRate(new BigDecimal("18.00"));
        settingsReq.setTaxName("GST");
        settingsReq.setInvoicePrefix("STUDIO");
        mockMvc.perform(patch("/api/businesses/" + bizId + "/settings")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(settingsReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.taxRate").value(18.00));

        // 3. Create customer
        CustomerRequest custReq = new CustomerRequest("Deepak Verma", "9988776655", "deepak@gmail.com", "Indiranagar", "VIP");
        String custRes = mockMvc.perform(post("/api/businesses/" + bizId + "/customers")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(custReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        long custId = objectMapper.readTree(custRes).path("data").path("id").asLong();

        // 4. Create Invoice: 2 items @ 500 = 1000 subtotal, discount = 100 -> taxable = 900 -> tax 18% = 162.00 -> total = 1062.00
        InvoiceItemRequest item1 = new InvoiceItemRequest(InvoiceItemType.SERVICE, null, "Studio Portrait Session", 2, new BigDecimal("500.00"));
        CreateInvoiceRequest invReq = new CreateInvoiceRequest();
        invReq.setCustomerId(custId);
        invReq.setInvoiceDate(LocalDate.now());
        invReq.setDiscountAmount(new BigDecimal("100.00"));
        invReq.setItems(List.of(item1));
        invReq.setNotes("Studio shoot invoice");

        String invRes = mockMvc.perform(post("/api/businesses/" + bizId + "/invoices")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.subtotal").value(1000.00))
                .andExpect(jsonPath("$.data.discountAmount").value(100.00))
                .andExpect(jsonPath("$.data.taxAmount").value(162.00))
                .andExpect(jsonPath("$.data.totalAmount").value(1062.00))
                .andExpect(jsonPath("$.data.balanceDue").value(1062.00))
                .andExpect(jsonPath("$.data.status").value("ISSUED"))
                .andReturn().getResponse().getContentAsString();
        long invId = objectMapper.readTree(invRes).path("data").path("id").asLong();

        // 5. Record Partial Payment (500.00) -> Balance should be 562.00, status PARTIALLY_PAID
        RecordPaymentRequest payment1 = new RecordPaymentRequest(new BigDecimal("500.00"), PaymentMethod.UPI, "UPI-REF-1001", "Advance payment");
        mockMvc.perform(post("/api/businesses/" + bizId + "/invoices/" + invId + "/payments")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payment1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.paidAmount").value(500.00))
                .andExpect(jsonPath("$.data.balanceDue").value(562.00))
                .andExpect(jsonPath("$.data.status").value("PARTIALLY_PAID"));

        // 6. Overpayment attempt (600.00 when balance is 562.00) -> MUST FAIL with 400 Bad Request
        RecordPaymentRequest overpay = new RecordPaymentRequest(new BigDecimal("600.00"), PaymentMethod.CASH, null, "Overpay test");
        mockMvc.perform(post("/api/businesses/" + bizId + "/invoices/" + invId + "/payments")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(overpay)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"));

        // 7. Record remaining payment (562.00) -> Balance should be 0.00, status PAID
        RecordPaymentRequest payment2 = new RecordPaymentRequest(new BigDecimal("562.00"), PaymentMethod.CARD, "CARD-REF-2002", "Settlement");
        mockMvc.perform(post("/api/businesses/" + bizId + "/invoices/" + invId + "/payments")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payment2)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.paidAmount").value(1062.00))
                .andExpect(jsonPath("$.data.balanceDue").value(0.00))
                .andExpect(jsonPath("$.data.status").value("PAID"));

        // 8. Record Expense (300.00 for Studio Props)
        ExpenseRequest expReq = new ExpenseRequest("Supplies", "Studio Backdrop Props", new BigDecimal("300.00"), LocalDate.now(), "UPI", "Amazon");
        mockMvc.perform(post("/api/businesses/" + bizId + "/expenses")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.amount").value(300.00));

        // 9. Fetch Financial Summary: Invoiced: 1062.00, Collected: 1062.00, Expenses: 300.00, Profit: 762.00
        mockMvc.perform(get("/api/businesses/" + bizId + "/reports/financial-summary")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.invoicedRevenue").value(1062.00))
                .andExpect(jsonPath("$.data.cashCollected").value(1062.00))
                .andExpect(jsonPath("$.data.recordedExpenses").value(300.00))
                .andExpect(jsonPath("$.data.operatingProfit").value(762.00))
                .andExpect(jsonPath("$.data.pendingReceivables").value(0.00));
    }
}
