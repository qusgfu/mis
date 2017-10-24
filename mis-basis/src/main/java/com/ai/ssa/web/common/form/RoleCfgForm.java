package com.ai.ssa.web.common.form;

import java.util.List;
import java.util.Map;

import com.ai.ssa.web.common.base.BaseForm;

public class RoleCfgForm extends BaseForm{
	private String role_id;
	private String name;
	private String title;
	private String description;
	private String menus;
  public String getRole_id() {
    return role_id;
  }
  public void setRole_id(String role_id) {
    this.role_id = role_id;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public String getMenus() {
    return menus;
  }
  public void setMenus(String menus) {
    this.menus = menus;
  }
  
	 
}
