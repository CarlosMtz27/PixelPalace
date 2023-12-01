package com.uacm.pixelpalace.service;

import org.springframework.stereotype.Component;

//En este componente definimos el cuerpo que tendra nuestro correo
@Component
public class EmailBodyBuilder {

    public String buildBody(String body) {
        String htmlBody = "<!DOCTYPE html>"//Creamos la estructura del cuerpo del correo
                + "<html lang=\"es\">"
                + "<head>"
                + "<meta charset=\"UTF-8\">"
                + "<title>Correo de Verificación</title>"
                + "</head>"
                + "<body>"
                + "<div style=\"text-align: center; padding: 20px;\">"
                + "</div>"
                + "<div style=\"background-color: #f4f4f4; padding: 20px; border-radius: 5px;\">"
                + "<h2>¡Correo de Verificación!</h2>"
                + "<p>Estimado usuario,</p>"
                + "<p>Por favor, ingresa el siguiente código: <strong>" + body + "</strong> para verificar tu cuenta:</p>"
                + "<p>¡Gracias!</p>"
                + "<p><em>Bungie Connect Solutions</em></p>"
                + "</div>"
                + "</body>"
                + "</html>";

        return htmlBody;
    }
}
