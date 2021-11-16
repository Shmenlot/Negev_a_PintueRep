package com.example;


public class TestObject {
    String memberID = "";
    int timer = 0;
    private int xp = 0;
    // contarctor
    public TestObject(String memberID, int timer, int xp) {
        this.memberID = memberID;
        this.timer = timer;
        this.xp = xp;
    }
    //! getters and setters
    public String getMemberID() {
        return this.memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public int getTimer() {
        return this.timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

    public int getXp() {
        return this.xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }
    //! End of getters and setters

}
