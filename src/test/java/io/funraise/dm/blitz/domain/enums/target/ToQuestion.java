package io.funraise.dm.blitz.domain.enums.target;

public class ToQuestion {
    public enum ToQuestionType {
        A, B, C
    }
    private String type;
    private Integer number;
    private ToQuestionType toType;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public ToQuestionType getToType() {
        return toType;
    }

    public void setToType(ToQuestionType toType) {
        this.toType = toType;
    }
}
