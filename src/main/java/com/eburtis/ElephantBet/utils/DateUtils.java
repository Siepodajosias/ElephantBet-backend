package com.eburtis.ElephantBet.utils;

import static com.eburtis.ElephantBet.utils.DateException.anneeIncoherent;
import static com.eburtis.ElephantBet.utils.DateException.dateMalformee;
import static com.eburtis.ElephantBet.utils.DateException.jourIncoherent;
import static com.eburtis.ElephantBet.utils.DateException.moisIncoherent;
import static java.lang.Integer.parseInt;
import static java.time.LocalDate.of;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.join;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DateUtils {

//	public static String localDateToString(LocalDate date) {
//		if (nonNull(date)) {
//			return join(of(date.getDayOfMonth() < 10 ? "0" + date.getDayOfMonth() : date.getDayOfMonth(), date.getMonthValue() < 10 ? "0" + date.getMonthValue() : date.getMonthValue(), date.getYear()), "-");
//		}
//		return "";
//	}
//
//	public static String localDateTimeToString(LocalDateTime date) {
//		if (nonNull(date)) {
//			return join(
//					of(
//							join(of(date.getDayOfMonth() < 10 ? "0" + date.getDayOfMonth() : date.getDayOfMonth(), date.getMonthValue() < 10 ? "0" + date.getMonthValue() : date.getMonthValue(), date.getYear()), "-"),
//							join(of(date.getHour() < 10 ? "0" + date.getHour() : date.getHour(), date.getMinute() < 10 ? "0" + date.getMinute() : date.getMinute()), ":")
//					), " "
//			);
//		}
//		return "";
//	}

	public static LocalDate stringToLocalDate(String date) {
		String[] split = date.split("-");
		if (split.length == 3) {
			if (parseInt(split[2]) < 0 || parseInt(split[2]) > 31) {
				throw jourIncoherent(split[0]);
			}
			else if (parseInt(split[1]) < 0 || parseInt(split[1]) > 12) {
				throw moisIncoherent(split[1]);
			}
			else if (parseInt(split[0]) < 1000) {
				throw anneeIncoherent(split[0]);
			}
			return of(parseInt(split[0]), parseInt(split[1]), parseInt(split[2]));
		}
		throw dateMalformee(date);
	}

//	public static String reverseDateString(String date) {
//		String[] split = date.split("-");
//		if (split.length == 3) {
//			if (parseInt(split[0]) < 0 || parseInt(split[0]) > 31) {
//				throw jourIncoherent(split[0]);
//			}
//			else if (parseInt(split[1]) < 0 || parseInt(split[1]) > 12) {
//				throw moisIncoherent(split[1]);
//			}
//			else if (parseInt(split[2]) < 1000) {
//				throw anneeIncoherent(split[0]);
//			}
//			return join(of(split[2], split[1], split[0]), "-");
//		}
//		throw dateMalformee(date);
//	}
}

