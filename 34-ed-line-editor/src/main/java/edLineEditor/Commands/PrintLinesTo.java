package edLineEditor.Commands;

import edLineEditor.LocInfo;
import edLineEditor.Page;

public class PrintLinesTo extends Command{
    public PrintLinesTo(LocInfo info, Page page) {
        super(info, page);
    }

    @Override
    public boolean run() {
        PrintLines print;
        if (info.isDefaultLoc()) begIndex++;
        if (info.getToIndex() == -1){        // 如果没有指定参数
            endIndex = page.getSize() - 1;
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
        return true;
    }
}
