package edu.nus.iss.POApp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.nus.iss.POApp.model.Order;
import edu.nus.iss.POApp.model.Quotation;
import edu.nus.iss.POApp.service.QuotationService;
import jakarta.json.Json;
import jakarta.json.JsonObject;

@RestController
public class PurchaseOrderRestController {

    @Autowired
    private QuotationService quotationSvc;

    @PostMapping(path="/api/po", consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getOrder(@RequestBody String payload) {

        Order order = quotationSvc.saveItems(payload);
        List<String> items = quotationSvc.getItems(order);
        Optional<Quotation> opt = quotationSvc.getQuotations(items);

        if(opt.isPresent()) {
            Float total = 0f;

            for(String item : items) {
                total += order.getQuantity(item) * opt.get().getQuotation(item);
            }

            JsonObject obj = Json.createObjectBuilder()
                .add("invoiceId", opt.get().getQuoteId())
                .add("name", order.getName())
                .add("total", total)
                .build();

            return ResponseEntity.ok(obj.toString());
            
        } else {
            JsonObject obj = Json.createObjectBuilder().build();
            
            return ResponseEntity.badRequest()
                .body(obj.toString());
        }
        
    }
    
}
