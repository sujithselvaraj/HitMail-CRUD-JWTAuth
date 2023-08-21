package Sujith.MailCrud.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "deleted_mail")
public class DeletedMail
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sender;
    @ElementCollection
    @CollectionTable(name = "deleted_mail_recipients", joinColumns = @JoinColumn(name = "deleted_mail_id"))
    @Column(name = "recipient_email")
    private List<String> recipients=new ArrayList<>();
    @Column(length = 1000)
    private String subject;
    @Column(length = 10000)
    private String content;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
