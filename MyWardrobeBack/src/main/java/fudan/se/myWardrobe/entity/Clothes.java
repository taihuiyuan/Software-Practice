package fudan.se.myWardrobe.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Clothes")
public class Clothes implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "imageUrl")
    private String imageUrl;

    @Column(name = "category")
    private String categoryName;

    @Column(name = "color")
    private String color;

    @Column(name = "season")
    private String season;

    @Column(name = "price")
    private Double price;

    @Column(name = "location")
    private String location;

    @Column(name = "note")
    private String note;

    public Clothes(String imageUrl, String category, String color, String season, double price, String location, String note) {
        this.imageUrl = imageUrl;
        this.categoryName = category;
        this.color = color;
        this.season = season;
        this.price = price;
        this.location = location;
        this.note = note;
    }
}
