package com.ai.ssa.web.common.form;

import com.ai.ssa.web.common.model.umps.UpmsUser;

public class UserCfgForm extends UpmsUser {
  private String orgids;
  private String roleids; 
  private Integer pageSize;
  private Integer pageIndex;
  private String sort;
  private String sort_key;
  private Integer startLimit;

  public String getOrgids() {
    return orgids;
  }

  public void setOrgids(String orgids) {
    this.orgids = orgids;
  }

  public String getRoleids() {
    return roleids;
  }

  public void setRoleids(String roleids) {
    this.roleids = roleids;
  }

  public Integer getPageSize() {
    return pageSize;
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  public Integer getPageIndex() {
    return pageIndex;
  }

  public void setPageIndex(Integer pageIndex) {
    this.pageIndex = pageIndex;
  }

  public String getSort() {
    return sort;
  }

  public void setSort(String sort) {
    this.sort = sort;
  }

  public String getSort_key() {
    return sort_key;
  }

  public void setSort_key(String sort_key) {
    this.sort_key = sort_key;
  }

  public Integer getStartLimit() {
    return startLimit;
  }

  public void setStartLimit(Integer startLimit) {
    this.startLimit = startLimit;
  }

 


}
