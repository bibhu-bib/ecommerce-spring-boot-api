package com.accodigi.ecart.controller;

import com.accodigi.ecart.dto.AddToCartRequest;
import com.accodigi.ecart.dto.CheckOutRequest;
import com.accodigi.ecart.dto.Item;
import com.accodigi.ecart.exception.EmptyCartException;
import com.accodigi.ecart.exception.InvalidPaymentModeException;
import com.accodigi.ecart.exception.UserNotFoundException;
import com.accodigi.ecart.model.cart.Cart;
import com.accodigi.ecart.model.order.Order;
import com.accodigi.ecart.service.ShoppingCartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.InputStream;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ShoppingControllerTest {

    ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ShoppingCartService shoppingCartService;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testAddItemsToCart() throws Exception {
        List<Item> products = List.of(new Item(2L, 2L));
        AddToCartRequest addToCartRequest = new AddToCartRequest(1L, products);
        addToCartRequest.setUserId(1L);

        final InputStream inputStream = loadJsonFile("cart.json");
        Cart cart = objectMapper.readValue(inputStream, Cart.class);
        when(shoppingCartService.addCartItem(addToCartRequest)).thenReturn(cart);

        mockMvc.perform(post("/api/1.0/carts/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addToCartRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.id").value(1))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testAddItemsToCart_WhenInvalidUser() throws Exception {
        List<Item> products = List.of(new Item(2L, 2L));
        AddToCartRequest addToCartRequest = new AddToCartRequest(1L, products);
        addToCartRequest.setUserId(99L);

        when(shoppingCartService.addCartItem(addToCartRequest))
                .thenThrow(new UserNotFoundException(99L));

        mockMvc.perform(post("/api/1.0/carts/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addToCartRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("There is no user exists with ID 99"));
    }


    @Test
    void testCheckOut() throws Exception {
        CheckOutRequest checkOutRequest = new CheckOutRequest(1L, "UPI", "1234@natwest");
        final InputStream inputStream = loadJsonFile("order.json");
        Order expectedOrder = objectMapper.readValue(inputStream, Order.class);

        when(shoppingCartService.checkout(checkOutRequest)).thenReturn(expectedOrder);

        mockMvc.perform(post("/api/1.0/carts/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(checkOutRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.paymentReferenceId").exists())
                .andExpect(jsonPath("$.paymentReferenceId").value("948f8934-bb39-4fba-ab80-cb0144cd8551"));

    }

    @Test
    void testCheckOut_WhenInvalidUser() throws Exception {
        CheckOutRequest request = new CheckOutRequest(7L, "UPI", "1234@natwest");
        when(shoppingCartService.checkout(request))
                .thenThrow(new EmptyCartException("Shopping cart is empty"));

        mockMvc.perform(post("/api/1.0/carts/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request).getBytes()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("Shopping cart is empty"));
    }

    @Test
    void testCheckOut_WhenInvalidPaymentMode() throws Exception {
        CheckOutRequest request = new CheckOutRequest(1L, "Net Banking", "1234@natwest");
        when(shoppingCartService.checkout(request))
                .thenThrow(new InvalidPaymentModeException("Payment mode not supported"));

        mockMvc.perform(post("/api/1.0/carts/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request).getBytes()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("Payment mode not supported"));
    }

    @Test
    void testCheckOut_WhenInvalidWallet() throws Exception {
        CheckOutRequest request = new CheckOutRequest(1L, "UPI", "1234@natwestZXC");
        when(shoppingCartService.checkout(request))
                .thenThrow(new InvalidPaymentModeException("Invalid payment mode"));

        mockMvc.perform(post("/api/1.0/carts/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("Invalid payment mode"));
    }


    public InputStream loadJsonFile(String fileName) {
        ClassLoader classLoader = this.getClass().getClassLoader();
        return classLoader.getResourceAsStream(fileName);
    }
}
