package com.hjp.testproject.vo;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hjp.testproject.controller.utils.EncryptionUtil;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberTestDataVO implements Serializable {

    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    private String userId;

    private String nickName;
    private String userPwd;
    private String email;
    private String profileImg;
    private String refreshToken;

    /** 등록일시. */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime joindate;

    private String socialType;

    public void encode() {
        email = EncryptionUtil.ENC_DES(email);
    }

    public void decode() {
        email = EncryptionUtil.DEC_DES(email);
    }

}
