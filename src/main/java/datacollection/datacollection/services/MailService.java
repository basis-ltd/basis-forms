package datacollection.datacollection.services;

import com.sendgrid.*;
import datacollection.datacollection.config.AppConfigurations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Component
public class MailService {
    @Autowired
    private AppConfigurations appConfigurations;

    public String sendMail(String to, String subject, String text) throws IOException {
        System.out.println(appConfigurations.getSendgridApiKey());
        SendGrid sendGrid = new SendGrid(appConfigurations.getSendgridApiKey());

        // INITIALIZE CONSTANTS
        Email from = new Email("princeelysee@gmail.com");
        Email receiver = new Email(to);
        Content content = new Content("text/plain", text);

        Mail mail = new Mail(from, subject, receiver, content);

        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        Response response = sendGrid.api(request);
        return response.getBody();
    }
}
