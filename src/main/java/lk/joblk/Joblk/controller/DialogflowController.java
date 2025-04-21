package lk.joblk.Joblk.controller;

import lk.joblk.Joblk.service.DialogflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/chatbot")

public class DialogflowController {

    private final DialogflowService dialogflowService;

    public DialogflowController(DialogflowService dialogflowService) {
        this.dialogflowService = dialogflowService;
    }

    @PostMapping("/message")
    public ResponseEntity<String> sendMessage(@RequestBody Map<String, String> payload) {
        System.out.println("Running chatbot");
        System.out.println("Payload message: " + payload);
        try {
            String message = payload.get("message");
            String response = dialogflowService.detectIntentTexts(
                    "joblk-support-team-gvcb",
                    message,
                    UUID.randomUUID().toString(),
                    "en"
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}
