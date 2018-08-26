package com.edgar_avc.morena.model;

public class RelacionRG_RC {
    String userRG;
    String userRC;

    public RelacionRG_RC(String userRG, String userRC) {
        this.userRG = userRG;
        this.userRC = userRC;
    }

    public String getUserRG() {
        return userRG;
    }

    public void setUserRG(String userRG) {
        this.userRG = userRG;
    }

    public String getUserRC() {
        return userRC;
    }

    public void setUserRC(String userRC) {
        this.userRC = userRC;
    }
}
