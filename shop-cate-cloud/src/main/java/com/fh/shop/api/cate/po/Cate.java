package com.fh.shop.api.cate.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("分类")
public class Cate implements Serializable {

    @ApiModelProperty(value = "分类id",example = "0")
    private Long id;


    @ApiModelProperty(value = "分类名称")
    private String cateName;


    @ApiModelProperty(value = "当前分类父id",example = "0")
    private Long pId;


    @ApiModelProperty(value = "类型id",example = "0")
    private Long typeId;


    @ApiModelProperty(value = "类型名称",example = "0")
    private String typeName;


}
