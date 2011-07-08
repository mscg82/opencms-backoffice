package com.mscg.emule.bean;

public class CategoryBean {

	private String name;
	private Integer id;
	private boolean checked;

	public CategoryBean() {
		this(null, null, false);
	}

	public CategoryBean(String name, Integer id) {
		this(name, id, false);
	}

	public CategoryBean(String name, Integer id, boolean checked) {
		setName(name);
		setId(id);
		setChecked(checked);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	@Override
	public String toString() {
		return name;
	}

}
