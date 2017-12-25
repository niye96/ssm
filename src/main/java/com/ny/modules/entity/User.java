package com.ny.modules.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

@Data
public class User implements Serializable {

    private static final long serialVersionUID = 8053855421807653201L;

    @Id
    private String userId;

    private String userCode;

    @NotNull(message = "年龄不能为空")
    @Min(value = 18, message = "年龄必须满18岁")
    @Max(value = 100, message = "年龄不能超过100岁")
    private Integer age;

    @NotNull(message = "邮箱不能为空")
    @Pattern(regexp = "\\w+(\\.\\w)*@\\w+(\\.\\w{2,3}){1,3}", message = "邮箱格式不正确")
    private String mail;

    @NotNull(message = "手机号码不能为空")
    @Pattern(regexp = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$", message = "手机号码格式不正确")
    private String phone;

    private String department;

    private Date createTime;

    private String createBy;

    private Date modifiedTime;

    private String modifiedBy;
}
