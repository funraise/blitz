package io.funraise.dm.blitz.domain.enums.source;

public enum FromQuestionType {
    FREE_RESPONSE("free"), SHORT_ANSWER("short");

    private final String label;

    FromQuestionType(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return this.label;
    }
}
