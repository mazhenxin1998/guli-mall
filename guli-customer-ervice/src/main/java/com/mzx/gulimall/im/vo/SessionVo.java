package com.mzx.gulimall.im.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author ZhenXinMa.
 * @slogan 滴水穿石, 不是力量大, 而是功夫深.
 * @date 2020/9/9 8:55.
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SessionVo<T, U, V> {

    /**
     * 保存当前用户的用户id.
     */
    private T userId;
    /**
     * 保存当前用户的会话.
     */
    private U session;
    /**
     * 保存当前用户对应的是和那个客服(ID)在进行聊天.
     */
    private V to;


}
