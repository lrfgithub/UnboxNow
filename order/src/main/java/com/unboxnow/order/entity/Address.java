package com.unboxnow.order.entity;

import com.unboxnow.order.dto.AddressDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.modelmapper.ModelMapper;

@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    @NotBlank
    private String name;

    @Column(name = "mobile")
    @NotBlank
    @Pattern(regexp = "^[+]?1?[(]?[0-9]{3}[)]?[-\\s.]?[0-9]{3}[-\\s.]?[0-9]{4,6}$", message = "Telephone wrong")
    private String mobile;

    @Column(name = "email")
    @NotBlank
    @Email(message = "Email wrong")
    private String email;

    @Column(name = "address1")
    @NotBlank
    private String address1;

    @Column(name = "address2")
    private String address2;

    @Column(name = "city")
    @NotBlank
    private String city;

    @Column(name = "state")
    @NotBlank
    private String state;

    @Column(name = "zip")
    @NotBlank
    @Pattern(regexp = "^[0-9]{5}(?:-[0-9]{4})?$", message = "Zipcode wrong")
    private String zip;

    @OneToOne(mappedBy = "address",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private Order order;

    public Address() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
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

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", address1='" + address1 + '\'' +
                ", address2='" + address2 + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zip='" + zip + '\'' +
                '}';
    }

    public static Address fromDTO(AddressDTO dto) {
        if (dto == null) return null;
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(dto, Address.class);
    }
}
