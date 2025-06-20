package com.hana.client_service.repository;

import com.hana.client_service.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client,Long> {
    Optional<Client> findByEmail(String email);

    Optional<Client> findByPhoneNumber(String phoneNumber);
}
