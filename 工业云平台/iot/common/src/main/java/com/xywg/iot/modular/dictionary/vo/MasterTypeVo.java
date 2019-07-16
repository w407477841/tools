package com.xywg.iot.modular.dictionary.vo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.xywg.iot.modular.dictionary.model.MasterType;
import lombok.Data;

import java.util.List;

/***
 *@author:jixiaojun
 *DATE:2018/12/29
 *TIME:17:11
 */
@Data
public class MasterTypeVo extends MasterType {
    /**
     * 子集
     */
    @TableField(exist = false)
    private List<MasterTypeVo> children;
}
