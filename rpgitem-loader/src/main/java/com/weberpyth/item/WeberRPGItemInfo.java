package com.weberpyth.item;
import java.util.ArrayList;
public class WeberRPGItemInfo {
  public String id;
  public String name;
  public ArrayList<String> lores;
  public String getName() {
    return this.name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public ArrayList<String> getLores() {
    return this.lores;
  }
  public void setLores(ArrayList<String> lores) {
    this.lores = lores;
  }
  public String getId() {
    return this.id;
  }
  public void setId(String id) {
    this.id = id;
  }
}
