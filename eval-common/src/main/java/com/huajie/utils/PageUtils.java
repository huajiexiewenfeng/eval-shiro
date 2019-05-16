package com.huajie.utils;


import java.io.Serializable;
import java.util.List;
/**
 * 分页对象，前端为bootstrap
 * 作为一种过滤数据的方式供大家参考
 * */
public class PageUtils<T> implements Serializable {
	   private static final long serialVersionUID = 1L;
	   // 总记录数
	   private Long total;
	   // 列表数据
	   private List<T> rows;

	   public PageUtils(List<T> list, Long total) {
	      this.rows = list;
	      this.total = total;
	   }
	   
	   public PageUtils(List<T> list) {
		      this.rows = list;
		      this.total = (long) list.size();
	   }

	   public long getTotal() {
	      return total;
	   }

	   public void setTotal(long total) {
	      this.total = total;
	   }

	   public List<T> getRows() {
	      return rows;
	   }

	   public void setRows(List<T> rows) {
	      this.rows = rows;
	   }



	}