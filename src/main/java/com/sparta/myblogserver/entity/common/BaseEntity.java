package com.sparta.myblogserver.entity.common;

import java.time.LocalDateTime;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@MappedSuperclass
//@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

//    @CreatedDate
    private Date createdAt;

//    @LastModifiedDate
    private Date modifiedAt;

}

