package au.com.reecefenwick.api.stub.rest.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class StubDTO {

    @NotNull
    private String name;

    @NotNull
    @Min(5)
    private int number;

    public StubDTO() {}

    public StubDTO(String name, int number) {
        this.name = name;
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
