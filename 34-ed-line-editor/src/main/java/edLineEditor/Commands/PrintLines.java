package edLineEditor.Commands;

import edLineEditor.CommandInfo;
import edLineEditor.Page;

public class PrintLines extends Command{
    public PrintLines(CommandInfo info, Page page) {
        super(info, page);
    }

    @Override
    public void run() {
        for (int i = begIndex; i <= endIndex; i++){
            System.out.println(page.getLine(i));
        }
        page.setCurrLine(endIndex);
    }
}
