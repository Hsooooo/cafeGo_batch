package co.kr.istarbucks.xo.batch.exception;

@SuppressWarnings("serial")
public class IbatisException extends RuntimeException {
	public IbatisException() {
		super();
	}
	
	public IbatisException(String msg) {
		super(msg);
	}
	
	public IbatisException(Throwable throwable) {
		super(throwable);
	}
}
