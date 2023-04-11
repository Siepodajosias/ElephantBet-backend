package com.eburtis.ElephantBet.utils;

public class DateException extends RuntimeException{

	/**
	 * Constructeur par défaut
	 * @param message le message d'erreur
	 */
	public DateException(String message) {
		super(message);
	}

	/**
	 * Exception levé lorsque la date n'est pas au format dd-mm-yyyy
	 *
	 * @param date la date
	 * @return l'exception
	 */
	public static DateException dateMalformee(String date) {
		return new DateException(String.format("La date %s est malformée, format attendu dd-mm-yyyy", date));
	}

	public static DateException jourIncoherent(String date) {
		return new DateException(String.format("%s n'est pas un jour valide", date));
	}

	public static DateException moisIncoherent(String date) {
		return new DateException(String.format("%s n'est pas un mois valide", date));
	}

	public static DateException anneeIncoherent(String date) {
		return new DateException(String.format("%s n'est pas une année valide. L'année doit contenir 4 chiffres au moins.", date));
	}
}
