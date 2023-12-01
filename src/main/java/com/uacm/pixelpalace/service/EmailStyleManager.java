package com.uacm.pixelpalace.service;

import org.springframework.stereotype.Component;
//En este componente definimos los estilos para nuestra estructura para el correo
@Component
public class EmailStyleManager {

    public String addStylesToBody(String body) {
        String styles = "<style>"
                + "body { font-family: Arial, sans-serif; }"
                + "h2 { color: #3498db; }"
                + "p { font-size: 16px; color: #333; }"
                + "strong { font-weight: bold; }"
                + "em { font-style: italic; font-weight: bold; }"
                + "</style>";

        return styles + body;
    }
}
