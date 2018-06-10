package edLineEditor.Commands;

import edLineEditor.FalseInputFormatException;
import edLineEditor.LocInfo;
import edLineEditor.Page;

public class Replace extends Command{
    public Replace(LocInfo info, Page page) {
        super(info, page);
    }

    @Override
    public boolean run() throws FalseInputFormatException {
        if (info.replaceTimes() == -1){            // 全部替换
            replaceAll(info.originStr(), info.changeToStr());
        }
        else {
            replace(info.originStr(), info.changeToStr(), info.replaceTimes());
        }
        return true;
    }

    private void replace(String Old, String New, int count) throws FalseInputFormatException {
        page.saveCurrent();
        isContain(Old);                // 检查能否成功替换
        for (int i = begIndex; i <= endIndex; i++){
            String line = page.getLine(i);
            for (int j = 0; j < count; j++){
                line = line.replaceFirst(Old, New);
            }
            page.deleteLine(i);
            page.addLine(i, line);
        }
        page.setCurrLine(endIndex);
        page.isSaved = false;
    }

    private void replaceAll(String Old, String New) throws FalseInputFormatException {
        page.saveCurrent();
        isContain(Old);
        for (int i = begIndex; i <= endIndex; i++){
            String line = page.getLine(i);
            String newLine = line.replace(Old, New);
            page.deleteLine(i);
            page.addLine(i, newLine);
        }
        page.setCurrLine(endIndex);
        page.isSaved = false;
    }

    private void isContain(String s) throws FalseInputFormatException {         // 只要有一行有目标，就能够替换
        for (int i = begIndex; i <= endIndex; i++){
            String line = page.getLine(i);
            if (line.contains(s)) return;
        }
        throw new FalseInputFormatException();
    }
}
