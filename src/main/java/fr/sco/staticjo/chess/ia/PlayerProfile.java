package fr.sco.staticjo.chess.ia;

public interface PlayerProfile {

	public default String aff(){
		
		return this.getClass().getSimpleName().toString();
	}
}
