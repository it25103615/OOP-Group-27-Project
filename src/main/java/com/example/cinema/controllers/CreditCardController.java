package com.example.cinema.controllers;

import com.example.cinema.models.CreditCard;
import com.example.cinema.repositories.CreditCardRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/creditcards")
public class CreditCardController {

    @Autowired
    private CreditCardRepository creditCardRepository;

    // Get all credit cards
    @GetMapping
    public List<CreditCard> getAllCards() {
        return creditCardRepository.findAll();
    }

    // Get credit card by ID
    @GetMapping("/{id}")
    public Optional<CreditCard> getCardById(@PathVariable Long id) {
        return creditCardRepository.findById(id);
    }

    // Add new credit card
    @PostMapping
    public CreditCard createCard(@RequestBody CreditCard creditCard) {
        return creditCardRepository.save(creditCard);
    }

    // Update credit card
    @PutMapping("/{id}")
    public CreditCard updateCard(@PathVariable Long id, @RequestBody CreditCard updatedCard) {

        return creditCardRepository.findById(id)
                .map(card -> {
                    card.setCardHolderName(updatedCard.getCardHolderName());
                    card.setCardNumber(updatedCard.getCardNumber());
                    card.setExpiry(updatedCard.getExpiry());
                    card.setCvv(updatedCard.getCvv());

                    return creditCardRepository.save(card);
                })
                .orElseThrow(() -> new RuntimeException("Card not found"));
    }

    // Delete credit card
    @DeleteMapping("/{id}")
    public String deleteCard(@PathVariable Long id) {
        creditCardRepository.deleteById(id);
        return "Credit card deleted successfully";
    }
}