package pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class Order {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;
    private long petId;
    private int quantity;
    private String shipDate;
    private String status;
    private boolean complete;

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Order)) return false;
        Order other = (Order) o;
        if (this.getId() == null || other.getId() == null ? false : !this.getId().equals(other.getId())) return false;
        if (this.getPetId() != other.getPetId()) return false;
        if (this.getQuantity() != other.getQuantity()) return false;
        if (this.getShipDate() == null
            ? other.getShipDate() != null
            : !this.getShipDate().split("\\.")[0].equals(other.getShipDate().split("\\.")[0]))
            return false;
        if (this.getStatus() == null ? other.getStatus() != null : !this.getStatus().equals(other.getStatus()))
            return false;
        if (this.isComplete() != other.isComplete()) return false;
        return true;
    }
}
