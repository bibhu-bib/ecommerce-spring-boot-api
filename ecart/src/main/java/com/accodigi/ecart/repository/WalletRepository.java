package com.accodigi.ecart.repository;

import com.accodigi.ecart.model.PaymentInfo;
import com.accodigi.ecart.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<PaymentInfo, Long> {

    Optional<PaymentInfo> findOneByWalletIdAndPaymentModeAndUser(String walletId, String paymentMode, User user);

}
