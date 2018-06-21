package com.company.Q1;

import java.io.*;
import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * Created by tianqiliu on 2018-06-18.
 */
public class Hotel {

    private String name;
    private Room[] rooms;
    private Reservation[] reservations;

    public Hotel(String name, Room[] rooms) {
        this.name = name;

        if (rooms == null) {
            return;
        }

        // copy each room reference as instructed
        this.rooms = new Room[rooms.length];
        for (int i = 0; i < rooms.length; i++) {
            this.rooms[i] = rooms[i];
        }

        this.reservations = new Reservation[0];
    }

    private void addReservation(Reservation reservation) {
        // make a copy of the original reservation array
        // and extend it by one element longer
        this.reservations = Arrays.copyOf(this.reservations, this.reservations.length + 1);

        // put the given reservation at the end
        this.reservations[this.reservations.length - 1] = reservation;

    }

    private void removeReservation(String nameReservedFor, String roomType) throws NoSuchElementException {

        // loop through all the reservations to find a match
        for (int i = 0; i < this.reservations.length; i++) {

            Reservation reservation = this.reservations[i];
            String reservationName = reservation.getName();
            Room roomReserved = reservation.getRoom();

            if (reservationName.equals(nameReservedFor) && roomReserved.getType().equals(roomType)) {
                // make a new array of reservations that excludes this reservation
                // and let this.reservations array points to the new array
                // finally mark room available
                Reservation[] updatedReservations = new Reservation[this.reservations.length - 1];
                System.arraycopy(this.reservations, 0, updatedReservations, 0, i);
                System.arraycopy(this.reservations, i + 1, updatedReservations, i, this.reservations.length - i - 1);

                this.reservations = updatedReservations;
                roomReserved.changeAvailability();

                return;
            }
        }

        throw new NoSuchElementException("There's no reservation for a " + roomType + " room under the name of " + nameReservedFor + ".");
    }

    public int createReservation(String nameReservedFor, String roomType) {

        // look for an available matching room
        Room room = Room.findAvailableRoom(this.rooms, roomType);
        if (room == null) {
            return -1;
        }

        // update room's availability and add to reservation
        room.changeAvailability();
        addReservation(new Reservation(room, nameReservedFor));
        return this.reservations.length;
    }

    public void cancelReservation(String nameReservedFor, String roomType) {
        // trying to remove reservation and catch error if any
        try {
            removeReservation(nameReservedFor, roomType);
            System.out.println(nameReservedFor + ", your reservation for a " + roomType + " room has been successfully cancelled.");
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
    }

    public void printInvoice(String nameReservedFor) {
        // sum up the money the person with this name has owed
        double sum = 0.0;
        for (int i = 0; i < this.reservations.length; i++) {
            if (this.reservations[i].getName().equals(nameReservedFor)) {
                sum += this.reservations[i].getRoom().getPrice();
            }
        }
        if (sum < 1.0) {
            System.out.println("No reservations have been made at this time");
        }
        System.out.println(nameReservedFor + "'s invoice is of $" + sum);
    }

    @Override
    public String toString() {
        // count how many available rooms for each type
        // by looping through all the rooms
        int doubleCount = 0, queenCount = 0, kingCount = 0;
        for (int i = 0; i < this.rooms.length; i++) {

            // check availability first
            boolean available = this.rooms[i].getAvailability();
            if (available) {

                // then check name
                String roomType = this.rooms[i].getType();
                if (roomType.equals("double")) {
                    doubleCount++;
                } else if (roomType.equals("queen")) {
                    queenCount++;
                } else if (roomType.equals("king")) {
                    kingCount++;
                } else {
                    System.out.println("This shouldn't print, roomType: " + roomType);
                }
            }

        }

        // build the string representation
        String hotelName = "Hotel name: " + this.name + "\n";
        String roomTypes = "Available Rooms: "
                + doubleCount + " double, "
                + queenCount + " queen, "
                + kingCount + " king.\n";

        return hotelName + roomTypes;




    }

    public void loadReservations(String fileName) throws IOException {

        // create a buffered reader for reading file
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;

        // read file line by line
        while ( (line = reader.readLine()) != null ) {

            // split the line by tab character
            String[] info = line.split("\t");
            String nameReservedFor = info[0];
            String roomType = info[1];

            // add reservation
            Room room = Room.findAvailableRoom(this.rooms, roomType);
            if (room == null) {
                throw new IOException("No available " + roomType +  " room for " + nameReservedFor + ".");
            }
            room.changeAvailability();
            addReservation(new Reservation(room, nameReservedFor));
        }

        reader.close();
    }

    public void saveReservations(String fileName) throws IOException {

        // prepare to write
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

        // visit each reservation and store its information to a file

        for (int i = 0; i < this.reservations.length; i++) {
            Reservation reservation = this.reservations[i];

            String nameReservedFor = reservation.getName();
            String roomType = reservation.getRoom().getType();
            String info = nameReservedFor + "\t" + roomType;
            writer.write(info);
            writer.newLine();
        }

        writer.close();

    }


}
