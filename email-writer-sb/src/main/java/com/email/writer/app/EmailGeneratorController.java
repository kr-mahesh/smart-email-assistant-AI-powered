package com.email.writer.app;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
@AllArgsConstructor
@CrossOrigin(origins = "*")
//Basically the request from the backend to 8080 url has been blocked from 5173(front end url)
//whenever we try to do an API request from one domain to another modern browers will usually block it
//useless we are setting the cross policy in our backend and explicitly mentioning that we are
//want to allow the API request coming in from the front end.This is configured through @Crossorigin.
public class EmailGeneratorController {
    private final EmailGeneratorService emailGeneratorService; //Autowiring service class
    @PostMapping("/generate")
    public ResponseEntity<String> generateEmail(@RequestBody EmailRequest emailRequest){
        String response =emailGeneratorService.generateEmailReply(emailRequest);
        return ResponseEntity.ok(response);

    }


}
