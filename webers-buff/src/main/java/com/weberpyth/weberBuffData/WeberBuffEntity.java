package com.weberpyth.weberBuffData;

public class WeberBuffEntity {

    public WeberBuffModel weberBuffModel;
    public Long timer;

    public void setTimer(Long t) {
        this.timer = t;
    }

    /**
     * A buff is active when it was granted indefinitely ({@code timer < 0}), when its template
     * length is negative, or while {@code timer + length} is in the future.
     */
    public boolean isActive() {
        if (this.timer == null || this.weberBuffModel == null || this.weberBuffModel.attributes == null) {
            return false;
        }
        if (this.timer < 0L) {
            return true;
        }
        Double length = this.weberBuffModel.attributes.get("length");
        if (length == null) {
            return true;
        }
        if (length < 0.0D) {
            return true;
        }
        return this.timer + length > System.currentTimeMillis();
    }
}
