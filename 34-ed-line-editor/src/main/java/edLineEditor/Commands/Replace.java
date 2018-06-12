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
    public boolean run(){
        if (isReplaceAll){            // 全部替换
            replaceAll(info.originStr(), info.changeToStr());
        }
        else {
            replace(info.originStr(), info.changeToStr());
        }
        return true;
    }

    private void replace(String Old, String New){
        page.saveCurrent();
        for (int i = begIndex; i <= endIndex; i++){
            String line = page.getLine(i);
            int from = -1;
            for (int j = 0; j < replaceCount; j++){
                from = line.indexOf(Old, from + 1);
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
            if (times(line, Old) >= 1){
                String newLine = line.replace(Old, New);
                page.deleteLine(i);
                page.addLine(i, newLine);
                page.setCurrLine(i);
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
                if (times(line, info.originStr()) >= replaceCount) return;
            }
        }
        throw new FalseInputFormatException();
    }

    public static int times(String line, String sub){
        if (sub.equals("?")) sub = "\\?";
        Pattern p = Pattern.compile(sub);
        Matcher m = p.matcher(line);
        int times = 0;
        while (m.find()){
            times++;
        }
        return times;
    }
}
