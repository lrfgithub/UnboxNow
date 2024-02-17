package com.unboxnow.inventory.entity;

import com.unboxnow.inventory.dto.WarehouseDTO;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "warehouse")
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "title", unique = true)
    @NotBlank
    private String title;

    @Column(name = "contact")
    @NotBlank
    private String contact;

    @Column(name = "telephone")
    @NotBlank
    @Pattern(regexp = "^[+]?1?[(]?[0-9]{3}[)]?[-\\s.]?[0-9]{3}[-\\s.]?[0-9]{4,6}$", message = "Telephone wrong")
    private String telephone;

    @Column(name = "email")
    @NotBlank
    @Email(message = "Email wrong")
    private String email;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "address_id")
    @NotNull
    @Valid
    private Address address;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    @UpdateTimestamp
    private LocalDateTime modifiedAt;

    @OneToMany(mappedBy = "warehouse",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @Valid
    private List<Inventory> inventories = new ArrayList<>();

    public Warehouse() {
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public List<Inventory> getInventories() {
        return inventories;
    }

    public void setInventories(List<Inventory> inventories) {
        this.inventories = inventories;
    }

    @Override
    public String toString() {
        return "Warehouse{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", contact='" + contact + '\'' +
                ", telephone='" + telephone + '\'' +
                ", email='" + email + '\'' +
                ", address=" + address +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                '}';
    }

    public static Warehouse fromDTO(WarehouseDTO dto) {
        if (dto == null) return null;
        ModelMapper modelMapper = new ModelMapper();
        PropertyMap<WarehouseDTO, Warehouse> warehouseDTOMap = new PropertyMap<>() {
            @Override
            protected void configure() {
                map().setEmail(source.getEmail());
                map().getAddress().setId(source.getAddressId());
                map().getAddress().setName(source.getAddressName());
                map().getAddress().setMobile(source.getAddressMobile());
                map().getAddress().setEmail(source.getAddressEmail());
                map().getAddress().setAddress1(source.getAddress1());
                map().getAddress().setAddress2(source.getAddress2());
                map().getAddress().setCity(source.getCity());
                map().getAddress().setState(source.getState());
                map().getAddress().setZip(source.getZip());
                map().getAddress().setWarehouse(null);
            }
        };
        modelMapper.addMappings(warehouseDTOMap);
        return modelMapper.map(dto, Warehouse.class);
    }
}
