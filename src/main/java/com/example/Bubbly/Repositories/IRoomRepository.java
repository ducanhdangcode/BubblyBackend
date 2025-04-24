package com.example.Bubbly.Repositories;

import com.example.Bubbly.Entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRoomRepository extends JpaRepository<Room, String> {
    // get room using room id
    Room findByRoomId (String roomId);
}
