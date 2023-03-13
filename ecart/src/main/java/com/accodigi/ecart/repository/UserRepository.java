package com.accodigi.ecart.repository;

import com.accodigi.ecart.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
