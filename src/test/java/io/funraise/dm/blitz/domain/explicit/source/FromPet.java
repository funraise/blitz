package io.funraise.dm.blitz.domain.explicit.source;

public class FromPet {
    public enum Type {
        CAT,
        DOG
    }

    private String nickname;
    private Type petType;
    private String type;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Type getPetType() {
        return petType;
    }

    public void setPetType(Type petType) {
        this.petType = petType;
    }
}
