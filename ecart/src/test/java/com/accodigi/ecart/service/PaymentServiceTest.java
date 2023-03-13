package com.accodigi.ecart.service;

import com.accodigi.ecart.exception.InvalidPaymentModeException;
import com.accodigi.ecart.exception.PaymentGatewayFailure;
import com.accodigi.ecart.model.PaymentInfo;
import com.accodigi.ecart.model.User;
import com.accodigi.ecart.repository.WalletRepository;
import com.accodigi.ecart.utils.TestDataGen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    private final String TEST_WALLET_ID = "1234@natwest";
    private final Long TEST_USER_ID = 1L;
    private final String SUPPORTED_PAYMENT_MODE = "UPI";
    PaymentService paymentService;
    @Mock
    private WalletRepository walletRepository;

    @BeforeEach
    void setUp() {
        paymentService = new PaymentService(walletRepository);
    }

    @Test
    void processPayment_successfulPayment() {
        User user = new User();
        user.setId(TEST_USER_ID);

        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setPaymentMode(SUPPORTED_PAYMENT_MODE);
        paymentInfo.setWalletId(TEST_WALLET_ID);
        paymentInfo.setUser(TestDataGen.getUser());

        String paymentId = paymentService.processPayment(paymentInfo);
        assertNotNull(paymentId);
    }

    @Test
    void processPayment_invalidPaymentMode() {
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setPaymentMode("InvalidPaymentMode");

        assertThrows(InvalidPaymentModeException.class, () -> paymentService.processPayment(paymentInfo));
    }


    @Test
    void processPayment_paymentGatewayFailure() {
        User user = new User();
        user.setId(TEST_USER_ID);

        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setPaymentMode(SUPPORTED_PAYMENT_MODE);
        paymentInfo.setWalletId("invalidWalletId");
        paymentInfo.setUser(user);

        assertThrows(PaymentGatewayFailure.class, () -> paymentService.processPayment(paymentInfo));
    }


    @Test
    void fetchPaymentInfo() {

        User user = TestDataGen.getUser();
        String walletId = "1234@natwest";
        String payMode = "UPI";
        when(walletRepository.findOneByWalletIdAndPaymentModeAndUser(walletId, payMode, user)).thenReturn(Optional.of(TestDataGen.paymentInfo()));

        Optional<PaymentInfo> paymentResult = paymentService.fetchPaymentInfo(user, payMode, walletId);
        assertTrue(paymentResult.isPresent());
        assertEquals(9L, paymentResult.get().getId());

    }
}