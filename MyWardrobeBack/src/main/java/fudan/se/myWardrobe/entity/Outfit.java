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
@Table(name = "Outfit")
public class Outfit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "wardrobe")
    private Wardrobe wardrobe;

    @Column(name = "outfitName")
    private String outfitName;

    @ManyToOne
    @JoinColumn(name = "clothes")
    private Clothes clothes;

    public Outfit(Wardrobe wardrobe, String outfitName, Clothes clothes){
        this.wardrobe = wardrobe;
        this.outfitName = outfitName;
        this.clothes = clothes;
    }
}
