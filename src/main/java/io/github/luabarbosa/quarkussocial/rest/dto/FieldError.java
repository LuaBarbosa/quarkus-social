package io.github.luabarbosa.quarkussocial.rest.dto;

public class FieldError {
    private String field;
    private String message;

    public FieldError(String field, String message) {
        this.field = field;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
