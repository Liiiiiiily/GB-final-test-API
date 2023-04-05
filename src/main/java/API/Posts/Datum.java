package API.Posts;

import lombok.Data;

import java.util.List;

@Data
public class Datum {
    public Integer id;
    public String title;
    public String description;
    public String content;
    public Integer authorId;
    public MainImage mainImage;
    public String updatedAt;
    public String createdAt;
    public List<Object> labels = null;
    public Object delayPublishTo;
    public Boolean draft;
}
