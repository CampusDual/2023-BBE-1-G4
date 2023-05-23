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
    private Hotel hotelId;

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

    public Hotel getHotelId() {
        return hotelId;
    }

    public void setHotelId(Hotel hotelId) {
        this.hotelId = hotelId;
    }
}
