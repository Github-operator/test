package pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Arrays;

@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Pet {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;
    private PetCategory category;
    private String name;
    private String[] photoUrls;
    private Tag[] tags;
    private String status;

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Pet)) return false;
        Pet other = (Pet) o;
        if (this.getId() == null || other.getId() == null ? false : !this.getId().equals(other.getId())) return false;
        if (this.getCategory() == null
            ? other.getCategory() == null
            : !this.getCategory().equals(other.getCategory())) return false;
        if (this.getName() == null ? other.getName() != null : !this.getName().equals(other.getName()))
            return false;
        if (this.getStatus() == null ? other.getStatus() != null : !this.getStatus().equals(other.getStatus()))
            return false;
        if (!Arrays.equals(this.getPhotoUrls(), other.getPhotoUrls())) return false;
        if (!Arrays.deepEquals(this.getTags(), other.getTags())) return false;
        return true;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class PetCategory {
        private long id;
        private String name;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Tag {

        private long id;
        private String name;
    }
}
