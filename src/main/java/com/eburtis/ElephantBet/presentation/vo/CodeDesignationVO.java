package com.eburtis.ElephantBet.presentation.vo;

import com.eburtis.ElephantBet.domain.AbstractCodeDesignationEntity;

public class CodeDesignationVO {
	private String code;
	private String designation;

	public CodeDesignationVO() {
	}

	protected CodeDesignationVO(String code, String designation) {
		this.code = code;
		this.designation = designation;
	}

	public  <T extends AbstractCodeDesignationEntity> CodeDesignationVO(T entity){
		this(entity.getCode(), entity.getDesignation());
	}

	public String getCode() {
		return code;
	}

	public String getDesignation() {
		return designation;
	}
}
