package com.example.myapplication.models;

import android.content.res.Resources;
import android.util.Log;
import com.example.myapplication.views.room_list.RoomCardType;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A product entry in the list of products.
 */
public class RoomEntry implements RoomCardType {
    private static final String TAG = RoomEntry.class.getSimpleName();
    /**
     * 必需属性
     */
    private String roomTitle;
    private String userName; // get speaker name from server
    private String roomDescription;
    private String roomID;
    private String direct; //language setting (int (0: zh->en, 1: en->zh, ……) (*required)
    private String pwd;// private roon require, public can set to null
    /**
     * 可选属性
     */
    //private Uri dynamicUrl;
    private String url;

    public RoomEntry(Builder builder) {
        this.roomTitle = builder.roomTitle;
        this.userName = builder.speakerName;
        this.roomID = builder.roomID;
        this.pwd = builder.password;
        this.roomDescription = builder.roomDescription;
        this.direct = builder.direct;

    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getPwd() {
        return pwd;
    }

    public String getRoomTitle() {
        return roomTitle;
    }

    public String getSpeakerName() {
        return userName;
    }

    public String getRoomID() {
        return roomID;
    }

    public String getRoomDescription() {
        return roomDescription;
    }

    public String getDirect() {
        return direct;
    }

    public String getUrl() {
        return url;
    }

    /**
     * Loads a raw JSON at R.raw and converts it into a list of RoomEntry objects
     */
    public static List<RoomEntry> initRoomEntryList(Resources resources, InputStream inputStream) {
        //  inputStream = resources.openRawResource(R.raw.rooms);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            int pointer;
            while ((pointer = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, pointer);
            }
        } catch (IOException exception) {
            Log.e(TAG, "Error writing/reading from the JSON file.", exception);
        } finally {
            try {
                inputStream.close();
            } catch (IOException exception) {
                Log.e(TAG, "Error closing the input stream.", exception);
            }
        }
        String jsonRoomsString = writer.toString();
        Gson gson = new Gson();

        //这里装在Json需要Raw里的数据格式和 定义的Field 名称一致！！否则私密房间无法正确显示。
        Type roomListType = new TypeToken<ArrayList<RoomEntry>>() {
        }.getType();
        return gson.fromJson(jsonRoomsString, roomListType);
    }

    @Override
    public int getListItemType() {
        if (this.pwd == null) {
            return Public_Room_Card;
        } else {
            return Private_Room_Card;
        }
    }


    //Builder class
    public static final class Builder {
        private String roomTitle;
        private String speakerName;
        private String roomID;
        private String roomDescription;
        private String direct;
        private String password;// private roon require, public can set to null
        private String url;

        public Builder() {
            roomTitle = "Default Titile";
            url = null;
            speakerName = "Speaker name unknown";
            roomID = "Need description";
        }

        /**
         * 实际属性配置方法
         *
         * @param roomTitle
         * @return Builder
         */
        public Builder roomTitle(String roomTitle) {
            this.roomTitle = roomTitle;
            return this;
        }

        public Builder speakerName(String speakerName) {
            this.speakerName = speakerName;
            return this;
        }

        public Builder roomDescription(String roomDescription) {
            this.roomDescription = roomDescription;
            return this;
        }

        public Builder direct(String direct) {
            this.direct = direct;
            return this;
        }

        public Builder roomID(String roomID) {
            if (roomID == null) {
                throw new NullPointerException("没roomID?? 必须要");
            }
            this.roomID = roomID;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        /**
         * 最后创造出实体car
         *
         * @return
         */
        public RoomEntry build() {
            return new RoomEntry(this);
        }
    }


}