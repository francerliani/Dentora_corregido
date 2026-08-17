package com.example.demo;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "*")
public class AudioController {

    // 🎤 Endpoint viejo (ya casi no lo usamos)
    @PostMapping("/upload-audio")
    public String uploadAudio(@RequestParam("audio") MultipartFile file) {

        try {

            String filePath = "audio_recibido.webm";

            java.nio.file.Files.write(
                java.nio.file.Paths.get(filePath),
                file.getBytes()
            );

            System.out.println("Audio recibido");

            // 🧠 Texto simulado
            String textoSimulado =
                    "caries en 16 y 17 mesial y distal";

            String json =
                    ParserOdontologico.parsear(textoSimulado);

            System.out.println("JSON generado: " + json);

            return json;

        } catch (Exception e) {

            e.printStackTrace();

            return "Error";
        }
    }

    // 🚀 NUEVO ENDPOINT
    @PostMapping("/procesar-texto")
    public String procesarTexto(@RequestBody String texto) {

        System.out.println("Texto recibido: " + texto);

        String json =
                ParserOdontologico.parsear(texto);

        System.out.println("JSON generado: " + json);

        return json;
    }
}