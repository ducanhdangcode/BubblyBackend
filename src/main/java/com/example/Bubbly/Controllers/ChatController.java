package com.example.Bubbly.Controllers;

import com.example.Bubbly.Entities.Message;
import com.example.Bubbly.Entities.Room;
import com.example.Bubbly.Payload.MessageRequest;
import com.example.Bubbly.Repositories.IRoomRepository;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;

@Controller
@CrossOrigin("http://localhost:3000")
public class ChatController {
    private IRoomRepository roomRepository;

    public ChatController(IRoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    // sending and receiving messages
    @MessageMapping("/sendMessage/{roomId}")
    @SendTo("/topic/room/{roomId}")
    public Message sendMessage (@DestinationVariable String roomId, @RequestBody MessageRequest request) {
        Room room = roomRepository.findByRoomId(request.getRoomId());
        Message message = new Message();
        message.setContent(request.getContent());
        message.setSender(request.getSender());
        message.setTimeStamp(LocalDateTime.now());

        if (room != null) {
            room.getMessages().add(message);
            roomRepository.save(room);
        }

        return message;
    }
}
