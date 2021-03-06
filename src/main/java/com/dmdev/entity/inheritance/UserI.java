package com.dmdev.entity.inheritance;

import com.dmdev.entity.*;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "username")
@ToString(exclude = {"company", "profile", "userChats"})
@Entity
@Table(name = "users", schema = "public")
@TypeDef(name = "dmdev", typeClass = JsonBinaryType.class)
@Inheritance(strategy = InheritanceType.JOINED)
//@DiscriminatorColumn(name = "type") for SINGLE_TABLE
//strategy = InheritanceType.TABLE_PER_CLASS - create 2 tables
//!!!! better strategy = InheritanceType.SINGLE_TABLE - create 1 table with all columns added column "type"
//strategy = InheritanceType.JOINED -create 3 tables users, programmer, manager
public class UserI implements Comparable<UserI>, BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @AttributeOverride(name = "birthDate", column = @Column(name = "birth_date"))
    private PersonalInfo personalInfo;

    @Column(unique = true)
    private String username;

    @Type(type = "dmdev")
    private String info;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id") // company_id
    private Company company;

    @OneToOne(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private Profile profile;

    //    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<UserChat> userChats = new ArrayList<>();

    @Override
    public int compareTo(UserI o) {
        return username.compareTo(o.username);
    }
}
