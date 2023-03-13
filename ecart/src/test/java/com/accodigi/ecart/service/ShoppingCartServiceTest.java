package com.accodigi.ecart.service;

import com.accodigi.ecart.dto.AddToCartRequest;
import com.accodigi.ecart.dto.CheckOutRequest;
import com.accodigi.ecart.dto.Item;
import com.accodigi.ecart.exception.EmptyCartException;
import com.accodigi.ecart.exception.InvalidPaymentModeException;
import com.accodigi.ecart.exception.ProductNotFoundException;
import com.accodigi.ecart.exception.UserNotFoundException;
import com.accodigi.ecart.model.PaymentInfo;
import com.accodigi.ecart.model.Product;
import com.accodigi.ecart.model.User;
import com.accodigi.ecart.model.cart.Cart;
import com.accodigi.ecart.model.cart.CartItem;
import com.accodigi.ecart.model.order.Order;
import com.accodigi.ecart.repository.ShoppingCartRepository;
import com.accodigi.ecart.utils.TestDataGen;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceTest {

    private static final Long TEST_USER_ID = 1L;
    private static final String PAYMENT_ID = "MOCKED_PAYMENT_REF_ID";
    AddToCartRequest addToCartRequest;
    CheckOutRequest checkOutRequest;
    @Mock
    private UserService userService;
    @Mock
    private PaymentService paymentService;
    @Mock
    private ProductService productService;
    @Mock
    private OrderService orderService;
    @Mock
    private ShoppingCartRepository cartRepository;
    private ShoppingCartService service;

    @BeforeEach
    void setUp() {
        service = new ShoppingCartService(userService, paymentService, productService, orderService, cartRepository);
        List<Item> products = List.of(new Item(5L, 1L));
        addToCartRequest = new AddToCartRequest(TEST_USER_ID, products);

        checkOutRequest = new CheckOutRequest(1L, "UPI", "123@okhdfcbank");
    }

    @Test
    void addCartItem_WhenInvalidUser_ShouldThrowException() {

        when(userService.getUser(TEST_USER_ID))
                .thenThrow(UserNotFoundException.class);

        Assertions.assertThrows(UserNotFoundException.class, () -> {
            service.addCartItem(addToCartRequest);
        });
    }

    @Test
    void addCartItem_WhenCartItemsEmpty_ShouldThrowException() {
        AddToCartRequest addToCartRequest = new AddToCartRequest(TEST_USER_ID, Collections.emptyList());

        when(userService.getUser(TEST_USER_ID)).thenReturn(TestDataGen.getUser());
        Assertions.assertThrows(ProductNotFoundException.class, () -> {
            service.addCartItem(addToCartRequest);
        });
    }


    @Test
    void addCartItem_WhenExistingCartEmpty() {
        User mockUser = TestDataGen.getUser();
        when(userService.getUser(TEST_USER_ID)).thenReturn(mockUser);
        when(cartRepository.findOneByUser(mockUser)).thenReturn(Optional.empty());

        Product mockProduct = TestDataGen.getProductTestData(1L);
        when(productService.getProduct(1L)).thenReturn(mockProduct);

        Cart cart = TestDataGen.getMockCart(mockProduct, mockUser);
        when(cartRepository.save(any())).thenReturn(cart);

        Cart resultCart = service.addCartItem(addToCartRequest);
        assertEquals(resultCart.getCartItems().size(),
                addToCartRequest.getProducts().size());
        assertEquals(resultCart.getUser(), mockUser);
    }


    @Test
    void addCartItem_GivenExistingCartContainsSameProduct_ShouldUpdateQuantity() {
        User mockUser = TestDataGen.getUser();
        when(userService.getUser(TEST_USER_ID)).thenReturn(mockUser);

        Product mockProduct = TestDataGen.getProductTestData(1L);
        Cart mockCart = TestDataGen.getMockCart(mockProduct, mockUser);
        when(cartRepository.findOneByUser(mockUser)).thenReturn(Optional.of(mockCart));

        Cart cart = new Cart();
        CartItem cartItem = new CartItem();
        cartItem.setProduct(mockProduct);
        cartItem.setQuantity(10L);
        cart.setUser(mockUser);
        cart.setCartItems(mockCart.getCartItems());

        when(cartRepository.save(any())).thenReturn(cart);

        Cart resultCart = service.addCartItem(addToCartRequest);
        assertEquals(resultCart.getCartItems().size(), addToCartRequest.getProducts().size());

        CartItem cartItemResult = (CartItem) resultCart.getCartItems().toArray()[0];
        assertEquals(10L, cartItemResult.getQuantity());
    }

    @Test
    void addCartItem_GivenExistingCartDoesNotContainsSameProduct_ShouldAddNewProductToCart() {
        User mockUser = TestDataGen.getUser();
        when(userService.getUser(TEST_USER_ID)).thenReturn(mockUser);

        Product mockProduct = TestDataGen.getProductTestData(1L);
        Cart mockCart = TestDataGen.getMockCart(mockProduct, mockUser);
        when(cartRepository.findOneByUser(mockUser)).thenReturn(Optional.of(mockCart));

        Product product2 = TestDataGen.getProductTestData(2L);

        Cart cart = new Cart();
        CartItem cartItem1 = new CartItem();
        cartItem1.setProduct(mockProduct);
        cartItem1.setQuantity(5L);
        CartItem cartItem2 = new CartItem();
        cartItem2.setProduct(product2);
        cartItem2.setQuantity(5L);
        cart.setUser(mockUser);
        cart.setCartItems(Set.of(cartItem1, cartItem2));

        when(cartRepository.save(any())).thenReturn(cart);

        Cart resultCart = service.addCartItem(addToCartRequest);
        assertEquals(2L, resultCart.getCartItems().size());
    }

    @Test
    void checkout_GivenUserInvalid_ShouldThrowException() {


        when(userService.getUser(TEST_USER_ID))
                .thenThrow(UserNotFoundException.class);

        Assertions.assertThrows(UserNotFoundException.class, () -> {
            service.checkout(checkOutRequest);
        });
    }

    @Test
    void checkout_GivenUserCartIsEmpty_ShouldThrowException() {
        User mockUser = TestDataGen.getUser();
        when(userService.getUser(TEST_USER_ID)).thenReturn(mockUser);

        when(cartRepository.findOneByUser(mockUser))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EmptyCartException.class, () -> {
            service.checkout(checkOutRequest);
        });
    }


    @Test
    void checkout_GivenUsersPaymentInfoInvalid_ShouldThrowException() {
        User mockUser = TestDataGen.getUser();
        when(userService.getUser(TEST_USER_ID)).thenReturn(mockUser);

        Cart cart = TestDataGen.getMockCart(TestDataGen.getProductTestData(1L), mockUser);
        when(cartRepository.findOneByUser(mockUser)).thenReturn(Optional.of(cart));

        when(paymentService.fetchPaymentInfo(mockUser, checkOutRequest.getPaymentMode(), checkOutRequest.getWalletId()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(InvalidPaymentModeException.class, () -> {
            service.checkout(checkOutRequest);
        });
    }


    @Test
    void checkout_GivenUserPaymentOptionsAreValid() {
        User mockUser = TestDataGen.getUser();
        when(userService.getUser(TEST_USER_ID)).thenReturn(mockUser);

        Cart cart = TestDataGen.getMockCart(TestDataGen.getProductTestData(1L), mockUser);
        when(cartRepository.findOneByUser(mockUser)).thenReturn(Optional.of(cart));

        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setUser(mockUser);
        paymentInfo.setPaymentMode("UPI");
        paymentInfo.setWalletId("123@okhdfcbank");

        when(paymentService.fetchPaymentInfo(mockUser, checkOutRequest.getPaymentMode(), checkOutRequest.getWalletId()))
                .thenReturn(Optional.of(paymentInfo));

        when(paymentService.processPayment(paymentInfo)).thenReturn(PAYMENT_ID);
        Order order = new Order();
        order.setPaymentReferenceId(PAYMENT_ID);
        when(orderService.createOrder(cart, PAYMENT_ID)).thenReturn(order);

        Order resultOrder = service.checkout(checkOutRequest);
        assertEquals(PAYMENT_ID, resultOrder.getPaymentReferenceId());
    }

}