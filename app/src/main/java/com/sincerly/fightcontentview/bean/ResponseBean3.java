package com.sincerly.fightcontentview.bean;

/**
 * Created by Administrator on 2018/4/16 0016.
 */

public class ResponseBean3 {


    /**
     * time : 2018-04-16 22:00:48
     * current : {"periodNumber":77,"period":"2018041677","awardTime":"2018-04-16 21:50:00","awardNumbers":"9,6,6,4,8"}
     * next : {"periodNumber":78,"period":"2018041678","awardTime":"2018-04-16 22:00:00","awardTimeInterval":-48000,"delayTimeInterval":15}
     * awardState : false
     */

    private String time;
    private CurrentBean current;
    private NextBean next;
    private boolean awardState;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
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
         * periodNumber : 77
         * period : 2018041677
         * awardTime : 2018-04-16 21:50:00
         * awardNumbers : 9,6,6,4,8
         */

        private int periodNumber;
        private String period;
        private String awardTime;
        private String awardNumbers;

        public int getPeriodNumber() {
            return periodNumber;
        }

        public void setPeriodNumber(int periodNumber) {
            this.periodNumber = periodNumber;
        }

        public String getPeriod() {
            return period;
        }

        public void setPeriod(String period) {
            this.period = period;
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
         * periodNumber : 78
         * period : 2018041678
         * awardTime : 2018-04-16 22:00:00
         * awardTimeInterval : -48000
         * delayTimeInterval : 15
         */

        private int periodNumber;
        private String period;
        private String awardTime;
        private int awardTimeInterval;
        private int delayTimeInterval;

        public int getPeriodNumber() {
            return periodNumber;
        }

        public void setPeriodNumber(int periodNumber) {
            this.periodNumber = periodNumber;
        }

        public String getPeriod() {
            return period;
        }

        public void setPeriod(String period) {
            this.period = period;
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
