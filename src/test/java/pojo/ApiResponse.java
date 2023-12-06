package pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class ApiResponse {
private Integer code;
private String type;
private String message;

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof ApiResponse)) return false;
        ApiResponse other = (ApiResponse) o;
        if (this.getCode() == null ? other.getCode() != null : !this.getCode().equals(other.getCode()))
            return false;
        if (this.getType() == null ? other.getType() != null : !this.getType().equals(other.getType()))
            return false;
        if (this.getMessage() == null || other.getMessage() == null
            ? false : !this.getMessage().equals(other.getMessage())) return false;
        return true;
    }
}