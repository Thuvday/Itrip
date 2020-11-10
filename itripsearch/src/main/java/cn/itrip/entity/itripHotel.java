package cn.itrip.entity;

import org.apache.solr.client.solrj.beans.Field;

public class itripHotel {
    @Field
    private String id;
    @Field
    private String hotelName;
    @Field
    private String address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
