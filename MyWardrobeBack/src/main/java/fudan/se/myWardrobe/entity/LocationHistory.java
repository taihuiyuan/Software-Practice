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
@Table(name = "Location_history")
public class LocationHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "wardrobe")
    private Wardrobe wardrobe;

    @ManyToOne
    @JoinColumn(name = "clothes")
    private Clothes clothes;

    @Column(name = "location")
    private String location;

    @Column(name = "time")
    private String time;

    public LocationHistory(Wardrobe wardrobe, Clothes clothes, String location, String time){
        this.wardrobe = wardrobe;
        this.clothes = clothes;
        this.location = location;
        this.time = time;
    }
}
