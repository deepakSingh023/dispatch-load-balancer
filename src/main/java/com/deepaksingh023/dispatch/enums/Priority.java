package com.deepaksingh023.dispatch.enums;

public enum Priority {
    HIGH(1),
    MEDIUM(2),
    LOW(3);
    private final int Rank;

    private Priority (int Rank){
        this.Rank = Rank;
    }

    public int getRank(){
        return Rank;
    }

}
