package com.kentwardrobe.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double price;

    @Column(name = "description", length = 1000)
    private String description;

    private String img;
    private String category;
    private String badge;
    private String type;

    // --- MANUAL GETTERS AND SETTERS ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImg() { return img; }
    public void setImg(String img) { this.img = img; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getBadge() { return badge; }
    public void setBadge(String badge) { this.badge = badge; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}