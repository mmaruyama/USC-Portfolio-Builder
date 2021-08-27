import java.io.*;
public class InvalidSecurityException extends IOException {
	public InvalidSecurityException() {
		super();
	}
	public InvalidSecurityException(String msg) {
		super(msg);
	}
}
