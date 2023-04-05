package API.Posts;

import lombok.Data;

import java.util.List;

@Data
public class Posts {
    public List<Datum> data = null;
    public Meta meta;
}
