package com.unboxnow.user.dto;

import com.unboxnow.common.entity.UniversalAddress;
import com.unboxnow.user.entity.Address;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AddressDTO {

    private int id;

    @Min(1)
    private int memberId;

    private String memberUsername;

    @NotBlank
    private String name;

    @NotBlank
    @Pattern(regexp = "^[+]?1?[(]?[0-9]{3}[)]?[-\\s.]?[0-9]{3}[-\\s.]?[0-9]{4,6}$", message = "Telephone wrong")
    private String mobile;

    @NotBlank
    @Email(message = "Email wrong")
    private String email;

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

    public AddressDTO() {
    }

    public AddressDTO(int memberId,
                      String name,
                      String mobile,
                      String email,
                      String address1,
                      String city,
                      String state,
                      String zip) {
        this.memberId = memberId;
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.address1 = address1;
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

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getMemberUsername() {
        return memberUsername;
    }

    public void setMemberUsername(String memberUsername) {
        this.memberUsername = memberUsername;
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

    @Override
    public String toString() {
        return "AddressDTO{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", memberUsername='" + memberUsername + '\'' +
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

    public static AddressDTO fromEntity(Address address) {
        if (address == null) return null;
        ModelMapper modelMapper = new ModelMapper();
        PropertyMap<Address, AddressDTO> entityMap = new PropertyMap<>() {
            @Override
            protected void configure() {
                map().setMemberId(source.getMember().getId());
                map().setMemberUsername(source.getMember().getUsername());
            }
        };
        modelMapper.addMappings(entityMap);
        return modelMapper.map(address, AddressDTO.class);
    }

    public static AddressDTO fromEntity(UniversalAddress address) {
        if (address == null) return null;
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(address, AddressDTO.class);
    }

    public static List<AddressDTO> fromEntities(List<Address> addresses) {
        if (addresses.isEmpty()) return new ArrayList<>();
        return addresses.stream()
                .map(AddressDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
