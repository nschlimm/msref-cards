/**
 * 
 */
package com.nschlimm.cards.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.nschlimm.cards.config.CardsConfigService;
import com.nschlimm.cards.model.Cards;
import com.nschlimm.cards.model.Customer;
import com.nschlimm.cards.model.Properties;
import com.nschlimm.cards.repository.CardsRepository;

/**
 * @author Niklas Schlimm
 *
 */

@RestController
public class CardsController {

    
    private static final Logger log = LoggerFactory.getLogger(CardsController.class);

    @Autowired
    private CardsRepository cardsRepository;

    @Autowired
    private CardsConfigService config;

    @PostMapping("/myCards")
    public List<Cards> getCardDetails(@RequestHeader("schlimmbank-correlation-id") String correlationId,@RequestBody Customer customer) {
        log.info("getCardDetails() method started");
        List<Cards> cards = cardsRepository.findByCustomerId(customer.getCustomerId());
        log.info("getCardDetails() method ended");
        if (cards != null) {
            return cards;
        } else {
            return null;
        }

    }

    @GetMapping("/cards/properties")
    public String getPropertyDetails() throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        Properties properties = new Properties(config.getMsg(), config.getBuildVersion(), config.getMailDetails(),
                config.getActiveBranches());
        String jsonStr = ow.writeValueAsString(properties);
        return jsonStr;
    }

}
