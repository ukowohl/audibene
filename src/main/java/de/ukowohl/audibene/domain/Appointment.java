package de.ukowohl.audibene.domain;

import de.ukowohl.audibene.domain.enumeration.Rating;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "audibene_data", name = "appointment")
@EntityListeners(AuditingEntityListener.class)
public class Appointment {

    @NotNull
    @CreatedDate
    @Column(name = "created")
    private OffsetDateTime createdAt;

    @NotNull
    @LastModifiedDate
    @Column(name = "modified")
    private OffsetDateTime modifiedAt;

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "id")
    private String id;

    @NotEmpty
    @Column(name = "customer_id")
    private String customerId;

    @NotEmpty
    @Column(name = "employee_id")
    private String employeeId;

    @NotNull
    @Column(name = "begins")
    private OffsetDateTime begins;

    @NotNull
    @Column(name = "ends")
    private OffsetDateTime ends;

    @Column(name = "rating")
    private Rating rating;

    @Column(name = "customer_comment")
    private String customerComment;

}
