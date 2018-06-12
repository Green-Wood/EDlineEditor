package edLineEditor.Commands;

import edLineEditor.LocInfo;
import edLineEditor.Page;

public class PrintLines extends Command{
    public PrintLines(LocInfo info, Page page) {
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
