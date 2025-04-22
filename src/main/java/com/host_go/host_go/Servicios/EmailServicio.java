package com.host_go.host_go.Servicios;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServicio {

    @Autowired
    private JavaMailSender mailSender;

    public void sendActivationEmail(String toEmail, String activationLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("Activa tu cuenta de Host & Go");
            helper.setText(buildHtmlBody(activationLink), true); // true indica que es HTML

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace(); // Puedes loguearlo o lanzar una excepción personalizada si lo prefieres
        }
    }

    private String buildHtmlBody(String link) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <style>
                        body {
                            font-family: 'Poppins', sans-serif;
                            background-color: #f9f9f9;
                            padding: 20px;
                        }
                        .email-container {
                            max-width: 600px;
                            margin: auto;
                            background-color: #ffffff;
                            border-radius: 12px;
                            box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
                            padding: 30px;
                            text-align: center;
                        }
                        .logo {
                            width: 150px;
                            margin-bottom: 20px;
                        }
                        h2 {
                            color: #ff5b2a;
                        }
                        p {
                            color: #333;
                            font-size: 16px;
                            line-height: 1.6;
                        }
                        .boton-activar {
                            display: inline-block;
                            margin-top: 20px;
                            padding: 14px 28px;
                            background-color: #ff7642;
                            color: white;
                            border-radius: 8px;
                            text-decoration: none;
                            font-weight: bold;
                            transition: background 0.3s ease;
                        }
                        .boton-activar:hover {
                            background-color: #ff5b2a;
                        }
                        .footer {
                            margin-top: 30px;
                            font-size: 12px;
                            color: #888;
                        }
                    </style>
                </head>
                <body>
                    <div class="email-container">
                        <img class="logo" src="https://i.postimg.cc/hvfRp9Rq/host-Go-Logo-And-Name.png" alt="Host & Go Logo" />
                        <h2>¡Bienvenido a Host & Go!</h2>
                        <p>Gracias por registrarte. Para completar el proceso, por favor activa tu cuenta haciendo clic en el botón:</p>
                        <a class="boton-activar" href="%s">Activar cuenta</a>
                        <p>O copia y pega este enlace en tu navegador:<br><small>%s</small></p>
                        <div class="footer">Si no fuiste tú quien intentó registrarse, puedes ignorar este mensaje.</div>
                    </div>
                </body>
                </html>
                """.formatted(link, link);
    }
}
