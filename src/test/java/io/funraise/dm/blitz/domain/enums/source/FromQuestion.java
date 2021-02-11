package io.funraise.dm.blitz.domain.enums.source;

public class FromQuestion {
    private FromQuestionType type;
    private String toType;
    private Long number;

    public FromQuestionType getType() {
        return type;
    }

    public void setType(FromQuestionType type) {
        this.type = type;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public String getToType() {
        return toType;
    }

    public void setToType(String toType) {
        this.toType = toType;
    }
}
