package edLineEditor.Commands;

import edLineEditor.LocInfo;
import edLineEditor.Page;

public class PrintLineNumber extends Command {
    public PrintLineNumber(LocInfo info, Page page) {
        super(info, page);
    }

    @Override
    public boolean run() {
        System.out.println(begIndex + 1);
        return true;
    }
}
