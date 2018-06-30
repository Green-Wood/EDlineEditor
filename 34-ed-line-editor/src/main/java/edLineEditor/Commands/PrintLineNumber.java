package edLineEditor.Commands;

import edLineEditor.CommandInfo;
import edLineEditor.Page;

public class PrintLineNumber extends Command {
    public PrintLineNumber(CommandInfo info, Page page) {
        super(info, page);
    }

    @Override
    public void execute() {
        if (info.isDefaultLoc()){
            System.out.println(page.getSize());
        }
        else {
            System.out.println(begIndex + 1);
        }
    }
}
