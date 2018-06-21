package com.company.Q1;

/**
 * Created by tianqiliu on 2018-06-18.
 */
public class Room {

    private String type;
    private double price;
    private boolean availability;

    public Room(String type) throws IllegalArgumentException {

        // initialize price based on tye of room
        if (type.equals("double")) {
            this.price = 90;
        } else if (type.equals("queen")) {
            this.price = 110;
        } else if (type.equals("king")) {
            this.price = 150;
        } else {
            throw new IllegalArgumentException("type " + type + " is not supported");
        }

        this.type = type;
        this.availability = true;
    }

    // getters
    public String getType() {
        return this.type;
    }

    public double getPrice() {
        return this.price;
    }

    public boolean getAvailability() {
        return this.availability;
    }

    public void changeAvailability() {
        this.availability = !this.availability;
    }

    public static Room findAvailableRoom(Room[] rooms, String typeWanted) {

        // error checking
        if (rooms == null || rooms.length == 0) {
            return null;
        }

        // loop through room array to find the first desired room
        for (int i = 0; i < rooms.length; i++) {
            Room room = rooms[i];
            if (room.type.equals(typeWanted) && room.availability == true) {
                return room;
            }
        }

        // reaching here means no desired room was found
        return null;
    }

}
