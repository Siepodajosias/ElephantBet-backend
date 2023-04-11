package com.eburtis.ElephantBet.service;


import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MailService {

    @Value("${sendgrid.api.key}")
    private  String apiKey;

    @Value("${sendgrid.email.from}")
    private  String emailFrom;

    public void envoyerMail(String text,String email,String object) throws IOException {
        Email from = new Email(emailFrom);
        Email to = new Email(email);
        Content content = new Content("text/html", text);
        Mail mail = new Mail(from, object, to, content);
        SendGrid sg = new SendGrid(apiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getBody());
        } catch (IOException ex) {
            throw ex;
        }
    }

    public void envoyerMailDeValidation(String text){
        List<String> destinataires = new ArrayList<>();
        destinataires.add("traoreabdoulaziz54@gmail.com");
        destinataires.add("alain-lucas.koutouan@eburtis.ci");
        destinataires.add("bethuel.koffi@eburtis.ci");
        destinataires.add("josias.sie@eburtis.ci");
        destinataires.forEach(destinataire->{
            try {
                this.envoyerMail(text,destinataire,"Enregistrement du fichier CSV");
            } catch (IOException e) {
                throw new RuntimeException("Erreur lors de l'envoi du mail");
            }

        });

    }


}
