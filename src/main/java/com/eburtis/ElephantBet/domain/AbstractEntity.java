package com.eburtis.ElephantBet.domain;

import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.time.LocalDate;

@MappedSuperclass
public class AbstractEntity {
	private String createBy;
	private LocalDate createAt;
	private String updateBy;
	private LocalDate updateAt;
	@Version
	private long version;

	public String getCreateBy() {
		return createBy;
	}

	public LocalDate getCreateAt() {
		return createAt;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public LocalDate getUpdateAt() {
		return updateAt;
	}

	public long getVersion() {
		return version;
	}
}
