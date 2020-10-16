package com.mzx.gulimall.ware.controller;

import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.ware.service.WareUndoLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-16 17:05 周五.
 */
@RestController
@RequestMapping(value = "/ware/undo/log")
public class WareUndoLogController {

    @Autowired
    private WareUndoLogService wareUndoLogService;

    @PostMapping(value = "/post/{messageId}")
    public R rollBack(@PathVariable(value = "messageId") String messageId) {

        try {

            wareUndoLogService.rollBackLog(messageId);
            return R.ok();
        } catch (Exception e) {

            return R.error();

        }

    }

}
