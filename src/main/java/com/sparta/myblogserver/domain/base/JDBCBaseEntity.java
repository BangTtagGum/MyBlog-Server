package com.sparta.myblogserver.domain.base;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@MappedSuperclass
//@EntityListeners(AuditingEntityListener.class)
public class JDBCBaseEntity {

//    @CreatedDate
    private Date createdAt;

//    @LastModifiedDate
    private Date modifiedAt;

}

