package model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;



@Entity
@Table(name = "Address")
@Access(AccessType.FIELD)
public class Address extends AbstractEntity {

    @Column(name = "city")
    @NotNull
    private String city;
    @Column(name = "street")
    private String street;
    @Column(name = "streetNr")
    @NotNull
    private String streetNr;
    @Id
    private Long id;

    public Address(String city, String street, String streetNr) {
        this.city = city;
        this.street = street;
        this.streetNr = streetNr;
    }

    public Address() {
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getStreetNr() {
        return streetNr;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setStreetNr(String streetNr) {
        this.streetNr = streetNr;
    }

    String getAddressInfo() {
        final StringBuilder sb = new StringBuilder("Rent{");
        sb.append("Miasto=").append(getCity());
        sb.append("Ulica=").append(getStreet());
        sb.append("Numer mieszkania=").append(getStreetNr());

        return sb.toString();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}