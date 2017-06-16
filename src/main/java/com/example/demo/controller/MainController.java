package com.example.demo.controller;

import com.example.demo.model.Message;
import com.example.demo.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {

    private MessageService messageService;

    private static final List<SseEmitter> emitters = new ArrayList<>();

    @Autowired
    public MainController(MessageService messageService) {
        this.messageService = messageService;
    }


    @GetMapping("/")
    public String main(Model model) {
        model.addAttribute("messages", messageService.getAll());

        return "index";
    }

    @GetMapping("/newMessages")
    @ResponseBody
    public SseEmitter getNewMessages() {
        SseEmitter emitter = new SseEmitter();

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitters.add(emitter);

        return emitter;
    }

    @PostMapping(value = "/send")
    @ResponseBody
    public void sendMessage(Message message) throws IOException {
        messageService.save(message);
        for (SseEmitter sseEmitter : emitters) {
            sseEmitter.send(message.getText());
        }
    }
}
