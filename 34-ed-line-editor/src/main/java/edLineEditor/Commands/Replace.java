package edLineEditor.Commands;

import edLineEditor.FalseInputFormatException;
import edLineEditor.CommandInfo;
import edLineEditor.Page;

public class Replace extends Command{
    private boolean isReplaceAll;
    private int replaceCount;
    private String originStr;
    private String changeToStr;
    public Replace(CommandInfo info, Page page) throws FalseInputFormatException {
        super(info, page);
        replaceCount = info.replaceCount();
        isReplaceAll = info.replaceCount() == -1;
        originStr = info.originStr();
        changeToStr = info.changeToStr();
        if (begIndex <= -1) throw new FalseInputFormatException();
        if (!isContain()) throw new FalseInputFormatException();                           // 检查能否成功替换
    }

    @Override
    public void execute(){
        if (isReplaceAll){            // 全部替换
            replaceAll(info.originStr(), info.changeToStr());
        }
        else {
            replace(info.originStr(), info.changeToStr());
        }
    }

    private void replace(String Old, String New){
        page.saveCurrent();
        for (int i = begIndex; i <= endIndex; i++){
            String line = page.getLine(i);
            int from = -1;
            for (int j = 0; j < replaceCount; j++){
                from = line.indexOf(Old, from + 1);        // 不断寻找下一个匹配的字符串，找不到就退出
                if (from == -1) break;
            }
            if (from != -1){
                String newLine = line.substring(0, from) + New +
                        line.substring(from + Old.length(), line.length());
                page.deleteLine(i);
                page.addLine(i, newLine);
                page.setCurrLine(i);
            }
        }
        page.isSaved = false;
    }

    private void replaceAll(String Old, String New){
        page.saveCurrent();
        for (int i = begIndex; i <= endIndex; i++){
            String line = page.getLine(i);
            if (line.contains(Old)){               // 有就替换
                String newLine = line.replace(Old, New);
                page.deleteLine(i);
                page.addLine(i, newLine);
                page.setCurrLine(i);           // 每次替换成功才更改当前行
            }
        }
        page.isSaved = false;
    }

    private boolean isContain(){         // 只要有一行有目标，就能够替换
        if (isReplaceAll){
            for (int i = begIndex; i <= endIndex; i++){
                String line = page.getLine(i);
                if (line.contains(originStr)) return true;
            }
        }
        else {
            for (int i = begIndex; i <= endIndex; i++){
                String line = page.getLine(i);
                if (CommandInfo.times(line, originStr) >= replaceCount)
                    return true;
            }
        }
        return false;
    }
}
