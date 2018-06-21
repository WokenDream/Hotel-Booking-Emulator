package com.company.Q1;

/**
 * Created by tianqiliu on 2018-06-18.
 */
public class Reservation {

    private String name;
    private Room roomReserved;

    public Reservation(Room room, String name) {
        this.roomReserved = room;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public Room getRoom() {
        return this.roomReserved;
    }
}
