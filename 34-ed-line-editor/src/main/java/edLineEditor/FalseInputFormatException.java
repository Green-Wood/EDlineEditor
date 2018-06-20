package edLineEditor;

public class FalseInputFormatException extends Exception {            // 任何输入错误都会引起报错
    public FalseInputFormatException(){
        super();
    }
    public FalseInputFormatException(String message){
        super(message);
    }
    public FalseInputFormatException(Throwable cause){
        super(cause);
    }

}
