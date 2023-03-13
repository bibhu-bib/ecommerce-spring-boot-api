package com.accodigi.ecart.service;

import com.accodigi.ecart.exception.InvalidPaymentModeException;
import com.accodigi.ecart.exception.PaymentGatewayFailure;
import com.accodigi.ecart.model.PaymentInfo;
import com.accodigi.ecart.model.User;
import com.accodigi.ecart.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {
    private final WalletRepository walletRepository;
    private final String SUPPORTED_PAYMENT_MODE = "UPI";
    private final String TEST_WALLET_ID = "1234@natwest";
    private final Long TEST_USER_ID = 1L;

    public String processPayment(PaymentInfo paymentInfo) {
        if (!Objects.equals(paymentInfo.getPaymentMode(), "UPI")) {
            throw new InvalidPaymentModeException("Payment mode not supported");
        }

        //Mocked Payment gateway repose
        if (SUPPORTED_PAYMENT_MODE.equals(paymentInfo.getPaymentMode())
                && TEST_WALLET_ID.equalsIgnoreCase(paymentInfo.getWalletId())
                && TEST_USER_ID.longValue() == paymentInfo.getUser().getId()) {
            return UUID.randomUUID().toString();
        }

        throw new PaymentGatewayFailure("Unable to process the payment");
    }

    public Optional<PaymentInfo> fetchPaymentInfo(User user, String paymentMode, String walletId) {
        return walletRepository.findOneByWalletIdAndPaymentModeAndUser(walletId, paymentMode, user);
    }

}
