package com.clinic.service;

import com.clinic.model.Room;
import com.clinic.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    public Room save(Room room) {
        return roomRepository.save(room);
    }

    public java.util.Optional<Room> findById(Long id) {
        return roomRepository.findById(id);
    }

    public void delete(Long id) {
        roomRepository.deleteById(id);
    }
}
