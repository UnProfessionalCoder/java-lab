package com.newbig.app.web.model;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class OrgPrivilege {
    private String userUuid;

    private Integer roleId;

//    private String

}


/**
 * {
 * "userUuid":"",
 * "orgPrv":{
 * "topOrgId":{
 * "orgId":11,
 * "roleId": 11,
 * "inner":{
 * 1,
 * 3,
 * 5
 * },
 * "outer":{
 * "orgId1（小区组织Id）":"0",
 * "orgId2":"表达式1",
 * }
 * }
 * }
 * }
 */
