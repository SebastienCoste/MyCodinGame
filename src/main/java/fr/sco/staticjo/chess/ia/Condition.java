package fr.sco.staticjo.chess.ia;

import java.util.Date;

public class Condition{

	public Limit limit;
	public Object limitValue;
	public Object initValue;

	public Condition (Limit limit, Object initValue, Object limitValue){
		this.limit = limit;
		this.limitValue = limitValue;
		this.initValue = initValue;
	}


	public boolean doContinue(){
		switch (limit) {
		case TIME:
			return new Date().getTime() < (long)limitValue;
		case ITERATIONS:
		default:
			initValue = (int)initValue+1;
			return (int)initValue < (int)limitValue;
		}
	}
}
