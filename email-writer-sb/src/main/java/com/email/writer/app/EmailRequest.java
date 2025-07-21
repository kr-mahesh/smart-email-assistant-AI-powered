package com.email.writer.app;

import lombok.Data;


@Data  //it will help in generate the Getters,setters and Constructor
public class EmailRequest {
    private String emailContent;
    private String tone;

}
