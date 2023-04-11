package com.eburtis.ElephantBet.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Objects;

@MappedSuperclass
public class AbstractCodeDesignationEntity extends AbstractEntity {
	@Column(name = "code", nullable = false, unique = true)
	protected String code;

	@Column(name = "designation", nullable = false)
	protected String designation;

	public AbstractCodeDesignationEntity() {
	}

	protected AbstractCodeDesignationEntity(String code, String designation) {
		this.code = code;
		this.designation = designation;
	}

	public String getCode() {
		return code;
	}

	public String getDesignation() {
		return designation;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AbstractCodeDesignationEntity that = (AbstractCodeDesignationEntity) o;
		return Objects.equals(code, that.code) && Objects.equals(designation, that.designation);
	}

	@Override
	public int hashCode() {
		return Objects.hash(code, designation);
	}
}
