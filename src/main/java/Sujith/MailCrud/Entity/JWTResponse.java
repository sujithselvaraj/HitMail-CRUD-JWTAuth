package Sujith.MailCrud.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.hql.internal.classic.Parser;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JWTResponse
{
    private String jwtToken;
}
