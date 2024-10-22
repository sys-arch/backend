package com.equipo3.reuneme.service;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Service;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;

@Service
public class EmailService {
    
    public void enviarEmail(String email, String asunto, String mensaje) {
        Resend resend = new Resend("re_MQy2TPNB_7Gga2xuXUNao1LsaQGEkzrS5");

        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("ReuneMe <noreply@dev.swey.net>")
                .to(email)
                .subject(asunto)
                .html(mensaje)
                .build();

         try {
            CreateEmailResponse data = resend.emails().send(params);
            data.getId();
        } catch (ResendException e) {
            e.printStackTrace();
        }
    }

	public boolean validarEmail(String email) {
        EmailValidator validator = EmailValidator.getInstance();
        return validator.isValid(email);
	}
    
}

