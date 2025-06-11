package com.atguigu.account.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author Atguigu
 * @since 2023-04-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="LogTbl对象", description="")
public class LogTbl implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    private String orderId;

    private Integer money;

    private Date gmtCreate;

    private Date gmtModified;


}
