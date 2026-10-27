package com.weberpyth.weberBuffData;
import com.weberpyth.item.WeberRPGItem;
public class WeberBuffModel
  extends WeberRPGItem {
  public void loadParent(WeberRPGItem p) {
    this.id = p.id;
    this.attributes = p.attributes;
  }
  public String name;
}
