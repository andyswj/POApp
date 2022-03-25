package edu.nus.iss.POApp.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import edu.nus.iss.POApp.model.Order;
import edu.nus.iss.POApp.model.Quotation;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class QuotationService {

    public static final String URL = "https://quotation.chuklee.com/quotation";
   
    public Order saveItems(String data) {

        Order order = new Order();
        Map<String, Integer> items = new HashMap<String, Integer>();
        
        try(InputStream is = new ByteArrayInputStream(data.getBytes())) {

            JsonReader r = Json.createReader(is);
            JsonObject obj = r.readObject();;

            order.setName(obj.getString("name"));
            order.setAddress(obj.getString("address"));
            order.setEmail(obj.getString("email"));

            JsonArray arrayOfItems = obj.getJsonArray("lineItems");

            arrayOfItems.stream()
                .map(v -> (JsonObject) v)
                .forEach(
                    v -> items.put(v.getString("item"), v.getInt("quantity"))
                );
        } catch(IOException e) {
            e.printStackTrace();
        }

        order.setLineItems(items);
        
        // System.out.println("Name: " + order.getName());

        return order;
    }

    public List<String> getItems(Order order) {
        List<String> items = new ArrayList<String>(order.getLineItems().keySet());

        // for(String i : items) {
        //     System.out.println("Fruits: " + i);
        // }

        return items;
    }

    public Optional<Quotation> getQuotations(List<String> items) {

        //Build Json
        JsonArrayBuilder b = Json.createArrayBuilder();
        for(String item : items) {
            b.add(item);
        } 
        JsonArray arrayOfItems = b.build();
        
        //RequestEntity
        RequestEntity<String> req = RequestEntity
            .post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .body(arrayOfItems.toString(), String.class);

        RestTemplate template = new RestTemplate();

        try {
            ResponseEntity<String> resp = template.exchange(req, String.class);
            System.out.println("Response: " + resp.toString());
            Quotation quotation = new Quotation();
    
            if(resp.getStatusCodeValue() == 200) {
                
                try(InputStream is = new ByteArrayInputStream(resp.getBody().getBytes())) {
                    JsonReader r = Json.createReader(is);
                    JsonObject obj = r.readObject();
    
                    quotation.setQuoteId(obj.getString("quoteId"));
                    JsonArray arrayOfQuotation = obj.getJsonArray("quotations");
    
                    // System.out.println("QuoteId: " + quotation.getQuoteId());
    
                    arrayOfQuotation.stream()
                        .map(v -> (JsonObject) v)
                        .forEach( v -> {   
                            quotation.addQuotation(v.getString("item"), (float) v.getJsonNumber("unitPrice").doubleValue());
                        });
    
                    // System.out.println(quotation.getQuotations().keySet().toString());
    
                    return Optional.of(quotation);
    
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (RestClientException e) {
            System.out.println("Error");
            return Optional.empty();
        }
    
        return Optional.empty();
    }
}
