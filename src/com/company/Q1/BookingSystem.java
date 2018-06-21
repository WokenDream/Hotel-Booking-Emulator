package com.company.Q1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by tianqiliu on 2018-06-20.
 */
public class BookingSystem {

    private static Room[] loadRooms(String fileName) {

        // open the file
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            return new Room[0];
        }

        // read through the file and calculate total number of rooms

        String doubleRoomStr, queenRoomStr, kingRoomStr;
        try {
            doubleRoomStr = reader.readLine();
            queenRoomStr = reader.readLine();
            kingRoomStr = reader.readLine();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return new Room[0];
        }
        int doubleRoomNum = Integer.parseInt(doubleRoomStr);
        int queenRoomNum = Integer.parseInt(queenRoomStr);
        int kingRoomNum = Integer.parseInt(kingRoomStr);
        int numberOfRooms = doubleRoomNum + queenRoomNum + kingRoomNum;

        // store file content into an array
        Room[] rooms = new Room[numberOfRooms];

        int i = 0;
        while (i < doubleRoomNum) {
            rooms[i] = new Room("double");
            i++;
        }

        while (i < doubleRoomNum + queenRoomNum) {
            rooms[i] = new Room("queen");
            i++;
        }

        while (i < numberOfRooms) {
            rooms[i] = new Room("king");
            i++;
        }

        return rooms;

    }

    private static void handleMakeReservation(Scanner scanner, Hotel hotel, String hotelName) {
        // gather user info
        System.out.print("Please enter your name:");
        String name = scanner.nextLine();

        System.out.print("What type of room would you like to reserve?");
        String type = scanner.nextLine();

        // make reservation
        int numOfReservations = hotel.createReservation(name, type.toLowerCase());
        if (numOfReservations < 0) {
            System.out.println("Sorry " + name + ", we have no available rooms of the desired type.");
        } else {
            System.out.println("You have successfully reserved a " + type + " room under the name of " + name + ". We look forward to having you at " + hotelName + "!");
        }
        System.out.println();

    }

    private static void handleCancelReservation(Scanner scanner, Hotel hotel) {
        // gather user info
        System.out.print("Please enter the name you used to make the reservation");
        String name = scanner.nextLine();

        System.out.print("What type of room did you reserve?");
        String type = scanner.nextLine().toLowerCase();

        hotel.cancelReservation(name, type);
    }

    private static void handlePrintInvoice(Scanner scanner, Hotel hotel) {
        System.out.print("Please enter your name");
        String name = scanner.nextLine();

        hotel.printInvoice(name);
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the COMP 202 booking system");
        System.out.println("Please enter the name of the hotel you'd like to book");
        System.out.println();

        // initialize hotel
        String hotelName = scanner.nextLine();
        Hotel hotel = new Hotel(hotelName, BookingSystem.loadRooms("roomInfo.txt"));
        try {
            hotel.loadReservations("reservationsInfo.txt");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            scanner.close();
            return;
        }

        // keep printing menu unless user chooses to stop
        boolean stop = false;
        int option;
        while (!stop) {
            System.out.println("****************************************************");
            System.out.println("Welcome to " + hotelName + ". Choose one of the following options:");
            System.out.println("1) Make a reservation");
            System.out.println("2) Cancel a reservation");
            System.out.println("3) See an invoice");
            System.out.println("4) See hotel info");
            System.out.println("5) Exit the Booking System");
            System.out.println("****************************************************");

            option = scanner.nextInt();
            scanner.nextLine(); // consume new line character

            if (option == 1) {
                handleMakeReservation(scanner, hotel, hotelName);
            } else if (option == 2) {
                handleCancelReservation(scanner, hotel);
            } else if (option == 3) {
                handlePrintInvoice(scanner, hotel);
            } else if (option == 4) {
                System.out.println("Here is the hotel info");
                System.out.println(hotel);
            } else if (option == 5) {
                // TODO: stop
                stop = true;
                try {
                    hotel.saveReservations("reservationsInfo.txt");
                    System.out.println("It was a pleasure doing business with you!");
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                } finally {
                    scanner.close();
                }

            }
        }
    }
}
