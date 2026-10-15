package com.vyparos.vyparos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vyparos.vyparos.business.dto.CreateBusinessRequest;
import com.vyparos.vyparos.identity.dto.RegisterRequest;
import com.vyparos.vyparos.inventory.dto.ProductRequest;
import com.vyparos.vyparos.inventory.dto.StockMovementRequest;
import com.vyparos.vyparos.inventory.dto.SupplierRequest;
import com.vyparos.vyparos.inventory.entity.MovementType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class InventoryAndStockTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldManageInventoryStockMovementsAndLowStockAlerts() throws Exception {
        // 1. Setup user & business
        RegisterRequest userReq = new RegisterRequest("invowner@test.com", "pass12345", "Manish Inventory", "9876543210");
        String userRes = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        String token = objectMapper.readTree(userRes).path("data").path("token").asText();

        CreateBusinessRequest bizReq = new CreateBusinessRequest("Manish Spa Supplies", "Spa", "INR", "Asia/Kolkata", "Indiranagar", "9876543210", "manish@spa.com");
        String bizRes = mockMvc.perform(post("/api/businesses")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bizReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        long bizId = objectMapper.readTree(bizRes).path("data").path("id").asLong();

        // 2. Add Supplier
        SupplierRequest supReq = new SupplierRequest("Herbal Cosmetics Ltd", "Vikas Gupta", "9898989898", "vikas@herbal.com", "Industrial Area");
        String supRes = mockMvc.perform(post("/api/businesses/" + bizId + "/suppliers")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(supReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        long supId = objectMapper.readTree(supRes).path("data").path("id").asLong();

        // 3. Add Product with initial stock = 0, reorder threshold = 5
        ProductRequest prodReq = new ProductRequest("SKU-OIL-100", "Organic Argan Hair Oil", "Pure Moroccan Argan Oil", "Haircare", supId, new BigDecimal("350.00"), new BigDecimal("700.00"), 0, 5);
        String prodRes = mockMvc.perform(post("/api/businesses/" + bizId + "/products")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(prodReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.currentStock").value(0))
                .andExpect(jsonPath("$.data.lowStock").value(true))
                .andReturn().getResponse().getContentAsString();
        long prodId = objectMapper.readTree(prodRes).path("data").path("id").asLong();

        // 4. Record Stock Receipt (+10 units)
        StockMovementRequest sm1 = new StockMovementRequest(MovementType.RECEIVED, 10, "Purchase Order #PO-1001", "PO-1001");
        mockMvc.perform(post("/api/businesses/" + bizId + "/products/" + prodId + "/stock-movements")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sm1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.previousStock").value(0))
                .andExpect(jsonPath("$.data.newStock").value(10));

        // 5. Attempt Negative Stock reduction (-15 units when stock is 10) -> MUST FAIL with 400 Bad Request
        StockMovementRequest overReduce = new StockMovementRequest(MovementType.DAMAGE, -15, "Broken in transit", null);
        mockMvc.perform(post("/api/businesses/" + bizId + "/products/" + prodId + "/stock-movements")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(overReduce)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"));

        // 6. Record Stock Sale (-8 units) -> Stock becomes 2 (low stock trigger)
        StockMovementRequest sm2 = new StockMovementRequest(MovementType.SALE, -8, "Salon counter sale", "INV-1001");
        mockMvc.perform(post("/api/businesses/" + bizId + "/products/" + prodId + "/stock-movements")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sm2)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.previousStock").value(10))
                .andExpect(jsonPath("$.data.newStock").value(2));

        // 7. Check Low Stock Alerts endpoint -> Must contain this product
        mockMvc.perform(get("/api/businesses/" + bizId + "/products/low-stock-alerts")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(prodId))
                .andExpect(jsonPath("$.data[0].currentStock").value(2))
                .andExpect(jsonPath("$.data[0].lowStock").value(true));

        // 8. Duplicate Product SKU prevention -> MUST FAIL with 409 Conflict
        ProductRequest dupSkuReq = new ProductRequest("SKU-OIL-100", "Another Oil", "Description", "Haircare", supId, new BigDecimal("100.00"), new BigDecimal("200.00"), 5, 2);
        mockMvc.perform(post("/api/businesses/" + bizId + "/products")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dupSkuReq)))
                .andExpect(status().isConflict());

        // 9. Duplicate Product Name prevention -> MUST FAIL with 409 Conflict
        ProductRequest dupNameReq = new ProductRequest("SKU-OIL-999", "Organic Argan Hair Oil", "Description", "Haircare", supId, new BigDecimal("100.00"), new BigDecimal("200.00"), 5, 2);
        mockMvc.perform(post("/api/businesses/" + bizId + "/products")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dupNameReq)))
                .andExpect(status().isConflict());

        // 10. Update product details & stock directly via PATCH
        ProductRequest updateReq = new ProductRequest("SKU-OIL-100", "Organic Argan Hair Oil Deluxe", "Pure Moroccan Argan Oil", "Haircare", supId, new BigDecimal("370.00"), new BigDecimal("750.00"), 25, 8);
        mockMvc.perform(patch("/api/businesses/" + bizId + "/products/" + prodId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Organic Argan Hair Oil Deluxe"))
                .andExpect(jsonPath("$.data.sellingPrice").value(750.00))
                .andExpect(jsonPath("$.data.currentStock").value(25))
                .andExpect(jsonPath("$.data.reorderThreshold").value(8));

        // 11. Toggle product status
        mockMvc.perform(patch("/api/businesses/" + bizId + "/products/" + prodId + "/status?active=false")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.active").value(false));
    }
}
