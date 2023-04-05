package API.AuthorizationTests;

import lombok.Data;

import java.util.List;

@Data
public class Authorization {
    public Integer id;
    public String username;
    public String token;
    public List<String> roles = null;
}
