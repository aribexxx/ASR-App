package com.example.myapplication.models;

import java.util.List;

public class MeetingListRes {
    private String state;
    private List<Meeting> meeting;

    public String getState() {
        return state;
    }

    public List<Meeting> getMeeting() {
        return meeting;
    }

    public static class Meeting {
        private String pwd;
        private String userName;
        private String meetingId;
        private String roomTitle;
        private String roomDescription;
        private String direct;
        private String imageUrl;

        public String getStatus() {
            return status;
        }

        private String status;

        public String getPwd() {
            return pwd;
        }

        public String getMeetingId() {
            return meetingId;
        }

        public String getRoomTitle() {
            return roomTitle;
        }

        public String getUserName() {
            return userName;
        }

        public String getRoomDescription() {
            return roomDescription;
        }

        public String getDirect(){
            return direct;
        }

        public String getImageUrl() {
            return imageUrl;
        }
    }

}
