package com.honghu.oauth2.dto;

import com.honghu.oauth2.Const;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 8:45 2018/12/29
 * Modified By : wangyifei
 */
@Data
public class DeleteDTO {
    @NotNull(groups = Const.GroupDelete.class)
    private List<Long> ids;


}
