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
@Table(name = "WardrobeClothes")
public class WardrobeClothes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "wardrobe")
    private Wardrobe wardrobe;

    @OneToOne
    @JoinColumn(name = "clothes")
    private Clothes clothes;

    public WardrobeClothes(Wardrobe wardrobe, Clothes clothes){
        this.wardrobe = wardrobe;
        this.clothes = clothes;
    }
}
