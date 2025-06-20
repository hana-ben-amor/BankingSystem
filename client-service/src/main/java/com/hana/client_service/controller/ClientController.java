package com.hana.client_service.controller;

import com.hana.client_service.dto.AccountDTO;
import com.hana.client_service.entity.Client;
import com.hana.client_service.exception.ClientNotFoundException;
import com.hana.client_service.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    /**
     * Crée un nouveau client.
     * POST /api/v1/clients
     * Request Body: JSON representation of a Client
     * @param client The client object to create.
     * @return The created Client object with its generated ID.
     */
    @PostMapping
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        Client createdClient = clientService.createClient(client);
        // You might return HttpStatus.CREATED (201) for creation success
        return new ResponseEntity<>(createdClient, HttpStatus.CREATED);
    }

    /**
     * Récupère tous les clients.
     * GET /api/v1/clients
     * @return A list of all clients.
     */
    @GetMapping
    public ResponseEntity<List<Client>> getAllClients() {
        List<Client> clients = clientService.getAll();
        return ResponseEntity.ok(clients);
    }

    /**
     * Récupère un client par son ID.
     * GET /api/v1/clients/{id}
     * @param id The ID of the client to retrieve.
     * @return The client if found, or 404 Not Found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        // Using map and orElse to handle Optional cleanly
        return clientService.findById(id)
                .map(ResponseEntity::ok) // If client is present, return 200 OK with client
                .orElseThrow(); // If not found, throw exception for 404
    }

    /**
     * Met à jour un client existant par son ID.
     * PUT /api/v1/clients/{id}
     * Request Body: JSON representation of the updated Client data
     * @param id The ID of the client to update.
     * @param updateClient The client object with updated data.
     * @return The updated Client object.
     */
    @PutMapping("/{id}") // Changed from /update/{id} to standard RESTful PUT /{id}
    public ResponseEntity<Client> updateClient(@PathVariable Long id, @RequestBody Client updateClient) {
        // The service's orElseThrow will handle not found, so no need for explicit check here
        Client updated = clientService.update(id, updateClient);
        return ResponseEntity.ok(updated);
    }

    /**
     * Supprime un client par son ID.
     * DELETE /api/v1/clients/{id}
     * @param id The ID of the client to delete.
     * @return 204 No Content if successful, or 404 Not Found if client doesn't exist.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        // The service's orElseThrow will handle not found.
        clientService.delete(id);
        return ResponseEntity.noContent().build(); // 204 No Content for successful deletion
    }

    // --- Custom Query Endpoints ---

    /**
     * Récupère un client par son adresse email.
     * GET /api/v1/clients/by-email?email=test@example.com
     * @param email The email address of the client.
     * @return The client if found, or 404 Not Found.
     */
    @GetMapping("/by-email")
    public ResponseEntity<Client> getClientByEmail(@RequestParam String email) {
        return clientService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ClientNotFoundException("Client with email '" + email + "' not found."));
    }

    /**
     * Récupère un client par son numéro de téléphone.
     * GET /api/v1/clients/by-phone?phoneNumber=1234567890
     * @param phoneNumber The phone number of the client.
     * @return The client if found, or 404 Not Found.
     */
    @GetMapping("/by-phone")
    public ResponseEntity<Client> getClientByPhoneNumber(@RequestParam String phoneNumber) {
        return clientService.findByPhoneNumber(phoneNumber)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ClientNotFoundException("Client with phone number '" + phoneNumber + "' not found."));
    }

    @GetMapping("/{id}/accounts")
    public List<AccountDTO> getAccounts(@PathVariable Long id) {
        return clientService.getAccountsByClientId(id);
    }

}
