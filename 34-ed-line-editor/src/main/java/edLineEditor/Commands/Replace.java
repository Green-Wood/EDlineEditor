package edLineEditor.Commands;

import edLineEditor.FalseInputFormatException;
import edLineEditor.LocInfo;
import edLineEditor.Page;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Replace extends Command{
    private boolean isReplaceAll;
    private int replaceCount;
    public Replace(LocInfo info, Page page) throws FalseInputFormatException {
        super(info, page);
        if (info.getBeginIndex() <= -1) throw new FalseInputFormatException();
        replaceCount = info.replaceCount();
        isReplaceAll = info.replaceCount() == -1;
    }

    @Override
    public void run(){
        if (isReplaceAll){            // 全部替换
            replaceAll(info.originStr(), info.changeToStr());
        }
        else {
            replace(info.originStr(), info.changeToStr());
        }

        int i = 0;                          // 清除可能由替换产生的空行
        while (i < page.getSize()){
            if (page.getLine(i).equals("")){
                page.deleteLine(i);
                i--;
            }
            i++;
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

    public void isContain(String s) throws FalseInputFormatException {         // 只要有一行有目标，就能够替换
        if (isReplaceAll){
            for (int i = begIndex; i <= endIndex; i++){
                String line = page.getLine(i);
                if (line.contains(s)) return;
            }
        }
        else {
            for (int i = begIndex; i <= endIndex; i++){
                String line = page.getLine(i);
                if (LocInfo.times(line, info.originStr()) >= replaceCount) return;
            }
        }
        throw new FalseInputFormatException();
    }

}
