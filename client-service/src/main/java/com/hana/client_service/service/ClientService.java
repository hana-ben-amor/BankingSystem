package com.hana.client_service.service;

import com.hana.client_service.dto.AccountDTO;
import com.hana.client_service.entity.Client;
import com.hana.client_service.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private WebClient webClient;
    public Client createClient(Client client)
    {
        return clientRepository.save(client);
    }
    public List<Client> getAll()
    {
        return clientRepository.findAll();
    }

    public Optional<Client> findById(Long id)
    {
        return clientRepository.findById(id);
    }

    public Client update(Long id,Client updateClient)
    {
        Client existing=clientRepository.findById(id).orElseThrow();
        existing.setFirstname(updateClient.getFirstname());
        existing.setLastname(updateClient.getLastname());
        existing.setEmail(updateClient.getEmail());
        existing.setAddress(updateClient.getAddress());
        existing.setPhoneNumber(updateClient.getPhoneNumber());
        clientRepository.save(existing);
        return existing;
    }

    public void delete(Long id) {
        Client existing = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        clientRepository.delete(existing);
    }

    public Optional<Client> findByEmail(String email) {
        return clientRepository.findByEmail(email);
    }
    public Optional<Client> findByPhoneNumber(String phoneNumber) {
        return clientRepository.findByPhoneNumber(phoneNumber);
    }

    public void sendCodeEmail(String email) {
        Optional<Client> optionalClient = clientRepository.findByEmail(email);

        Client client = optionalClient.get();

        String code = String.valueOf((int)(Math.random() * 900000) + 100000); // 6 chiffres
        System.out.println("Code de confirmation : " + code);

        // Tu peux remplacer cette partie par un appel à Twilio, OVH, ou Mailjet...

            System.out.println("✉️ Email envoyé à " + client.getEmail() + " avec code " + code);
    }


    public void sendCodePhone(String phone) {
        Optional<Client> optionalClient = clientRepository.findByPhoneNumber(phone);

        Client client = optionalClient.get();

        String code = String.valueOf((int)(Math.random() * 900000) + 100000); // 6 chiffres
        System.out.println("Code de confirmation : " + code);

        // Tu peux remplacer cette partie par un appel à Twilio, OVH, ou Mailjet...

        System.out.println("✉️ SMS envoyé à " + client.getPhoneNumber() + " avec code " + code);
    }

    public List<AccountDTO> getAccountsByClientId(Long clientId) {
        return webClient.get()
                .uri("/client/{clientId}", clientId)
                .retrieve()
                .bodyToFlux(AccountDTO.class)
                .collectList()
                .block();
    }



}
