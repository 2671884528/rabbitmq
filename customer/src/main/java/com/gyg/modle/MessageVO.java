package com.gyg.modle;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @author by gyg
 * @date 2021/12/29 21:46
 * @description
 */
@Data
public class MessageVO {
    public String messageId;
    public Integer version;
    public Integer retry = 0;
    public JSONObject data;
}
