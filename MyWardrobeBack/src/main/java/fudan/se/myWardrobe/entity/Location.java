package fudan.se.myWardrobe.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Location")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "wardrobe")
    private Wardrobe wardrobe;

    @Column(name = "name")
    private String name;

    public Location(Wardrobe wardrobe, String name){
        this.wardrobe = wardrobe;
        this.name = name;
    }
}
