package com.dmdev.entity;


import lombok.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class Payment extends AuditableEntity<Long>
//        implements BaseEntity<Long>
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer amount;

    //    @NotAudited
    @ManyToOne(optional = false)
    @JoinColumn(name = "receiver_id")
    private User receiver;

}

