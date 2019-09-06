package com.honghu.oauth2.dto;

import com.honghu.oauth2.Const;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author : wangyifei
 * Description 分页查询
 * Date: Created in 13:13 2018/12/29
 * Modified By : wangyifei
 */
@Data
public class QueryPageDTO extends  QueryDTO {
        @NotNull(groups = {Const.GroupPage.class})
        Integer pageSize;
        @NotNull(groups = {Const.GroupPage.class})
        Integer pageNum;

}
