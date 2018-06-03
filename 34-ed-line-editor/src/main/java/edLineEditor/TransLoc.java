package edLineEditor;

import java.util.LinkedList;

public class TransLoc {
    static String transLoc(String line, Editor editor){
        int currentLine = editor.page.getCurrLine();
        if (times(line, '/') % 2 != 0){             // 解决替换时'/'产生的冲突
            int count = 0;
            int index = 0;
            for (int i = line.length() - 1; i >= 0 ; i--){
                if (line.charAt(i) == '/') count++;
                if (count == 3) {
                    index = i;
                    break;
                }
            }
            line = line.substring(0, index);
        }
        if (line.length() == 0){
            editor.setLines(currentLine, currentLine);
            return Integer.toString(currentLine) + "," + Integer.toString(currentLine);
        }
        if (line.charAt(0) == ';') {
            String command = line.substring(1, line.length());
            editor.setLines(currentLine, currentLine);
            return Integer.toString(currentLine) + "," + Integer.toString(currentLine) + " " + command;
        }
        while (line.contains("'")){                // 转化标记符
            int i = line.indexOf("'");
            String o = line.substring(i, i + 2);
            int lineNumber = editor.page.getMark(o.charAt(1));
            line = line.replace(o, Integer.toString(lineNumber));
        }
        while (line.contains("/")){              // 转化匹配字符
            int i1 = line.indexOf("/");
            int i2 = line.indexOf("/", i1 + 1);
            String s = line.substring(i1, i2 + 1);
            int lineNumber = editor.page.findDownLineNumber(s.substring(1, s.length() - 1));
            line = line.replace(s, Integer.toString(lineNumber));
        }
        while (line.contains("?")){
            int i1 = line.indexOf("?");
            int i2 = line.indexOf("?", i1 + 1);
            String s = line.substring(i1, i2 + 1);
            int lineNumber = editor.page.findUpLineNumber(s.substring(1, s.length() - 1));
            line = line.replace(s, Integer.toString(lineNumber));
        }
        if (line.contains(".")){
            if (line.contains(".-")){
                line = line.substring(1, line.length());
            }
            else {
                line = line.replace(".", Integer.toString(editor.page.getCurrLine()));
            }
        }
        if (line.contains(",") && (Character.isAlphabetic(line.charAt(line.indexOf(",") + 1))
                || line.charAt(line.indexOf(",") + 1) == '=')){
            line = line.replace(",", String.format("1,%d", editor.page.currPage.size()));
        }
        if (Character.isAlphabetic(line.charAt(0)) || line.charAt(0) == '='){
            line = String.format("%d,%d", currentLine, currentLine) + line;
        }
        while (line.contains("-")){
            if (line.contains("$-")){
                int i = line.indexOf("-");
                int n = Integer.parseInt(line.substring(i + 1, i + 2));
                String formal = line.substring(i - 1, i + 2);
                String newS = Integer.toString(editor.page.currPage.size() - n);
                line = line.replace(formal, newS);
            }
            else {                      // 在当前行进行修改
                int i = line.indexOf("-");
                int n = Integer.parseInt(line.substring(i + 1, i + 2));
                String formal = line.substring(i, i + 2);
                String newS = Integer.toString(currentLine - n);
                line = line.replace(formal, newS);
            }
        }
        while (line.contains("+")){
            if (line.contains("$+")){
                int i = line.indexOf("+");
                int n = Integer.parseInt(line.substring(i + 1, i + 2));
                String formal = line.substring(i - 1, i + 2);
                String newS = Integer.toString(editor.page.currPage.size() + n);
                line = line.replace(formal, newS);
            }
            else {                      // 在当前行进行修改
                int i = line.indexOf("+");
                int n = Integer.parseInt(line.substring(i + 1, i + 2));
                String formal = line.substring(i, i + 2);
                String newS = Integer.toString(currentLine + n);
                line = line.replace(formal, newS);
            }
        }
        if (line.contains("$")){
            line = line.replace("$", Integer.toString(editor.page.currPage.size()));
        }
        if (line.contains(",") && Character.isDigit(line.charAt(line.indexOf(",") - 1))
                && (Character.isAlphabetic(line.charAt(line.indexOf(",") + 1))
                || line.charAt(line.indexOf(",") + 1) == '=')){
            String old = line.substring(line.indexOf(",") - 1, line.indexOf(",") + 1);
            String New = old + Integer.toString(currentLine);
            line = line.replace(old, New);
        }
        if (line.charAt(0) == ',' && Character.isDigit(line.charAt(line.indexOf(",") + 1))){
            String old = line.substring(0, 2);
            String New = old + Integer.toString(currentLine);
            line = line.replace(old, New);
        }

        if (!line.contains(",")){
            int i;
            for (i = 0; i < line.length(); i++){
                if (Character.isAlphabetic(line.charAt(i)) || line.charAt(i) == '='){
                    break;
                }
            }
            String num = line.substring(0, i);
            String New = num + "," + num;
            line = New + line.substring(i, line.length());
        }
        int i;
        for (i = 0; i < line.length(); i++){
            if (Character.isAlphabetic(line.charAt(i)) || line.charAt(i) == '='){
                break;
            }
        }
        line = line.substring(0, i) + " " + line.substring(i, line.length());

        String[] loc = line.split(" ")[0].split(",");
        int beginIndex = Integer.parseInt(loc[0]);
        int endIndex = Integer.parseInt(loc[1]);

        editor.setLines(beginIndex, endIndex);

        return line;
    }

    private static int times(String s, char c){
        int n = 0;
        for (int i = 0; i < s.length(); i++){
            if (s.charAt(i) == c) n++;
        }
        return n;
    }

    public static void main(String[] args){
        LinkedList<String> a = new LinkedList<>();
        LinkedList<String> b = new LinkedList<>();
        a.add("love");
        b.add("love");
        System.out.println(a.equals(b));
    }
}
