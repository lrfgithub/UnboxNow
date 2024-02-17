package com.unboxnow.inventory.dto;

import com.unboxnow.inventory.entity.Warehouse;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

public class WarehouseDTO {

    private int id;

    @NotBlank
    private String title;

    @NotBlank
    private String contact;

    @NotBlank
    @Pattern(regexp = "^[+]?1?[(]?[0-9]{3}[)]?[-\\s.]?[0-9]{3}[-\\s.]?[0-9]{4,6}$", message = "Telephone wrong")
    private String telephone;

    @NotBlank
    @Email(message = "Email wrong")
    private String email;

    private int addressId;

    @NotBlank
    private String addressName;

    @NotBlank
    @Pattern(regexp = "^[+]?1?[(]?[0-9]{3}[)]?[-\\s.]?[0-9]{3}[-\\s.]?[0-9]{4,6}$", message = "Telephone wrong")
    private String addressMobile;

    @NotBlank
    @Email(message = "Email wrong")
    private String addressEmail;

    @NotBlank
    private String address1;

    private String address2;

    @NotBlank
    private String city;

    @NotBlank
    private String state;

    @NotBlank
    @Pattern(regexp = "^[0-9]{5}(?:-[0-9]{4})?$", message = "Zipcode wrong")
    private String zip;

    public WarehouseDTO() {
    }

    public WarehouseDTO(String title,
                        String contact,
                        String telephone,
                        String email,
                        String addressName,
                        String addressMobile,
                        String addressEmail,
                        String address1,
                        String city,
                        String state,
                        String zip) {
        this.title = title;
        this.contact = contact;
        this.telephone = telephone;
        this.email = email;
        this.addressName = addressName;
        this.addressMobile = addressMobile;
        this.addressEmail = addressEmail;
        this.address1 = address1;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    public WarehouseDTO(String title,
                        String contact,
                        String telephone,
                        String email,
                        String addressName,
                        String addressMobile,
                        String addressEmail,
                        String address1,
                        String address2,
                        String city,
                        String state,
                        String zip) {
        this.title = title;
        this.contact = contact;
        this.telephone = telephone;
        this.email = email;
        this.addressName = addressName;
        this.addressMobile = addressMobile;
        this.addressEmail = addressEmail;
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getAddressMobile() {
        return addressMobile;
    }

    public void setAddressMobile(String addressMobile) {
        this.addressMobile = addressMobile;
    }

    public String getAddressEmail() {
        return addressEmail;
    }

    public void setAddressEmail(String addressEmail) {
        this.addressEmail = addressEmail;
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

    @Override
    public String toString() {
        return "WarehouseDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", contact='" + contact + '\'' +
                ", telephone='" + telephone + '\'' +
                ", email='" + email + '\'' +
                ", addressId=" + addressId +
                ", addressName='" + addressName + '\'' +
                ", addressMobile='" + addressMobile + '\'' +
                ", addressEmail='" + addressEmail + '\'' +
                ", address1='" + address1 + '\'' +
                ", address2='" + address2 + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zip='" + zip + '\'' +
                '}';
    }

    public static WarehouseDTO fromEntity(Warehouse warehouse) {
        if (warehouse == null) return null;
        ModelMapper modelMapper = new ModelMapper();
        PropertyMap<Warehouse, WarehouseDTO> warehouseMap = new PropertyMap<>() {
            @Override
            protected void configure() {
                map().setAddressId(source.getAddress().getId());
                map().setAddressName(source.getAddress().getName());
                map().setAddressMobile(source.getAddress().getMobile());
                map().setAddressEmail(source.getAddress().getEmail());
                map().setAddress1(source.getAddress().getAddress1());
                map().setAddress2(source.getAddress().getAddress2());
                map().setCity(source.getAddress().getCity());
                map().setState(source.getAddress().getState());
                map().setZip(source.getAddress().getZip());
            }
        };
        modelMapper.addMappings(warehouseMap);
        return modelMapper.map(warehouse, WarehouseDTO.class);
    }
}
