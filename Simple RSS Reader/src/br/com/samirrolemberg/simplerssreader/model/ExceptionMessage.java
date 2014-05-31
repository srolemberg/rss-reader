package br.com.samirrolemberg.simplerssreader.model;

public class ExceptionMessage {

	private Throwable throwable;
	
	@SuppressWarnings("unused")
	private ExceptionMessage(){
		super();
	}
	public ExceptionMessage(Throwable e){
		super();
		this.throwable = e;
	}
	public Throwable getThrowable() {
		return throwable;
	}
	public void setThrowable(Throwable e) {
		this.throwable = e;
	}
	
}
