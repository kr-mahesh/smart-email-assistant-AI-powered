package com.email.writer.app;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class EmailGeneratorService {
    //Going to prepare this particular service to help us make an API call

    private final WebClient webClient;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;
    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public EmailGeneratorService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    } // this will be injected during the runtime by spring

    public String generateEmailReply(EmailRequest emailRequest){
         //1. Build the Prompt - this go as an input to the backend , basically the prompt goes to Gemini api.
        String prompt = buildPrompt(emailRequest);
        //2. Craft a request - because the request is to be in certain format
        //---->Request needs the format as in the body
        Map<String,Object> responseBody = Map.of(
                "contents" , new Object[]{
                        Map.of("parts",new Object[]{
                                Map.of("text",prompt)
                        })
        }
        );

        //3. Do request and get response

        //For making an API Call ,we are going to make use of web client,
        // the way to do this APi request,it is build on top of project React and it
        //enables us to handle asynchronous nonblocking http requests and responses ,it
        //makes the String well suited for modern react web applications
        String response = webClient.post()
                .uri(geminiApiUrl+geminiApiKey)
                .header("Content-Type","application/json")
                .bodyValue(responseBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        //4.Extract Response and  Return
        return extractResponseContent(response);
    }

    private String extractResponseContent(String response) {
        try{
            ObjectMapper mapper = new ObjectMapper();
            //ObjectMapper-->it is a tool from jackson library helps in working with json data,
            //it will read,write and convert json data into java Objects vice-versa.
            JsonNode rootNode = mapper.readTree(response);
            //readTree is a method that turns the json response into  Treelike Structure,
            //Now the treelike structure is represented by jsonNode and represents the entire json tree
            //And using this rootNode(Object of type JsonNode),we can navigate to the entire tree.

            return rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();
            }
        catch(Exception e){
            return "Error processing request: " + e.getMessage();
        }
    }

    private String buildPrompt(EmailRequest emailRequest) {//Crafting a prompt
        StringBuilder prompt = new StringBuilder();
        prompt.append("Generate a professional email reply for the following email content,Please dont generate a Subject line ");
        if(emailRequest.getTone()!=null && !emailRequest.getTone().isEmpty()){
            prompt.append("Use a ").append(emailRequest.getTone()).append(" tone.");
        }
        prompt.append("\nOriginal Email\n").append(emailRequest.getEmailContent());

        return prompt.toString();
    }

}
