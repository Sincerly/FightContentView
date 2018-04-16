package com.sincerly.fightcontentview.bean;

/**
 * Created by Administrator on 2018/4/16 0016.
 */

public class ResponseBean {

    /**
     * time : 1523886999887
     * current : {"periodNumber":95,"periodDate":"20180416095","awardTime":"2018-04-16 21:50:00","awardNumbers":"4,9,7,0,3"}
     * next : {"periodNumber":96,"periodDate":"20180416096","awardTime":"2018-04-16 22:00:00","awardTimeInterval":200000,"delayTimeInterval":15}
     * awardState : false
     */

    private long time;
    private CurrentBean current;
    private NextBean next;
    private boolean awardState;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public CurrentBean getCurrent() {
        return current;
    }

    public void setCurrent(CurrentBean current) {
        this.current = current;
    }

    public NextBean getNext() {
        return next;
    }

    public void setNext(NextBean next) {
        this.next = next;
    }

    public boolean isAwardState() {
        return awardState;
    }

    public void setAwardState(boolean awardState) {
        this.awardState = awardState;
    }

    public static class CurrentBean {
        /**
         * periodNumber : 95
         * periodDate : 20180416095
         * awardTime : 2018-04-16 21:50:00
         * awardNumbers : 4,9,7,0,3
         */

        private int periodNumber;
        private String periodDate;
        private String awardTime;
        private String awardNumbers;

        public int getPeriodNumber() {
            return periodNumber;
        }

        public void setPeriodNumber(int periodNumber) {
            this.periodNumber = periodNumber;
        }

        public String getPeriodDate() {
            return periodDate;
        }

        public void setPeriodDate(String periodDate) {
            this.periodDate = periodDate;
        }

        public String getAwardTime() {
            return awardTime;
        }

        public void setAwardTime(String awardTime) {
            this.awardTime = awardTime;
        }

        public String getAwardNumbers() {
            return awardNumbers;
        }

        public void setAwardNumbers(String awardNumbers) {
            this.awardNumbers = awardNumbers;
        }
    }

    public static class NextBean {
        /**
         * periodNumber : 96
         * periodDate : 20180416096
         * awardTime : 2018-04-16 22:00:00
         * awardTimeInterval : 200000
         * delayTimeInterval : 15
         */

        private int periodNumber;
        private String periodDate;
        private String awardTime;
        private int awardTimeInterval;
        private int delayTimeInterval;

        public int getPeriodNumber() {
            return periodNumber;
        }

        public void setPeriodNumber(int periodNumber) {
            this.periodNumber = periodNumber;
        }

        public String getPeriodDate() {
            return periodDate;
        }

        public void setPeriodDate(String periodDate) {
            this.periodDate = periodDate;
        }

        public String getAwardTime() {
            return awardTime;
        }

        public void setAwardTime(String awardTime) {
            this.awardTime = awardTime;
        }

        public int getAwardTimeInterval() {
            return awardTimeInterval;
        }

        public void setAwardTimeInterval(int awardTimeInterval) {
            this.awardTimeInterval = awardTimeInterval;
        }

        public int getDelayTimeInterval() {
            return delayTimeInterval;
        }

        public void setDelayTimeInterval(int delayTimeInterval) {
            this.delayTimeInterval = delayTimeInterval;
        }
    }
}
