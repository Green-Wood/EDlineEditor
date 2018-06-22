package edLineEditor.Commands;

import edLineEditor.CommandInfo;
import edLineEditor.Page;

public class PrintLinesTo extends Command{
    public PrintLinesTo(CommandInfo info, Page page) {
        super(info, page);
    }

    @Override
    public void execute() {
        if (info.isDefaultLoc()) {
            begIndex++;
            endIndex = info.getToIndex() + begIndex;
        }
        else {    // 有参数
            if (begIndex + info.getToIndex() >= page.getSize()){   // 如果超出
                endIndex = page.getSize() - 1;
            }
            else {
                endIndex = begIndex + info.getToIndex();
            }
        }
        for (int i = begIndex; i <= endIndex; i++){
            System.out.println(page.getLine(i));
        }
        page.setCurrLine(endIndex);
    }
}
