package com.dmdev.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.*;
import org.hibernate.envers.NotAudited;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

import static com.dmdev.util.StringUtils.SPACE;

//
@NamedEntityGraph(
        name = "WithCompany",
        attributeNodes = {
                @NamedAttributeNode(value = "company")
        }
)
@NamedEntityGraph(
        name = "WithCompanyAndChat",
        attributeNodes = {
                @NamedAttributeNode("company"),
                @NamedAttributeNode(value = "userChats", subgraph = "chats")
        },
        subgraphs = {
                @NamedSubgraph(name = "chats", attributeNodes = @NamedAttributeNode("chat"))
        }
)
//only for search po id
@FetchProfile(name = "withCompanyAndPayment", fetchOverrides = {
        @FetchProfile.FetchOverride(
                entity = User.class, association = "company", mode = FetchMode.JOIN
        ),
        @FetchProfile.FetchOverride(
                entity = User.class, association = "payments", mode = FetchMode.JOIN
        )
})
@NamedQuery(name = "findUserByName", query = "select u from User u " +
        "left join u.company c " +
        "where u.personalInfo.firstname = :firstname and c.name = :companyName " +
        "order by u.personalInfo.lastname desc")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "username")
@ToString(exclude = {"company", "userChats", "payments"})
@Builder
@Entity
@Table(name = "users", schema = "public")
@TypeDef(name = "dmdev", typeClass = JsonBinaryType.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Users")
public class User implements Comparable<User>, BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Valid
    @AttributeOverride(name = "birthDate", column = @Column(name = "birth_date"))
    private PersonalInfo personalInfo;

    @NotNull
    @Column(unique = true)
    private String username;

    @Type(type = "dmdev")
    private String info;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id") // company_id
    private Company company;

//    @OneToOne(
//            mappedBy = "user",
//            cascade = CascadeType.ALL,
//            fetch = FetchType.LAZY
//    )
//    private Profile profile;

    @NotAudited
    @Builder.Default
    @OneToMany(mappedBy = "user")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private List<UserChat> userChats = new ArrayList<>();

    @Builder.Default
//    @BatchSize(size = 3)
//    //    1 + N -> 1 + 5 -> 1 + 5/3 -> 3
//    @Fetch(FetchMode.SUBSELECT)
//     1 + N -> 1 + 1 -> 2
    @OneToMany(mappedBy = "receiver")
    private List<Payment> payments = new ArrayList<>();

    @Override
    public int compareTo(User o) {
        return username.compareTo(o.username);
    }

    public String fullName() {
        return getPersonalInfo().getFirstname() + SPACE + getPersonalInfo().getLastname();
    }
}












