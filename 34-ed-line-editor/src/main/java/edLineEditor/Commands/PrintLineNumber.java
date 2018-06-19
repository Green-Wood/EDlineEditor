package edLineEditor.Commands;

import edLineEditor.CommandInfo;
import edLineEditor.Page;

public class PrintLineNumber extends Command {
    public PrintLineNumber(CommandInfo info, Page page) {
        super(info, page);
    }

    @Override
    public void execute() {
        System.out.println(begIndex + 1);
    }
}
