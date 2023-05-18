package com.campusdual.dominiondiamondhotel.model;

import javax.persistence.*;

@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private int number;

    @ManyToOne
    @JoinColumn (name = "hotel_id")
    private Hotel hotel_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Hotel getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(Hotel hotel_id) {
        this.hotel_id = hotel_id;
    }
}
