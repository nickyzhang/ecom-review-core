package com.core.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class Science {
    private int id;
    String agency;
    String center;
    String centerStatus;
    String facility;
    int occupied;
    String status;
    String link;
    LocalDate recordDate;
    LocalDate lastUpdate;
    String address;
    String city;
    String state;
    String zip;
    String country;
    String contact;
    String mailStop;
    String phone;

    public Science() {
    }

    public Science(int id, String agency, String center, String centerStatus, String facility, int occupied, String status, String link, LocalDate recordDate, LocalDate lastUpdate, String address, String city, String state, String zip, String country, String contact, String mailStop, String phone) {
        this.id = id;
        this.agency = agency;
        this.center = center;
        this.centerStatus = centerStatus;
        this.facility = facility;
        this.occupied = occupied;
        this.status = status;
        this.link = link;
        this.recordDate = recordDate;
        this.lastUpdate = lastUpdate;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.country = country;
        this.contact = contact;
        this.mailStop = mailStop;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public String getCenterStatus() {
        return centerStatus;
    }

    public void setCenterStatus(String centerStatus) {
        this.centerStatus = centerStatus;
    }

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }

    public int getOccupied() {
        return occupied;
    }

    public void setOccupied(int occupied) {
        this.occupied = occupied;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public LocalDate getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
    }

    public LocalDate getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDate lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getMailStop() {
        return mailStop;
    }

    public void setMailStop(String mailStop) {
        this.mailStop = mailStop;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
