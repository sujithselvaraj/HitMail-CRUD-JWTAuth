package Sujith.MailCrud.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class SimpleMailDTO
{
    private Long id;
    private String sender;
    private String subject;
    private List<String> recipients;
    private LocalDateTime time;
}
