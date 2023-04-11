package com.eburtis.ElephantBet.facade;

public class EntiteNonConnueException extends RuntimeException {

	/**
	 * Constructeur par défaut
	 *
	 * @param message le message d'erreur
	 */
	public EntiteNonConnueException(String message) {
		super(message);
	}

	/**
	 * Exception ramené lorsque une entité n'a pas été trouvé par son code
	 *
	 * @param code le code de l'entité
	 * @return l'exception
	 */
	public static EntiteNonConnueException entiteNonConnu(String code) {
		return new EntiteNonConnueException(String.format("L'entité recherché n'a pas été trouvé avec le code suivant %s", code));
	}
}
