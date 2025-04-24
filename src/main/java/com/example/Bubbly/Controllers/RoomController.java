package com.example.Bubbly.Controllers;

import com.example.Bubbly.Entities.Message;
import com.example.Bubbly.Entities.Room;
import com.example.Bubbly.Repositories.IRoomRepository;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/rooms")
@CrossOrigin("http://localhost:3000")
public class RoomController {
    private IRoomRepository roomRepository;

    public RoomController(IRoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }
    // create room
    @PostMapping
    public ResponseEntity<?> createRoom (@RequestBody String roomId) {
        if (roomRepository.findByRoomId(roomId) != null) {
            return ResponseEntity.badRequest().body("Room has already existed");
        }

        Room room = new Room();
        room.setId(UUID.randomUUID().toString());
        room.setRoomId(roomId);
        Room savedRoom = roomRepository.save(room);
        return ResponseEntity.status(HttpStatus.CREATED).body(room);
    }

    // get room - join room
    @GetMapping("/{roomId}")
    public ResponseEntity<?> joinRoom (@PathVariable String roomId) {
        Room room = roomRepository.findByRoomId(roomId);
        if (room == null) {
            return ResponseEntity.badRequest().body("Room not found!!");
        }

        return ResponseEntity.ok(room);
    }

    // get messages of room
    @GetMapping("/{roomId}/messages")
    public ResponseEntity<List<Message>> getMessages (@PathVariable String roomId, @RequestParam(value = "page",
            defaultValue = "0", required = false) int page, @RequestParam(value = "size", defaultValue = "20",
            required = false) int size) {
        Room room = roomRepository.findByRoomId(roomId);
        if (room == null) {
            return ResponseEntity.badRequest().build();
        }
        List<Message> messages = room.getMessages();
        int start = Math.max(0, messages.size() - (page+1)*size);
        int end = Math.min(messages.size(), start+size);

        List<Message> paginatedMessages = messages.subList(start, end);

        return ResponseEntity.ok(paginatedMessages);
    }

    // update room
    @PutMapping("/{roomId}")
    public ResponseEntity<Room> updateRoom (@PathVariable String roomId, @RequestBody Room updatedRoom) {
        Room room = roomRepository.findByRoomId(roomId);
        if (room == null) {
            return ResponseEntity.badRequest().build();
        }
        room.setRoomId(updatedRoom.getRoomId());

        Room savedRoom = roomRepository.save(room);
        return ResponseEntity.ok(room);
    }
}
