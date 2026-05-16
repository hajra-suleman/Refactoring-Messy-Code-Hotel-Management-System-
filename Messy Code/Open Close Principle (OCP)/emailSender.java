package Mail;

import com.sendgrid.*;

import java.io.IOException;

public class emailSender {
    private final String apiKey;

    public emailSender(String apiKey) {
        this.apiKey = apiKey;
    }

    public void emailConfigurations(String toEmail, String subject, String content) {
        Email from = new Email("rana228safi@gmail.com");
        Email to = new Email(toEmail);
        Content emailContent = new Content("text/plain", content);
        Mail mail = new Mail(from, subject, to, emailContent);

        SendGrid sg = new SendGrid(apiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);
            System.out.println("Status Code: " + response.getStatusCode());

            if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
                System.out.println("Email sent successfully!");
            } else {
                System.out.println("Failed to send email. Body: " + response.getBody());
            }

        } catch (IOException ex) {
            System.err.println("Error sending email: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void sendEmail(String toEmail, String subject, String content) {
        String apiKey = System.getenv("SENDER_GRID_API_KEY");
        emailSender emailService = new emailSender(apiKey);
        emailService.emailConfigurations(toEmail, subject, content);
    }


}
